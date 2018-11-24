package com.jarredweb.rest.tools.ui.provider;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;
import org.springframework.core.env.Environment;

<<<<<<< HEAD
import com.jarredweb.domain.users.Account;
import com.jarredweb.domain.users.AppUser;
import com.jarredweb.rest.tools.ui.features.StartupProvider;

import works.hop.plugins.api.Poppin;

public class AppUserFactory implements Factory<AppUser> {

	@Inject
	private StartupProvider service;
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
			Account account = Poppin.use(service.get()).func("getUserAccount", String.class).call(username).pop(Account.class);
			if (account != null) {
				AppUser user = new AppUser(account.getUsername(), account.getProfile().getId(),
						account.getProfile().getFirstName() + " " + account.getProfile().getLastName());
				return user;
			}
		}
		return null;
	}
=======
import com.jarredweb.common.util.AppResult;
import com.jarredweb.domain.users.Account;
import com.jarredweb.domain.users.AppUser;
import com.jarredweb.plugins.users.dao.UserDao;

public class AppUserFactory implements Factory<AppUser> {

    @Inject
    private UserDao userDao;
    @Inject
    private Environment env;

    @Override
    public void dispose(AppUser user) {
        //do nothing
    }

    @Override
    public AppUser provide() {
        Boolean autoLogin = env.getProperty("auto.login.allow", Boolean.TYPE);
        if(autoLogin){
            String username = env.getProperty("auto.login.username");
            AppResult<Account> selected = userDao.findByUsername(username);
            if(selected.getCode() == 0){
                Account account = selected.getEntity();
                AppUser user = new AppUser(account.getUsername(), account.getProfile().getId(), account.getProfile().getFirstName() + " " + account.getProfile().getLastName());
                return user;
            }
        }
        return null;
    }
}
