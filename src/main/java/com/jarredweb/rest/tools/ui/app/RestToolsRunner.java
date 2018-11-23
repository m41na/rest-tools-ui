package com.jarredweb.rest.tools.ui.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.jarredweb.rest.tools.ui.config.RestUIConfig;
import com.jarredweb.rest.tools.ui.provider.AppUserBinder;
import com.jarredweb.zesty.http.app.ZestyRunner;

import works.hop.plugins.api.Poppin;
import works.hop.plugins.loader.PluginCentral;

public class RestToolsRunner extends ZestyRunner {
    
    private static final Logger LOG = LoggerFactory.getLogger(RestToolsRunner.class);
    private static final String STARTUP_SERVICE_PLUGIN = "com.jarredweb.plugins.users.StartupPlugin";
    private static final String REST_TOOLS_PLUGIN = "com.jarredweb.plugins.rest.tools.RestToolsPlugin";
    
    @Override
    public void initApplication(ApplicationContext ctx) {    	
		PluginCentral central = ctx.getBean(PluginCentral.class);
		//load startup plugin
		central.loadPlugin(STARTUP_SERVICE_PLUGIN);
		central.discoverFeatures(STARTUP_SERVICE_PLUGIN);
		//now invoke 'initialize' on StartupService
    	Object startup = central.getInstance(STARTUP_SERVICE_PLUGIN, "StartupService");
		Poppin.use(startup).func("initialize").call().pop();
        //load rest tools plugin
        central.loadPlugin(REST_TOOLS_PLUGIN);
		central.discoverFeatures(REST_TOOLS_PLUGIN);
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
