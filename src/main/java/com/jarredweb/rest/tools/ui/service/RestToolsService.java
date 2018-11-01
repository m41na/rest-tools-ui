package com.jarredweb.rest.tools.ui.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.jarredweb.common.util.AppResult;

import works.hop.rest.tools.model.ApiAssert;
import works.hop.rest.tools.model.ApiReq;
import works.hop.rest.tools.model.ApplicationModel;
import works.hop.rest.tools.model.EndpointsList;
import works.hop.rest.tools.model.EndpointsModel;
import works.hop.rest.tools.model.UserEndpoints;

public interface RestToolsService {

	ApplicationModel getApplicationModel();
	
	EndpointsModel getViewModel(Long userId);
    
    EndpointsModel getViewModel(Long userId, Boolean reset);
	
	Map<String, Object> invokeEndpoint(Long userId, String id);

	Map<String, Object> invokeEndpoint(Long userId, ApiReq endpoint);

	List<EndpointsList> uploadEndpoints(Long userId, InputStream uploadStream);

	UserEndpoints downloadEndpoints(Long userId);
	
	String endpointsToJson(UserEndpoints endps);

	AppResult<EndpointsList> createCollection(Long userId, String title);

	AppResult<Integer> updateCollection(Long userId, Long collId, String title);

	AppResult<Integer> deleteCollection(Long userId, Long collId);

	AppResult<ApiReq> createEndpoint(Long userId, Long collId, ApiReq endpoint);

	AppResult<Integer> updateEndpoint(Long userId, Long collId, ApiReq endpoint);

	AppResult<Integer> deleteEndpoint(Long userId, Long collId, String endpoint);

	AppResult<ApiAssert> createAssertion(Long userId, Long collId, String endpId, ApiAssert<String> assertion);

	AppResult<Integer> updateAssertion(Long userId, Long collId, String endpId, ApiAssert<String> assertion);

	AppResult<Integer> deleteAssertiont(Long userId, Long collId, String endpoint, Long assertId);
}