package com.jarredweb.rest.tools.ui.resource;

import com.jarredweb.rest.tools.ui.features.EndpointsFeatures;
import com.jarredweb.rest.tools.ui.provider.AppUserProvider;
import com.practicaldime.common.entity.rest.ApiAssert;
import com.practicaldime.common.entity.rest.ApiReq;
import com.practicaldime.common.entity.rest.EndpointsList;
import com.practicaldime.common.entity.rest.UserEndpoints;
import com.practicaldime.common.entity.view.Navigation;
import com.practicaldime.common.entity.view.RouteLink;
import com.practicaldime.common.model.AppUser;
import com.practicaldime.common.util.AResult;
import com.practicaldime.common.util.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RestToolsResource {

    private static final Logger LOG = LoggerFactory.getLogger(RestToolsResource.class);

    private final AppUserProvider appUser;
    private final EndpointsFeatures service;

    public RestToolsResource(AppUserProvider appUser, EndpointsFeatures service) {
        this.appUser = appUser;
        this.service = service;
    }

    @GET
    public Response homeView(@Context AppUser user) {
        URI redirect = URI.create("/ws/rest/" + user.userId);
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
            throw new AppException(400, "A user value is expected but was null");
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
		//List<UserEndpoints> uendp = new InputStreamLoader(uploadStream).readValue(new TypeReference<List<UserEndpoints>>(){});
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
        AResult<EndpointsList> created = service.createCollection(userId, title);
        
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
        AResult<Integer> updated = service.updateCollection(userId, collId, title);
        
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
        AResult<Integer> removed = service.deleteCollection(userId, collId);
        
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
        AResult<ApiReq> created = service.createEndpoint(userId, collId, endpoint);
        
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
        AResult<Integer> updated = service.updateEndpoint(userId, collId, endpoint);
        
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
        AResult<Integer> removed = service.deleteEndpoint(userId, collId, endpoint);
        
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
        AResult<ApiAssert> created = service.createAssertion(userId, collId, endpId, assertion);
        
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
        AResult<Integer> updated = service.updateAssertion(userId, collId, endpId, assertion);
        
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
        AResult<Integer> removed = service.deleteAssertiont(userId, collId, endpoint, assertId);
        
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
