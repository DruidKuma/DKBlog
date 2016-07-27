CREATE TABLE blog_entry (
  be_id SERIAL PRIMARY KEY,
  be_author TEXT,
  be_permalink TEXT NOT NULL,
  be_creation_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  be_comments_enabled BOOLEAN DEFAULT FALSE,
  be_is_published BOOLEAN DEFAULT FALSE,
  be_num_views BIGINT,
  be_num_comments BIGINT,
  be_content_id INTEGER REFERENCES content(c_id)
);

CREATE TABLE content (
  c_id SERIAL PRIMARY KEY,
  c_image_url TEXT,
  c_title TEXT,
  c_contents TEXT
);

CREATE TABLE comments (
  cm_id SERIAL PRIMARY KEY,
  cm_blog_entry_id INTEGER REFERENCES blog_entry(be_id),
  cm_author TEXT,
  cm_author_email TEXT,
  cm_author_ip TEXT,
  cm_comment_parent_id INTEGER REFERENCES comments(cm_id),
  cm_author_user_agent TEXT,
  cm_body TEXT,
  cm_approved BOOLEAN DEFAULT FALSE,
  cm_creation_date TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE country (
  c_id SERIAL PRIMARY KEY,
  c_name TEXT NOT NULL,
  c_iso_2_alpha CHARACTER VARYING(2) NOT NULL,
  c_iso_3_alpha CHARACTER VARYING(3) NOT NULL,
  c_iso_numeric TEXT,
  c_enabled BOOLEAN DEFAULT FALSE
);

CREATE TABLE language (
  l_id SERIAL PRIMARY KEY,
  l_iso_code CHARACTER VARYING(2) NOT NULL,
  l_name TEXT,
  l_native_name TEXT
);

CREATE TABLE country_2_language (
  c2l_country_id INTEGER REFERENCES country(c_id),
  c2l_language_id INTEGER REFERENCES language(l_id)
);

CREATE TABLE translation_groups (
  tg_id SERIAL PRIMARY KEY,
  tg_name TEXT,
  tg_parent_group_id INTEGER REFERENCES translation_groups(tg_id)
);

CREATE TABLE translations (
  tr_id SERIAL PRIMARY KEY,
  tr_group_id INTEGER REFERENCES translation_groups (tg_id),
  tr_key TEXT,
  tr_value TEXT,
  tr_language_id INTEGER REFERENCES language(l_id),
  tr_last_modified TIMESTAMP WITHOUT TIME ZONE
);

ALTER TABLE country_2_language ADD COLUMN c2l_is_default NOT NULL DEFAULT FALSE;

CREATE TABLE blog_entry_2_country (
  be2c_blog_entry_id INTEGER REFERENCES blog_entry(be_id),
  be2c_country_id INTEGER REFERENCES country(c_id)
);

DO $$
DECLARE
  i record;
  l_id INTEGER;
BEGIN
  FOR i in (select * from blog_entry) LOOP
    SELECT c_id INTO l_id
    FROM country
    OFFSET floor(random()*100) LIMIT 1;

    INSERT INTO blog_entry_2_country(be2c_blog_entry_id, be2c_country_id) VALUES(i.be_id, l_id);
  END LOOP;
END
$$;