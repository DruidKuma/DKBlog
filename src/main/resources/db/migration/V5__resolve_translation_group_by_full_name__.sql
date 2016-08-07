CREATE OR REPLACE FUNCTION resolve_translation_group_by_full_name(p_full_name TEXT) RETURNS INTEGER
AS
$BODY$
DECLARE
  result INTEGER;
  name_splitted TEXT[];
  names_length INTEGER;
BEGIN

  SELECT regexp_split_to_array(p_full_name, '\.') INTO name_splitted;
  SELECT array_length(name_splitted, 1) INTO names_length;

  SELECT tg_id INTO result
  FROM translation_groups
  WHERE tg_name = name_splitted[names_length]
        AND
        CASE WHEN names_length < 2
          THEN tg_parent_group_id IS NULL
        ELSE tg_parent_group_id = resolve_translation_group_by_full_name(array_to_string(name_splitted[1:names_length-1], '.'))
        END;
  RETURN result;
END
$BODY$
LANGUAGE plpgsql VOLATILE SECURITY DEFINER;