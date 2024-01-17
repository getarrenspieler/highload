CREATE TABLE user_info
(
    id          varchar(40)  not null primary key,
    first_name  varchar(255),
    second_name varchar(255),
    birthdate   date,
    biography   varchar(2000),
    city        varchar(255),
    password    varchar(255) not null
);
