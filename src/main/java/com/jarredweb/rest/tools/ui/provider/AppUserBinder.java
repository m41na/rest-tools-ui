package com.jarredweb.rest.tools.ui.provider;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import com.jarredweb.domain.users.AppUser;

public class AppUserBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bindFactory(AppUserFactory.class).to(AppUser.class);
    }
}
