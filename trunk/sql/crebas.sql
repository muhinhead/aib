create database if not exists aibcontact1 default character set=utf8 default collate=utf8_general_ci;

use aibcontact1;

create table dbversion
(
    dbversion_id    int not null auto_increment,
    version_id      int not null,
    version         varchar(12),
    constraint dbversion_pk primary key (dbversion_id)
);

insert into dbversion (dbversion_id,version_id,version) values (1,2,'0.2');


create table user
(
    user_id 	int not null auto_increment,
    first_name 	varchar(32) not null,
    last_name 	varchar(32) not null,
    initials 	char(2) not null,
    login 	varchar(32),
    passwd 	varchar(32),
    is_admin 	bit default 0,
    photo       mediumblob,
    constraint user_pk primary key (user_id)
);

insert into user (user_id,first_name,last_name,initials,login,passwd) values(1,'Admin','Adminson','AA','admin','admin');

create table worldregion 
(
    worldregion_id int not null auto_increment,
    descr          varchar(64),
    post_price     decimal(6,2),
    post_status    int,
    post_number    int,
    constraint worldregion_pk primary key (worldregion_id)
);

create table country 
(
    country_id     int not null auto_increment,
    country        varchar(128),
    shortname      varchar(4),
    worldregion_id int not null,
    status         bit default 1,
    constraint country_pk primary key (country_id),
    constraint country_worldregion_fk foreign key (worldregion_id) references worldregion (worldregion_id)
);

create table company 
(
    company_id    int not null auto_increment,
    full_name     varchar(512) not null	comment 'Full company name',
    is_dummy      bit default 0                comment 'Is dummy company?',
    abbreviation  varchar(64) not null         comment 'Abbreviation',
    logo          mediumblob                   comment 'Logotype',
    turnover      decimal(10.2)                comment 'Turnover/Year',
    address       varchar(512)                 comment 'Address',
    mailaddress   varchar(512),
    country_id    int,
    main_phone    varchar(32),
    main_fax      varchar(32),
    member_level  int,          -- ?
    renewal_date  date,
    verify_date   date,
    comments      text,
    parent_id     int,
    lastedited_by int,
    lastedit_date datetime,
    constraint company_pk primary key (company_id),
    constraint company_country_fk foreign key (country_id) references country (country_id),
    constraint company_user_fk foreign key (lastedited_by) references user (user_id),
    constraint company_company_fk foreign key (parent_id) references company (company_id)
);

create unique index company_abbreviation_uniq on company (abbreviation);

create table link 
(
    link_id     int not null auto_increment,
    url         varchar(512) not null,
    constraint link_pk primary key (link_id)
);

create table complink
(
    complink_id int not null auto_increment,
    company_id      int not null,
    link_id         int not null,
    constraint complink_pk primary key (complink_id),
    constraint complink_company_fk foreign key (company_id) references company (company_id) on delete cascade,
    constraint complink_link_fk foreign key (link_id) references link (link_id) on delete cascade
);

create table industry 
(
    industry_id  int not null auto_increment,
    descr        varchar(128) not null,
    constraint industry_pk primary key (industry_id)
);

create table compindustry 
(
    compindustry_id int not null auto_increment,
    company_id          int not null,
    industry_id         int not null,
    constraint compindustry_pk primary key (compindustry_id),
    constraint compindustry_company_fk foreign key (company_id) references company (company_id) on delete cascade,
    constraint compindustry_industry_fk foreign key (industry_id) references industry (industry_id) on delete cascade
);

create table aibpublic 
(
    aibpublic_id      int not null auto_increment,
    publication       varchar(256) not null,
    pub_date          date not null,
    constraint aibpublic_pk primary key (aibpublic_id)
);

create table comppublic 
(
    comppublic_id int not null auto_increment,
    company_id           int not null,
    aibpublic_id         int not null,
    constraint comppublic_pk primary key (comppublic_id),
    constraint comppublic_company_fk foreign key (company_id) references company (company_id) on delete cascade,
    constraint comppublic_aibpublic_fk foreign key (aibpublic_id) references aibpublic (aibpublic_id) on delete cascade
);

