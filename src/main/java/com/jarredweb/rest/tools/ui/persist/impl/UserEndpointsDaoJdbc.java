package com.jarredweb.rest.tools.ui.persist.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jarredweb.rest.tools.ui.model.EndpointsList;
import com.jarredweb.rest.tools.ui.model.UserEndpoints;
import com.jarredweb.rest.tools.ui.persist.UserEndpointsDao;
import com.jarredweb.webjar.common.bean.AppResult;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import works.hop.rest.tools.api.ApiReq;
import works.hop.rest.tools.util.RestToolsJson;

@Repository
public class UserEndpointsDaoJdbc implements UserEndpointsDao {

    private final NamedParameterJdbcTemplate template;

    @Autowired
    public UserEndpointsDaoJdbc(DataSource ds) {
        template = new NamedParameterJdbcTemplate(ds);
    }

    @Override
    public AppResult<EndpointsList> createCollection(EndpointsList collection, Long userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", collection.getCollectionTitle());
        params.put("owner", userId);

        String sql = "insert into tbl_endpoints_list (list_title, fk_list_owner, list_created_ts) values (:title, :owner, current_timestamp)";

        KeyHolder holder = new GeneratedKeyHolder();
        int res = template.update(sql, new MapSqlParameterSource(params), holder);
        collection.setCollectionId(holder.getKey().longValue());

        return (res > 0) ? new AppResult<>(collection) : new AppResult<>(1, "failed to create a new collection");
    }

    @Override
    public AppResult<Integer> updateCollection(Long id, String title) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("title", title);

        String sql = "update tbl_endpoints_list set list_title=:title where list_id=:id";

