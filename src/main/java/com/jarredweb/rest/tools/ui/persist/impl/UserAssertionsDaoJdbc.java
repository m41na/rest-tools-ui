package com.jarredweb.rest.tools.ui.persist.impl;

import com.jarredweb.rest.tools.ui.persist.UserAssertionsDao;
import com.jarredweb.webjar.common.bean.AppResult;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import works.hop.rest.tools.api.ApiAssert;

@Repository
public class UserAssertionsDaoJdbc implements UserAssertionsDao{

    private final NamedParameterJdbcTemplate template;

    @Autowired
    public UserAssertionsDaoJdbc(DataSource ds) {
        template = new NamedParameterJdbcTemplate(ds);
    }
    
    @Override
    public AppResult<ApiAssert> createApiAssert(String endpoint, ApiAssert assertion) {
        Map<String, Object> params = new HashMap<>();
        params.put("fk_parent_endp", endpoint);
        params.put("assert_type", assertion.getAssertType().toString());
        params.put("fail_message", assertion.getFailMessage());
        params.put("expected_value", assertion.getExpectedValue().toString());
        params.put("actual_value", assertion.getActualValue());
        params.put("assert_execute", assertion.getExecute());
        
        String sql = "insert into tbl_endpoint_assert (fk_parent_endp, assert_type, fail_message, expected_value, actual_value, assert_execute, assert_created_ts) values (:fk_parent_endp, :assert_type, :fail_message, :expected_value, :actual_value, :assert_execute, current_timestamp)";

        KeyHolder holder = new GeneratedKeyHolder();
        int res = template.update(sql, new MapSqlParameterSource(params), holder);
        assertion.setId(holder.getKey().longValue());

        return (res > 0) ? new AppResult<>(assertion) : new AppResult<>("failed to create a new assertion");
    }

    @Override
    public AppResult<Integer> updateApiAssert(ApiAssert assertion) {
        Map<String, Object> params = new HashMap<>();
        params.put("assert_id", assertion.getId());
        params.put("assert_type", assertion.getAssertType().toString());
        params.put("fail_message", assertion.getFailMessage());
        params.put("expected_value", assertion.getExpectedValue().toString());
        params.put("actual_value", assertion.getActualValue());
        params.put("assert_execute", assertion.getExecute());
        
        String sql = "update tbl_endpoint_assert set assert_type=:assert_type, fail_message=:fail_message, expected_value=:expected_value, "
                + "actual_value=:actual_value, assert_execute=:assert_execute where assert_id=:assert_id";

        int res = template.update(sql, new MapSqlParameterSource(params));

        return (res > 0) ? new AppResult<>(res) : new AppResult<>("failed to update assertion");
    }

    @Override
    public AppResult<Integer> removeApiAssert(Long assertId) {
        Map<String, Object> params = new HashMap<>();
        params.put("assert_id", assertId);

        String sql = "delete from tbl_endpoint_assert where assert_id = :assert_id";

        int res = template.update(sql, params);
        return (res > 0) ? new AppResult<>(res) : new AppResult<>("failed to delete assertion");
    }

    @Override
    public AppResult<ApiAssert> retrieveApiAssert(Long assertId) {
        Map<String, Object> params = new HashMap<>();
        params.put("assert_id", assertId);

        String sql = "SELECT * FROM tbl_endpoint_assert where assert_id = :assert_id";

        ApiAssert endpoint = template.queryForObject(sql, params, assertionMapper());
        return endpoint != null ? new AppResult<>(endpoint) : new AppResult<>("could not find assertion");
    }

    @Override
    public AppResult<List<ApiAssert<?>>> retrieveEndpointAssertions(String endpoint) {
        Map<String, Object> params = new HashMap<>();
        params.put("fk_parent_endp", endpoint);

        String sql = "SELECT * FROM tbl_endpoint_assert where fk_parent_endp = :fk_parent_endp";

        List<ApiAssert<?>> assertions = template.query(sql, params, assertionMapper());
        return endpoint != null ? new AppResult<>(assertions) : new AppResult<>("could not retrieve assertions for given endpoint");
    }
    
    private RowMapper<ApiAssert<?>> assertionMapper(){
        return (ResultSet rs, int i) -> {
            ApiAssert assertion = new ApiAssert<>();
            assertion.setActualValue(rs.getString("actual_value"));
            assertion.setAssertType(ApiAssert.AssertType.valueOf(rs.getString("assert_type")));
            assertion.setExecute(rs.getBoolean("assert_execute"));
            assertion.setExpectedValue((Comparable)rs.getObject("expected_value"));
            assertion.setFailMessage(rs.getString("fail_message"));
            assertion.setId(rs.getLong("assert_id"));
            return assertion;
        };
    }
}
