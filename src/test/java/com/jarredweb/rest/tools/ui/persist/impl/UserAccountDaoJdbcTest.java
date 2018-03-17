package com.jarredweb.rest.tools.ui.persist.impl;

import com.jarredweb.rest.tools.ui.persist.RestToolsTestConfig;
import com.jarredweb.rest.tools.ui.persist.UserAccountDao;
import com.jarredweb.rest.tools.ui.persist.entity.Account;
import com.jarredweb.rest.tools.ui.persist.entity.Profile;
import com.jarredweb.webjar.common.bean.AppResult;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RestToolsTestConfig.class, loader = AnnotationConfigContextLoader.class)
@Sql({"/sql/create-tables.sql"})
@Sql(scripts = "/sql/insert-data.sql", config = @SqlConfig(commentPrefix = "--"))
public class UserAccountDaoJdbcTest {

    @Autowired
    private UserAccountDao dao;
 
    @Test
    public void testFindAccountById() {
        AppResult<Account> account = dao.findAccount(2);
        String expecting = "Guest";
        assertEquals(String.format("Expecting '%s' for first name", expecting), expecting, account.getEntity().getProfile().getFirstName());
    }

    @Test
    public void testFindAccountByUsername() {
        AppResult<Account> account = dao.findByUsername("admin");
        String expecting = "Admin";
        assertEquals(String.format("Expecting '%s' for first name", expecting), expecting,
                account.getEntity().getProfile().getFirstName());
    }

    @Test
    public void testRegisterAccount() {
        // register user profile
        Profile profile = new Profile();
        String email = "test" + DaoUtils.randomAlphaNumeric(7) + "@friendmail.com";
        profile.setEmailAddress(email);
        profile.setFirstName("tester1");
        profile.setLastName("laster");
        profile.setPhoneNumber("3334445555");
        AppResult<Profile> result = dao.register(profile);
        // register account for the user
        Account account = new Account();
        account.setUsername("user1");
        account.setPassword("password1");
        account.setProfile(result.getEntity());
        AppResult<Account> result2 = dao.register(account);
        assertTrue(result2.getEntity().getId() > 0);
        // find account by username
        AppResult<Account> acc = dao.findByUsername("user1");
        assertNotNull(acc);
        assertEquals("expecting 'user1'", "user1", acc.getEntity().getUsername());
    }

    @Test
    public void testUpdateAccount() {
        Account acc = dao.findByUsername("admin").getEntity();
        assertNotNull(acc);
        assertEquals("expecting 'admin'", "admin", acc.getUsername());
        acc.setPassword("buzzbuzz");
        AppResult<Account> result = dao.update(acc);
        assertEquals("Expecting 'buzzbuzz'", "buzzbuzz", result.getEntity().getPassword());
    }

    @Test
    public void testDeleteAccount() {
        testRegisterAccount();
        // find account by username
        AppResult<Account> acc = dao.findByUsername("user1");
        assertNotNull(acc);
        AppResult<Integer> result = dao.deleteAccount(acc.getEntity().getId());
        assertEquals("Expecting 1", 1, result.getEntity().intValue());
        // find removed account
        acc = dao.findByUsername("user1");
        assertEquals(null, acc.getEntity());
    }

    @Test
    public void testFindProfileById() {
        AppResult<Profile> profile = dao.findProfile(2);
        String expecting = "Guest";
        assertEquals(String.format("Extecting '%s' as first name", expecting), expecting,
                profile.getEntity().getFirstName());
    }

    @Test
    public void testFindProfileByEmail() {
        AppResult<Profile> profile = dao.findByEmail("guest.user@host.com");
        String expecting = "User";
        assertEquals(String.format("Extecting '%s' as last name", expecting), expecting,
                profile.getEntity().getLastName());
    }

    @Test
    public void testRegisterProfile() {
        // register user profile
        Profile profile = new Profile();
        profile.setEmailAddress("tester1@sample.com");
        profile.setFirstName("tester1");
        profile.setLastName("laster");
        AppResult<Profile> result = dao.register(profile);
        assertNotNull(result.getEntity());

        // retrieve by email
        AppResult<Profile> fromDb = dao.findByEmail(profile.getEmailAddress());
        assertEquals(result.getEntity().getId(), fromDb.getEntity().getId());
    }

    @Test
    public void testUpdateProfile() {
        Profile generated = DaoUtils.generateAndRegisterProfile(dao);
        // find profile by email addr
        Profile profile = dao.findByEmail(generated.getEmailAddress()).getEntity();
        String phone = "7776669999";
        profile.setPhoneNumber(phone);
        AppResult<Profile> result = dao.update(profile);
        assertEquals("Expecting same id", profile.getId(), result.getEntity().getId());
        assertEquals("Expecting different numbers", profile.getPhoneNumber(), result.getEntity().getPhoneNumber());
    }

    @Test
    public void testDeleteProfile() {
        Profile generated = DaoUtils.generateAndRegisterProfile(dao);
        // find profile by email addr
        AppResult<Profile> profile = dao.findByEmail(generated.getEmailAddress());
        assertNotNull(profile);
        AppResult<Integer> result = dao.deleteProfile(profile.getEntity().getId());
        assertEquals("Expecting 1", 1, result.getEntity().intValue());
        // find removed account
        profile = dao.findByEmail(generated.getEmailAddress());
        assertEquals(null, profile.getEntity());
    }
}
