package com.jarredweb.rest.tools.ui.model;

import java.util.List;

public class UserEndpoints {

    private long userId;
    private String userName;
    private Endpoint template;
    private List<EndpointsList> collections;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String user) {
        this.userName = user;
    }

    public Endpoint getTemplate() {
        return template;
    }

    public void setTemplate(Endpoint template) {
        this.template = template;
    }

    public List<EndpointsList> getCollections() {
        return collections;
    }

    public void setCollections(List<EndpointsList> collections) {
        this.collections = collections;
    }
}
