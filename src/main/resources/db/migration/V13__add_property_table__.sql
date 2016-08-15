CREATE TABLE property (
  pr_id SERIAL PRIMARY KEY,
  pr_key TEXT,
  pr_country_id INTEGER REFERENCES country(c_id),
  pr_value TEXT,
  pr_last_modified TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()
);