CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     username VARCHAR(255) NOT NULL UNIQUE,
                                     password VARCHAR(255) NOT NULL,
                                     email VARCHAR(255) NOT NULL UNIQUE,
                                     role VARCHAR(50) NOT NULL,
                                     enabled BOOLEAN NOT NULL DEFAULT TRUE,
                                     account_non_locked BOOLEAN NOT NULL DEFAULT TRUE,
                                     account_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
                                     credentials_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
                                     comment_id BIGINT UNIQUE,
                                     CONSTRAINT fk_users_comment
                                         FOREIGN KEY (comment_id) REFERENCES comment (id) ON DELETE SET NULL
);