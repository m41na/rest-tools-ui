package com.jarredweb.rest.tools.ui.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jarredweb.common.util.AppResult;

import works.hop.plugins.api.PlugAid;
import works.hop.rest.tools.model.ApiAssert;
import works.hop.rest.tools.model.ApiReq;
import works.hop.rest.tools.model.ApplicationModel;
import works.hop.rest.tools.model.EndpointsList;
import works.hop.rest.tools.model.EndpointsModel;
import works.hop.rest.tools.model.UserEndpoints;

@Service
public class RestToolsServiceImpl implements RestToolsService {

	@Autowired
    private EndpointsService endpointsService;
	
	@Override
	public ApplicationModel getApplicationModel() {
		return PlugAid.use(endpointsService.get()).func("getApplicationModel").call().pop(ApplicationModel.class);
	}

	@Override
	public EndpointsModel getViewModel(Long userId) {
		return PlugAid.use(endpointsService.get()).func("getViewModel", Long.class).call(userId).pop(EndpointsModel.class);
	}

	@Override
	public EndpointsModel getViewModel(Long userId, Boolean reset) {
		return PlugAid.use(endpointsService.get()).func("getViewModel", Long.class, Boolean.class).call(userId, reset).pop(EndpointsModel.class);
	}

	@Override
	public Map<String, Object> invokeEndpoint(Long userId, String id) {
		return (Map<String, Object>)PlugAid.use(endpointsService.get()).func("invokeEndpoint", Long.class, String.class).call(userId, id).pop();
	}

	@Override
	public Map<String, Object> invokeEndpoint(Long userId, ApiReq endpoint) {
		return (Map<String, Object>)PlugAid.use(endpointsService.get()).func("invokeEndpoint", Long.class, ApiReq.class).call(userId, endpoint).pop();
	}

	@Override
	public List<EndpointsList> uploadEndpoints(Long userId, InputStream input) {
		return (List<EndpointsList>)PlugAid.use(endpointsService.get()).func("uploadEndpoints", Long.class, InputStream.class).call(userId, input).pop();
	}

	@Override
	public UserEndpoints downloadEndpoints(Long userId) {
		return PlugAid.use(endpointsService.get()).func("downloadEndpoints", Long.class).call(userId).pop(UserEndpoints.class);
	}

	@Override
	public String endpointsToJson(UserEndpoints endps) {
		return PlugAid.use(endpointsService.get()).func("endpointsToJson", UserEndpoints.class).call(endps).pop(String.class);
	}

	@Override
	public AppResult<EndpointsList> createCollection(Long userId, String title) {
		return (AppResult<EndpointsList>)PlugAid.use(endpointsService.get()).func("addNewCollection", Long.class, String.class).call(userId, title).pop();
	}

	@Override
	public AppResult<Integer> updateCollection(Long userId, Long collId, String title) {
		return (AppResult<Integer>)PlugAid.use(endpointsService.get()).func("updateCollection", Long.class, Long.class, String.class).call(userId, collId, title).pop();
	}

	@Override
	public AppResult<Integer> deleteCollection(Long userId, Long collId) {
		return (AppResult<Integer>)PlugAid.use(endpointsService.get()).func("dropCollection", Long.class, Long.class).call(userId, collId).pop();
	}

	@Override
	public AppResult<ApiReq> createEndpoint(Long userId, Long collId, ApiReq endpoint) {
		return (AppResult<ApiReq>)PlugAid.use(endpointsService.get()).func("addNewEndpoint", Long.class, Long.class, ApiReq.class).call(userId, collId, endpoint).pop();
	}

	@Override
	public AppResult<Integer> updateEndpoint(Long userId, Long collId, ApiReq endpoint) {
		return (AppResult<Integer>)PlugAid.use(endpointsService.get()).func("updateEndpoint", Long.class, Long.class, ApiReq.class).call(userId, collId, endpoint).pop();
	}

	@Override
	public AppResult<Integer> deleteEndpoint(Long userId, Long collId, String endpoint) {
		return (AppResult<Integer>)PlugAid.use(endpointsService.get()).func("dropEndpoint", Long.class, Long.class, String.class).call(userId, collId, endpoint).pop();
	}

	@Override
	public AppResult<ApiAssert> createAssertion(Long userId, Long collId, String endpId, ApiAssert<String> assertion) {
		return (AppResult<ApiAssert>)PlugAid.use(endpointsService.get()).func("addNewAssertion", Long.class, Long.class, String.class, ApiAssert.class).call(userId, collId, endpId, assertion).pop();
	}

	@Override
	public AppResult<Integer> updateAssertion(Long userId, Long collId, String endpId, ApiAssert<String> assertion) {
		return (AppResult<Integer>)PlugAid.use(endpointsService.get()).func("updateAssertion", Long.class, Long.class, String.class, ApiAssert.class).call(userId, collId, endpId, assertion).pop();
	}

	@Override
	public AppResult<Integer> deleteAssertiont(Long userId, Long collId, String endpoint, Long assertId) {
		return (AppResult<Integer>)PlugAid.use(endpointsService.get()).func("dropAssertion", Long.class, Long.class, String.class, Long.class).call(userId, collId, endpoint, assertId).pop();
	}
}
