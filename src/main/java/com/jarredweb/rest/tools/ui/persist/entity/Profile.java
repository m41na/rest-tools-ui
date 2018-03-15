package com.jarredweb.rest.tools.ui.persist.entity;

import java.util.Date;

public class Profile {

    private long id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private Date createdTs;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getCreatedTs() {
        return createdTs;
    }

    public void setCreatedTs(Date createTs) {
        this.createdTs = createTs;
    }
}
