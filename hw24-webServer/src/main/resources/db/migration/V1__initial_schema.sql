create sequence hibernate_sequence start with 1 increment by 1;

create table users
(
    id   bigint not null primary key,
    login varchar(50) not null,
    password varchar(50) not null
);


INSERT INTO users (id, login, password)
VALUES (1, 'otus', 'otus'),
       (2, 'mos', 'mos'),
       (3, 'java', 'java');
