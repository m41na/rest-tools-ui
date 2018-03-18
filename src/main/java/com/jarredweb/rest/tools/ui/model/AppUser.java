package com.jarredweb.rest.tools.ui.model;

import java.io.Serializable;
import java.util.Objects;

public class AppUser implements Serializable, Comparable<AppUser>{
    
    private final Long userId;
    private final String username;
    private String fullName;

    public AppUser(String username, Long userId, String fullName) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.userId);
        hash = 71 * hash + Objects.hashCode(this.username);
        return hash;
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
        final AppUser other = (AppUser) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        return Objects.equals(this.userId, other.userId);
    }

    @Override
    public int compareTo(AppUser that) {
        if (this == that) {
            return 0;
        }
        if (this.userId > that.userId) {
            return 1;
        }
        if (this.userId < that.userId) {
            return -1;
        }
        return this.username.compareTo(that.username);
    }

    @Override
    public String toString() {
        return "AppUser{" + "userId=" + userId + ", username=" + username + ", fullName=" + fullName + '}';
    }
}
