package com.jarredweb.rest.tools.ui.persist;

import com.jarredweb.rest.tools.ui.model.EndpointsList;
import com.jarredweb.rest.tools.ui.model.UserEndpoints;
import com.jarredweb.zesty.common.bean.AppResult;
import works.hop.rest.tools.api.ApiReq;

public interface UserEndpointsDao {
    
    AppResult<EndpointsList> createCollection(EndpointsList collection, Long userId);
    
    AppResult<Integer> updateCollection(Long id, String title);
    
    AppResult<Integer> deleteCollection(Long id);
    
    AppResult<EndpointsList> retrieveCollection(Long id);
    
    AppResult<UserEndpoints> retrieveUserEndpoints(Long userId);
    
    AppResult<ApiReq> createApiRequest(Long collection, ApiReq endpoint);
    
    AppResult<Integer> updateApiRequest(Long collection, ApiReq endpoint);
    
    AppResult<Integer> removeApiRequest(Long collection, java.lang.String endpointId);
    
    AppResult<ApiReq> retrieveApiRequest(Long collection, String endpointId);
}
