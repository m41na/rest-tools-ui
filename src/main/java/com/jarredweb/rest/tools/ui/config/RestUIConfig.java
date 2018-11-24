package com.jarredweb.rest.tools.ui.config;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import works.hop.plugins.api.Pluggable;
import works.hop.plugins.config.PlugConfig;
import works.hop.plugins.loader.PluginCentral;

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
=======
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.jarredweb.plugins.rest.tools.config.RestToolsConfig;
import com.jarredweb.plugins.users.config.UsersServiceConfig;

@Configuration
@Import({RestToolsConfig.class, UsersServiceConfig.class})
public class RestUIConfig {}