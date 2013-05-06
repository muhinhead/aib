use aibcontact1;

create table user (
    user_id int not null auto_increment,
    first_name varchar(32),
    last_name varchar(32),
    login varchar(32),
    passwd varchar(32),
    constraint user_pk primary key (user_id)
);

create table worldregion (
    worldregion_id int not null auto_increment,
    descr          varchar(64),
    constraint worldregion_pk primary key (worldregion_id)
);

create table country (
    country_id     int not null auto_increment,
    country        varchar(128),
    shortname      varchar(4),
    worldregion_id int not null, 
    constraint country_pk primary key (country_id),
    constraint country_worldregion_fk foreign key (worldregion_id) references worldregion (worldregion_id)
);

create table company (
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

create table link (
    link_id     int not null auto_increment,
    url         varchar(512) not null,
    constraint link_pk primary key (link_id)
);

create table company_link (
    company_link_id int not null auto_increment,
    company_id      int not null,
    link_id         int not null,
    constraint complink_pk primary key (company_link_id),
    constraint complink_company_fk foreign key (company_id) references company (company_id) on delete cascade,
    constraint complink_link_fk foreign key (link_id) references link (link_id) on delete cascade
);

create table industry (
    industry_id  int not null auto_increment,
    descr        varchar(128) not null,
    constraint industry_pk primary key (industry_id)
);

create table company_industry (
    company_industry_id int not null auto_increment,
    company_id          int not null,
    industry_id         int not null,
    constraint compindustry_pk primary key (company_industry_id),
    constraint compindustry_company_fk foreign key (company_id) references company (company_id) on delete cascade,
    constraint compindustry_industry_fk foreign key (industry_id) references industry (industry_id) on delete cascade
);

create table aibpublic (
    aibpublic_id      int not null auto_increment,
    publication       varchar(256) not null,
    pub_date          date not null,
    constraint aibpublic_pk primary key (aibpublic_id)
);

create table company_aibpublic (
    company_aibpublic_id int not null auto_increment,
    company_id           int not null,
    aibpublic_id         int not null,
    constraint comppublic_pk primary key (company_aibpublic_id),
    constraint comppublic_company_fk foreign key (company_id) references company (company_id) on delete cascade,
    constraint comppublic_aibpublic_fk foreign key (aibpublic_id) references aibpublic (aibpublic_id) on delete cascade
);