create table location 
(
    location_id   int not null auto_increment,
    name          varchar(256) not null,
    abbreviation  varchar(32),
    address       varchar(512),
    mailaddress   varchar(512),
    country_id    int,
    main_phone    varchar(32),
    main_fax      varchar(32),
    logo          mediumblob,
    comments      text,
    company_id    int not null,
    lastedited_by int,
    lastedit_date datetime,
    constraint location_pk primary key (location_id),
    constraint location_country_fk foreign key (country_id) references country (country_id),
    constraint location_user_fk foreign key (lastedited_by) references user (user_id),
    constraint location_company_fk foreign key (company_id) references company (company_id)
);

create table locindustry 
(
    locindustry_id int not null auto_increment,
    location_id          int not null,
    industry_id          int not null,
    constraint locindustry_pk primary key (locindustry_id),
    constraint locindustry_location_fk foreign key (location_id) references location (location_id) on delete cascade,
    constraint locindustry_industry_fk foreign key (industry_id) references industry (industry_id) on delete cascade
); 

create table loclink 
(
    loclink_id int not null auto_increment,
    location_id      int not null,
    link_id         int not null,
    constraint loclink_pk primary key (loclink_id),
    constraint loclink_location_fk foreign key (location_id) references location (location_id) on delete cascade,
    constraint loclink_link_fk foreign key (link_id) references link (link_id) on delete cascade
);

create table people 
(
    people_id        int not null auto_increment,
    source           varchar(50),
    title            varchar(16),
    first_name       varchar(32),
    last_name        varchar(32) not null,
    suffix           varchar(16),
    greeting         varchar(32),
    location_id      int,
    photo            mediumblob,
    level            varchar(64),
    job_discip       varchar(150),
    department       varchar(128),
    spec_address     varchar(128),
    mailaddress      varchar(512),
    desk_phone       varchar(80),
    desk_fax         varchar(80),
    mobile_phone     varchar(32),
    main_email       varchar(64),
    alter_email      varchar(64),
    pa               varchar(128), #--?
    pa_phone         varchar(32),
    pa_email         varchar(64),
    other_contacts   varchar(512),
    is_primary       bit default 0,
    is_subscriber    bit default 0,
    is_marketintl    bit default 0,
    is_mediabrief    bit default 0,
    is_insourcebook  bit default 0,
    verify_date      date,
    sales_contact_id int,
    action_date      date,
    next_action      varchar(512),
    external_user    varchar(64),
    external_passwd  varchar(32),
    lastedited_by    int,
    lastedit_date    datetime,
    constraint people_pk primary key (people_id),
    constraint people_location_fk foreign key (location_id) references location (location_id),
    constraint people_user_fk foreign key (lastedited_by) references user (user_id),
    constraint people_user_fk2 foreign key (sales_contact_id) references user (user_id)
);

create table peoplenote
(
    peoplenote_id    int not null auto_increment,
    people_id        int not null,
    comments         text,
    note_date        date not null,
    lastedited_by    int,
    lastedit_date    datetime,
    constraint peoplenote_pk primary key (peoplenote_id),
    constraint peoplenote_people_fk foreign key (people_id) references people (people_id) on delete cascade,
    constraint peoplenote_user_fk foreign key (lastedited_by) references user (user_id)
);

create table departmenthistory
(
    departmenthistory_id int not null auto_increment,
    people_id            int not null,
    department           varchar(128),
    until_date           datetime,
    constraint departmenthistory_pk primary key (departmenthistory_id),
    constraint departmenthistory_people_fk foreign key (people_id) references people (people_id) on delete cascade
);

create table product 
(
    product_id  int not null auto_increment,
    descr       varchar(512) not null,
    constraint product_pk primary key (product_id)
);

create table peopleproduct 
(
    peopleproduct_id int not null auto_increment,
    purchase_date     date not null,
    people_id         int not null,
    product_id        int not null,
    constraint peopleproduct_pk primary key (peopleproduct_id),
    constraint peopleproduct_people_fk foreign key (people_id) references people (people_id) on delete cascade,
    constraint peopleproduct_product_fk foreign key (product_id) references product (product_id) on delete cascade
);

