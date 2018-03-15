package com.jarredweb.rest.tools.ui.common;

public class ResStatus {

    private int code;
    private String reason;
    private String message;

    public ResStatus() {
        super();
        this.code = 0;
    }

    public ResStatus(int code, String message) {
        this();
        this.code = code;
        this.message = message;
    }

    public ResStatus(int code, String message, String reason) {
        this(code, message);
        this.reason = reason;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