        int res = template.update(sql, params);
        return (res > 0) ? new AppResult<>(res) : new AppResult<>(1, "failed updating collection");
    }

    @Override
    public AppResult<Integer> deleteCollection(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        String sql = "delete from tbl_endpoints_list where list_id=:id";

        int res = template.update(sql, params);
        return (res > 0) ? new AppResult<>(res) : new AppResult<>(1, "failed deleting collection");
    }

    @Override
    public AppResult<EndpointsList> retrieveCollection(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        String sql = "SELECT * FROM tbl_endpoint_item e "
                + "inner join tbl_endpoints_list c on e.fk_parent_list=c.list_id WHERE c.list_id=:id";

        EndpointsList collection = template.query(sql, params, endpointsListExtractor());
        return collection != null ? new AppResult<>(collection) : new AppResult<>(1, "could not find collection");
    }

    @Override
    public AppResult<UserEndpoints> retrieveUserEndpoints(Long userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", userId);

        String sql = "SELECT * FROM tbl_endpoints_list c \n" +
                    "left outer join tbl_endpoint_item e on e.fk_parent_list=c.list_id\n" +
                    "inner join tbl_profile p on p.profile_id=c.fk_list_owner\n" +
                    "where c.fk_list_owner=:id order by c.list_id, e.endp_id;";

        UserEndpoints userEndp = template.query(sql, params, userEndpointsExtractor());
        AppResult<ApiReq> baseTemplate = retrieveApiRequest(1l, "1");
        userEndp.setTemplate(baseTemplate != null? baseTemplate.getEntity() : null);
        return new AppResult<>(userEndp);
    }

    @Override
    public AppResult<ApiReq> createApiRequest(Long collection, ApiReq endpoint) {
        Map<String, Object> params = new HashMap<>();
        params.put("endp_id", endpoint.getId());
        params.put("endp_name", endpoint.getName());
        params.put("endp_descr", endpoint.getDescr());
        params.put("endp_method", endpoint.getMethod());
        params.put("endp_url", endpoint.getUrl());
        params.put("endp_path", endpoint.getPath());
        params.put("path_params", String.join(",", endpoint.getPathParams()));
        params.put("endp_query", endpoint.getQuery());
        params.put("query_params", String.join(",", endpoint.getQueryParams()));
        params.put("endp_consumes", endpoint.getConsumes());
        params.put("endp_produces", endpoint.getProduces());
        params.put("endp_headers", RestToolsJson.toJson(endpoint.getHeaders()));
        params.put("endp_entity", endpoint.getEntity());
        params.put("endp_authorized", String.join(",", endpoint.getAuthorized()));
        params.put("endp_execute", endpoint.getExecute());
        params.put("fk_parent_list", collection);

        String sql = "insert into tbl_endpoint_item (endp_id, endp_name, endp_descr, endp_method, endp_url, endp_path, path_params, endp_query, query_params, endp_consumes, endp_produces, endp_headers, endp_entity, endp_authorized, endp_execute, fk_parent_list, endp_created_ts) "
                + "values (:endp_id, :endp_name, :endp_descr, :endp_method, :endp_url, :endp_path, :path_params, :endp_query, :query_params, :endp_consumes, :endp_produces, :endp_headers, :endp_entity, :endp_authorized, :endp_execute, :fk_parent_list, current_timestamp)";

        int res = template.update(sql, new MapSqlParameterSource(params));

        return (res > 0) ? new AppResult<>(endpoint) : new AppResult<>(1, "failed to create a new endpoint");
    }

    @Override
    public AppResult<Integer> updateApiRequest(Long collection, ApiReq endpoint) {
        Map<String, Object> params = new HashMap<>();
        params.put("endp_id", endpoint.getId());
        params.put("endp_name", endpoint.getName());
        params.put("endp_descr", endpoint.getDescr());
        params.put("endp_method", endpoint.getMethod());
        params.put("endp_url", endpoint.getUrl());
        params.put("endp_path", endpoint.getPath());
        params.put("path_params", String.join(",", endpoint.getPathParams()));
        params.put("endp_query", endpoint.getQuery());
        params.put("query_params", String.join(",", endpoint.getQueryParams()));
        params.put("endp_consumes", endpoint.getConsumes());
        params.put("endp_produces", endpoint.getProduces());
        params.put("endp_headers", RestToolsJson.toJson(endpoint.getHeaders()));
        params.put("endp_entity", endpoint.getEntity());
        params.put("endp_authorized", String.join(",", endpoint.getAuthorized()));
        params.put("endp_execute", endpoint.getExecute());
        params.put("fk_parent_list", collection);

        String sql = "update tbl_endpoint_item set endp_name=:endp_name, endp_descr=:endp_descr, endp_method=:endp_method, "
                + "endp_url=:endp_url, endp_path=:endp_path, path_params=:path_params, endp_query=:endp_query, query_params=:query_params, "
                + "endp_consumes=:endp_consumes, endp_produces=:endp_produces, endp_headers=:endp_headers, endp_entity=:endp_entity, "
                + "endp_authorized=:endp_authorized, endp_execute=:endp_execute, fk_parent_list=:fk_parent_list where endp_id=:endp_id";

        int res = template.update(sql, new MapSqlParameterSource(params));

        return (res > 0) ? new AppResult<>(res) : new AppResult<>(1, "failed to update endpoint");
    }

    @Override
    public AppResult<Integer> removeApiRequest(Long collection, String endpointId) {
        Map<String, Object> params = new HashMap<>();
        params.put("collection", collection);
        params.put("endpoint", endpointId);

        String sql = "delete from tbl_endpoint_item where fk_parent_list = :collection AND endp_id = :endpoint";

        int res = template.update(sql, params);
        return (res > 0) ? new AppResult<>(res) : new AppResult<>(1, "failed to delete endpoint");
    }

    @Override
    public AppResult<ApiReq> retrieveApiRequest(Long collection, String endpointId) {
        Map<String, Object> params = new HashMap<>();
        params.put("collection", collection);
        params.put("endpoint", endpointId);

        String sql = "SELECT * FROM tbl_endpoint_item where fk_parent_list = :collection AND endp_id = :endpoint";

        ApiReq endpoint = template.queryForObject(sql, params, endpointItemMapper());
        return endpoint != null ? new AppResult<>(endpoint) : new AppResult<>(1, "could not find endpoint");
    }

    private ResultSetExtractor<UserEndpoints> userEndpointsExtractor() {
        return (ResultSet rs) -> {
            UserEndpoints endp = new UserEndpoints();
            Long userId = null;
            Long listId = null;
            String endpId = null;
            EndpointsList list;
            List<ApiReq> items = null;
            int row = 1;
            while (rs.next()) {
                if (userId == null) {
                    userId = rs.getLong("profile_id");
                    endp.setUserName(rs.getString("last_name") + ", " + rs.getString("first_name"));
                    endp.setUserId(rs.getLong("profile_id"));
                }
                Long collId = rs.getLong("list_id");
                if (!collId.equals(listId)) {
                    listId = rs.getLong("list_id");
                    list = new EndpointsList();
                    items = new ArrayList<>();
                    list.setCollectionId(listId);
                    list.setCollectionTitle(rs.getString("list_title"));
                    list.setEndpoints(items);
                    endp.getCollections().add(list);
                }
                endpId = rs.getString("endp_id");
                if(endpId != null){
                    ApiReq item = endpointItemMapper().mapRow(rs, row++);
                    items.add(item);
                }
            }
            return endp;
        };
    }

    private RowMapper<ApiReq> endpointItemMapper() {
        return (ResultSet rs, int i) -> {
            ApiReq req = new ApiReq();
            req.setId(rs.getString("endp_id"));
            req.setAuthorized(rs.getString("endp_authorized").split(","));
            req.setConsumes(rs.getString("endp_consumes"));
            req.setPath(rs.getString("endp_produces"));
            req.setDescr(rs.getString("endp_descr"));
            req.setExecute(rs.getBoolean("endp_execute"));
            req.setMethod(rs.getString("endp_method"));
            req.setName(rs.getString("endp_name"));
            req.setPath(rs.getString("endp_path"));
            req.setPathParams(rs.getString("path_params").split(","));
            req.setQuery(rs.getString("endp_query"));
            req.setQueryParams(rs.getString("query_params").split(","));
            req.setUrl(rs.getString("endp_url"));
            req.setEntity(rs.getString("endp_entity"));
            String headers = StringEscapeUtils.unescapeJson(rs.getString("endp_headers"));
            if (headers != null && headers.length() > 0) {
                req.setHeaders(RestToolsJson.fromJson(headers, new TypeReference<Map<String, String>>() {
                }));
            }
            return req;
        };
    }

    private ResultSetExtractor<EndpointsList> endpointsListExtractor() {

        return (ResultSet rs) -> {
            EndpointsList list = null;
            if (rs.next()) {
                list = new EndpointsList();
                List<ApiReq> items = new ArrayList<>();
                Long listId = rs.getLong("list_id");
                list.setCollectionId(listId);
                list.setCollectionTitle(rs.getString("list_title"));
                list.setEndpoints(items);
                int row = 1;
                ApiReq item = endpointItemMapper().mapRow(rs, row++);
                items.add(item);
                while (rs.next()) {
                    item = endpointItemMapper().mapRow(rs, row++);
                    items.add(item);
                }
            }
            return list;
        };
    }
}
