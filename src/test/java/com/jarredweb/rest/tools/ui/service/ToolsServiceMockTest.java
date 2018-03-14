package com.jarredweb.rest.tools.ui.service;

import com.jarredweb.rest.tools.ui.model.EndpointsViewModel;
import com.jarredweb.rest.tools.ui.model.NotesViewModel;
import org.junit.Test;
import static org.junit.Assert.*;

public class ToolsServiceMockTest {
    
    private final ToolsService service = ToolsServiceMock.getInstance();

    @Test
    public void testGetRestViewModel() {
        EndpointsViewModel model = service.getRestViewModel();
        assertNotNull(model);
        assertEquals("Expecting 2", 2, model.getUserCollections().size());
    }

    @Test
    public void testGetNotesViewModel() {
        NotesViewModel model = service.getNotesViewModel();
        assertNotNull(model);
        assertEquals("Expecting 3", 3, model.getUserNotes().getNotes().size());
    }
}
