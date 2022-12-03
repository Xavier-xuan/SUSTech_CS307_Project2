CREATE SCHEMA IF NOT EXISTS project2;
SET SCHEMA 'project2';

CREATE TABLE port_city
(
    name varchar(50) primary key
);

CREATE TABLE city
(
    name varchar(50) primary key
);

CREATE TABLE container
(
    code varchar(50) primary key,
    type varchar(50)
);

CREATE TABLE company
(
    name varchar(50) primary key
);

CREATE TABLE company_manager
(
    name         varchar(50) primary key,
    phone_number varchar(50),
    gender       varchar(10),
    age          int,
    password     text,
    company_name varchar(50) references company (name)
);

CREATE TABLE sustc_manager
(
    name         varchar(50) primary key,
    phone_number varchar(50),
    gender       varchar(10),
    age          int,
    password     text
);

CREATE TABLE courier
(
    name         varchar(50) primary key,
    phone_number varchar(50),
    gender       varchar(10),
    age          int,
    password     text,
    company_name varchar(50) references company (name),
    city_name    varchar(50) references city (name)
);

CREATE TABLE officer
(
    name           varchar(50) primary key,
    phone_number   varchar(50),
    gender         varchar(10),
    age            int,
    password       text,
    port_city_name varchar(50) references port_city (name)
);

CREATE TABLE ship
(
    name         varchar(50) primary key,
    company_name varchar(50) references company (name)
);

CREATE TABLE item
(
    name              varchar(50) primary key,
    price             numeric(10, 2),
    type              varchar(50),
    export_tax        numeric(10, 2),
    import_tax        numeric(10, 2),
    export_city       varchar(50) references port_city,
    import_city       varchar(50) references port_city,
    export_officer    varchar(50) references officer (name),
    import_officer    varchar(50) references officer (name),
    from_city_name    varchar(50) references city (name),
    to_city_name      varchar(50) references city (name),
    delivery_courier  varchar(50) references courier (name),
    retrieval_courier varchar(50) references courier (name),
    container_code    varchar(50) references container (code),
    ship_name         varchar(50) references ship (name),
    state             varchar(50)
);

