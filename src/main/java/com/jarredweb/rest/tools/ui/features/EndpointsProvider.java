package com.jarredweb.rest.tools.ui.features;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import works.hop.plugins.loader.PluginCentral;
import works.hop.plugins.util.ServiceProvider;

@Component
public class EndpointsProvider implements ServiceProvider {

	private PluginCentral central;

	public EndpointsProvider(@Autowired PluginCentral central) {
		this.central = central;
	}

	@Override
	public Object get() {
		return central.getInstance("com.jarredweb.plugins.rest.tools.RestToolsPlugin", "persist");
	}
}
