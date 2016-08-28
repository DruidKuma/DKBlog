CREATE OR REPLACE FUNCTION select_previous_blog_entry_id(p_blog_entry_id integer)
  RETURNS int4
AS
$BODY$
DECLARE
  result INTEGER;
BEGIN

  WITH ordered_entries AS (
      SELECT
        ROW_NUMBER() OVER (ORDER BY be_creation_date DESC) rownum,
        be_id,
        be_creation_date
      FROM blog_entry
  )
  SELECT INTO result shifted.be_id
  FROM ordered_entries current
    INNER JOIN ordered_entries shifted on shifted.rownum = current.rownum - 1
  WHERE current.be_id = p_blog_entry_id;

  RETURN result;
END
$BODY$
LANGUAGE plpgsql VOLATILE SECURITY DEFINER;

CREATE OR REPLACE FUNCTION select_next_blog_entry_id(p_blog_entry_id integer)
  RETURNS int4
AS
$BODY$
DECLARE
  result INTEGER;
BEGIN

  WITH ordered_entries AS (
      SELECT
        ROW_NUMBER() OVER (ORDER BY be_creation_date DESC) rownum,
        be_id,
        be_creation_date
      FROM blog_entry
  )
  SELECT INTO result shifted.be_id
  FROM ordered_entries current
    INNER JOIN ordered_entries shifted on shifted.rownum = current.rownum + 1
  WHERE current.be_id = p_blog_entry_id;

  RETURN result;
END
$BODY$
LANGUAGE plpgsql VOLATILE SECURITY DEFINER;