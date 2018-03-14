package com.jarredweb.rest.tools.ui.model;

import java.util.List;
import works.hop.rest.tools.api.ApiReq;
import works.hop.rest.tools.client.RestConnector;

public class UserEndpoints {

    private long userId;
    private String userName;
    private ApiReq template;
    private List<EndpointsList> collections;
    private boolean merged = false;

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

    public ApiReq getTemplate() {
        return template;
    }

    public void setTemplate(ApiReq template) {
        this.template = template;
    }

    public List<EndpointsList> getCollections() {
        if(!merged){
            this.collections.stream().forEach((list) -> {
                List<ApiReq> endpoints = list.getEndpoints();
                list.setEndpoints(RestConnector.mergeEndpoints(template, endpoints));
                merged = true;
            });
        }
        return collections;
    }

    public void setCollections(List<EndpointsList> collections) {
        this.collections = collections;
    }
}
