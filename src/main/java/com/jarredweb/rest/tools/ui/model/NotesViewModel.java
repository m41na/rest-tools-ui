package com.jarredweb.rest.tools.ui.model;

import java.util.List;

public interface NotesViewModel {

    String getCurrentNote();

    void setCurrentNote(String currentNote);

    String getCurrentFilter();

    void setCurrentFilter(String currentFilter);

    UserNotes getModel();

    void setModel(UserNotes model);

    UserNotes getUserNotes();

    Note getNote(String noteId);

    String addNewNote(Note note);

    List<Note> getFavoriteNotes();

    void updateNote(Note note);

    void deleteNote(String noteId);
}
