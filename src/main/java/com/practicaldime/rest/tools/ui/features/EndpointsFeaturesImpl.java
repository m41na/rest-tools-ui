package com.practicaldime.rest.tools.ui.features;

import com.practicaldime.common.entity.rest.*;
import com.practicaldime.common.util.AResult;
import com.practicaldime.plugins.api.PlugStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;


@Service
public class EndpointsFeaturesImpl implements EndpointsFeatures {

	@Autowired
    private EndpointsProvider service;
	
	@Override
	public ApplicationModel getApplicationModel() {
		return PlugStack.use(service.get()).push("getApplicationModel").call().pop(ApplicationModel.class);
	}

	@Override
	public EndpointsModel getViewModel(Long userId) {
		return PlugStack.use(service.get()).push("getViewModel", Long.class).call(userId).pop(EndpointsModel.class);
	}

	@Override
	public EndpointsModel getViewModel(Long userId, Boolean reset) {
		return PlugStack.use(service.get()).push("getViewModel", Long.class, Boolean.class).call(userId, reset).pop(EndpointsModel.class);
	}

	@Override
	public Map<String, Object> invokeEndpoint(Long userId, String id) {
		return (Map<String, Object>)PlugStack.use(service.get()).push("invokeEndpoint", Long.class, String.class).call(userId, id).pop();
	}

	@Override
	public Map<String, Object> invokeEndpoint(Long userId, ApiReq endpoint) {
		return (Map<String, Object>)PlugStack.use(service.get()).push("invokeEndpoint", Long.class, com.practicaldime.common.entity.rest.ApiReq.class).call(userId, endpoint).pop();
	}

	@Override
	public List<EndpointsList> uploadEndpoints(Long userId, InputStream input) {
		return (List<EndpointsList>)PlugStack.use(service.get()).push("uploadEndpoints", Long.class, InputStream.class).call(userId, input).pop();
	}

	@Override
	public UserEndpoints downloadEndpoints(Long userId) {
		return PlugStack.use(service.get()).push("downloadEndpoints", Long.class).call(userId).pop(UserEndpoints.class);
	}

	@Override
	public String endpointsToJson(UserEndpoints endps) {
		return PlugStack.use(service.get()).push("endpointsToJson", UserEndpoints.class).call(endps).pop(String.class);
	}

	@Override
	public AResult<EndpointsList> createCollection(Long userId, String title) {
		return (AResult<EndpointsList>)PlugStack.use(service.get()).push("addNewCollection", Long.class, String.class).call(userId, title).pop();
	}

	@Override
	public AResult<Integer> updateCollection(Long userId, Long collId, String title) {
		return (AResult<Integer>)PlugStack.use(service.get()).push("updateCollection", Long.class, Long.class, String.class).call(userId, collId, title).pop();
	}

	@Override
	public AResult<Integer> deleteCollection(Long userId, Long collId) {
		return (AResult<Integer>)PlugStack.use(service.get()).push("dropCollection", Long.class, Long.class).call(userId, collId).pop();
	}

	@Override
	public AResult<ApiReq> createEndpoint(Long userId, Long collId, ApiReq endpoint) {
		return (AResult<ApiReq>)PlugStack.use(service.get()).push("addNewEndpoint", Long.class, Long.class, ApiReq.class).call(userId, collId, endpoint).pop();
	}

	@Override
	public AResult<Integer> updateEndpoint(Long userId, Long collId, ApiReq endpoint) {
		return (AResult<Integer>)PlugStack.use(service.get()).push("updateEndpoint", Long.class, Long.class, ApiReq.class).call(userId, collId, endpoint).pop();
	}

	@Override
	public AResult<Integer> deleteEndpoint(Long userId, Long collId, String endpoint) {
		return (AResult<Integer>)PlugStack.use(service.get()).push("dropEndpoint", Long.class, Long.class, String.class).call(userId, collId, endpoint).pop();
	}

	@Override
	public AResult<ApiAssert> createAssertion(Long userId, Long collId, String endpId, ApiAssert<String> assertion) {
		return (AResult<ApiAssert>)PlugStack.use(service.get()).push("addNewAssertion", Long.class, Long.class, String.class, ApiAssert.class).call(userId, collId, endpId, assertion).pop();
	}

	@Override
	public AResult<Integer> updateAssertion(Long userId, Long collId, String endpId, ApiAssert<String> assertion) {
		return (AResult<Integer>)PlugStack.use(service.get()).push("updateAssertion", Long.class, Long.class, String.class, ApiAssert.class).call(userId, collId, endpId, assertion).pop();
	}

	@Override
	public AResult<Integer> deleteAssertion(Long userId, Long collId, String endpoint, Long assertId) {
		return (AResult<Integer>)PlugStack.use(service.get()).push("dropAssertion", Long.class, Long.class, String.class, Long.class).call(userId, collId, endpoint, assertId).pop();
	}
}
