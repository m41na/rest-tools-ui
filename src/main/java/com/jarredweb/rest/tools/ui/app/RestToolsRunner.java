package com.jarredweb.rest.tools.ui.app;

import com.jarredweb.rest.tools.ui.config.RestToolsConfig;
import com.jarredweb.rest.tools.ui.provider.AppUserBinder;
import com.jarredweb.rest.tools.ui.service.StartupService;
import com.jarredweb.webjar.http.app.AppRunnerBuilder;
import com.jarredweb.webjar.http.app.WebjarBootup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public class RestToolsRunner extends AppRunnerBuilder {
    
    private static final Logger LOG = LoggerFactory.getLogger(RestToolsRunner.class);
    
    @Override
    public WebjarBootup initStartupService(ApplicationContext ctx) {
        StartupService startup = ctx.getBean(StartupService.class);
        startup.initialize();
        return this;
    }

    public static void main(String[] args) {
        String configClass = RestToolsConfig.class.getName();
        LOG.info("loading configuration for {} from {}", RestToolsRunner.class.getName(), configClass);
        System.setProperty("context.lookup", configClass);
        new RestToolsRunner()
                .packages("com.jarredweb.rest.tools.ui.resource")
                .component(new AppUserBinder())
                .create(args);
    }
}
