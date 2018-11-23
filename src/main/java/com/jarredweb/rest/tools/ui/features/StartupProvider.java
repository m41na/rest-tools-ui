package com.jarredweb.rest.tools.ui.features;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import works.hop.plugins.loader.PluginCentral;
import works.hop.plugins.util.ServiceProvider;

@Component
public class StartupProvider implements ServiceProvider{

	private PluginCentral central;
	
	public StartupProvider(@Autowired PluginCentral central) {
		this.central = central;
	}
	
	@Override
	public Object get() {
		return central.getInstance("com.jarredweb.plugins.users.StartupPlugin", "StartupService");
	}
}
