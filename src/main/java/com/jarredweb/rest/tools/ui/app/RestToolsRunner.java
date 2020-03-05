package com.jarredweb.rest.tools.ui.app;

import com.jarredweb.rest.tools.ui.config.RestUIConfig;
import com.jarredweb.rest.tools.ui.resource.RestToolsResource;
import com.practicaldime.plugins.api.Poppin;
import com.practicaldime.plugins.loader.PluginCentral;
import com.practicaldime.router.core.server.IServer;
import com.practicaldime.router.core.server.Rest;
import com.practicaldime.router.http.app.AppServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;
import java.util.function.Function;

public class RestToolsRunner  {
    
    private static final Logger LOG = LoggerFactory.getLogger(RestToolsRunner.class);
    private static final String STARTUP_SERVICE_PLUGIN = "com.jarredweb.plugins.users.StartupPlugin";
    private static final String REST_TOOLS_PLUGIN = "com.jarredweb.plugins.rest.tools.RestToolsPlugin";

    public static void initApplication(ApplicationContext ctx) {
		PluginCentral central = ctx.getBean(PluginCentral.class);
		//load startup plugin
		central.loadPlugin(STARTUP_SERVICE_PLUGIN);
		central.discoverFeatures(STARTUP_SERVICE_PLUGIN);
		//now invoke 'initialize' on StartupService
    	Object startup = central.getInstance(STARTUP_SERVICE_PLUGIN, "StartupService");
		Poppin.use(startup).push("initialize").call().pop();
        //load rest tools plugin
        central.loadPlugin(REST_TOOLS_PLUGIN);
		central.discoverFeatures(REST_TOOLS_PLUGIN);
    }

    public static void main(String... args) {
        String configClass = RestUIConfig.class.getName();
        LOG.info("loading configuration for {} from {}", RestToolsRunner.class.getName(), configClass);
        System.setProperty("context.lookup", configClass);
        ApplicationContext ctx = new AnnotationConfigApplicationContext(RestUIConfig.class);

        initApplication(ctx);
        RestToolsResource resources = ctx.getBean(RestToolsResource.class);

        Rest rest = new Rest(){

            @Override
            public IServer provide(Map<String, String> map) {
                return AppServer.instance(map);
            }

            @Override
            public Function<Map<String, String>, IServer> build(IServer iServer) {
                return (props) -> resources.link(iServer);
            }
        };
        rest.start(args);
    }
}
