package com.jarredweb.rest.tools.ui.persist;

import com.jarredweb.rest.tools.ui.persist.impl.UserAccountDaoJdbc;
import com.jarredweb.rest.tools.ui.persist.impl.UserEndpointsDaoJdbc;
import java.io.File;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class RestToolsTestConfig {
    
    @Bean
    public DataSource dataSource() {
        String dbPath = "./data/rest-tools-ui-test.db";
        //drop database if exists
        File dbFile = new File(dbPath);
        if (dbFile.exists()) {
            String result = dbFile.delete() ? "database dropped" : "could not drop database";
            System.out.println(result);
        }

        // Create the ConnectionPoolDataSource
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:h2:" + dbPath);
        ds.setUsername("sa");
        ds.setPassword("sa");
        ds.setDriverClassName("org.h2.Driver");

        // return datasource bean
        return ds;
    }

    @Bean
    public PlatformTransactionManager txManager() {
        return new DataSourceTransactionManager(dataSource());
    }
    
    @Bean
    @Autowired
    public UserAccountDao userAccountDao(DataSource ds) {
        return new UserAccountDaoJdbc(ds);
    }

    @Bean
    @Autowired
    public UserEndpointsDao userEndpointsDao(DataSource ds) {
        return new UserEndpointsDaoJdbc(ds);
    }
}
