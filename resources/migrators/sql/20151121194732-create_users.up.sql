CREATE TABLE users (
       id serial PRIMARY KEY,
       email VARCHAR(128) NOT NULL,
       github_token VARCHAR NOT NULL,
       github_scope VARCHAR NOT NULL,
       created_at TIMESTAMPTZ NOT NULL,
       modified_at TIMESTAMPTZ NOT NULL
);
