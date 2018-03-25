package com.jarredweb.rest.tools.ui.model;

import java.util.ArrayList;
import java.util.List;
import works.hop.rest.tools.api.ApiReq;

public class EndpointsList {

    private long collectionId;
    private String collectionTitle;
    private List<ApiReq> endpoints = new ArrayList<>();

    public long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(long collectionId) {
        this.collectionId = collectionId;
    }

    public String getCollectionTitle() {
        return collectionTitle;
    }

    public void setCollectionTitle(String title) {
        this.collectionTitle = title;
    }

    public List<ApiReq> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<ApiReq> endpoints) {
        this.endpoints = endpoints;
    }
}
