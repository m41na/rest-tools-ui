package com.jarredweb.rest.tools.ui.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jarredweb.rest.tools.ui.model.EndpointsViewMock;
import com.jarredweb.rest.tools.ui.model.NotesViewMock;
import com.jarredweb.rest.tools.ui.model.UserEndpoints;
import com.jarredweb.rest.tools.ui.model.UserNotes;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import com.jarredweb.rest.tools.ui.model.NotesViewModel;
import com.jarredweb.rest.tools.ui.model.EndpointsViewModel;

public class ToolsServiceMock implements ToolsService {

    private static final ToolsService INSTANCE = new ToolsServiceMock();
    private EndpointsViewModel restViewModel;
    private NotesViewModel notesViewModel;

    private ToolsServiceMock() {
        super();
        restViewModel = restViewModel();
        notesViewModel = notesViewModel();
    }

    public static ToolsService getInstance() {
        return INSTANCE;
    }

    @Override
    public EndpointsViewModel getRestViewModel() {
        return restViewModel;
    }

    @Override
    public void setRestViewModel(EndpointsViewModel restViewModel) {
        this.restViewModel = restViewModel;
    }

    @Override
    public NotesViewModel getNotesViewModel() {
        return notesViewModel;
    }

    @Override
    public void setNotesViewModel(NotesViewModel notesViewModel) {
        this.notesViewModel = notesViewModel;
    }

    private EndpointsViewModel restViewModel() {
        ObjectMapper mapper = new ObjectMapper();
        try(InputStream src = getClass().getClassLoader().getResourceAsStream("json/rest.json")) {
            List<UserEndpoints> endpoints = mapper.readValue(src, new TypeReference<List<UserEndpoints>>() {
            });
            return new EndpointsViewMock(endpoints.get(0));
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    private NotesViewModel notesViewModel() {
        InputStream src = getClass().getClassLoader().getResourceAsStream("json/notes.json");
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<UserNotes> endpoints = mapper.readValue(src, new TypeReference<List<UserNotes>>() {
            });
            return new NotesViewMock(endpoints.get(0));
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }
}
