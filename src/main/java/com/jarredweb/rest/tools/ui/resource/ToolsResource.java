package com.jarredweb.rest.tools.ui.resource;

import com.jarredweb.rest.tools.ui.model.ApplicationModel;
import com.jarredweb.rest.tools.ui.service.MockToolsService;
import com.jarredweb.rest.tools.ui.service.ToolsService;
import java.util.Map;
import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.glassfish.jersey.server.mvc.Viewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
public class ToolsResource {
    
    private static final Logger LOG = LoggerFactory.getLogger(ToolsResource.class);
    @Context
    private UriInfo uriInfo;
    private final ApplicationModel appModel = ApplicationModel.getInstance();
    private final ToolsService service = MockToolsService.getInstance();

    @GET
    @Produces(MediaType.TEXT_HTML)
    @PermitAll
    public Response homeView() {
        Map<String, Object> model = appModel.startViewModel();
        model.put("nav", appModel.buildNavModel(uriInfo));
        model.put("title", "Rest Tools Home");
        model.put("view", "rest");
        model.put("content", service.getRestViewModel());
        LOG.info("responding with 'default' page");
        return Response.ok(new Viewable("/index", model)).build();
    }
}
