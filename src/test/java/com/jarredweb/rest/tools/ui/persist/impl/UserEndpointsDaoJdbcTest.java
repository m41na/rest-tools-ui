package com.jarredweb.rest.tools.ui.persist.impl;

import com.jarredweb.rest.tools.ui.model.EndpointsList;
import com.jarredweb.rest.tools.ui.model.UserEndpoints;
import com.jarredweb.rest.tools.ui.persist.RestToolsTestConfig;
import com.jarredweb.rest.tools.ui.persist.UserEndpointsDao;
import com.jarredweb.webjar.common.bean.AppResult;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;
import works.hop.rest.tools.api.ApiReq;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RestToolsTestConfig.class, loader = AnnotationConfigContextLoader.class)
@Sql({ "/sql/create-tables.sql" })
@Sql(scripts = "/sql/insert-data.sql", config = @SqlConfig(commentPrefix = "--"))
public class UserEndpointsDaoJdbcTest {
    
    @Autowired
    private UserEndpointsDao dao;

    @Test
    public void testCreateCollection() {
        EndpointsList list = new EndpointsList();
        list.setCollectionTitle("Yummy test");
        AppResult<EndpointsList> created = dao.createCollection(list, 1l);
        assertEquals("Expecting id value of 3", 3l, created.getEntity().getCollectionId());
    }

    @Test
    public void testUpdateCollection() {
        //retrieve saved entity
        EndpointsList list = dao.retrieveCollection(1l).getEntity();
        assertNotNull("Expecting non-null value", list);
        
        String newTitle = "Yummy title updated";
        AppResult<Integer> result = dao.updateCollection(list.getCollectionId(), newTitle);
        assertEquals("Expecting 1", 1, result.getEntity().intValue());
    }

    @Test
    public void testDeleteCollection() {
        EndpointsList list = new EndpointsList();
        list.setCollectionTitle("Yummy test");
        AppResult<EndpointsList> created = dao.createCollection(list, 1l);
        
        //delete created row
        AppResult<Integer> result =dao.deleteCollection(list.getCollectionId());
        assertEquals("Expecting 1", 1, result.getEntity().intValue());
    }

    @Test
    public void testRetrieveCollection() {
        AppResult<EndpointsList> list = dao.retrieveCollection(1l);
        String expecting = "Localhost - Users";
        assertEquals(String.format("Expecting '%s' for title", expecting), expecting, list.getEntity().getCollectionTitle());
    }
    
    @Test
    public void testRetrieveUserEndpoints() {
        AppResult<UserEndpoints> endp = dao.retrieveUserEndpoints(1l);
        UserEndpoints result = endp.getEntity();
        assertTrue("User name contains 'Admin'", result.getUserName().contains("Admin"));
        assertEquals("Expecting 2", 2, result.getMergedCollections().size());
    }

    @Test
    public void testCreateApiRequest() {
        ApiReq req = new ApiReq();
        req.setConsumes("application/json");
        req.setDescr("an xml endpoint");
        req.setExecute(Boolean.TRUE);
        req.setId("xml-wonder");
        req.setName("xml-wonder");
        req.setPath("/ws/magic");
        req.setMethod("GET");
        req.setUrl("http://localhost:9999");
        req.setProduces("application/xml");
        AppResult<ApiReq> created = dao.createApiRequest(1l, req);
        assertEquals("Expecting 'xml-wonder' for id", "xml-wonder", created.getEntity().getId());
    }

    @Test
    public void testUpdateApiRequest() {
        ApiReq req = dao.retrieveApiRequest(1l, "2").getEntity();
        req.getHeaders().put("ix-secret", "12345");
        req.setConsumes("application/xml");
        req.setProduces("text/html");
        AppResult<Integer> result = dao.updateApiRequest(1l, req);
        assertEquals("Expecting 1", 1, result.getEntity().intValue());
        
        //retrieve updated entity and compare fields
        ApiReq updated = dao.retrieveApiRequest(1l, "2").getEntity();
        assertEquals("Expecting similar value for 'consumes'", updated.getConsumes(), req.getConsumes());
    }

    @Test
    public void testRemoveApiRequest() {
        Integer result = dao.removeApiRequest(1l, "7").getEntity();
        assertEquals("Expecting 1", 1, result.intValue());
    }

    @Test
    public void testRetrieveApiRequest() {
        ApiReq req = dao.retrieveApiRequest(1l, "1").getEntity();
        assertEquals("Expecting 'template'", "template", req.getName());
    }    
}
