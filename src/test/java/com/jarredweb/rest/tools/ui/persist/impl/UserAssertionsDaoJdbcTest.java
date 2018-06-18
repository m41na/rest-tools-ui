package com.jarredweb.rest.tools.ui.persist.impl;

import com.jarredweb.rest.tools.ui.persist.RestToolsTestConfig;
import com.jarredweb.rest.tools.ui.persist.UserAssertionsDao;
import com.jarredweb.zesty.common.bean.AppResult;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;
import works.hop.rest.tools.api.ApiAssert;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RestToolsTestConfig.class, loader = AnnotationConfigContextLoader.class)
@Sql({"/sql/create-tables.sql"})
@Sql(scripts = "/sql/insert-data.sql", config = @SqlConfig(commentPrefix = "--"))
public class UserAssertionsDaoJdbcTest {

    @Autowired
    private UserAssertionsDao assertDao;

    @Test
    public void testCreateApiAssert() {
        ApiAssert assertion = new ApiAssert();
        assertion.setActualValue("id");
        assertion.setExpectedValue(10);
        assertion.setExecute(Boolean.TRUE);
        assertion.setAssertType(ApiAssert.AssertType.assertEquals);
        assertion.setFailMessage("Expected 10 but was id");
        AppResult<ApiAssert> created = assertDao.createApiAssert("2", assertion);
        assertTrue("Expecting same failure message", assertion.getFailMessage().equals(created.getEntity().getFailMessage()));
    }

    @Test
    public void testUpdateApiAssert() {
        AppResult<ApiAssert> retrieved = assertDao.retrieveApiAssert(2l);
        assertEquals("Expecting 'userIdn'", "userIdn", retrieved.getEntity().getActualValue());
        
        ApiAssert assertion = retrieved.getEntity();
        assertion.setActualValue("id");
        Integer update = assertDao.updateApiAssert(assertion).getEntity();
        assertEquals("Expecting 1", 1, update.intValue());
    }

    @Test
    public void testRemoveApiAssert() {
        AppResult<Integer> removed = assertDao.removeApiAssert(2l);
        assertEquals("Expecting 1", 1, removed.getEntity().intValue());
    }

    @Test
    public void testRetrieveApiAssert() {
        AppResult<ApiAssert> retrieved = assertDao.retrieveApiAssert(2l);
        assertEquals("Expecting 'userIdn'", "userIdn", retrieved.getEntity().getActualValue());
    }

    @Test
    public void testRetrieveEndpointAssertions() {
        AppResult<List<ApiAssert<?>>> retrieved = assertDao.retrieveEndpointAssertions("6");
        assertEquals("Expecting 2", 2, retrieved.getEntity().size());
    }
}
