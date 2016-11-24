CREATE TABLE seo_settings (
  ss_id SERIAL PRIMARY KEY,
  ss_title TEXT,
  ss_description TEXT,
  ss_keywords TEXT,
  ss_meta_robots_nofollow BOOLEAN,
  ss_meta_robots_noindex BOOLEAN,
  ss_og_description TEXT,
  ss_og_image TEXT,
  ss_og_title TEXT,
  ss_og_type TEXT
);

ALTER TABLE blog_entry ADD COLUMN be_seo_settings_id INTEGER REFERENCES seo_settings(ss_id);