package com.jarredweb.rest.tools.ui.app;

import com.jarredweb.rest.tools.ui.config.ToolsConfig;
import com.jarredweb.rest.tools.ui.resource.ToolsResource;
import com.jarredweb.webjar.http.app.AppRunnerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToolsRunner extends AppRunnerBuilder {
    
    private static final Logger LOG = LoggerFactory.getLogger(ToolsRunner.class);

    public static void main(String[] args) {
        String configClass = ToolsConfig.class.getName();
        LOG.info("loading configuration for {} from {}", ToolsRunner.class.getName(), configClass);
        System.setProperty("context.lookup", configClass);
        new ToolsRunner().create(args, ToolsResource.class);
    }
}
