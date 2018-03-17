package com.jarredweb.rest.tools.ui.service;

import com.jarredweb.rest.tools.ui.model.EndpointsList;
import com.jarredweb.rest.tools.ui.model.EndpointsModel;
import com.jarredweb.rest.tools.ui.model.PairValue;
import java.util.List;
import works.hop.rest.tools.api.ApiReq;

public interface EndpointsService {    
    
    EndpointsModel getViewModel(Long userId);

    List<EndpointsList> getUserCollections(Long userId);

    List<PairValue> getCollectionTitles(Long userId);

    List<ApiReq> getUserEndpoints(Long userId, long collectionId);

    ApiReq getUserEndpoint(Long userId, long collectionId, String endpointId);

    void addNewEndpoint(Long userId, long collectionId, ApiReq endpoint);

    void updateEndpoint(Long userId, long collectionId, ApiReq endpoint);

    void dropEndpoint(Long userId, long collectionId, String endpointId);
}
