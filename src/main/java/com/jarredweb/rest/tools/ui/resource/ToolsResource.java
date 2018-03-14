package com.jarredweb.rest.tools.ui.resource;

import com.jarredweb.rest.tools.ui.model.ApplicationModel;
import com.jarredweb.rest.tools.ui.service.ToolsServiceMock;
import com.jarredweb.rest.tools.ui.service.ToolsService;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import works.hop.rest.tools.client.ApiResListener;
import works.hop.rest.tools.client.AssertionResListener;
import works.hop.rest.tools.client.MultiPathJsonLoader;
import works.hop.rest.tools.client.RestConnector;
import works.hop.rest.tools.client.StringJsonLoader;

@Path("/")
public class ToolsResource {

    private static final Logger LOG = LoggerFactory.getLogger(ToolsResource.class);
    @Context
    private UriInfo uriInfo;
    private final ApplicationModel appModel = ApplicationModel.getInstance();
    private final ToolsService service = ToolsServiceMock.getInstance();

    @GET
    @PermitAll
    public Response homeView() {
        URI redirect = URI.create("/ws/rest");
        LOG.info("redirecting to {}", redirect.toString());
        return Response.seeOther(redirect).build();
    }

    @Path("rest")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response restView() {
        Map<String, Object> model = appModel.startViewModel();
        model.put("nav", appModel.buildNavModel(uriInfo));
        model.put("rest", service.getRestViewModel());
        return Response.ok(model).build();
    }

    @Path("notes")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response notesView() {
        Map<String, Object> model = appModel.startViewModel();
        model.put("nav", appModel.buildNavModel(uriInfo));
        model.put("notes", service.getNotesViewModel());
        return Response.ok(model).build();
    }

    @POST
    @Path("/execute/:id")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response invokeClientUsingFileSource(@PathParam("id") String id) {
        ApiResListener assertListener = new AssertionResListener();
        String file = "json/rest.json";
        RestConnector rest = new RestConnector(new MultiPathJsonLoader(file), assertListener);
        rest.run();

        //prepare and send response
        return Response.ok(assertListener.getAssertionResults()).build();
    }

    @POST
    @Path("/execute")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response invokeClientUsingStringSource(String source) {
        ApiResListener assertListener = new AssertionResListener();
        RestConnector rest = new RestConnector(new StringJsonLoader(source), assertListener);
        rest.run();

        //prepare and send response
        Map<String, Object> model = new HashMap<>();
        model.put("response", assertListener.getApiResponse());
        model.put("results", assertListener.getAssertionResults());
        return Response.ok(model).build();
    }
}
