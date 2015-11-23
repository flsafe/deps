CREATE TABLE IF NOT EXISTS dependency_versions (
       id SERIAL PRIMARY KEY,
       repo_id  INTEGER REFERENCES repos (id),
       dependencies_doc VARCHAR NOT NULL,
       dependencies_doc_hash VARCHAR NOT NULL,
       created_at timestamptz NOT NULL,
       updated_at timestamptz NOT NULL
);
