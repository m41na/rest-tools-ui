package com.jarredweb.rest.tools.ui.resource;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
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

import com.jarredweb.common.util.AppException;
import com.jarredweb.common.util.AppResult;
import com.jarredweb.common.util.ResStatus;
import com.jarredweb.domain.simple.tools.Navigation;
import com.jarredweb.domain.simple.tools.RouteLink;
import com.jarredweb.domain.users.AppUser;
import com.jarredweb.rest.tools.ui.service.RestToolsService;

import works.hop.rest.tools.model.ApiAssert;
import works.hop.rest.tools.model.ApiReq;
import works.hop.rest.tools.model.EndpointsList;
import works.hop.rest.tools.model.UserEndpoints;

@Path("/")
public class RestToolsResource {

    private static final Logger LOG = LoggerFactory.getLogger(RestToolsResource.class);
    @Context
    private UriInfo uriInfo;
    @Inject
    private RestToolsService service;

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

	@Path("/rest/{uid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response restJson(@PathParam("uid") Long userId) {
        Map<String, Object> model = service.getApplicationModel().startViewModel();
        model.put("nav", buildNavModel(uriInfo));
        model.put("rest", service.getViewModel(userId));
        return Response.ok(model).build();
    }
    
	@Path("/rest/{uid}/reset")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response resetJson(@PathParam("uid") Long userId) {
        Map<String, Object> model = service.getApplicationModel().startViewModel();
        model.put("nav", buildNavModel(uriInfo));
        model.put("rest", service.getViewModel(userId, true));
        return Response.ok(model).build();
    }

	@GET
    @Path("/rest/{uid}/execute/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response invokeEndpoint(@PathParam("uid") Long userId, @PathParam("id") String id) {
		Map<String, Object> model = service.invokeEndpoint(userId, id);
        return Response.ok(model).build();
    }

	@POST
    @Path("/rest/{uid}/execute")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response invokeEndpoint(@PathParam("uid") Long userId, ApiReq endpoint) {
		Map<String, Object> model = service.invokeEndpoint(userId, endpoint);
        return Response.ok(model).build();
    }

	@POST
    @Path("/rest/{uid}/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadEndpoints(@PathParam("uid") Long userId,
            @FormDataParam("file") InputStream uploadStream,
            @FormDataParam("file") FormDataContentDisposition fileMetaData) {
		List<EndpointsList> uendp = service.uploadEndpoints(userId, uploadStream);
        //prepare and send response
        return Response.ok(uendp).build();
    }

	@GET
    @Path("/rest/{uid}/download")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadEndpoints(@PathParam("uid") Long userId) {
        UserEndpoints uep = service.downloadEndpoints(userId);
        
        //prepare response
        StreamingOutput outputStream = (OutputStream output) -> {
            String json = service.endpointsToJson(uep);;
            output.write(json.getBytes());
        };
        
        //send response
        return Response.ok(outputStream, MediaType.APPLICATION_OCTET_STREAM)
                .header("content-disposition", "attachment; filename = rest-file.json")
                .build();
    }
    
	@POST
    @Path("/rest/{uid}/collection")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCollection(@PathParam("uid") Long userId, @FormParam("title") String title) {
        AppResult<EndpointsList> created = service.createCollection(userId, title);
        
        //prepare and send response
        if(created.getCode() == 0){  
            return Response.ok(created.getEntity()).build();
        }
        else{
            Map<String, Object> model = new HashMap<>();
            model.put("error", created.getError());
            model.put("reason", created.getMessage());
            model.put("userId", userId);
            model.put("title", title);
            return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
        }
    }
    
	@PUT
    @Path("/rest/{uid}/collection/{cid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCollection(@PathParam("uid") Long userId, @PathParam("cid") Long collId, @FormParam("title") String title) {
        AppResult<Integer> updated = service.updateCollection(userId, collId, title);
        
        //prepare and send response
        if(updated.getCode() == 0){  
            return Response.ok(updated.getEntity()).build();
        }
        else{
            Map<String, Object> model = new HashMap<>();
            model.put("error", updated.getError());
            model.put("reason", updated.getMessage());
            model.put("userId", userId);
            model.put("collectionId", collId);
            model.put("title", title);
            return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
        }
    }
    
	@DELETE
    @Path("/rest/{uid}/collection/{cid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCollection(@PathParam("uid") Long userId, @PathParam("cid") Long collId) {
        AppResult<Integer> removed = service.deleteCollection(userId, collId);
        
        //prepare and send response
        if(removed.getCode() == 0){  
            return Response.ok(removed.getEntity()).build();
        }
        else{
            Map<String, Object> model = new HashMap<>();
            model.put("error", removed.getError());
            model.put("reason", removed.getMessage());
            model.put("userId", userId);
            model.put("collectionId", collId);
            return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
        }
    }
    
