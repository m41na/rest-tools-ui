package com.jarredweb.rest.tools.ui.provider;

import com.jarredweb.rest.tools.ui.model.AppUser;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class AppUserBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bindFactory(AppUserFactory.class).to(AppUser.class);
    }
}
