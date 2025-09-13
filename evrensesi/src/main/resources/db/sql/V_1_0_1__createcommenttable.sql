create table comment (
                         id bigserial primary key,
                         comment varchar(255),
                         x double precision not null,
                         y double precision not null,
                         username varchar(255) not null unique,

                         constraint fk_comment_user
                             foreign key (username)
                                 references users (username)         
);