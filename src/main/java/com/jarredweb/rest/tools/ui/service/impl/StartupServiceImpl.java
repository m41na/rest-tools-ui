package com.jarredweb.rest.tools.ui.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jarredweb.rest.tools.ui.model.UserEndpoints;
import com.jarredweb.rest.tools.ui.persist.UserAccountDao;
import com.jarredweb.rest.tools.ui.persist.UserEndpointsDao;
import com.jarredweb.rest.tools.ui.persist.entity.AccRole;
import com.jarredweb.rest.tools.ui.persist.entity.AccStatus;
import com.jarredweb.rest.tools.ui.persist.entity.Account;
import com.jarredweb.rest.tools.ui.persist.entity.Profile;
import com.jarredweb.rest.tools.ui.service.StartupService;
import com.jarredweb.zesty.common.bean.AppResult;
import com.jarredweb.zesty.common.bean.ResStatus;
import com.jarredweb.zesty.common.exception.AppException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class StartupServiceImpl implements StartupService {

    private static final Logger LOG = LoggerFactory.getLogger(StartupServiceImpl.class);

    @Autowired
    private UserAccountDao userDao;
    @Autowired
    private UserEndpointsDao endpDao;
    @Autowired
    private DataSource dataSource;

    private Properties props = null;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void initialize() {
        // 1. populate props with initial values
        props = new Properties();

        // 2. read values from config files
        try {
            props.load(this.getClass().getResourceAsStream("/config/app-default.properties"));
            // load overriding properties
            props.load(new FileReader(new File("app-config.properties")));
        } catch (IOException e) {
            LOG.error(e.getMessage());
            throw new AppException(new ResStatus(1, "could not locate properties file to intialize application", e.getMessage()));
        }

        // 3. check initialized property
        if ("true".equals(props.getProperty("app.jdbc.configure")) && !"true".equals(props.getProperty("app.initialized"))) {

            // 4. initialize database schema
            ResourceDatabasePopulator schema = new ResourceDatabasePopulator();
            schema.addScript(new ClassPathResource("sql/update-schema.sql"));
            schema.execute(dataSource);

            // 5. check if default admin user exists
            Account account = getUserAccount(props.getProperty("app.admin.username"));

            if (account == null) {
                // 6. register default admin user
                Profile user = new Profile();
                user.setEmailAddress(props.getProperty("app.admin.email"));
                user.setFirstName(props.getProperty("app.admin.firstname"));
                user.setLastName(props.getProperty("app.admin.lastname"));
                AppResult<Profile> createResult = userDao.register(user);

                Profile profile = createResult.getEntity();

                account = new Account();
                account.setProfile(profile);
                account.setUsername(props.getProperty("app.admin.username"));
                account.setPassword(props.getProperty("app.admin.password"));
                AppResult<Account> created = userDao.register(account);

                if (created.getEntity() != null) {
                    // 7. update user info
                    account.setRole(AccRole.admin);
                    account.setStatus(AccStatus.active);
                    AppResult<Account> updated = userDao.update(account);
                    if (updated.getEntity() == null) {
                        throw new Error("Could not create default admin");
                    }

                    LOG.info("new '{}' account registered", account.getUsername());
                }

                //8. Add default data from json
                ObjectMapper mapper = new ObjectMapper();
                try (InputStream src = getClass().getClassLoader().getResourceAsStream("json/rest.json")) {
                    List<UserEndpoints> endpoints = mapper.readValue(src, new TypeReference<List<UserEndpoints>>() {
                    });

                    endpoints.get(0).getMergedCollections().stream().forEach(endp -> {
                        endpDao.createCollection(endp, profile.getId());
                    });
                } catch (IOException e) {
                    LOG.error("There was a problem saving the collection", e);
                }
            }
        
            // 9. populate with initial data
            ResourceDatabasePopulator dbdata = new ResourceDatabasePopulator();
            dbdata.addScript(new ClassPathResource("sql/insert-data.sql"));
            dbdata.execute(dataSource);

            // 10. all clear - update initialized flag
            onInitialized();
        }
    }

    @Override
    public Account getUserAccount(String username) {
        return userDao.findByUsername(username).getEntity();
    }

    @Override
    public void onInitialized() {
        try {
            Path configFilePath = Paths.get("app-config.properties");
            List<String> fileContent = new ArrayList<>(Files.readAllLines(configFilePath, StandardCharsets.UTF_8));

            for (int i = 0; i < fileContent.size(); i++) {
                if (fileContent.get(i).startsWith("app.initialized")) {
                    fileContent.set(i, "app.initialized=true");
                    break;
                }
            }

            Files.write(configFilePath, fileContent, StandardCharsets.UTF_8);
            LOG.info("app-config.properties: 'initialized' property updated");
        } catch (IOException e) {
            throw new Error(
                    "Application was initialized but could not update the initialized property. Consider doing this manually and then restart");
        }
    }
}
