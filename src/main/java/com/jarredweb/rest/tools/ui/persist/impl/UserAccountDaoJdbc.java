package com.jarredweb.rest.tools.ui.persist.impl;

import com.jarredweb.rest.tools.ui.persist.UserAccountDao;
import com.jarredweb.rest.tools.ui.persist.entity.Account;
import com.jarredweb.rest.tools.ui.persist.entity.Profile;
import com.jarredweb.rest.tools.ui.persist.entity.AccRole;
import com.jarredweb.rest.tools.ui.persist.entity.AccStatus;
import com.jarredweb.zesty.common.bean.AppResult;
import com.jarredweb.zesty.common.util.PasswordUtil;
import java.util.Date;
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

@Repository
public class UserAccountDaoJdbc implements UserAccountDao {

    private final NamedParameterJdbcTemplate template;

    @Autowired
    public UserAccountDaoJdbc(DataSource ds) {
        template = new NamedParameterJdbcTemplate(ds);
    }

    @Override
    public AppResult<List<Account>> retrieveAccounts(int start, int size) {
        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("size", size);
        String sql = "SELECT * FROM tbl_account a inner join tbl_profile p on a.fk_profile_id = p.fk_profile_id limit :size offset :start order by a.username";
        List<Account> list = template.query(sql, params, userMapper());
        return list != null ? new AppResult<>(list) : new AppResult<>(1, "could not find accounts");
    }

    @Override
    public AppResult<Account> findAccount(long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        String sql = "SELECT * FROM tbl_account a inner join tbl_profile p on a.fk_profile_id = p.profile_id "
                + "WHERE account_id=:id";

        Account account = template.queryForObject(sql, params, userMapper());
        return account != null ? new AppResult<>(account) : new AppResult<>(1, "could not find account");
    }

    @Override
    public AppResult<Account> findByUsername(String username) {
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);

        String sql = "SELECT * FROM tbl_account a inner join tbl_profile p on a.fk_profile_id = p.profile_id "
                + "WHERE username=:username";

        List<Account> list = template.query(sql, params, userMapper());

        Account result = null;
        if (list != null && !list.isEmpty()) {
            result = list.get(0);
        }

        return result != null ? new AppResult<>(result) : new AppResult<>(1, "could not find account");
    }

    @Override
    public AppResult<Account> register(Account acc) {
        Map<String, Object> params = new HashMap<>();
        params.put("username", acc.getUsername());
        params.put("password", PasswordUtil.hashPassword(acc.getPassword()));
        params.put("user", acc.getProfile().getId());

        String sql = "insert into tbl_account (username, password, fk_profile_id, account_created_ts) values (:username, :password, :user, current_timestamp)";

        KeyHolder holder = new GeneratedKeyHolder();
        int res = template.update(sql, new MapSqlParameterSource(params), holder);
        acc.setId(holder.getKey().longValue());

        return (res > 0) ? new AppResult<>(acc) : new AppResult<>(1, "failed registration");
    }

    @Override
    public AppResult<Account> update(Account account) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", account.getId());
        params.put("status", account.getStatus().toString());
        params.put("role", account.getRole().toString());

        String sql = "update tbl_account set acc_role=:role, acc_status=:status where account_id=:id";

        int res = template.update(sql, params);
        return (res > 0) ? new AppResult<>(account) : new AppResult<>(1, "failed updating account");
    }

    @Override
    public AppResult<Account> resetPassword(Account account) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", account.getId());
        params.put("password", account.getPassword());

        String sql = "update tbl_account set password=:password where account_id=:id";

        int res = template.update(sql, params);
        return (res > 0) ? new AppResult<>(account) : new AppResult<>(1, "failed updating account");
    }

    @Override
    public AppResult<Integer> deleteAccount(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        String sql = "delete from tbl_account where account_id = :id";

        int res = template.update(sql, params);
        return (res > 0) ? new AppResult<>(res) : new AppResult<>(1, "failed to delete account");
    }

    @Override
    public AppResult<Profile> findProfile(long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        String sql = "SELECT * FROM tbl_profile WHERE profile_id=:id";

        Profile profile = template.queryForObject(sql, params, profileMapper());
        return profile != null ? new AppResult<>(profile) : new AppResult<>(1, "could not find profile");
    }

    @Override
    public AppResult<Profile> findByEmail(String email) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);

        String sql = "SELECT * FROM tbl_profile WHERE email_addr=:email";

        List<Profile> list = template.query(sql, params, profileMapper());

        Profile result = null;
        if (list != null && !list.isEmpty()) {
            result = list.get(0);
        }

        return result != null ? new AppResult<>(result) : new AppResult<>(1, "could not find profile");
    }

    @Override
    public AppResult<Profile> register(Profile profile) {
        Map<String, Object> params = new HashMap<>();
        params.put("firstName", profile.getFirstName());
        params.put("lastName", profile.getLastName());
        params.put("emailAddr", profile.getEmailAddress());
        params.put("phoneNum", profile.getPhoneNumber());

        String sql = "insert into tbl_profile (first_name, last_name, email_addr, phone_num, profile_created_ts) values (:firstName, :lastName, :emailAddr, :phoneNum, current_timestamp)";

        KeyHolder holder = new GeneratedKeyHolder();
        int res = template.update(sql, new MapSqlParameterSource(params), holder);
        profile.setId(holder.getKey().longValue());

        return (res > 0) ? new AppResult<>(profile) : new AppResult<>(1, "failed new category");
    }

    @Override
    public AppResult<Profile> update(Profile profile) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", profile.getId());
        params.put("firstName", profile.getFirstName());
        params.put("lastName", profile.getLastName());
        params.put("phoneNum", profile.getPhoneNumber());

        String sql = "update tbl_profile set first_name=:firstName, last_name=:lastName, phone_num=:phoneNum where profile_id=:id";

        int res = template.update(sql, params);
        return (res > 0) ? new AppResult<>(profile) : new AppResult<>(1, "failed to update profile");
    }

    @Override
    public AppResult<Integer> deleteProfile(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        String sql = "delete from tbl_profile where profile_id = :id";

        int res = template.update(sql, params);
        return (res > 0) ? new AppResult<>(res) : new AppResult<>(1,"failed to delete profile");
    }

    private RowMapper<Account> userMapper() {
        return (rs, rowNum) -> {

            Account acc = new Account();

            acc.setId(rs.getLong("account_id"));
            acc.setUsername(rs.getString("username"));
            acc.setPassword(rs.getString("password"));
            acc.setStatus(AccStatus.valueOf(rs.getString("acc_status")));
            acc.setRole(AccRole.valueOf(rs.getString("acc_role")));
            acc.setCreatedTs(new Date(rs.getDate("account_created_ts").getTime()));
            Profile user = new Profile();
            user.setId(rs.getLong("fk_profile_id"));
            user.setEmailAddress(rs.getString("email_addr"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            acc.setProfile(user);

            return acc;
        };
    }

    private RowMapper<Profile> profileMapper() {
        return (rs, rowNum) -> {
            Profile prof = new Profile();

            prof.setId(rs.getLong("profile_id"));
            prof.setFirstName(rs.getString("first_name"));
            prof.setLastName(rs.getString("last_name"));
            prof.setEmailAddress(rs.getString("email_addr"));
            prof.setPhoneNumber(rs.getString("phone_num"));
            prof.setCreatedTs(new Date(rs.getDate("profile_created_ts").getTime()));

            return prof;
        };
    }
}
