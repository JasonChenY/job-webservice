drop table if exists favorites;
drop table if exists comments;
drop table if exists authorities;
drop table if exists users;
drop table if exists complaints;

create table users(
	    username varchar(50) not null,
	    password varchar(50) not null default 'N/A',
	    enabled boolean not null default 1,
	    register_time timestamp not null default CURRENT_TIMESTAMP,
	    last_login_time datetime,
	    last_login_ip varchar(50),
	    email varchar(50),
	    phone varchar(50),
	    account_type int not null default (0), --comment '0: user registered, 1: system generated account'
        primary key(username)        
);

create table authorities (
	    username varchar(50) not null,
	    authority varchar(50) not null,
	    constraint fk_authorities_users foreign key(username) references users(username)
);
create unique index ix_auth_username on authorities (username,authority);

create table users_binding (
        identifier varchar(100) not null, -- unique id from third party
        identity_type int not null, -- 1: TestServer 2: QQ 3: Weibo 4: Sina
        user_id varchar(50) not null, -- system id in users
        binding_time timestamp not null default CURRENT_TIMESTAMP,
        last_login_time datetime,
        last_login_ip varchar(50),
        access_token varchar(100),
        refresh_token varchar(100),
        constraint fk_binding_users foreign key(user_id) references users(username),
        primary key(identifier, identity_type)
);

create table comments(
        id BIGINT NOT NULL AUTO_INCREMENT primary key, 
        user_id varchar(100) not null,
        job_id varchar(200) not null,
        creation_time datetime not null,
        modification_time datetime,
        content longtext not null,
        anonymous boolean not null default false,
        constraint fk_comments_users foreign key(user_id) references users(username) 
);

create table favorites(
        id BIGINT NOT NULL AUTO_INCREMENT primary key,
        user_id varchar(100) not null,
        job_id varchar(200) not null,
        constraint fk_favorites_users foreign key(user_id) references users(username)
);

create table complains(
        id BIGINT NOT NULL AUTO_INCREMENT primary key,
        user_id varchar(100) not null,
        job_id varchar(200) not null,
        type int not null,
        content longtext,
        creation_time datetime not null,
        status int not null,
        approve_time datetime,
        approve_user_id varchar(100),
        constraint fk_complains_users foreign key(user_id) references users(username),
        constraint fk_complains_users2 foreign key(approve_user_id) references users(username)
);

insert into users values('user', 'password', 1);
insert into authorities values('user', 'ROLE_USER');
insert into users values('jason', 'password', 1);
insert into authorities values('jason', 'ROLE_ADMIN');





