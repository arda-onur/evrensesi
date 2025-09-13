create table users (
                       id bigserial primary key,
                       username varchar(255) not null unique,
                       password varchar(255) not null,
                       email varchar(255) not null unique,
                       role varchar(255) not null,
                       enabled boolean not null,
                       account_non_locked boolean not null,
                       account_non_expired boolean not null,
                       credentials_non_expired boolean not null
);