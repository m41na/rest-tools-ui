package com.jarredweb.rest.tools.ui.resource;

import com.jarredweb.rest.tools.ui.features.EndpointsFeatures;
import com.jarredweb.rest.tools.ui.provider.AppUserProvider;
import com.practicaldime.common.entity.rest.ApiAssert;
import com.practicaldime.common.entity.rest.ApiReq;
import com.practicaldime.common.entity.rest.EndpointsList;
import com.practicaldime.common.util.AResult;
import com.practicaldime.router.core.handler.HttpStatusCode;
import com.practicaldime.router.core.server.IServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
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

    public IServer link(IServer app) {
        homeView(app)
                .restView(app)
                .restJson(app)
                .invokeEndpoint(app)
                .invokePayload(app)
                .createCollection(app)
                .updateCollection(app)
                .deleteCollection(app)
                .createEndpoint(app)
                .updateEndpoint(app)
                .deleteEndpoint(app)
                .createAssertion(app)
                .updateAssertion(app)
                .deleteAssertion(app);
        return app;
    }

    public RestToolsResource homeView(IServer app) {
        app.get("/ws/rest/", (req, res, done) -> {
            res.redirect(HttpStatusCode.See_Other.code, "/");
        });
        return this;
    }

    public RestToolsResource restView(IServer app) {
        app.get("/rest", (req, res, done) -> {
//            if (user != null) { //TODO fix me
//                res.redirect(HttpStatusCode.See_Other.code, "/");
//            } else {
            res.send(HttpStatusCode.Not_Found.code, "A user value is expected but was null");
//            }
        });
        return this;
    }

    public RestToolsResource restJson(IServer app) {
        app.get("/rest/{uid}", null, "application/json", null, (req, res, done) -> {
            Long userId = req.param("uid", Long.class);
            Map<String, Object> model = service.getApplicationModel().startViewModel();
            model.put("nav", null); //TODO fix me
            model.put("rest", service.getViewModel(userId));
            res.status(HttpStatusCode.Ok.code);
            res.json(model);
        });
        return this;
    }

    public RestToolsResource invokeEndpoint(IServer app) {
        app.get("/rest/{uid}/execute/{id}", "application/json", "application/json", null, (req, res, done) -> {
            Long userId = req.param("uid", Long.class);
            String id = req.param("id");
            Map<String, Object> model = service.invokeEndpoint(userId, id);
            res.status(HttpStatusCode.Ok.code);
            res.json(model);
        });
        return this;
    }

    public RestToolsResource invokePayload(IServer app) {
        app.post("/rest/{uid}/execute", "application/json", "application/json", null, (req, res, done) -> {
            Long userId = req.param("uid", Long.class);
            ApiReq endpoint = req.body(ApiReq.class);
            Map<String, Object> model = service.invokeEndpoint(userId, endpoint);
            res.status(HttpStatusCode.Ok.code);
            res.json(model);
        });
        return this;
    }

//	@POST
//    @Path("/rest/{uid}/upload")
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    @Produces(MediaType.APPLICATION_JSON)
//    public IServer uploadEndpoints(@PathParam("uid") Long userId,
//            @FormDataParam("file") InputStream uploadStream,
//            @FormDataParam("file") FormDataContentDisposition fileMetaData) {
//		List<EndpointsList> uendp = service.uploadEndpoints(userId, uploadStream);
//		//List<UserEndpoints> uendp = new InputStreamLoader(uploadStream).readValue(new TypeReference<List<UserEndpoints>>(){});
//        //prepare and send response
//        return Response.ok(uendp).build();
//    }

