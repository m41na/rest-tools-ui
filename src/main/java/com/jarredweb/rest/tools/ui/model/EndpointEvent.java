package com.jarredweb.rest.tools.ui.model;

import java.io.Serializable;

public class EndpointEvent implements Comparable<EndpointEvent>, Serializable {

    private static final long serialVersionUID = 1L;
    private String requestPath; //url path
    private String requestType; //post, get, put
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((requestPath == null) ? 0 : requestPath.hashCode());
        result = prime * result + ((requestType == null) ? 0 : requestType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        EndpointEvent other = (EndpointEvent) obj;
        if (requestPath == null) {
            if (other.requestPath != null) {
                return false;
            }
        } else if (!requestPath.equals(other.requestPath)) {
            return false;
        }
        if (requestType == null) {
            if (other.requestType != null) {
                return false;
            }
        } else if (!requestType.equals(other.requestType)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(EndpointEvent o) {
        if (o == this) {
            return 0;
        }
        if (requestPath.equals(o.requestPath) && requestType.equals(o.requestType)) {
            return 0;
        }
        return requestPath.compareTo(o.requestPath);
    }

    @Override
    public String toString() {
        return "EndpointEvent [requestPath=" + requestPath + ", requestType=" + requestType + ", success=" + success
                + "]";
    }
}
