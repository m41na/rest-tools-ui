package com.jarredweb.rest.tools.ui.model;

public class EndpointsModel {

    private long currentCollection;
    private String currentEndpoint;
    private String currentMode;
    private UserEndpoints model;

    public EndpointsModel(UserEndpoints model) {
        super();
        this.model = model;
        this.currentCollection = model.getMergedCollections().get(0).getCollectionId();
        this.currentEndpoint = model.getMergedCollections().get(0).getEndpoints().get(0).getId();
        this.currentMode = "normal";
    }

    public long getCurrentCollection() {
        return currentCollection;
    }

    public void setCurrentCollection(long currentCollection) {
        this.currentCollection = currentCollection;
    }

    public String getCurrentEndpoint() {
        return currentEndpoint;
    }

    public void setCurrentEndpoint(String currentEndpoint) {
        this.currentEndpoint = currentEndpoint;
    }

    public String getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(String currentMode) {
        this.currentMode = currentMode;
    }

    public UserEndpoints getModel() {
        return model;
    }

    public void setModel(UserEndpoints model) {
        this.model = model;
    }
}
