package com.jarredweb.rest.tools.ui.model;

import java.util.List;
import java.util.stream.Collectors;

public class UserNotes {

    private long userId;
    private String userName;
    private List<Note> notes;

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

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public List<Note> getFavoriteNotes() {
        return notes.stream().filter(n -> {
            return n.isFavorite();
        }).collect(Collectors.toList());
    }
}
