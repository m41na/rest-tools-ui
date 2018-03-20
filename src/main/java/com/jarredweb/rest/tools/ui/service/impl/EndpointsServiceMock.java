package com.jarredweb.rest.tools.ui.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jarredweb.rest.tools.ui.model.*;
import com.jarredweb.rest.tools.ui.service.EndpointsService;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import works.hop.rest.tools.api.ApiAssert;
import works.hop.rest.tools.api.ApiReq;
import works.hop.rest.tools.util.LongIdGenerator;
import works.hop.rest.tools.util.MockIdGenerator;
import works.hop.rest.tools.util.StringIdGenerator;

@Service("mock")
public class EndpointsServiceMock implements EndpointsService {

    private final static EndpointsService INSTANCE = new EndpointsServiceMock();
    private final EndpointsModel viewModel;
    private final IdGenerator idGen = new IdGenerator();

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
        endpoint.setId(idGen.nextStringId());
        List<EndpointsList> endpoints = getViewModel(userId).getModel().getMergedCollections();

        endpoints.stream().filter(item -> {
            return item.getCollectionId() == collectionId;
        }).findFirst().get().getEndpoints().add(endpoint);
    }

    @Override
    public void updateEndpoint(Long userId, long collectionId, ApiReq endpoint) {
        List<EndpointsList> endpoints = getViewModel(userId).getModel().getMergedCollections();

        List<ApiReq> list = endpoints.stream().filter(item -> {
            return item.getCollectionId() == collectionId;
        }).findFirst().get().getEndpoints();

        //replace endpoint in collection
        list.removeIf(rq -> rq.getId().equals(endpoint.getId()));
        list.add(endpoint);
    }

    @Override
    public void dropEndpoint(Long userId, long collectionId, String endpointId) {
        List<EndpointsList> endpoints = getViewModel(userId).getModel().getMergedCollections();

        List<ApiReq> list = endpoints.stream().filter(item -> {
            return item.getCollectionId() == collectionId;
        }).findFirst().get().getEndpoints();

        list.removeIf(rq -> rq.getId().equals(endpointId));
    }

    @Override
    public ApiAssert getEndpointAssertion(Long userId, Long collectionId, String endpointId, Long assertId) {
        return getEndpointAssertions(userId, collectionId, endpointId).stream().filter(as -> as.getId().equals(assertId)).findFirst().get();
    }

    @Override
    public List<ApiAssert<?>> getEndpointAssertions(Long userId, Long collectionId, String endpointId) {
        List<EndpointsList> endpoints = getViewModel(userId).getModel().getMergedCollections();

        return endpoints.stream().filter(item -> {
            return item.getCollectionId() == collectionId;
        }).findFirst().get().getEndpoints().stream().filter(ep -> ep.getId().equals(endpointId)).findFirst().get().getAssertions();
    }

    @Override
    public void addNewAssertion(Long userId, Long collectionId, String endpointId, ApiAssert assertion) {
        assertion.setId(idGen.nextLongId());
        List<EndpointsList> endpoints = getViewModel(userId).getModel().getMergedCollections();

        endpoints.stream().filter(item -> {
            return item.getCollectionId() == collectionId;
        }).findFirst().get().getEndpoints().stream().filter(ep -> ep.getId().equals(endpointId)).findFirst().get().getAssertions().add(assertion);

    }

    @Override
    public void updateAssertion(Long userId, Long collectionId, String endpointId, ApiAssert assertion) {
        List<EndpointsList> assertions = getViewModel(userId).getModel().getMergedCollections();

        List<ApiAssert<?>> list = assertions.stream().filter(item -> {
            return item.getCollectionId() == collectionId;
        }).findFirst().get().getEndpoints().stream().filter(ep -> ep.getId().equals(endpointId)).findFirst().get().getAssertions();

        list.removeIf(as -> as.getId().equals(assertion.getId()));
        list.add(assertion);
    }

    @Override
    public void dropAssertion(Long userId, Long collectionId, String endpointId, long assertId) {
        List<EndpointsList> assertions = getViewModel(userId).getModel().getMergedCollections();

        List<ApiAssert<?>> list = assertions.stream().filter(item -> {
            return item.getCollectionId() == collectionId;
        }).findFirst().get().getEndpoints().stream().filter(ep -> ep.getId().equals(endpointId)).findFirst().get().getAssertions();

        list.removeIf(as -> as.getId() == assertId);
    }

    private EndpointsModel restViewModel() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream src = getClass().getClassLoader().getResourceAsStream("json/rest.json")) {
            List<UserEndpoints> endpoints = mapper.readValue(src, new TypeReference<List<UserEndpoints>>() {
            });
            return new EndpointsModel(endpoints.get(0));
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    public static class IdGenerator {

        private final Map<String, MockIdGenerator<?>> idGens = new HashMap<>();
        private final String LONGS = "longs";
        private final String STRINGS = "strings";

        public Long nextLongId() {
            if (idGens.get(LONGS) == null) {
                idGens.put(LONGS, new LongIdGenerator(LONGS, 0l));
            }
            return (Long) idGens.get(LONGS).nextId();
        }

        public Long nextLongId(String table, Long start) {
            if (table != null) {
                if (idGens.get(table) == null) {
                    if (start != null) {
                        idGens.put(table, new LongIdGenerator(table, 0l));
                    } else {
                        idGens.put(table, new LongIdGenerator(table, start));
                    }
                }
                return (Long) idGens.get(table).nextId();
            }
            return nextLongId();
        }

        public String nextStringId() {
            if (idGens.get(STRINGS) == null) {
                idGens.put(STRINGS, new StringIdGenerator(STRINGS));
            }
            return (String) idGens.get(STRINGS).nextId();
        }

        public String nextStringId(String table) {
            if (table != null) {
                if (idGens.get(table) == null) {
                    idGens.put(table, new StringIdGenerator(table));
                }
                return (String) idGens.get(table).nextId();
            }
            return nextStringId();
        }
    }
}
