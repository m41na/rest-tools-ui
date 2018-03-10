package com.jarredweb.rest.tools.ui.model;

import java.util.HashMap;
import java.util.Map;

public class Endpoint {

    private Long id;
    private String name;
    private String method;
    private String url;
    private String description;
    private String path;
    private String queryParams;
    private String[] consumes = {};
    private String[] produces = {};
    private String[] authorized = {};
    private Map<String, String> headers = new HashMap<>();
    private String entity;
    private String response;
    private Integer status;
    private String onSuccess;
    private String onError;
    private Boolean execute;
    protected Map<String, String> envs = new HashMap<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getQueryParams() {
        return (queryParams != null) ? queryParams : "";
    }

    public void setQueryParams(String queryParams) {
        this.queryParams = queryParams;
    }

    public String[] getConsumes() {
        return consumes;
    }

    public void setConsumes(String[] consumes) {
        this.consumes = consumes;
    }

    public String[] getProduces() {
        return produces;
    }

    public void setProduces(String[] produces) {
        this.produces = produces;
    }

    public String[] getAuthorized() {
        return authorized;
    }

    public void setAuthorized(String[] authorized) {
        this.authorized = authorized;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOnSuccess() {
        return onSuccess;
    }

    public void setOnSuccess(String onSuccess) {
        this.onSuccess = onSuccess;
    }

    public String getOnError() {
        return onError;
    }

    public void setOnError(String onError) {
        this.onError = onError;
    }

    public Boolean getExecute() {
        return execute;
    }

    public void setExecute(Boolean execute) {
        this.execute = execute;
    }

    public Map<String, String> getEnvs() {
        return envs;
    }

    public void setEnvs(Map<String, String> envs) {
        this.envs = envs;
    }

    public Endpoint dup(Endpoint guest) {
        //ensure first that id, method and path do match
        if (!this.name.equalsIgnoreCase(guest.getName())
                || !this.method.equalsIgnoreCase(guest.getMethod())
                || !this.path.equalsIgnoreCase(guest.path)) {
            throw new RuntimeException(String.format("Looks like these two endpoints \r\n %s \r\n AND \r\n %s \r\n do not quite match", this, guest));
        }
        //copy without remorse
        if (queryParams != null) {
            guest.setQueryParams(queryParams);
        }
        if (consumes != null) {
            guest.setConsumes(consumes);
        }
        if (description != null) {
            guest.setDescription(description);
        }
        if (headers != null) {
            guest.setHeaders(headers);
        }
        if (produces != null) {
            guest.setProduces(produces);
        }
        if (entity != null && entity.trim().length() > 0) {
            guest.setEntity(entity);
        }
        //return happy guest
        return guest;
    }

    public static Map<String, String> stringToMap(String input) {
        Map<String, String> result = new HashMap<>();
        String[] pairs = input.split(",|;");
        for (String pair : pairs) {
            String[] keyPair = pair.split("=|:");
            String key = keyPair[0];
            String val = keyPair[1];
            if (result.keySet().contains(key)) {
                String value = result.get(key);
                value = value + ";" + val;
            } else {
                result.put(key, val);
            }
        }
        return result;
    }
}
