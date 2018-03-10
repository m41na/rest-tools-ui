package com.jarredweb.rest.tools.ui.model;

import java.util.List;

public class Navigation {

    private String title;
    private List<RouteLink> links;

    public Navigation(String title, List<RouteLink> links) {
        super();
        this.title = title;
        this.links = links;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<RouteLink> getLinks() {
        return links;
    }

    public void setLinks(List<RouteLink> links) {
        this.links = links;
    }
}
