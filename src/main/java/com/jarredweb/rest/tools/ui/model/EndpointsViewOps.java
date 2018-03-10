package com.jarredweb.rest.tools.ui.model;

import java.util.List;

public interface EndpointsViewOps {

    long getCurrentUser();

    void setCurrentUser(long currentUser);

    long getCurrentCollection();

    void setCurrentCollection(long currentCollection);

    long getCurrentEndpoint();

    void setCurrentEndpoint(long currentEndpoint);

    String getCurrentMode();

    void setCurrentMode(String currentMode);

    List<UserEndpoints> getModel();

    void setModel(List<UserEndpoints> model);

    List<PairValue> getUsers();

    List<EndpointsList> getUserCollections(long userId);

    List<PairValue> getCollectionTitles(long userId);

    List<Endpoint> getUserEndpoints(long userId, long collectionId);

    Endpoint getUserEndpoint(long userId, long collectionId, long endpointId);

    void addNewEndpoint(long userId, long collectionId, Endpoint endpoint);

    void updateEndpoint(long userId, long collectionId, Endpoint endpoint);

    void dropEndpoint(long userId, long collectionId, long endpointId);
}
