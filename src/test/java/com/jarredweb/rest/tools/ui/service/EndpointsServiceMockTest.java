package com.jarredweb.rest.tools.ui.service;

import com.jarredweb.rest.tools.ui.service.impl.EndpointsServiceMock;
import com.jarredweb.rest.tools.ui.model.EndpointsModel;
import org.junit.Test;
import static org.junit.Assert.*;

public class EndpointsServiceMockTest {
    
    private final EndpointsService service = new EndpointsServiceMock();

    @Test
    public void testGetRestViewModel() {
        EndpointsModel model = service.getViewModel(null);
        assertNotNull(model);
        assertEquals("Expecting 2", 2, model.getModel().getCollections().size());
    }
}
