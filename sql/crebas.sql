create database if not exists aibcontact1 default character set=utf8 default collate=utf8_general_ci;

use aibcontact1;

create table user 
(
    user_id int not null auto_increment,
    first_name varchar(32) not null,
    last_name varchar(32) not null,
    initials char(2) not null,
    login varchar(32),
    passwd varchar(32),
    constraint user_pk primary key (user_id)
);

create table worldregion 
(
    worldregion_id int not null auto_increment,
    descr          varchar(64),
    constraint worldregion_pk primary key (worldregion_id)
);

create table country 
(
    country_id     int not null auto_increment,
    country        varchar(128),
    shortname      varchar(4),
    worldregion_id int not null, 
    constraint country_pk primary key (country_id),
    constraint country_worldregion_fk foreign key (worldregion_id) references worldregion (worldregion_id)
);

create table company 
(
    company_id    int not null auto_increment,
    full_name     varchar(512) not null,
    is_dummy      bit default 0,
    abbreviation  varchar(64) not null,
    logo          mediumblob,
    turnover      decimal(10.2),
    address       varchar(512),
    postcode      varchar(128),
    mailaddress   varchar(512),
    mailpostcode  varchar(128),
    country_id    int,
    main_phone    varchar(32),
    main_fax      varchar(32),
    member_level  int,          -- ?
    renewal_date  date,
    verify_date   date,
    comments      text,
    lastedited_by int,
    lastedit_date datetime,
    constraint company_pk primary key (company_id),
    constraint company_country_fk foreign key (country_id) references country (country_id),
    constraint company_user_fk foreign key (lastedited_by) references user (user_id)
);

create table link 
(
    link_id     int not null auto_increment,
    url         varchar(512) not null,
    constraint link_pk primary key (link_id)
);

create table company_link 
(
    company_link_id int not null auto_increment,
    company_id      int not null,
    link_id         int not null,
    constraint complink_pk primary key (company_link_id),
    constraint complink_company_fk foreign key (company_id) references company (company_id) on delete cascade,
    constraint complink_link_fk foreign key (link_id) references link (link_id) on delete cascade
);

create table industry 
(
    industry_id  int not null auto_increment,
    descr        varchar(128) not null,
    constraint industry_pk primary key (industry_id)
);

create table company_industry 
(
    company_industry_id int not null auto_increment,
    company_id          int not null,
    industry_id         int not null,
    constraint compindustry_pk primary key (company_industry_id),
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

create table company_aibpublic 
(
    company_aibpublic_id int not null auto_increment,
    company_id           int not null,
    aibpublic_id         int not null,
    constraint comppublic_pk primary key (company_aibpublic_id),
    constraint comppublic_company_fk foreign key (company_id) references company (company_id) on delete cascade,
    constraint comppublic_aibpublic_fk foreign key (aibpublic_id) references aibpublic (aibpublic_id) on delete cascade
);

create table location 
(
    location_id   int not null auto_increment,
    name          varchar(256) not null,
    abbreviation  varchar(6),
    address       varchar(512),
    postcode      varchar(128),
    mailaddress   varchar(512),
    mailpostcode  varchar(128),
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

create table location_industry 
(
    location_industry_id int not null auto_increment,
    location_id          int not null,
    industry_id          int not null,
    constraint locindustry_pk primary key (location_industry_id),
    constraint locindustry_company_fk foreign key (company_id) references company (company_id) on delete cascade,
    constraint locindustry_industry_fk foreign key (industry_id) references industry (industry_id) on delete cascade
); 

create table location_link 
(
    location_link_id int not null auto_increment,
    location_id      int not null,
    link_id         int not null,
    constraint loclink_pk primary key (location_link_id),
    constraint loclink_location_fk foreign key (location_id) references location (location_id) on delete cascade,
    constraint loclink_link_fk foreign key (link_id) references link (link_id) on delete cascade
);

create table people 
(
    people_id        int not null auto_increment,
    title            varchar(16),
    first_name       varchar(32),
    last_name        varchar(32) not null,
    suffix           varchar(16),
    greeting         varchar(16),
    photo            mediumblob,
    level            varchar(64),
    job_discip       varchar(64),
    department       varchar(128),
    spec_address     varchar(128),
    mailaddress      varchar(512),
    mailpostcode     varchar(128),
    desk_phone       varchar(32),
    desk_fax         varchar(32),
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
    verify_date      date,
    comments         text,
    sales_contact_id int,
    action_date      date,
    next_action      varchar(512),
    external_user    varchar(64),
    external_passwd  varchar(32),
    lastedited_by    int,
    lastedit_date    datetime,
    constraint people_pk primary key (people_id),
    constraint people_user_fk foreign key (lastedited_by) references user (user_id),
    constraint people_user_fk2 foreign key (sales_contact_id) references user (user_id)
);

create table product 
(
    product_id  int not null auto_increment,
    descr       varchar(512) not null,
    constraint product_pk primary key (product_id)
);

create table people_product 
(
    people_product_id int not null auto_increment,
    purchase_date     date not null,
    people_id         int not null,
    product_id        int not null,
    constraint people_product_pk primary key (people_product_id),
    constraint peoplprod_people_fk foreign key (people_id) references people (people_id) on delete cascade,
    constraint peoplprod_product_fk foreign key (product_id) references product (product_id) on delete cascade
);

create table people_interest 
( #-- purchase interest
    people_interest_id int not null auto_increment,
    purchase_date     date not null,
    people_id         int not null,
    product_id        int not null,
    prospecting_level varchar(32),
    purchase_time     decimal(4,2),
    constraint people_interest_pk primary key (people_interest_id),
    constraint peoplinter_people_fk foreign key (people_id) references people (people_id) on delete cascade,
    constraint peoplinter_product_fk foreign key (product_id) references product (product_id) on delete cascade
);

create table people_link 
(
    people_link_id int not null auto_increment,
    people_id      int not null,
    link_id        int not null,
    constraint peoplelink_pk primary key (people_link_id),
    constraint peoplelink_people_fk foreign key (people_id) references people (people_id) on delete cascade,
    constraint peoplelink_link_fk foreign key (link_id) references link (link_id) on delete cascade
);

create table people_industry 
(
    people_industry_id int not null auto_increment,
    people_id          int not null,
    industry_id        int not null,
    constraint peopleindustry_pk primary key (people_industry_id),
    constraint peopleindustry_people_fk foreign key (people_id) references people (people_id) on delete cascade,
    constraint peopleindustry_industry_fk foreign key (industry_id) references industry (industry_id) on delete cascade
); 

create table people_location 
(
    people_location_id int not null auto_increment,
    people_id          int not null,
    location_id        int not null,
    constraint people_location_pk primary key (people_location_id),
    constraint peopleloc_people_fk foreign key (people_id) references people (people_id) on delete cascade,
    constraint peopleloc_location_fk foreign key (location_id) references location (location_id) on delete cascade
);

create table aibaward 
(
    aibaward_id  int not null auto_increment,
    award        varchar(512) not null,
    award_date   date,
    constraint aibaward_pk primary key (aibaward_id)
);

create table people_aibaward 
(
    people_aibaward_id int not null auto_increment,
    people_id          int not null,
    aibaward_id        int not null,
    constraint people_aibaward_pk primary key (people_aibaward_id),
    constraint peopleaib_people_fk foreign key (people_id) references people (people_id) on delete cascade,
    constraint peopleaib_aibaward_fk foreign key (aibaward_id) references aibaward (aibaward_id) on delete cascade
);

