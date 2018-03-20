package com.jarredweb.rest.tools.ui.persist;

import com.jarredweb.webjar.common.bean.AppResult;
import java.util.List;
import works.hop.rest.tools.api.ApiAssert;

public interface UserAssertionsDao {
    
    AppResult<ApiAssert> createApiAssert(String endpoint, ApiAssert assertion);
    
    AppResult<Integer> updateApiAssert(ApiAssert assertion);
    
    AppResult<Integer> removeApiAssert(Long assertId);
    
    AppResult<ApiAssert> retrieveApiAssert(Long assertId);
    
    AppResult<List<ApiAssert<?>>> retrieveEndpointAssertions(String endpoint);
}
