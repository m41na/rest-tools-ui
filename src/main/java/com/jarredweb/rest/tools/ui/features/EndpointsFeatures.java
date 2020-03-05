package com.jarredweb.rest.tools.ui.features;

import com.practicaldime.common.entity.rest.*;
import com.practicaldime.common.util.AResult;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface EndpointsFeatures {

	ApplicationModel getApplicationModel();
	
	EndpointsModel getViewModel(Long userId);
    
    EndpointsModel getViewModel(Long userId, Boolean reset);
	
	Map<String, Object> invokeEndpoint(Long userId, String id);

	Map<String, Object> invokeEndpoint(Long userId, ApiReq endpoint);

	List<EndpointsList> uploadEndpoints(Long userId, InputStream uploadStream);

	UserEndpoints downloadEndpoints(Long userId);
	
	String endpointsToJson(UserEndpoints endps);

	AResult<EndpointsList> createCollection(Long userId, String title);

	AResult<Integer> updateCollection(Long userId, Long collId, String title);

	AResult<Integer> deleteCollection(Long userId, Long collId);

	AResult<ApiReq> createEndpoint(Long userId, Long collId, ApiReq endpoint);

	AResult<Integer> updateEndpoint(Long userId, Long collId, ApiReq endpoint);

	AResult<Integer> deleteEndpoint(Long userId, Long collId, String endpoint);

	AResult<ApiAssert> createAssertion(Long userId, Long collId, String endpId, ApiAssert<String> assertion);

	AResult<Integer> updateAssertion(Long userId, Long collId, String endpId, ApiAssert<String> assertion);

	AResult<Integer> deleteAssertion(Long userId, Long collId, String endpoint, Long assertId);
}