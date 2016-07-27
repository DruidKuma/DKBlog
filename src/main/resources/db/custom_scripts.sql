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