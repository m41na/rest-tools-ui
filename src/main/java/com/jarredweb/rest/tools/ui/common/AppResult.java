package com.jarredweb.rest.tools.ui.common;

public class AppResult<T> {

    private ResStatus status;
    private T entity;

    public AppResult() {
        super();
        this.status = new ResStatus();
    }

    public AppResult(T entity) {
        super();
        this.entity = entity;
    }

    public AppResult(ResStatus status) {
        super();
        this.status = status;
    }

    public AppResult(String message) {
        super();
        this.status = new ResStatus(1, message, null);
    }

    public AppResult(String message, String reason) {
        super();
        this.status = new ResStatus(1, message, reason);
    }

    public AppResult(int code, String message, String reason) {
        super();
        this.status = new ResStatus(code, message, reason);
    }

    public ResStatus getStatus() {
        return status;
    }

    public void setStatus(ResStatus status) {
        this.status = status;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public int getCode() {
        return status != null ? status.getCode() : 0;
    }

    public String getReason() {
        return status != null ? status.getReason() : null;
    }

    public String getMessage() {
        return status != null ? status.getMessage() : null;
    }
}
