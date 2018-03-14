package com.jarredweb.rest.tools.ui.model;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import works.hop.rest.tools.api.ApiReq;

public class EndpointsViewMock implements EndpointsViewModel {

    private long currentCollection;
    private String currentEndpoint;
    private String currentMode;
    private UserEndpoints model;

    public EndpointsViewMock(UserEndpoints model) {
        super();
        this.model = model;
        this.currentCollection = model.getCollections().get(0).getCollectionId();
        this.currentEndpoint = model.getCollections().get(0).getEndpoints().get(0).getId();
        this.currentMode = "normal";
    }

    @Override
    public long getCurrentCollection() {
        return currentCollection;
    }

    @Override
    public void setCurrentCollection(long currentCollection) {
        this.currentCollection = currentCollection;
    }

    @Override
    public String getCurrentEndpoint() {
        return currentEndpoint;
    }

    @Override
    public void setCurrentEndpoint(String currentEndpoint) {
        this.currentEndpoint = currentEndpoint;
    }

    @Override
    public String getCurrentMode() {
        return currentMode;
    }

    @Override
    public void setCurrentMode(String currentMode) {
        this.currentMode = currentMode;
    }

    @Override
    public UserEndpoints getModel() {
        return model;
    }

    @Override
    public void setModel(UserEndpoints model) {
        this.model = model;
    }

    @Override
    public List<EndpointsList> getUserCollections() {
        return model.getCollections();
    }

    @Override
    public List<PairValue> getCollectionTitles() {
        return getUserCollections().stream().map(t -> {
            return new PairValue(new Object[]{"collectionTitle", t.getCollectionTitle(), "collectionId", t.getCollectionId()});
        }).collect(Collectors.toList());
    }

    @Override
    public List<ApiReq> getUserEndpoints(long collectionId) {
        return getUserCollections().stream().filter(t -> {
            return t.getCollectionId() == collectionId;
        }).findFirst().get().getEndpoints();
    }

    @Override
    public ApiReq getUserEndpoint(long collectionId, String endpointId) {
        return getUserEndpoints(collectionId).stream().filter(ep -> {
            return ep.getId().equals(endpointId);
        }).findFirst().get();
    }

    @Override
    public void addNewEndpoint(long collectionId, ApiReq endpoint) {
        endpoint.setId(UUID.randomUUID().toString());
        List<EndpointsList> endpoints = model.getCollections();

        endpoints.stream().filter(item -> {
            return item.getCollectionId() == collectionId;
        }).findFirst().get().getEndpoints().add(endpoint);
    }

    @Override
    public void updateEndpoint(long collectionId, ApiReq endpoint) {
    }

    @Override
    public void dropEndpoint(long collectionId, String endpointId) {
    }
}
