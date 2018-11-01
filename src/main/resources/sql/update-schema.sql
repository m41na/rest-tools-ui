PRAGMA foreign_keys = ON;

drop TABLE if EXISTS tbl_endpoint_assert;
drop INDEX if EXISTS unq_collection_endpoint;
drop TABLE if EXISTS tbl_endpoint_item;
drop TABLE if EXISTS tbl_endpoints_list;
drop TABLE if EXISTS tbl_login_status;
drop TABLE if EXISTS tbl_account;
drop TABLE if EXISTS tbl_profile;

--create tbl_profile table--
create table if not exists tbl_profile (
  profile_id integer not null,
  first_name varchar(32),
  last_name varchar(32),
  email_addr varchar(128) not null unique,
  phone_num varchar(16),
  profile_created_ts timestamp not null default current_timestamp,
  primary key (profile_id)
);

--create tbl_account table--
create table if not exists tbl_account (
  account_id integer not null,
  username varchar(32) not null unique,
  password varchar(128) not null,
  acc_role char(8) default 'user',
  acc_status char(8) default 'pending',
  account_profile integer unique,
  account_created_ts timestamp not null default current_timestamp,
  primary key (account_id),
  foreign key (account_profile) references tbl_profile(profile_id)
);

--create tbl_login_status table--
create table if not exists tbl_login_status (
  fk_account_id integer,
  acc_login_token text,
  lock_expiry_ts text default(datetime('now')),
  acc_status_info text,
  login_attempts integer default 0,
  login_success_ts text,
  status_created_ts text not null default current_timestamp,
  foreign key (fk_account_id) references tbl_account(account_id)
);

--create tbl_item table--
create table if not exists tbl_endpoints_list (
  list_id integer not null,
  list_title varchar(32) not null,
  fk_list_owner integer not null,
  list_created_ts timestamp not null DEFAULT current_timestamp,
  primary key (list_id),
  foreign key (fk_list_owner) references tbl_profile(profile_id)
);

--create tbl_todo_list table--
create table if not exists tbl_endpoint_item(
  endp_id varchar(20) not null,
  endp_name varchar(32) not null,
  endp_descr varchar(128),
  endp_method char(7) not null,
  endp_url varchar(128),
  endp_path varchar(64) not null,
  path_params varchar(32),
  endp_query varchar(128),
  query_params varchar(32),
  endp_consumes varchar(64) not null default 'text/html',
  endp_produces varchar(64) not null default 'text/html',
  endp_headers varchar(256),
  endp_entity varchar(1024),
  endp_authorized varchar(32),
  endp_execute boolean default false,
  fk_parent_list integer not null,
  endp_created_ts timestamp not null DEFAULT current_timestamp,
  FOREIGN KEY (fk_parent_list) REFERENCES tbl_endpoints_list (list_id) ON DELETE CASCADE,
  primary key (endp_id)
 );

--add unique constraint
CREATE UNIQUE INDEX IF NOT EXISTS unq_collection_endpoint ON tbl_endpoint_item (fk_parent_list, endp_id);

--create tbl_todo_item table--
create table if not exists tbl_endpoint_assert(
  assert_id integer not null,
  fk_parent_endp varchar(20) not null,
  assert_type varchar(32) not null,
  fail_message varchar(64) not null,
  expected_value varchar(64) not null,
  actual_value varchar(64) not null,
  assert_execute boolean not null default false,
  assert_created_ts timestamp not null DEFAULT current_timestamp,
  primary key (assert_id),
  foreign key (fk_parent_endp) references tbl_endpoint_item(endp_id)
);

