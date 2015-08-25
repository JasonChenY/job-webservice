drop table if exists authorities;
drop table if exists users;
create table users(
	username varchar(50) not null, 
	password varchar(50) not null,
	enabled boolean not null,
        primary key(username)        
);

create table authorities (
	username varchar(50) not null,
	authority varchar(50) not null,
	constraint fk_authorities_users foreign key(username) references users(username)
);
create unique index ix_auth_username on authorities (username,authority);


insert into users values('user', 'password', 1);
insert into authorities values('user', 'ROLE_USER');

insert into todos values(1,'2015-08-25 11:17:31','test1','2015-08-25 11:17:31','test1',0);
insert into todos values(2,'2015-08-25 11:17:31','test2','2015-08-25 11:17:31','test2',0);
insert into todos values(3,'2015-08-25 11:17:31','test3','2015-08-25 11:17:31','test3',0);
insert into todos values(4,'2015-08-25 11:17:31','test4','2015-08-25 11:17:31','test4',0);
insert into todos values(5,'2015-08-25 11:17:31','test5','2015-08-25 11:17:31','test5',0);
insert into todos values(6,'2015-08-25 11:17:31','test6','2015-08-25 11:17:31','test6',0);
insert into todos values(7,'2015-08-25 11:17:31','test7','2015-08-25 11:17:31','test7',0);
insert into todos values(8,'2015-08-25 11:17:31','test8','2015-08-25 11:17:31','test8',0);
insert into todos values(9,'2015-08-25 11:17:31','test9','2015-08-25 11:17:31','test9',0);
insert into todos values(10,'2015-08-25 11:17:31','test10','2015-08-25 11:17:31','test10',0);
insert into todos values(11,'2015-08-25 11:17:31','test11','2015-08-25 11:17:31','test11',0);
insert into todos values(12,'2015-08-25 11:17:31','test12','2015-08-25 11:17:31','test12',0);
