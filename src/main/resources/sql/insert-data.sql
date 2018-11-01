--create profiles--
insert into tbl_profile (profile_id, first_name, last_name, email_addr, phone_num, profile_created_ts)
select 1, 'Admin', 'User', 'admin.user@host.com', '', datetime('now')
where not exists (select * from tbl_profile where profile_id = 1);

insert into tbl_profile (profile_id, first_name, last_name, email_addr, phone_num, profile_created_ts)
select 2, 'Guest', 'User', 'guest.user@host.com', '', datetime('now')
where not exists (select * from tbl_profile where profile_id = 2);

--create accounts--
insert into tbl_account (account_id, username, password, acc_role, acc_status, account_profile, account_created_ts)
select 1, 'admin', 'p455w0rd', 'admin', 'active', 1, datetime('now')
where not exists (select * from tbl_account where account_id = 1);

insert into tbl_account (account_id, username, password, acc_role, acc_status, account_profile, account_created_ts)
select 2, 'guest', 'p455w0rd', 'guest', 'active', 2, datetime('now')
where not exists (select * from tbl_account where account_id = 2);

--create login_status--
insert into tbl_login_status (fk_account_id, acc_login_token, login_attempts, acc_status_info, status_created_ts, lock_expiry_ts, login_success_ts)
select 1, null,  1,  'wrong credentials used', datetime('now'), null, null
where not exists (select count(fk_account_id) from tbl_login_status where fk_account_id = 1) < 1;

insert into tbl_login_status (fk_account_id, acc_login_token, login_attempts, acc_status_info, status_created_ts, lock_expiry_ts, login_success_ts)
select 1, 'you_are_good_to_go',  2, 'account is active', datetime('now'), null, datetime('now')
where not exists (select count(fk_account_id) from tbl_login_status where fk_account_id = 1) < 2;

--user endpoints collection
insert INTO tbl_endpoints_list (list_id, list_title, fk_list_owner, list_created_ts)
select 1, 'Localhost - Users', 1, CURRENT_TIMESTAMP
where not exists (select list_id from tbl_endpoints_list where list_id = 1);

insert INTO tbl_endpoints_list (list_id, list_title, fk_list_owner, list_created_ts) 
select 2, 'Localhost - Endpoints', 1, CURRENT_TIMESTAMP
where not exists (select list_id from tbl_endpoints_list where list_id = 2);

-- collection endpoints
insert into tbl_endpoint_item (endp_id, endp_name, endp_descr, endp_method, endp_url, endp_path, path_params, endp_query, query_params, endp_consumes, endp_produces, endp_headers, endp_entity, endp_authorized, endp_execute, fk_parent_list, endp_created_ts) 
select 1, 'template', 'testing grid endpoints', 'GET', 'http://localhost:4567', '/', '', '', '', 'text/html', 'application/json', '', '', '', false, 1, CURRENT_TIMESTAMP
where not exists (select endp_id from tbl_endpoint_item where endp_id = 1);

insert into tbl_endpoint_item (endp_id, endp_name, endp_descr, endp_method, endp_url, endp_path, path_params, endp_query, query_params, endp_consumes, endp_produces, endp_headers, endp_entity, endp_authorized, endp_execute, fk_parent_list, endp_created_ts) 
select 2, 'GET_HOME', 'testing grid endpoints', 'GET', 'http://localhost:4567', '/', '', '', '', 'text/html', 'application/json', '', '', '', true, 1, CURRENT_TIMESTAMP
where not exists (select endp_id from tbl_endpoint_item where endp_id = 2);

insert into tbl_endpoint_item (endp_id, endp_name, endp_descr, endp_method, endp_url, endp_path, path_params, endp_query, query_params, endp_consumes, endp_produces, endp_headers, endp_entity, endp_authorized, endp_execute, fk_parent_list, endp_created_ts) 
select 3, 'CHECK_LOGIN', 'testing grid endpoints', 'GET', 'http://localhost:4567', '/check', '', '', '', 'text/html', 'application/json', '', '', '', false, 1, CURRENT_TIMESTAMP
where not exists (select endp_id from tbl_endpoint_item where endp_id = 3);

insert into tbl_endpoint_item (endp_id, endp_name, endp_descr, endp_method, endp_url, endp_path, path_params, endp_query, query_params, endp_consumes, endp_produces, endp_headers, endp_entity, endp_authorized, endp_execute, fk_parent_list, endp_created_ts) 
select 4, 'POST_LOGIN', 'testing grid endpoints', 'POST', 'http://localhost:4567', '/login', '', '', '', 'application/json', 'application/json', '{\"host\":\"localhost:4567\"}', 'username=admin&password=admin', '', false, 1, CURRENT_TIMESTAMP
where not exists (select endp_id from tbl_endpoint_item where endp_id = 4);

insert into tbl_endpoint_item (endp_id, endp_name, endp_descr, endp_method, endp_url, endp_path, path_params, endp_query, query_params, endp_consumes, endp_produces, endp_headers, endp_entity, endp_authorized, endp_execute, fk_parent_list, endp_created_ts) 
select 5, 'GET_GAMES', 'testing grid endpoints', 'GET', 'http://localhost:4567', '/game', '', '', '', 'text/html', 'application/json', '{\"host\": \"localhost:4567\",\"X-Requested-With\": \"XMLHttpRequest\",\"Cookie\": \"JSESSIONID=t44nvwitsi131fyp97cbc2za8;Path=/\",\"xah-user-id\": \"1\"}', '', '', false, 1, CURRENT_TIMESTAMP
where not exists (select endp_id from tbl_endpoint_item where endp_id = 5);

insert into tbl_endpoint_item (endp_id, endp_name, endp_descr, endp_method, endp_url, endp_path, path_params, endp_query, query_params, endp_consumes, endp_produces, endp_headers, endp_entity, endp_authorized, endp_execute, fk_parent_list, endp_created_ts) 
select 6, 'GET_GAME_BY_ID', 'testing grid endpoints', 'GET', 'http://localhost:4567', '/game/2', '', '', '', 'text/html', 'application/json', '', '', '', false, 1, CURRENT_TIMESTAMP
where not exists (select endp_id from tbl_endpoint_item where endp_id = 6);

insert into tbl_endpoint_item (endp_id, endp_name, endp_descr, endp_method, endp_url, endp_path, path_params, endp_query, query_params, endp_consumes, endp_produces, endp_headers, endp_entity, endp_authorized, endp_execute, fk_parent_list, endp_created_ts) 
select 7, 'GET_ALL_GUESTS', 'testing grid endpoints', 'GET', 'http://localhost:4567', '/guests', '', '', '', 'text/html', 'application/json', '', '', '', false, 1, CURRENT_TIMESTAMP
where not exists (select endp_id from tbl_endpoint_item where endp_id = 7);

-- endpoint assertions
insert into tbl_endpoint_assert (assert_id, assert_type, fail_message, expected_value, actual_value, assert_execute, fk_parent_endp, assert_created_ts) 
select 1, 'assertContains', 'expecting to find a number 20 in the text', '20', 'questions[0].question', true, 6, CURRENT_TIMESTAMP
where not exists (select assert_id from tbl_endpoint_assert where assert_id = 1);

insert into tbl_endpoint_assert (assert_id, assert_type, fail_message, expected_value, actual_value, assert_execute, fk_parent_endp, assert_created_ts) 
select 2, 'assertEquals', 'expecting user id value of 1', '1', 'userIdn', true, 6, CURRENT_TIMESTAMP
where not exists (select assert_id from tbl_endpoint_assert where assert_id = 2);