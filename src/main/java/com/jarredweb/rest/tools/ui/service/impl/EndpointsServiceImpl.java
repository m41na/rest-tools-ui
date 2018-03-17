package com.jarredweb.rest.tools.ui.service.impl;

import com.jarredweb.rest.tools.ui.model.*;
import com.jarredweb.rest.tools.ui.persist.UserAccountDao;
import com.jarredweb.rest.tools.ui.persist.UserEndpointsDao;
import com.jarredweb.rest.tools.ui.service.EndpointsCache;
import com.jarredweb.rest.tools.ui.service.EndpointsService;
import com.jarredweb.rest.tools.ui.service.PersistService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import works.hop.rest.tools.api.ApiReq;

@Service("persist")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class EndpointsServiceImpl implements EndpointsService, PersistService {

    @Autowired
    private EndpointsCache endpCache;
    @Autowired
    private UserAccountDao accountDao;
    @Autowired
    private UserEndpointsDao endpDao;
    
    @Override
    public EndpointsCache getEndpointsCache(){
        return this.endpCache;
    }
    
    @Override
    public void setEndpointsCache(EndpointsCache cache){
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
        if(model == null){
            UserEndpoints endp = endpDao.retrieveUserEndpoints(userId).getEntity();
            model = new EndpointsModel(endp);
            endpCache.cacheUserViewModel(userId, model);
        }
        return model;
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
    public void addNewEndpoint(Long userId, long collectionId, ApiReq endpoint) {
        endpoint.setId(UUID.randomUUID().toString());
        List<EndpointsList> endpoints = getViewModel(userId).getModel().getMergedCollections();

        endpoints.stream().filter(item -> {
            return item.getCollectionId() == collectionId;
        }).findFirst().get().getEndpoints().add(endpoint);
    }

    @Override
    public void updateEndpoint(Long userId, long collectionId, ApiReq endpoint) {
    }

    @Override
    public void dropEndpoint(Long userId, long collectionId, String endpointId) {
    }
}
