-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

 */

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence hibernate_sequence start with 1 increment by 1;

create table client
(
    id   bigint not null primary key,
    name varchar(50)
);

create table address
(
    id   bigint not null primary key,
    client_id bigint not null,
    street varchar(50),
    FOREIGN KEY (client_id) REFERENCES client (id) ON DELETE CASCADE
);

create table phone
(
    id   bigint not null primary key,
    client_id bigint not null,
    number varchar(50),
    FOREIGN KEY (client_id) REFERENCES client (id) ON DELETE CASCADE
);
