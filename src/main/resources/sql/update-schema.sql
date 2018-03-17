--create tbl_profile table--
create table if not exists tbl_profile (
  profile_id integer auto_increment not null,
  first_name varchar(32),
  last_name varchar(32),
  email_addr varchar(128) not null unique,
  phone_num varchar(16),
  profile_created_ts timestamp not null default CURRENT_TIMESTAMP,
  primary key (profile_id)
);

--create tbl_account table--
create table if not exists tbl_account (
  account_id integer auto_increment not null,
  username varchar(32) not null unique,
  password varchar(128) not null,
  acc_role char(8) default 'user',
  acc_status char(8) default 'pending',
  fk_profile_id integer unique,
  account_created_ts timestamp not null default CURRENT_TIMESTAMP,
  primary key (account_id),
  foreign key (fk_profile_id) references tbl_profile(profile_id)
);

--create tbl_item table--
create table if not exists tbl_endpoints_list (
  list_id integer auto_increment not null,
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
  primary key (endp_id)
 );

--add unique constraint
ALTER TABLE tbl_endpoint_item ADD CONSTRAINT IF NOT EXISTS unq_collection_endpoint UNIQUE (fk_parent_list, endp_id);
--add foreign key constraint with cascade on delete
ALTER TABLE tbl_endpoint_item ADD CONSTRAINT IF NOT EXISTS fk_endpoint_for_collection FOREIGN KEY (fk_parent_list) REFERENCES tbl_endpoints_list (list_id) ON DELETE CASCADE;

--create tbl_todo_item table--
create table if not exists tbl_endpoint_assert(
  assert_id integer auto_increment not null,
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