create table peopleinterest
( #-- purchase interest
    peopleinterest_id int not null auto_increment,
    purchase_date     date,
    people_id         int not null,
    product_id        int not null,
    prospecting_level varchar(64),
    constraint peopleinterest_pk primary key (peopleinterest_id),
    constraint peoplinterest_people_fk foreign key (people_id) references people (people_id) on delete cascade,
    constraint peoplinterest_product_fk foreign key (product_id) references product (product_id) on delete cascade
);

create table peoplelink 
(
    peoplelink_id int not null auto_increment,
    people_id      int not null,
    link_id        int not null,
    constraint peoplelink_pk primary key (peoplelink_id),
    constraint peoplelink_people_fk foreign key (people_id) references people (people_id) on delete cascade,
    constraint peoplelink_link_fk foreign key (link_id) references link (link_id) on delete cascade
);

create table peoplecompany 
(
    peoplecompany_id int not null auto_increment,
    people_id      int not null,
    company_id        int not null,
    constraint peoplecompany_pk primary key (peoplecompany_id),
    constraint peoplecompany_people_fk foreign key (people_id) references people (people_id) on delete cascade,
    constraint peoplecompany_company_fk foreign key (company_id) references company (company_id) on delete cascade
);

create table peopleindustry 
(
    peopleindustry_id int not null auto_increment,
    people_id          int not null,
    industry_id        int not null,
    constraint peopleindustry_pk primary key (peopleindustry_id),
    constraint peopleindustry_people_fk foreign key (people_id) references people (people_id) on delete cascade,
    constraint peopleindustry_industry_fk foreign key (industry_id) references industry (industry_id) on delete cascade
); 

create table aibaward 
(
    aibaward_id  int not null auto_increment,
    award        varchar(512) not null,
    award_date   date,
    constraint aibaward_pk primary key (aibaward_id)
);

create table peopleaward
(
    peopleaward_id     int not null auto_increment,
    people_id          int not null,
    aibaward_id        int not null,
    constraint peopleaward_pk primary key (peopleaward_id),
    constraint peopleaib_people_fk foreign key (people_id) references people (people_id) on delete cascade,
    constraint peopleaib_aibaward_fk foreign key (aibaward_id) references aibaward (aibaward_id) on delete cascade
);

create table filter
(
    filter_id  int not null auto_increment,
    tablename  varchar(32) not null,
    name       varchar(64) not null,
    descr      text,
    owner_id   int not null,
    query      text,
    is_complex bit default 1 not null,
    constraint filter_pk primary key (filter_id),
    constraint filter_user_fk foreign key (owner_id) references user (user_id)
);

create table reportform
(
    reportform_id  int not null auto_increment,
    tablename      varchar(32) not null,
    name           varchar(64) not null,
    descr          text,
    owner_id       int not null,
    constraint reportform_pk primary key (reportform_id),
    constraint reportform_user_fk foreign key (owner_id) references user (user_id)
);

create table reportformitem
(   
    reportformitem_id  int not null auto_increment,
    reportform_id      int not null,
    columnname         varchar(32) not null,
    header             varchar(64) not null,
    format             varchar(32),
    constraint reportformitem_pk primary key (reportformitem_id),
    constraint reportformitem_reportform_fk foreign key (reportform_id) references reportform (reportform_id) on delete cascade
);


delimiter |

CREATE TRIGGER tr_people_afterupdate
AFTER UPDATE ON people
FOR EACH ROW
BEGIN
    IF NOT old.department IS NULL AND new.department<>old.department THEN
        INSERT INTO departmenthistory (people_id, department, until_date) VALUES (new.people_id,old.department,NOW());
    END IF;
END;
|

#-- create function to_char(dt datetime, fmt varchar(32))
#-- returns varchar(32) deterministic
#-- begin
#--    declare fmt char(32) default fmt;
#--    set fmt = replace(fmt,'DD','%e');
#--    set fmt = replace(fmt,'MM','%m');
#--    set fmt = replace(fmt,'YYYY','%Y');
#--    set fmt = replace(fmt,'YY','%y');
#--    set fmt = replace(fmt,'HH24','%H');
#--    set fmt = replace(fmt,'MI','%i');
#--    set fmt = replace(fmt,'SS','%S');
#--    return DATE_FORMAT(dt, fmt);
#-- end;
#-- |
delimiter ;