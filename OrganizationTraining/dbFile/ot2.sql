drop database if exists ot2;

-- create database
create database  ot2;
use ot2;
 
 
-- create a user and grant all permissions on tm_a, user:owen, password:123
-- grant All on tm_a.* to ot@localhost  IDENTIFIED by '123';

-- grant All on tm_a.* to tanner@localhost  IDENTIFIED by '123';

-- grant All on tm_a.* to chad@localhost  IDENTIFIED by '123';

drop table if exists employee;

drop table if exists course;

drop table if exists course_attachment;

drop table if exists course_type;

drop table if exists course_tag;

drop table if exists employee_custom_column_map;

drop table if exists custom_column;

drop table if exists employee_page_size_map;

drop table if exists page_size;

drop table if exists privilege;

drop table if exists role;

drop table if exists role_privilege_map;

drop table if exists employee_role_map;

drop table if exists external_role;

drop table if exists external_role_map;

-- drop table if exists course_update_history_tb;

drop table if exists program_course_attachment;

drop table if exists program_course;

drop table if exists program_attachment;

drop table if exists plan_tag;

drop table if exists plan_attachment;

drop table if exists plan_employee_map;

drop table if exists plan;

drop table if exists plan_type;

drop table if exists program_tb;

drop table if exists assessment;

drop table if exists assessment_attend_log;

drop table if exists assessment_item_type;

drop table if exists assessment_item;

drop table if exists assessment_item_score;

drop table if exists role_level;

drop table if exists employee_role_level_map;

drop table if exists course_author_map;

drop table if exists actual_course;

drop table if exists actual_course_attachment;

drop table if exists actual_course_employee_map;

drop table if exists course_info;

drop table if exists session_info;

drop table if exists leave_note;

-- create employee_tb
create table employee(
	id int(11) not null  AUTO_INCREMENT,
	email varchar(255) DEFAULT NULL,
	managerEmail varchar(255) DEFAULT NULL,
	job_number varchar(255) DEFAULT NULL,
	name varchar(255) DEFAULT NULL,
	password varchar(255) DEFAULT NULL,
	site varchar(255) DEFAULT NULL,
	custom_column_type_identity int(11) DEFAULT 0,
	primary key (id),
	unique key(name)
)engine=InnoDB default charset=utf8;

-- create table course_type_tb
create table course_type(
	id int(11) not null,
	name varchar(50) not null,
	primary key (id)
)engine=InnoDB default charset=utf8;

create table course(
	 id int not null,-- course_id='C001', program implement
	 name varchar(200) not null,
	 brief text not null,
	 brief_without_tag text not null,
	 target_trainee text not null,  -- which trainee learn the course
	 duration float(4,2) not null,
	 is_certificated int(1) not null default 0, -- 0: 
	 has_homework int(1) not null default 0, -- 1:
	 type_id int(11),
	 author_name varchar(50) not null,
	 create_datetime datetime not null,
	 last_update_datetime datetime not null,
	 creator varchar(50) not null,
	 tag varchar(100),
	 is_deleted int(1) not null default 0, -- 0: not deleted, 1: deleted
	 has_attachments int(1) not null,
	 level_type_flag int(1),
	 level_id int(11),
	 prefix_id_value varchar(50),
	 historyTrainers text,
	 primary key (id),
	 constraint course_type_id_fk foreign key (type_id) references course_type(id)
)engine=InnoDB default charset=utf8;

create table course_attachment(
	 id int(11) not null,
	 path varchar(200) not null,
	 name varchar(200) not null,
	 size varchar(10),
	 visible int(1) not null default 1, -- whether the attachment is visible for trainner
	 is_deleted int(1) not null default 0, -- 0: not deleted, 1: deleted
	 course_id int(11),
	 create_datetime datetime,
	 primary key (id),
	 constraint course_id_fk foreign key (course_id) references course(id)
)engine=InnoDB default charset=utf8;

create table course_tag(
	id int(11) not null,
  	name varchar(100) not null,
  	primary key (id)
 )engine=InnoDB default charset=utf8;
 
