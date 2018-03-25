package com.jarredweb.rest.tools.ui.service.impl;

import com.jarredweb.rest.tools.ui.model.*;
import com.jarredweb.rest.tools.ui.persist.UserAccountDao;
import com.jarredweb.rest.tools.ui.persist.UserAssertionsDao;
import com.jarredweb.rest.tools.ui.persist.UserEndpointsDao;
import com.jarredweb.rest.tools.ui.service.EndpointsCache;
import com.jarredweb.rest.tools.ui.service.EndpointsService;
import com.jarredweb.rest.tools.ui.service.PersistService;
import com.jarredweb.webjar.common.bean.AppResult;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import works.hop.rest.tools.api.ApiAssert;
import works.hop.rest.tools.api.ApiReq;
import works.hop.rest.tools.client.RestConnector;

@Service("persist")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class EndpointsServiceImpl implements EndpointsService, PersistService {

    @Autowired
    private EndpointsCache endpCache;
    @Autowired
    private UserAccountDao accountDao;
    @Autowired
    private UserEndpointsDao endpDao;
    @Autowired
    private UserAssertionsDao assertDao;
    @Autowired
    private ApiReq templateReq;
    
    @Override
    public EndpointsCache getEndpointsCache() {
        return this.endpCache;
    }

    @Override
    public void setEndpointsCache(EndpointsCache cache) {
        this.endpCache = cache;
    }

    @Override
    public UserAccountDao getUserAccountDao() {
        return this.accountDao;
    }

    @Override
    public void setUserAccountDao(UserAccountDao userDao) {
        this.accountDao = userDao;
    }

    @Override
    public UserEndpointsDao getUserEndpointsDao() {
        return this.endpDao;
    }

    @Override
    public void setUserEndpointsDao(UserEndpointsDao endpDao) {
        this.endpDao = endpDao;
    }

    @Override
    public EndpointsModel getViewModel(Long userId) {
        EndpointsModel model = endpCache.getUserViewModel(userId);
        if (model == null) {
            UserEndpoints endp = endpDao.retrieveUserEndpoints(userId).getEntity();
            //retrieve assertions
            endp.getCollections().stream().forEach((list) -> {
                list.getEndpoints().stream().forEach((req) -> {
                    List<ApiAssert<?>> assertions = assertDao.retrieveEndpointAssertions(req.getId()).getEntity();
                    req.getAssertions().addAll(assertions);
                });
            });
            model = new EndpointsModel(endp);
            endpCache.cacheUserViewModel(userId, model);
        }
        return model;
    }
    
    @Override
    public EndpointsModel getViewModel(Long userId, Boolean reset) {
        if(reset){
            endpCache.resetUserViewModel(userId);
        }
        return getViewModel(userId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public AppResult<EndpointsList> addNewCollection(Long userId, String title) {
        EndpointsList collection = new EndpointsList();
        collection.setCollectionTitle(title);
        AppResult<EndpointsList> created = endpDao.createCollection(collection, userId);
        if(created.getCode() == 0){
            endpCache.getUserViewModel(userId).getModel().getCollections().add(created.getEntity());
        }
        return created;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public AppResult<Integer> updateCollection(Long userId, Long collId, String title) {
        AppResult<Integer> updated = endpDao.updateCollection(collId, title);
        if(updated.getCode() == 0){
            endpCache.getUserViewModel(userId).getModel().getCollections().forEach(c->{
                if(c.getCollectionId() == collId){
                    c.setCollectionTitle(title);
                }
            });
        }
        return updated;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public AppResult<Integer> dropCollection(Long userId, Long collId) {
        AppResult<Integer> dropped = endpDao.deleteCollection(collId);
        if(dropped.getCode() == 0){
            endpCache.getUserViewModel(userId).getModel().getCollections().removeIf(c-> c.getCollectionId() == collId);
        }
        return dropped;
    }

    @Override
    public List<EndpointsList> getUserCollections(Long userId) {
        return getViewModel(userId).getModel().getMergedCollections();
    }

    @Override
    public List<PairValue> getCollectionTitles(Long userId) {
        return getUserCollections(userId).stream().map(t -> {
            return new PairValue(new Object[]{"collectionTitle", t.getCollectionTitle(), "collectionId", t.getCollectionId()});
        }).collect(Collectors.toList());
    }

    @Override
    public List<ApiReq> getUserEndpoints(Long userId, long collectionId) {
        return getUserCollections(userId).stream().filter(t -> {
            return t.getCollectionId() == collectionId;
        }).findFirst().get().getEndpoints();
    }

    @Override
    public ApiReq getUserEndpoint(Long userId, long collectionId, String endpointId) {
        return getUserEndpoints(userId, collectionId).stream().filter(ep -> {
            return ep.getId().equals(endpointId);
        }).findFirst().get();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public AppResult<ApiReq> addNewEndpoint(Long userId, long collectionId, ApiReq endpoint) {
        endpoint = RestConnector.mergeEndpoint(templateReq, endpoint);
        String nextId = "_" + System.currentTimeMillis();
        endpoint.setId(nextId);
        endpoint.setName(endpoint.getMethod() + " " + endpoint.getPath());
        endpoint = RestConnector.mergeEndpoint(templateReq, endpoint);
        AppResult<ApiReq> createResult = endpDao.createApiRequest(collectionId, endpoint);
        if (createResult.getCode() == 0) {
            List<EndpointsList> endpoints = getViewModel(userId).getModel().getMergedCollections();

            endpoints.stream().filter(item -> {
                return item.getCollectionId() == collectionId;
            }).findFirst().get().getEndpoints().add(createResult.getEntity());
        }
        return createResult;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public AppResult<Integer> updateEndpoint(Long userId, long collectionId, ApiReq endpoint) {
        ApiReq mergedEndpoint = RestConnector.mergeEndpoint(templateReq, endpoint);
        AppResult<Integer> updateResult = endpDao.updateApiRequest(collectionId, mergedEndpoint);
        if (updateResult.getCode() == 0) {
            List<EndpointsList> endpoints = getViewModel(userId).getModel().getMergedCollections();

            List<ApiReq> list = endpoints.stream().filter(item -> {
                return item.getCollectionId() == collectionId;
            }).findFirst().get().getEndpoints();
            
            //replace endpoint in collection
            list.removeIf(rq->rq.getId().equals(mergedEndpoint.getId()));
            list.add(mergedEndpoint);
        }
        return updateResult;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public AppResult<Integer> dropEndpoint(Long userId, long collectionId, String endpointId) {
        AppResult<Integer> dropResult = endpDao.removeApiRequest(collectionId, endpointId);
        if(dropResult.getCode() == 0){
            List<EndpointsList> endpoints = getViewModel(userId).getModel().getMergedCollections();

            List<ApiReq> list = endpoints.stream().filter(item -> {
                return item.getCollectionId() == collectionId;
            }).findFirst().get().getEndpoints();
            
            list.removeIf(rq->rq.getId().equals(endpointId));
        }
        return dropResult;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public AppResult<ApiAssert> addNewAssertion(Long userId, Long collectionId, String endpointId, ApiAssert assertion) {
        AppResult<ApiAssert> createResult = assertDao.createApiAssert(endpointId, assertion);
        if(createResult.getCode() == 0){
            ApiAssert created = createResult.getEntity();
            List<EndpointsList> endpoints = getViewModel(userId).getModel().getMergedCollections();

            endpoints.stream().filter(item -> {
                return item.getCollectionId() == collectionId;
            }).findFirst().get().getEndpoints().stream().filter(ep->ep.getId().equals(endpointId)).findFirst().get().getAssertions().add(created);
        }
        return createResult;
    }

    @Override
    public ApiAssert getEndpointAssertion(Long userId, Long collectionId, String endpointId, Long assertId) {
        return getEndpointAssertions(userId, collectionId, endpointId).stream().filter(e->e.getId().equals(assertId)).findFirst().get();
    }

    @Override
    public List<ApiAssert<?>> getEndpointAssertions(Long userId, Long collectionId, String endpointId) {
        List<EndpointsList> assertions = getViewModel(userId).getModel().getMergedCollections();
        List<ApiAssert<?>> cacheList = assertions.stream().filter(item -> {
                return item.getCollectionId() == collectionId;
            }).findFirst().get().getEndpoints().stream().filter(ep->ep.getId().equals(endpointId)).findFirst().get().getAssertions();
        
        return cacheList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public AppResult<Integer> updateAssertion(Long userId, Long collectionId, String endpointId, ApiAssert assertion) {
        AppResult<Integer> updateResult = assertDao.updateApiAssert(assertion);
        if(updateResult.getCode() == 0){
            List<EndpointsList> assertions = getViewModel(userId).getModel().getMergedCollections();

            List<ApiAssert<?>> list = assertions.stream().filter(item -> {
                return item.getCollectionId() == collectionId;
            }).findFirst().get().getEndpoints().stream().filter(ep->ep.getId().equals(endpointId)).findFirst().get().getAssertions();
            
            list.removeIf(as->as.getId().equals(assertion.getId()));
            list.add(assertion);
        }
        return updateResult;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public AppResult<Integer> dropAssertion(Long userId, Long collectionId, String endpointId, long assertId) {
        AppResult<Integer> dropResult = assertDao.removeApiAssert(assertId);
        if(dropResult.getCode() == 0){
            List<EndpointsList> assertions = getViewModel(userId).getModel().getMergedCollections();

            List<ApiAssert<?>> list = assertions.stream().filter(item -> {
                return item.getCollectionId() == collectionId;
            }).findFirst().get().getEndpoints().stream().filter(ep->ep.getId().equals(endpointId)).findFirst().get().getAssertions();
            
            list.removeIf(as->as.getId() == assertId);
        }
        return dropResult;
    }
}
