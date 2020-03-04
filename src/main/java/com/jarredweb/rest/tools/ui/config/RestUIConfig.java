package com.jarredweb.rest.tools.ui.config;

import com.practicaldime.plugins.api.Pluggable;
import com.practicaldime.plugins.config.PlugConfig;
import com.practicaldime.plugins.loader.PluginCentral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.inject.Singleton;

@Configuration
@ComponentScan(basePackages="com.jarredweb.rest.tools.ui")
public class RestUIConfig {

	@Bean
	public Pluggable pluggable() {
		return PlugConfig.getInstance().loadConfig();
	}

	@Bean
	@Singleton
	public PluginCentral pluginCentral(@Autowired Pluggable plugs) {
		return new PluginCentral(plugs.getSources());
	}
}