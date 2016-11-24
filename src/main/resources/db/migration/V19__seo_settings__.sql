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
  ss_og_type TEXT,
  ss_blog_entry_id INTEGER REFERENCES blog_entry(be_id)
);