//	@GET
//    @Path("/rest/{uid}/download")
//    @Produces(MediaType.APPLICATION_OCTET_STREAM)
//    public Response downloadEndpoints(@PathParam("uid") Long userId) {
//        UserEndpoints uep = service.downloadEndpoints(userId);
//
//        //prepare response
//        StreamingOutput outputStream = (OutputStream output) -> {
//            String json = service.endpointsToJson(uep);;
//            output.write(json.getBytes());
//        };
//
//        //send response
//        return Response.ok(outputStream, MediaType.APPLICATION_OCTET_STREAM)
//                .header("content-disposition", "attachment; filename = rest-file.json")
//                .build();
//    }

    public RestToolsResource createCollection(IServer app) {
        app.post("/rest/{uid}/collection", "application/json", "application/json", null, (req, res, done) -> {
            Long userId = req.param("uid", Long.class);
            String title = req.param("title");
            AResult<EndpointsList> created = service.createCollection(userId, title);

            //prepare and send response
            if (created.code == 0) {
                res.json(created.data);
            } else {
                Map<String, Object> model = new HashMap<>();
                model.put("error", created.errorString());
                model.put("userId", userId);
                model.put("title", title);
                res.json(model);
            }
        });
        return this;
    }

    public RestToolsResource updateCollection(IServer app) {
        app.put("/rest/{uid}/collection/{cid}", null, "application/json", null, (req, res, done) -> {
            Long userId = req.param("uid", Long.class);
            Long collId = req.param("cid", Long.class);
            String title = req.param("title");
            AResult<Integer> updated = service.updateCollection(userId, collId, title);

            //prepare and send response
            if (updated.code == 0) {
                res.json(updated.data);
            } else {
                Map<String, Object> model = new HashMap<>();
                model.put("error", updated.errorString());
                model.put("userId", userId);
                model.put("collectionId", collId);
                model.put("title", title);
                res.json(model);
            }
        });
        return this;
    }

    public RestToolsResource deleteCollection(IServer app) {
        app.delete("/rest/{uid}/collection/{cid}", null, "application/json", null, (req, res, done) -> {
            Long userId = req.param("uid", Long.class);
            Long collId = req.param("cid", Long.class);
            AResult<Integer> removed = service.deleteCollection(userId, collId);

            //prepare and send response
            if (removed.code == 0) {
                res.json(removed.data);
            } else {
                Map<String, Object> model = new HashMap<>();
                model.put("error", removed.errorString());
                model.put("userId", userId);
                model.put("collectionId", collId);
                res.json(model);
            }
        });
        return this;
    }

    public RestToolsResource createEndpoint(IServer app) {
        app.post("/rest/{uid}/collection/{cid}/endpoint", "application/json", "application/json", null, (req, res, done) -> {
            Long userId = req.param("uid", Long.class);
            Long collId = req.param("cid", Long.class);
            ApiReq endpoint = req.body(ApiReq.class);

            AResult<ApiReq> created = service.createEndpoint(userId, collId, endpoint);

            //prepare and send response
            if (created.code == 0) {
                res.json(created.data);
            } else {
                Map<String, Object> model = new HashMap<>();
                model.put("error", created.errorString());
                model.put("userId", userId);
                model.put("collectionId", collId);
                model.put("entity", endpoint);
                res.json(model);
            }
        });
        return this;
    }

    public RestToolsResource updateEndpoint(IServer app) {
        app.put("/rest/{uid}/collection/{cid}/endpoint", "application/json", "application/json", null, (req, res, done) -> {
            Long userId = req.param("uid", Long.class);
            Long collId = req.param("cid", Long.class);
            ApiReq endpoint = req.body(ApiReq.class);
            AResult<Integer> updated = service.updateEndpoint(userId, collId, endpoint);

            //prepare and send response
            if (updated.code == 0) {
                res.json(updated.data);
            } else {
                Map<String, Object> model = new HashMap<>();
                model.put("error", updated.errorString());
                model.put("userId", userId);
                model.put("collectionId", collId);
                model.put("entity", endpoint);
                res.json(model);
            }
        });
        return this;
    }

    public RestToolsResource deleteEndpoint(IServer app) {
        app.delete("/rest/{uid}/collection/{cid}/endpoint/{eid}", null, "application/json", null, (req, res, done) -> {
            Long userId = req.param("uid", Long.class);
            Long collId = req.param("cid", Long.class);
            String endpoint = req.param("eid");
            AResult<Integer> removed = service.deleteEndpoint(userId, collId, endpoint);

            //prepare and send response
            if (removed.code == 0) {
                res.json(removed.data);
            } else {
                Map<String, Object> model = new HashMap<>();
                model.put("error", removed.errorString());
                model.put("userId", userId);
                model.put("collectionId", collId);
                model.put("endpointId", endpoint);
                res.json(model);
            }
        });
        return this;
    }

    public RestToolsResource createAssertion(IServer app) {
        app.post("/rest/{uid}/collection/{cid}/endpoint/{eid}/assertion", "application/json", "application/json", null, (req, res, done) -> {
            Long userId = req.param("uid", Long.class);
            Long collId = req.param("cid", Long.class);
            String endpoint = req.param("eid");
            ApiAssert<String> assertion = req.body(ApiAssert.class);
            AResult<ApiAssert> created = service.createAssertion(userId, collId, endpoint, assertion);

            //prepare and send response
            if (created.code == 0) {
                res.json(created);
            } else {
                Map<String, Object> model = new HashMap<>();
                model.put("error", created.errorString());
                model.put("userId", userId);
                model.put("collectionId", collId);
                model.put("entity", assertion);
                res.json(model);
            }
        });
        return this;
    }

    public RestToolsResource updateAssertion(IServer app) {
        app.put("/rest/{uid}/collection/{cid}/endpoint/{eid}/assertion", "application/json", "application/json", null, (req, res, done) -> {
            Long userId = req.param("uid", Long.class);
            Long collId = req.param("cid", Long.class);
            String endpoint = req.param("eid");
            ApiAssert<String> assertion = req.body(ApiAssert.class);
            AResult<Integer> updated = service.updateAssertion(userId, collId, endpoint, assertion);

            //prepare and send response
            if (updated.code == 0) {
                res.json(updated);
            } else {
                Map<String, Object> model = new HashMap<>();
                model.put("error", updated.errorString());
                model.put("userId", userId);
                model.put("collectionId", collId);
                model.put("entity", assertion);
                res.json(model);
            }
        });
        return this;
    }

    public RestToolsResource deleteAssertion(IServer app) {
        app.delete("/rest/{uid}/collection/{cid}/endpoint/{eid}/assertion/{aid}", null, "application/json", null, (req, res, done) -> {
            Long userId = req.param("uid", Long.class);
            Long collId = req.param("cid", Long.class);
            String endpoint = req.param("eid");
            Long assertId = req.param("aid", Long.class);
            AResult<Integer> removed = service.deleteAssertion(userId, collId, endpoint, assertId);

            //prepare and send response
            if (removed.code == 0) {
                res.json(removed.data);
            } else {
                Map<String, Object> model = new HashMap<>();
                model.put("error", removed.errorString());
                model.put("userId", userId);
                model.put("collectionId", collId);
                model.put("endpointId", endpoint);
                model.put("assertionId", assertId);
                res.json(model);
            }
        });
        return this;
    }

//	public Navigation buildNavModel(UriInfo uriInfo) {
//        List<RouteLink> links = new ArrayList<>();
//        links.add(new RouteLink("Rest Client", uriInfo.getBaseUriBuilder().path("/restview").build().toASCIIString()));
//        links.add(new RouteLink("Work Notes", uriInfo.getBaseUriBuilder().path("/notesview").build().toASCIIString()));
//        return new Navigation("Simple Tools", links);
//    }
}
