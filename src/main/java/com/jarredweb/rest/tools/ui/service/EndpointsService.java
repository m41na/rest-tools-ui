package com.jarredweb.rest.tools.ui.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import works.hop.plugins.loader.PluginCentral;

@Component
public class EndpointsService implements ServiceFactory {

	private PluginCentral central;

	public EndpointsService(@Autowired PluginCentral central) {
		this.central = central;
	}

	@Override
	public Object get() {
		return central.getInstance("com.jarredweb.plugins.rest.tools.RestToolsPlugin", "persist");
	}
}
