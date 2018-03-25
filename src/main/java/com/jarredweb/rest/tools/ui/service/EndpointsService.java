package com.jarredweb.rest.tools.ui.service;

import com.jarredweb.rest.tools.ui.model.EndpointsList;
import com.jarredweb.rest.tools.ui.model.EndpointsModel;
import com.jarredweb.rest.tools.ui.model.PairValue;
import com.jarredweb.webjar.common.bean.AppResult;
import java.util.List;
import works.hop.rest.tools.api.ApiAssert;
import works.hop.rest.tools.api.ApiReq;

public interface EndpointsService {    
    
    EndpointsModel getViewModel(Long userId);
    
    EndpointsModel getViewModel(Long userId, Boolean reset);
    
    AppResult<EndpointsList> addNewCollection(Long userId, String title);
    
    AppResult<Integer> updateCollection(Long userId, Long collId, String title);
    
    AppResult<Integer> dropCollection(Long userId, Long collId);

    List<EndpointsList> getUserCollections(Long userId);

    List<PairValue> getCollectionTitles(Long userId);

    List<ApiReq> getUserEndpoints(Long userId, long collectionId);

    ApiReq getUserEndpoint(Long userId, long collectionId, String endpointId);

    AppResult<ApiReq> addNewEndpoint(Long userId, long collectionId, ApiReq endpoint);

    AppResult<Integer> updateEndpoint(Long userId, long collectionId, ApiReq endpoint);

    AppResult<Integer> dropEndpoint(Long userId, long collectionId, String endpointId);
    
    ApiAssert getEndpointAssertion(Long userId, Long collectionId, String endpointId, Long assertId);
    
    List<ApiAssert<?>> getEndpointAssertions(Long userId, Long collectionId, String endpointId);

    AppResult<ApiAssert> addNewAssertion(Long userId, Long collectionId, String endpointId, ApiAssert assertion);

    AppResult<Integer> updateAssertion(Long userId, Long collectionId, String endpointId, ApiAssert assertion);

    AppResult<Integer> dropAssertion(Long userId, Long collectionId, String endpointId, long assertId);
}