create table course_author_map(
	course_id  int(11),
 	employee_id int(11)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table custom_column(
 	id int primary key,
	category_flag int(1) not null, -- 0:course 1:plan
	field_name varchar(100) not null,
	column_order int(1),
	is_default int(1) default 1,
	is_disabled int(1) default 1
)engine=InnoDB default charset=utf8;

create table employee_custom_column_map(
 	custom_column_id int(11),
 	employee_id int(11)
)engine=InnoDB default charset=utf8;

create table page_size(
 	id int primary key,
 	category_flag int(1) not null, -- 0:course 1:program 2:plan 3:role
 	page_value int not null,
 	is_default int(1)
)engine=InnoDB default charset=utf8;

create table employee_page_size_map(
 	page_size_id int(11),
 	employee_id int(11)
)engine=InnoDB default charset=utf8;

-- -------------------------------program----------------------------
create table  program(
	 id int(11) primary key,
	 name varchar(200) not null,
	 brief text,
	 target_trainee text,
	 tag varchar(100),
	 creator varchar(100),
	 create_datetime datetime,
	 last_update_datetime datetime,
	 has_attachments int(1),
	 level_type_flag int(1),
	 level_id int(11),
	 prefix_id_value varchar(50)
)engine=InnoDB default charset=utf8;

create table program_attachment(
	 id int(11) primary key,
	 path varchar(100),
	 visible int(1),
	 name varchar(100),
	 size varchar(10),
	 is_deleted int(1),
	 program_id int(11),
	 constraint program_id_fk foreign key (program_id) references program(id)
)engine=InnoDB default charset=utf8;

create table program_course(
	id int(11) primary key,
	course_id int(11),
	name varchar(100),
	brief text not null,
	target_trainee text not null,  -- which trainee learn the course
	original_duration float(4,2) not null,
	type_id int not null,
	has_homework int not null default 0, -- 1:
	author_name varchar(100) not null,
	tag varchar(100) not null,
	program_id int(11),
	prefix_id_value varchar(50),
	constraint program_course_fk foreign key (program_id) references program(id)
)engine=InnoDB default charset=utf8;

create table program_course_attachment(
	 id int(11) primary key,
	 path varchar(100),
	 visible int(1),
	 name varchar(100),
	 size varchar(10),
	 is_deleted int(1) default 1, -- 1:exist 0:not exist
	 program_course_id int(11),
	 constraint program_course_attachment_fk foreign key (program_course_id ) references program_course(id)
)engine=InnoDB default charset=utf8;

-- -------------------------------plan----------------------------
create table plan_tag(
  	id int(11) not null,
  	name varchar(100) not null,
  	primary key (id)
 )engine=InnoDB default charset=utf8;

create table plan_type(
	id int(11) primary key,
	name varchar(100)
)engine=InnoDB default charset=utf8;

create table plan(
	 id int(11) primary key,
	 name varchar(200) not null,
	 brief text not null,
	 brief_without_tag text not null,
	 tag varchar(100),
	 is_completed int(1),
	 creator varchar(100),
	 type_id int,
	 create_datetime datetime not null,
	 update_datetime datetime not null,
	 is_published int(1),
	 has_attachments int(1),
	 is_deleted int default 0,
	 execute_start_datetime datetime,
	 execute_end_datetime datetime,
	 register_notice int(1) default 0,
	 reminder_email int default 0,
	 publish_datetime datetime,
	 is_all_employees int default 0,
	 is_canceled int default 0,
	 cancel_plan_reason varchar(500),
	 trainers varchar(3000),
	 plan_trainee_names text,
	 trainees int default 0,
	 level_type_flag int(1),
	 level_id int(11),
	 prefix_id_value varchar(50),
	 need_assessment int(1) default 0 not null,       -- 1:plan should be assessed, 0:plan cannot be assessed.
	 constraint type_id_fk foreign key (type_id) references plan_type(id)
)engine=InnoDB default charset=utf8;

create table plan_attachment(
	 id int(11) primary key,
	 path varchar(100),
	 visible int(1),
	 name varchar(100),
	 size varchar(10),
	 is_deleted int(1) default 0,-- 1:deleted 0:not deleted
	 create_datetime datetime,
	 plan_id int(11)
)engine=InnoDB default charset=utf8;

create table plan_employee_map(
	 id int(11) primary key,
	 employee_id int(11),
	 plan_id int(11),
	 attend_type int(1),
	 is_deleted int(1),
	 operation_time datetime not null
)engine=InnoDB default charset=utf8;

-- create role_tb
create table role(
	  id int(11) not null,
	  name varchar(50) not null,
	  privilege_value_sum blob,
	  primary key (id)
)engine=InnoDB default charset=utf8;

-- create role_level_tb
create table role_level(
	  id int(11) not null,
	  level int(11) not null,
	  primary key (id)
)engine=InnoDB default charset=utf8;

-- create employee_role_level_tb
create table employee_role_level_map(
	  id int(11) not null,
	  employee_id int(11) not null,
	  role_id int(11) not null,
	  role_level_id int(11),
	  primary key (id)
)engine=InnoDB default charset=utf8;

-- create privilege_tb
create table privilege(
	  id int(11) not null,
	  name varchar(100) not null,
	  privilege_value int not null,
	  primary key (id)
)engine=InnoDB default charset=utf8;

create table role_privilege_map(
	  role_id int(11),
	  privilege_id int(11)
)engine=InnoDB default charset=utf8;

-- create external_role_tb
create table external_role(
	  id int not null,
	  name varchar(50) not null,
	  primary key (id)
)engine=InnoDB default charset=utf8;

create table external_role_map(
	  role_id int(11),
	  external_role_id int(11)
)engine=InnoDB default charset=utf8;

-- ------------------------------------------dashboard table------------------------------------
create table assessment (
	 id int(11) NOT NULL,
	 type_flag int(1),
	 plan_id int(11),
	 plan_course_id int(11),
	 master_id int(11) ,
	 trainer_id int(11) ,
	 trainee_id int(11),
	 is_ignore int(1) default 1, -- 1:not ignore, 0:ignore
	 assess_comment varchar(500),
	 is_deleted int(1) default 1,
	 create_datetime datetime,
	 update_datetime datetime,
	 has_been_assessed int(1),
	 PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table assessment_item_type(
	 id int(11) not null,
	 name varchar(100) not null,
	 primary key(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table assessment_item (
	 id int(11) not null,
	 name varchar(100) not null,
	 type_id int(11),
	 item_describe varchar(500),
	 is_optional int(11) default 0,
	 primary key(id),
	 constraint assessment_item_type_id_fk foreign key (type_id) references assessment_item_type(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table assessment_item_score(
	  id int(11) primary key,
	  item_id int(11),
	  score double,
	  assessment_id int(11)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table assessment_attend_log(
	  attend_log_id int(11) primary key,
	  assessment_id int(11),
	  attend_log_key varchar(30)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table course_info (
    id int(11) NOT NULL,
    original_course_id int(11) NOT NULL,
    target_trainee text not null,  -- which trainee learn the course
    type_id int(11) not null, -- 0: 
    has_homework int(1) not null default 0, -- 1:
    author_name varchar(100) not null,
    tag varchar(100) not null,
    trainee_to_trainer int(1) default 0 not null,   -- 1:trainee should assess trainer, 0:trainee cannot assess trainer
    trainer_to_trainee int(1) default 0 not null,   -- 1:trainer should assess trainee, 0:trainer cannot assess trainee
    course_has_homework int(1) default 0 not null,
    primary key (id)
)engine=InnoDB DEFAULT charset=utf8;

create table session_info (
    id int(11) NOT NULL,
    primary key (id)
)engine=InnoDB DEFAULT charset=utf8;

create table actual_course (
    id int(11) NOT NULL,
    prefix_id_value varchar(50) DEFAULT NULL,
    name varchar(200) NOT NULL,
    brief text NOT NULL,
    brief_without_tag text NOT NULL,
    duration double NOT NULL,
    end_datetime datetime DEFAULT NULL,
    has_attachment tinyint(1) DEFAULT 0,
    room_num varchar(50) DEFAULT NULL,
    start_datetime datetime DEFAULT NULL,
    trainer varchar(100) DEFAULT NULL,
    plan_id int(11) DEFAULT NULL,
    program_id int(11) DEFAULT NULL,
    course_order int(11),
    course_info_id int(11) DEFAULT NULL,
    session_info_id int(11) DEFAULT NULL,
    primary key (id),
    constraint actual_course_plan_id_fk foreign key (plan_id) references plan(id),
    constraint actual_course_program_id_fk foreign key (program_id) references program(id),
    constraint actual_course_course_info_id_fk foreign key (course_info_id) references course_info(id),
    constraint actual_course_session_info_id_fk foreign key (session_info_id) references session_info(id)
) ENGINE=InnoDB default CHARSET=utf8;

create table actual_course_attachment(
     id int(11) primary key auto_increment,
     path varchar(200),
     visible int(1),
     name varchar(100),
     size varchar(10),
     create_datetime datetime,
     actual_course_id int(11),
     is_deleted int(1) default 0, -- 1:deleted 0:not deleted
     constraint attachment_actual_course_id_fk foreign key (actual_course_id) references actual_course(id)
)engine=InnoDB default charset=utf8;

CREATE TABLE actual_course_employee_map (
      employee_id int(11) NOT NULL,
      actual_course_id int(11) NOT NULL
)engine=InnoDB DEFAULT charset=utf8;

create table `leave_note` (
  `id` int(11) not null auto_increment,
  `actual_course_id` int(11) not null,
  `employee_id` int(11) not null,
  `reason` varchar(200) default null,
  `apply_leave_date` datetime not null,
  `plan_id` int(11) not null,
  primary key (`id`),
  key `actual_course_id_leave_fk` (`actual_course_id`),
  key `employee_leave_fk` (`employee_id`),
  constraint `actual_course_id_leave_fk` foreign key (`actual_course_id`) references `actual_course` (`id`),
  constraint `employee_leave_fk` foreign key (`employee_id`) references `employee` (`id`)
) engine=innodb auto_increment=20 default charset=utf8;