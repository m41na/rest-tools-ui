package com.practicaldime.rest.tools.ui.features;

import com.practicaldime.plugins.loader.PluginCentral;
import com.practicaldime.plugins.util.ServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartupProvider implements ServiceProvider {

	private PluginCentral central;
	
	public StartupProvider(@Autowired PluginCentral central) {
		this.central = central;
	}
	
	@Override
	public Object get() {
		return central.getInstance("com.practicaldime.plugins.users.StartupPlugin", "StartupService");
	}
}
