package com.practicaldime.rest.tools.ui.config;

import com.practicaldime.plugins.api.PlugDefinition;
import com.practicaldime.plugins.config.PlugConfig;
import com.practicaldime.plugins.loader.PluginCentral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.practicaldime.rest.tools.ui")
public class RestUIConfig {

    @Bean
    public PlugDefinition PlugDefinition() {
        return PlugConfig.getInstance().loadConfig();
    }

    @Bean
    public PluginCentral pluginCentral(@Autowired PlugDefinition plugs) {
        return new PluginCentral(plugs.getDefinitions());
    }
}