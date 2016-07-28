CREATE TABLE category (
  ct_id SERIAL PRIMARY KEY,
  ct_name_key TEXT,
  ct_hex_color TEXT,
  ct_last_modified TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE category_2_country (
  c2c_category_id INTEGER REFERENCES category(ct_id),
  c2c_country_id INTEGER REFERENCES country(c_id)
);

CREATE TABLE blog_entry_2_category (
  be2c_blog_entry_id INTEGER REFERENCES blog_entry(be_id),
  be2c_category_id INTEGER REFERENCES category(ct_id)
);