package com.jarredweb.rest.tools.ui.model;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class EndpointsViewMock implements EndpointsViewOps {

    private long currentUser;
    private long currentCollection;
    private long currentEndpoint;
    private String currentMode;
    private List<UserEndpoints> model;
    private final AtomicLong idGen = new AtomicLong(7);

    public EndpointsViewMock(List<UserEndpoints> model) {
        super();
        this.model = model;
        this.currentUser = model.get(0).getUserId();
        this.currentCollection = model.get(0).getCollections().get(0).getCollectionId();
        this.currentEndpoint = model.get(0).getCollections().get(0).getEndpoints().get(0).getId();
        this.currentMode = "normal";
    }

    @Override
    public long getCurrentUser() {
        return currentUser;
    }

    @Override
    public void setCurrentUser(long currentUser) {
        this.currentUser = currentUser;
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
    public long getCurrentEndpoint() {
        return currentEndpoint;
    }

    @Override
    public void setCurrentEndpoint(long currentEndpoint) {
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
    public List<UserEndpoints> getModel() {
        return model;
    }

    @Override
    public void setModel(List<UserEndpoints> model) {
        this.model = model;
    }

    @Override
    public List<PairValue> getUsers() {
        return model.stream().map((UserEndpoints t) -> new PairValue(new Object[]{"userName", t.getUserName(), "userId", t.getUserId()})).collect(Collectors.toList());
    }

    @Override
    public List<EndpointsList> getUserCollections(long userId) {
        return model.stream().filter(t -> {
            return t.getUserId() == userId;
        }).findFirst().get().getCollections();
    }

    @Override
    public List<PairValue> getCollectionTitles(long userId) {
        return getUserCollections(userId).stream().map(t -> {
            return new PairValue(new Object[]{"collectionTitle", t.getCollectionTitle(), "collectionId", t.getCollectionId()});
        }).collect(Collectors.toList());
    }

    @Override
    public List<Endpoint> getUserEndpoints(long userId, long collectionId) {
        return getUserCollections(userId).stream().filter(t -> {
            return t.getCollectionId() == collectionId;
        }).findFirst().get().getEndpoints();
    }

    @Override
    public Endpoint getUserEndpoint(long userId, long collectionId, long endpointId) {
        return getUserEndpoints(endpointId, endpointId).stream().filter(ep -> {
            return ep.getId() == endpointId;
        }).findFirst().get();
    }

    @Override
    public void addNewEndpoint(long userId, long collectionId, Endpoint endpoint) {
        endpoint.setId(idGen.incrementAndGet());
        List<EndpointsList> endpoints = model.stream().filter(item -> {
            return item.getUserId() == userId;
        }).findFirst().get().getCollections();

        endpoints.stream().filter(item -> {
            return item.getCollectionId() == collectionId;
        }).findFirst().get().getEndpoints().add(endpoint);
    }

    @Override
    public void updateEndpoint(long userId, long collectionId, Endpoint endpoint) {
    }

    @Override
    public void dropEndpoint(long userId, long collectionId, long endpointId) {
    }
}