	@POST
    @Path("/rest/{uid}/collection/{cid}/endpoint")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createEndpoint(@PathParam("uid") Long userId, @PathParam("cid") Long collId, ApiReq endpoint) {
        AppResult<ApiReq> created = service.createEndpoint(userId, collId, endpoint);
        
        //prepare and send response
        if(created.getCode() == 0){  
            return Response.ok(created.getEntity()).build();
        }
        else{
            Map<String, Object> model = new HashMap<>();
            model.put("error", created.getError());
            model.put("reason", created.getMessage());
            model.put("userId", userId);
            model.put("collectionId", collId);
            model.put("entity", endpoint);
            return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
        }
    }
    
	@PUT
    @Path("/rest/{uid}/collection/{cid}/endpoint/{eid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEndpoint(@PathParam("uid") Long userId, @PathParam("cid") Long collId, ApiReq endpoint) {
        AppResult<Integer> updated = service.updateEndpoint(userId, collId, endpoint);
        
        //prepare and send response
        if(updated.getCode() == 0){  
            return Response.ok(updated.getEntity()).build();
        }
        else{
            Map<String, Object> model = new HashMap<>();
            model.put("error", updated.getError());
            model.put("reason", updated.getMessage());
            model.put("userId", userId);
            model.put("collectionId", collId);
            model.put("entity", endpoint);
            return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
        }
    }
    
	@DELETE
    @Path("/rest/{uid}/collection/{cid}/endpoint/{eid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteEndpoint(@PathParam("uid") Long userId, @PathParam("cid") Long collId, @PathParam("eid") String endpoint) {
        AppResult<Integer> removed = service.deleteEndpoint(userId, collId, endpoint);
        
        //prepare and send response
        if(removed.getCode() == 0){  
            return Response.ok(removed.getEntity()).build();
        }
        else{
            Map<String, Object> model = new HashMap<>();
            model.put("error", removed.getError());
            model.put("reason", removed.getMessage());
            model.put("userId", userId);
            model.put("collectionId", collId);
            model.put("endpointId", endpoint);
            return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
        }
    }
    
	@POST
    @Path("/rest/{uid}/collection/{cid}/endpoint/{eid}/assertion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAssertion(@PathParam("uid") Long userId, @PathParam("cid") Long collId, @PathParam("eid") String endpId, ApiAssert<String> assertion) {
        AppResult<ApiAssert> created = service.createAssertion(userId, collId, endpId, assertion);
        
        //prepare and send response
        if(created.getCode() == 0){  
            return Response.ok(created.getEntity()).build();
        }
        else{
            Map<String, Object> model = new HashMap<>();
            model.put("error", created.getError());
            model.put("reason", created.getMessage());
            model.put("userId", userId);
            model.put("collectionId", collId);
            model.put("entity", assertion);
            return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
        }
    }
    
	@PUT
    @Path("/rest/{uid}/collection/{cid}/endpoint/{eid}/assertion/{aid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAssertion(@PathParam("uid") Long userId, @PathParam("cid") Long collId, @PathParam("eid") String endpId, ApiAssert<String> assertion) {
        AppResult<Integer> updated = service.updateAssertion(userId, collId, endpId, assertion);
        
        //prepare and send response
        if(updated.getCode() == 0){  
            return Response.ok(updated.getEntity()).build();
        }
        else{
            Map<String, Object> model = new HashMap<>();
            model.put("error", updated.getError());
            model.put("reason", updated.getMessage());
            model.put("userId", userId);
            model.put("collectionId", collId);
            model.put("entity", assertion);
            return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
        }
    }
    
	@DELETE
    @Path("/rest/{uid}/collection/{cid}/endpoint/{eid}/assertion/{aid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAssertiont(@PathParam("uid") Long userId, @PathParam("cid") Long collId, @PathParam("eid") String endpoint, @PathParam("aid") Long assertId) {
        AppResult<Integer> removed = service.deleteAssertiont(userId, collId, endpoint, assertId);
        
        //prepare and send response
        if(removed.getCode() == 0){  
            return Response.ok(removed.getEntity()).build();
        }
        else{
            Map<String, Object> model = new HashMap<>();
            model.put("error", removed.getError());
            model.put("reason", removed.getMessage());
            model.put("userId", userId);
            model.put("collectionId", collId);
            model.put("endpointId", endpoint);
            model.put("assertionId", assertId);
            return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
        }
    }
	
	public Navigation buildNavModel(UriInfo uriInfo) {
        List<RouteLink> links = new ArrayList<>();
        links.add(new RouteLink("Rest Client", uriInfo.getBaseUriBuilder().path("/restview").build().toASCIIString()));
        links.add(new RouteLink("Work Notes", uriInfo.getBaseUriBuilder().path("/notesview").build().toASCIIString()));
        return new Navigation("Simple Tools", links);
    }
}
