package com.jarredweb.rest.tools.ui.provider;

import java.util.function.Supplier;

import javax.inject.Inject;
import javax.inject.Named;

import org.glassfish.hk2.api.Factory;
import org.springframework.core.env.Environment;

import com.jarredweb.domain.users.Account;
import com.jarredweb.domain.users.AppUser;
import com.jarredweb.rest.tools.ui.service.StartupService;

import works.hop.plugins.api.PlugAid;

public class AppUserFactory implements Factory<AppUser> {

	@Inject
	private StartupService service;
	@Inject
	private Environment env;

	@Override
	public void dispose(AppUser user) {
		// do nothing
	}

	@Override
	public AppUser provide() {
		Boolean autoLogin = env.getProperty("auto.login.allow", Boolean.TYPE);
		if (autoLogin) {
			String username = env.getProperty("auto.login.username");
			Account account = PlugAid.use(service.get()).func("getUserAccount", String.class).call(username).pop(Account.class);
			if (account != null) {
				AppUser user = new AppUser(account.getUsername(), account.getProfile().getId(),
						account.getProfile().getFirstName() + " " + account.getProfile().getLastName());
				return user;
			}
		}
		return null;
	}
}
