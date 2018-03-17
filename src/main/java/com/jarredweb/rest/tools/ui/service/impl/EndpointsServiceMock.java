package com.jarredweb.rest.tools.ui.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jarredweb.rest.tools.ui.model.*;
import com.jarredweb.rest.tools.ui.service.EndpointsService;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import works.hop.rest.tools.api.ApiReq;

@Service("mock")
public class EndpointsServiceMock implements EndpointsService {

    private final static EndpointsService INSTANCE = new EndpointsServiceMock();
    private final EndpointsModel viewModel;

    public EndpointsServiceMock() {
        super();
        this.viewModel = restViewModel();
    }

    public static EndpointsService getInstance() {
        return INSTANCE;
    }

    @Override
    public EndpointsModel getViewModel(Long userId) {
        return viewModel;
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

    private EndpointsModel restViewModel() {
        ObjectMapper mapper = new ObjectMapper();
        try(InputStream src = getClass().getClassLoader().getResourceAsStream("json/rest.json")) {
            List<UserEndpoints> endpoints = mapper.readValue(src, new TypeReference<List<UserEndpoints>>() {
            });
            return new EndpointsModel(endpoints.get(0));
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }
}
