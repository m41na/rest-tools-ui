package com.jarredweb.rest.tools.ui.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.jarredweb.plugins.users.service.StartupService;
import com.jarredweb.rest.tools.ui.config.RestUIConfig;
import com.jarredweb.rest.tools.ui.provider.AppUserBinder;
import com.jarredweb.zesty.http.app.ZestyRunner;

public class RestToolsRunner extends ZestyRunner {
    
    private static final Logger LOG = LoggerFactory.getLogger(RestToolsRunner.class);
    
    @Override
    public void initApplication(ApplicationContext ctx) {
        StartupService startup = ctx.getBean(StartupService.class);
        startup.initialize();
    }

    public static void main(String... args) {
        String configClass = RestUIConfig.class.getName();
        LOG.info("loading configuration for {} from {}", RestToolsRunner.class.getName(), configClass);
        System.setProperty("context.lookup", configClass);
        new RestToolsRunner()
                .packages("com.jarredweb.rest.tools.ui.resource")
                .component(new AppUserBinder())
                .create(args);
    }
}
