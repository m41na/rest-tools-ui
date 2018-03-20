package com.jarredweb.rest.tools.ui.resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jarredweb.rest.tools.ui.model.AppUser;
import com.jarredweb.rest.tools.ui.model.ApplicationModel;
import com.jarredweb.rest.tools.ui.model.EndpointsList;
import com.jarredweb.rest.tools.ui.model.UserEndpoints;
import com.jarredweb.rest.tools.ui.persist.UserEndpointsDao;
import com.jarredweb.rest.tools.ui.service.EndpointsService;
import com.jarredweb.webjar.common.bean.AppResult;
import com.jarredweb.webjar.common.bean.ResStatus;
import com.jarredweb.webjar.common.exception.AppException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
    @Named("persist")
    private EndpointsService service;
    @Inject
    private UserEndpointsDao endpDao;

    @GET
    public Response homeView(@Context AppUser user) {
        URI redirect = URI.create("/ws/rest/" + user.getUserId());
        LOG.info("redirecting to {}", redirect.toString());
        return Response.seeOther(redirect).build();
    }
    
    @Path("/rest")
    @GET
    public Response restView(@Context AppUser user) {
        if(user != null){
            return homeView(user);
        }
        else{
            throw new AppException(new ResStatus(1, "A user value is expected but was null"));
        }
    }

    @Path("rest/{uid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response restJson(@PathParam("uid") Long userId) {
        Map<String, Object> model = appModel.startViewModel();
        model.put("nav", appModel.buildNavModel(uriInfo));
        model.put("rest", service.getViewModel(userId));
        return Response.ok(model).build();
    }

    @POST
    @Path("/rest/{uid}/execute/:id")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response invokeEndpoint(@PathParam("uid") Long userId, @PathParam("id") String id) {
        ApiResListener assertListener = new AssertionResListener();
        String file = "json/rest.json";
        RestConnector rest = new RestConnector(new MultiPathJsonLoader(file), assertListener);
        rest.run();

        //prepare and send response
        return Response.ok(assertListener.getAssertionResults()).build();
    }

    @POST
    @Path("/rest/{uid}/execute")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
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
    @Path("/rest/{uid}/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
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
    @Path("/rest/{uid}/download")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadEndpoints(@PathParam("uid") Long userId) {
        UserEndpoints uep = endpDao.retrieveUserEndpoints(userId).getEntity();
        
        //prepare response
        StreamingOutput outputStream = (OutputStream output) -> {
            String json = SimpleJson.toJson(uep);
            output.write(json.getBytes());
        };
        
        //send response
        return Response.ok(outputStream, MediaType.APPLICATION_OCTET_STREAM)
                .header("content-disposition", "attachment; filename = rest-file.json")
                .build();
    }
    
    @POST
    @Path("/rest/{uid}/collection/{cid}/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createEndpoint(@PathParam("uid") Long userId, @PathParam("cid") Long collId, ApiReq endpoint) {
        AppResult<ApiReq> created = endpDao.createApiRequest(collId, endpoint);
        
        //prepare and send response
        if(created.getCode() == 0){  
            return Response.ok(created.getEntity()).build();
        }
        else{
            Map<String, Object> model = new HashMap<>();
            model.put("error", created.getMessage());
            model.put("reason", created.getReason());
            model.put("userId", userId);
            model.put("collectionId", collId);
            model.put("entity", endpoint);
            return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
        }
    }
    
    @PUT
    @Path("/rest/{uid}/collection/{cid}/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEndpoint(@PathParam("uid") Long userId, @PathParam("cid") Long collId, ApiReq endpoint) {
        AppResult<Integer> updated = endpDao.updateApiRequest(collId, endpoint);
        
        //prepare and send response
        if(updated.getCode() == 0){  
            return Response.ok(updated.getEntity()).build();
        }
        else{
            Map<String, Object> model = new HashMap<>();
            model.put("error", updated.getMessage());
            model.put("reason", updated.getReason());
            model.put("userId", userId);
            model.put("collectionId", collId);
            model.put("entity", endpoint);
            return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
        }
    }
    
    @DELETE
    @Path("/rest/{uid}/collection/{cid}/drop/{eid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteEndpoint(@PathParam("uid") Long userId, @PathParam("cid") Long collId, @PathParam("eid") String endpoint) {
        AppResult<Integer> removed = endpDao.removeApiRequest(collId, endpoint);
        
        //prepare and send response
        if(removed.getCode() == 0){  
            return Response.ok(removed.getEntity()).build();
        }
        else{
            Map<String, Object> model = new HashMap<>();
            model.put("error", removed.getMessage());
            model.put("reason", removed.getReason());
            model.put("userId", userId);
            model.put("collectionId", collId);
            model.put("endpointId", endpoint);
            return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
        }
    }
}
