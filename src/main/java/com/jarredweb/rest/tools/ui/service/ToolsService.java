package com.jarredweb.rest.tools.ui.service;

import com.jarredweb.rest.tools.ui.model.NotesViewModel;
import com.jarredweb.rest.tools.ui.model.EndpointsViewModel;

public interface ToolsService {

    EndpointsViewModel getRestViewModel();

    void setRestViewModel(EndpointsViewModel restViewModel);

    NotesViewModel getNotesViewModel();

    void setNotesViewModel(NotesViewModel notesViewModel);
}
