 /****用户表***/
 create table s_user
(
   id                   BIGINT      not NULL auto_increment,
   user_code            varchar(20),
   user_name            varchar(50),
   sex                  char(1)  COMMENT '0:女，1：男',
   pwd                  varchar(50),
   user_type            char(1) COMMENT '0:前台用户，1：后台用户',
   head_url             varchar(100),
   we_chat              varchar(20),
   mobile               varchar(20),
   email                varchar(20),
   qq                   varchar(20),
   create_user          bigint,
   create_time          datetime,
   constraint PK_S_USER primary key clustered (id)
);

 /****角色表***/
create table s_role
(
   id                   bigint             not NULL  auto_increment,
   role_code            varchar(20),
   role_name            varchar(20),
   remark               varchar(200),
   create_user          bigint,
   create_time          datetime,
   constraint PK_S_ROLE primary key clustered (id)
);

/******用户角色表*****/
 create table s_user_role
(
   id                   BIGINT           not NULL auto_increment,
   user_id              bigint,
   role_id              bigint,
   create_user          bigint,
   create_time          datetime,
   constraint PK_S_USER_ROLE primary key clustered (id)
);


/******资源表*****/
create table s_resource
(
   id                   bigint            not null    auto_increment,
   pid                  bigint,
   name                 varchar(50),
   url                  varchar(100),
   seq                  int,
   type                 char(1)   COMMENT '0:菜单，1：按钮',
   create_user          bigint,
   create_time          datetime,
   constraint PK_S_RESOURCE primary key clustered (id)
);


/******角色资源表*****/
create table s_role_resource
(
   id                   bigint            not null auto_increment,
   res_url              varchar(50),
   role_id              bigint,
   create_time          datetime,
   create_user          bigint,
   constraint PK_S_ROLE_RESOURCE primary key clustered (id)
);