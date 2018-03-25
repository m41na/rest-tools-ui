package com.jarredweb.rest.tools.ui.service;

import com.jarredweb.rest.tools.ui.model.EndpointsModel;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class EndpointsCache {
    
    private final Map<Long, EndpointsModel> CACHE = new HashMap<>();
    
    public EndpointsModel getUserViewModel(Long id){
        return CACHE.get(id);
    }
    
    public void cacheUserViewModel(Long id, EndpointsModel model){
        CACHE.put(id, model);
    }
    
    public void resetUserViewModel(Long id){
        CACHE.remove(id);
    }
}
