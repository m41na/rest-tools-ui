package com.jarredweb.rest.tools.ui.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.UriInfo;

public class ApplicationModel {

    private final static ApplicationModel INSTANCE = new ApplicationModel();

    private ApplicationModel() {
    }

    public static ApplicationModel getInstance() {
        return INSTANCE;
    }

    public Map<String, Object> startViewModel() {
        Map<String, Object> model = new HashMap<>();
        model.put("title", "Rest Tools");
        model.put("username", "Admin");
        return model;
    }

    public Navigation buildNavModel(UriInfo uriInfo) {
        List<RouteLink> links = new ArrayList<>();
        links.add(new RouteLink("Rest Client", uriInfo.getBaseUriBuilder().path("mvc/rest").build().toASCIIString()));
        return new Navigation("Simple Tools", links);
    }
}
