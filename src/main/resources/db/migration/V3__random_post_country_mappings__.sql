CREATE OR REPLACE FUNCTION create_random_blog_post_country_mappings() RETURNS VOID
AS
$BODY$
DECLARE
  post RECORD;
  country_id INTEGER;
BEGIN
  FOR post in (SELECT * FROM blog_entry) LOOP
    SELECT c_id INTO country_id
    FROM country
    OFFSET FLOOR(RANDOM()*100) LIMIT 1;

    INSERT INTO blog_entry_2_country(be2c_blog_entry_id, be2c_country_id) VALUES(i.be_id, l_id);
  END LOOP;
END
$BODY$
LANGUAGE plpgsql VOLATILE SECURITY DEFINER;