package com.jarredweb.rest.tools.ui.service;

import com.jarredweb.rest.tools.ui.model.EndpointsViewOps;

public interface ToolsService {

    EndpointsViewOps getRestViewModel();

    void setRestViewModel(EndpointsViewOps restViewModel);
}
