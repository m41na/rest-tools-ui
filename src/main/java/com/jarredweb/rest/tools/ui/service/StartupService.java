package com.jarredweb.rest.tools.ui.service;

import com.jarredweb.rest.tools.ui.persist.entity.Account;

public interface StartupService {
    
    void initialize();

    Account getUserAccount(String username);

    void onInitialized();
}
