package com.jarredweb.rest.tools.ui.persist.entity;

import java.util.Date;

public class Account {

    private long id;
    private String username;
    private String password;
    private Role role;
    private Status status;
    private Profile profile;
    private Date createdTs;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Date getCreatedTs() {
        return createdTs;
    }

    public void setCreatedTs(Date createTs) {
        this.createdTs = createTs;
    }
}
