DROP database Assignment4;
create database Assignment4;
USE Assignment4


create table SEC_USER
(
  userId           BIGINT NOT NULL Primary Key AUTO_INCREMENT,
  userName         VARCHAR(36) NOT NULL UNIQUE,
  encryptedPassword VARCHAR(128) NOT NULL,
  email             VARCHAR(500),
  fullName          VARCHAR(100),
  phone             VARCHAR(15),
  DOB               VARCHAR(20),
  ENABLED           BIT NOT NULL 
) ;

create table EVENT
(
  eventId         BIGINT NOT NULL Primary Key AUTO_INCREMENT,
  location        VARCHAR(500) NOT NULL,
  eTime           VARCHAR(10) NOT NULL,
  eDate           VARCHAR(20) NOT NULL,
  type            VARCHAR(100) NOT NULL,
  description     VARCHAR(500),
  maxTickets      BIGINT,
  ticketPrice     BIGINT
);  
  
create table USER_EVENT
(
    ID      BIGINT NOT NULL Primary Key AUTO_INCREMENT,
    userId  BIGINT NOT NULL,
    eventId BIGINT NOT NULL,
    ticketNumber VARCHAR(10) UNIQUE
);
    
create table SEC_ROLE
(
  roleId   BIGINT NOT NULL Primary Key AUTO_INCREMENT,
  roleName VARCHAR(30) NOT NULL UNIQUE
) ;


create table USER_ROLE
(
  ID      BIGINT NOT NULL Primary Key AUTO_INCREMENT,
  userId BIGINT NOT NULL,
  roleId BIGINT NOT NULL 
);

alter table USER_ROLE
  add constraint USER_ROLE_UK unique (userId, roleId);

alter table USER_ROLE
  add constraint USER_ROLE_FK1 foreign key (userId)
  references SEC_USER (userId);
 
alter table USER_ROLE
  add constraint USER_ROLE_FK2 foreign key (roleId)
  references SEC_ROLE (roleId);

alter table USER_EVENT
add constraint USER_EVENT_UK unique (userId, eventId);

alter table USER_EVENT
 add constraint USER_EVENT_FK1 foreign key (userId)
    references SEC_USER (userId);

alter table USER_EVENT
add constraint USER_EVENT_FK2 foreign key (eventId)
    references EVENT (eventId);   
 
insert into sec_role (roleName)
values ('ROLE_ADMIN');
 
insert into sec_role (roleName)
values ('ROLE_MEMBER');

insert into sec_user (username, encryptedPassword, email, fullName, phone, DOB, ENABLED)
values ('Sam','$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu',
'sam@gmail.com', 'Sam Johnson', '123456789', '2000-05-03', 1);

insert into event (location, eTime, eDate, type, description, maxTickets, ticketPrice)
values ('Mississauga', '11AM', '2019-11-11', 'Seminar','Event event event!',
100, 20);

insert into user_event (userId, eventId, ticketNumber)
values (1 , 1, '32910');  

COMMIT;

