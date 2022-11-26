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

CREATE TABLE manager
(
    name         varchar(50) primary key,
    phone_number varchar(50),
    gender       varchar(10),
    age          int,
    password     text,
    company_name varchar(50) references company (name)
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
    name           varchar(50) primary key,
    price          numeric(10, 2),
    type           varchar(50),
    export_tax     numeric(10, 2),
    import_tax     numeric(10, 2),
    from_city_name varchar(50) references city (name),
    to_city_name   varchar(50) references city (name),
    container_code varchar(50) references container (code),
    ship_name      varchar(50) references ship (name),
    state          varchar(50)
);

CREATE TABLE delivery_and_retrieval
(
    id           SERIAL primary key,
    item_name    varchar(50) references item (name),
    type         varchar(50) not null,
    city_name    varchar(50) not null references city (name),
    courier_name varchar(50) not null references courier (name)
);

CREATE TABLE export_and_import
(
    id             SERIAL primary key,
    type           varchar(50) not null,
    item_name      varchar(50) references item (name),
    officer_name   varchar(50) references officer (name),
    port_city_name varchar(50) references port_city (name)
)
