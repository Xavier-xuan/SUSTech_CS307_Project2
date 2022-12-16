CREATE TABLE IF NOT EXISTS port_city
(
    name varchar(50) primary key
);

CREATE TABLE IF NOT EXISTS city
(
    name varchar(50) primary key
);

CREATE TABLE IF NOT EXISTS container
(
    code varchar(50) primary key,
    type varchar(50)
);

CREATE TABLE IF NOT EXISTS company
(
    name varchar(50) primary key
);

CREATE TABLE IF NOT EXISTS company_manager
(
    name         varchar(50) primary key,
    phone_number varchar(50),
    gender       varchar(10),
    age          int,
    password     text,
    company_name varchar(50) references company (name)
);

CREATE TABLE IF NOT EXISTS sustc_manager
(
    name         varchar(50) primary key,
    phone_number varchar(50),
    gender       varchar(10),
    age          int,
    password     text
);

CREATE TABLE IF NOT EXISTS courier
(
    name         varchar(50) primary key,
    phone_number varchar(50),
    gender       varchar(10),
    age          int,
    password     text,
    company_name varchar(50) references company (name),
    city_name    varchar(50) references city (name)
);

CREATE TABLE IF NOT EXISTS officer
(
    name           varchar(50) primary key,
    phone_number   varchar(50),
    gender         varchar(10),
    age            int,
    password       text,
    port_city_name varchar(50) references port_city (name)
);

CREATE TABLE IF NOT EXISTS ship
(
    name         varchar(50) primary key,
    company_name varchar(50) references company (name)
);

CREATE TABLE IF NOT EXISTS item
(
    name              varchar(50) primary key,
    price             numeric(20, 10),
    type              varchar(50),
    export_tax        numeric(20, 10),
    import_tax        numeric(20, 10),
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

