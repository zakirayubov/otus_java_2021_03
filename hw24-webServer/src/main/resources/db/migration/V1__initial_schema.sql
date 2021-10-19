create sequence hibernate_sequence start with 1 increment by 1;

create table users
(
    id   bigint not null primary key,
    name varchar(50),
    login varchar(50) not null,
    password varchar(50) not null
);


INSERT INTO users (id, name, login, password)
VALUES (1, 'OTUS', 'otus', 'otus'),
       (2, 'MOS', 'mos', 'mos'),
       (3, 'Max', 'user7', '11111');
