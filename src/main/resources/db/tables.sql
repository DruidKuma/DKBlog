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
)