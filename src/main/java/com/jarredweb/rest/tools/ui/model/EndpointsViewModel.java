package com.jarredweb.rest.tools.ui.model;

import java.util.List;
import works.hop.rest.tools.api.ApiReq;

public interface EndpointsViewModel {

    long getCurrentCollection();

    void setCurrentCollection(long currentCollection);

    String getCurrentEndpoint();

    void setCurrentEndpoint(String currentEndpoint);

    String getCurrentMode();

    void setCurrentMode(String currentMode);

    UserEndpoints getModel();

    void setModel(UserEndpoints model);

    List<EndpointsList> getUserCollections();

    List<PairValue> getCollectionTitles();

    List<ApiReq> getUserEndpoints(long collectionId);

    ApiReq getUserEndpoint(long collectionId, String endpointId);

    void addNewEndpoint(long collectionId, ApiReq endpoint);

    void updateEndpoint(long collectionId, ApiReq endpoint);

    void dropEndpoint(long collectionId, String endpointId);
}
