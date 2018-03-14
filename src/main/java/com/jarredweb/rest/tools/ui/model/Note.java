package com.jarredweb.rest.tools.ui.model;

public class Note {

    private String noteId;
    private String content;
    private boolean favorite;

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getSummary() {
        return (content.length() > 30) ? content.substring(0, 30) : content;
    }
}
