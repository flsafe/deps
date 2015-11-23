CREATE TABLE IF NOT EXISTS repos (
       id SERIAL PRIMARY KEY,
       user_id INTEGER REFERENCES users (id),
       name VARCHAR NOT NULL,
       github_url varchar NOT NULL,
       
       created_at TIMESTAMPTZ NOT NULL,
       updated_at TIMESTAMPTZ NOT NULL
);
