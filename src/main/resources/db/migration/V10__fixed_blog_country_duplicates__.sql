CREATE TABLE public.tmp (
  be2c_blog_entry_id INTEGER,
  be2c_country_id INTEGER,
  FOREIGN KEY (be2c_blog_entry_id) REFERENCES public.blog_entry (be_id)
  MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  FOREIGN KEY (be2c_country_id) REFERENCES public.country (c_id)
  MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

INSERT INTO tmp SELECT DISTINCT * FROM blog_entry_2_country;
DROP TABLE blog_entry_2_country;
ALTER TABLE tmp RENAME TO blog_entry_2_country;
CREATE INDEX be2c_indx ON blog_entry_2_country USING BTREE (be2c_blog_entry_id, be2c_country_id);