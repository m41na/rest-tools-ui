package com.jarredweb.rest.tools.ui.resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jarredweb.rest.tools.ui.model.ApplicationModel;
import com.jarredweb.rest.tools.ui.model.EndpointsList;
import com.jarredweb.rest.tools.ui.model.UserEndpoints;
import com.jarredweb.rest.tools.ui.persist.UserEndpointsDao;
import com.jarredweb.rest.tools.ui.service.EndpointsService;
import com.jarredweb.webjar.common.bean.AppResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import works.hop.rest.tools.api.ApiReq;
import works.hop.rest.tools.client.ApiResListener;
import works.hop.rest.tools.client.AssertionResListener;
import works.hop.rest.tools.client.InputStreamLoader;
import works.hop.rest.tools.client.MultiPathJsonLoader;
import works.hop.rest.tools.client.RestConnector;
import works.hop.rest.tools.util.SimpleJson;

@Path("/")
public class RestToolsResource {

    private static final Logger LOG = LoggerFactory.getLogger(RestToolsResource.class);
    @Context
    private UriInfo uriInfo;
    private final ApplicationModel appModel = ApplicationModel.getInstance();
    @Inject
    @Named("mock")
    private EndpointsService service;
    @Inject
    private UserEndpointsDao endpDao;

    @GET
    @PermitAll
    public Response homeView() {
        URI redirect = URI.create("/ws/rest");
        LOG.info("redirecting to {}", redirect.toString());
        return Response.seeOther(redirect).build();
    }

    @Path("user/{uid}/rest")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response restView(@PathParam("uid") Long userId) {
        Map<String, Object> model = appModel.startViewModel();
        model.put("nav", appModel.buildNavModel(uriInfo));
        model.put("rest", service.getViewModel(userId));
        return Response.ok(model).build();
    }

    @POST
    @Path("/user/{uid}/execute/:id")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response invokeEndpoint(@PathParam("uid") Long userId, @PathParam("id") String id) {
        ApiResListener assertListener = new AssertionResListener();
        String file = "json/rest.json";
        RestConnector rest = new RestConnector(new MultiPathJsonLoader(file), assertListener);
        rest.run();

        //prepare and send response
        return Response.ok(assertListener.getAssertionResults()).build();
    }

    @POST
    @Path("/user/{uid}/execute")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response invokeEndpoint(@PathParam("uid") Long userId, ApiReq endpoint) {
        ApiResListener assertListener = new AssertionResListener();
        RestConnector rest = new RestConnector(null, assertListener);
        rest.executeEndpoint(endpoint);

        //prepare and send response
        Map<String, Object> model = new HashMap<>();
        model.put("response", assertListener.getApiResponse());
        model.put("results", assertListener.getAssertionResults());
        return Response.ok(model).build();
    }

    @POST
    @Path("/user/{uid}/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response uploadEndpoints(@PathParam("uid") Long userId,
            @FormDataParam("file") InputStream uploadStream,
            @FormDataParam("file") FormDataContentDisposition fileMetaData) {
        List<EndpointsList> collections = new InputStreamLoader(uploadStream).readValue(new TypeReference<List<EndpointsList>>() {
        });

        //update database
        collections.stream().forEach(collection -> {
            AppResult<EndpointsList> result = endpDao.createCollection(collection, userId);
            if (result.getCode() == 0) {
                service.getUserCollections(userId).add(result.getEntity());
            }
        });

        //prepare and send response
        return Response.ok(collections).build();
    }

    @GET
    @Path("/user/{uid}/download")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @PermitAll
    public Response downloadEndpoints(@PathParam("uid") Long userId) {
        UserEndpoints uep = endpDao.retrieveUserEndpoints(userId).getEntity();
        StreamingOutput fileStream = (OutputStream output) -> {
            String json = SimpleJson.toJson(uep);
            output.write(json.getBytes());
        };
        return Response.ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
                .header("content-disposition", "attachment; filename = rest-file.json")
                .build();
    }
}
