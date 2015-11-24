CREATE TABLE IF NOT EXISTS dependency_docs (
       id SERIAL PRIMARY KEY,
       repo_id  INTEGER REFERENCES repos (id),
       doc VARCHAR NOT NULL,
       doc_hash VARCHAR NOT NULL,
       created_at timestamptz NOT NULL,
       updated_at timestamptz NOT NULL
);
