package com.jarredweb.rest.tools.ui.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jarredweb.rest.tools.ui.model.EndpointsViewMock;
import com.jarredweb.rest.tools.ui.model.EndpointsViewOps;
import com.jarredweb.rest.tools.ui.model.UserEndpoints;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MockToolsService implements ToolsService {

    private static final ToolsService INSTANCE = new MockToolsService();
    private EndpointsViewOps restViewModel;

    private MockToolsService() {
        super();
        restViewModel = restViewModel();
    }

    public static ToolsService getInstance() {
        return INSTANCE;
    }

    @Override
    public EndpointsViewOps getRestViewModel() {
        return restViewModel;
    }

    @Override
    public void setRestViewModel(EndpointsViewOps restViewModel) {
        this.restViewModel = restViewModel;
    }
    
    private EndpointsViewOps restViewModel() {
        InputStream src = getClass().getClassLoader().getResourceAsStream("json/rest.json");
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<UserEndpoints> endpoints = mapper.readValue(src, new TypeReference<List<UserEndpoints>>() {
            });
            return new EndpointsViewMock(endpoints);
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }
}
