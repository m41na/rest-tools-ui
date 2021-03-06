package com.practicaldime.rest.tools.ui.provider;

import com.practicaldime.common.entity.users.Account;
import com.practicaldime.common.model.AppUser;
import com.practicaldime.plugins.api.PlugStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import com.practicaldime.rest.tools.ui.features.StartupProvider;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class AppUserProvider implements Supplier<AppUser> {

	@Autowired
	private StartupProvider service;
	@Autowired
	private Environment env;

	@Override
	public AppUser get() {
		Boolean autoLogin = env.getProperty("auto.login.allow", Boolean.TYPE);
		if (autoLogin) {
			String username = env.getProperty("auto.login.username");
			Account account = PlugStack.use(service.get()).push("getUserAccount", String.class).call(username).pop(Account.class);
			if (account != null) {
				AppUser user = new AppUser(account.getUsername(), account.getProfile().getId(),
						account.getProfile().getFirstName() + " " + account.getProfile().getLastName());
				return user;
			}
		}
		return null;
	}
}
