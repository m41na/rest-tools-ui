package com.jarredweb.rest.tools.ui.service;

import com.jarredweb.rest.tools.ui.persist.UserAccountDao;
import com.jarredweb.rest.tools.ui.persist.UserEndpointsDao;

public interface PersistService {

    EndpointsCache getEndpointsCache();
    
    void setEndpointsCache(EndpointsCache cache);
    
    UserAccountDao getUserAccountDao();
    
    void setUserAccountDao(UserAccountDao userDao);
    
    UserEndpointsDao getUserEndpointsDao();
    
    void setUserEndpointsDao(UserEndpointsDao endpDao);
}
