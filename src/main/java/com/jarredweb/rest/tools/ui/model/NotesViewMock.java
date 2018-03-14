package com.jarredweb.rest.tools.ui.model;

import java.util.List;
import java.util.UUID;

public class NotesViewMock implements NotesViewModel {

    private String currentNote;
    private String currentFilter;
    private UserNotes model;

    public NotesViewMock(UserNotes model) {
        super();
        this.model = model;
        this.currentNote = model.getNotes().stream().findFirst().get().getNoteId();
    }

    @Override
    public String getCurrentNote() {
        return currentNote;
    }

    @Override
    public void setCurrentNote(String currentNote) {
        this.currentNote = currentNote;
    }

    @Override
    public String getCurrentFilter() {
        return currentFilter;
    }

    @Override
    public void setCurrentFilter(String currentFilter) {
        this.currentFilter = currentFilter;
    }

    @Override
    public UserNotes getModel() {
        return model;
    }

    @Override
    public void setModel(UserNotes model) {
        this.model = model;
    }

    @Override
    public UserNotes getUserNotes() {
        return model;
    }

    @Override
    public Note getNote(String noteId) {
        return getUserNotes().getNotes().stream().filter(n -> {
            return n.getNoteId().equals(noteId);
        }).findFirst().get();
    }

    @Override
    public String addNewNote(Note note) {
        note.setNoteId(UUID.randomUUID().toString());
        getUserNotes().getNotes().add(note);
        return note.getNoteId();
    }

    @Override
    public List<Note> getFavoriteNotes() {
        return getUserNotes().getFavoriteNotes();
    }

    @Override
    public void updateNote(Note note) {
        List<Note> notes = getUserNotes().getNotes();
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getNoteId().equals(note.getNoteId())) {
                notes.add(i, note);
                break;
            }
        }
    }

    @Override
    public void deleteNote(String noteId) {
        List<Note> notes = getUserNotes().getNotes();
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getNoteId().equals(noteId)) {
                notes.remove(i);
                break;
            }
        }
    }
}
