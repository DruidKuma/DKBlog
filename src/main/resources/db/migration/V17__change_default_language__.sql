CREATE OR REPLACE FUNCTION change_default_country_language(p_country_iso TEXT, p_lang_iso TEXT) RETURNS VOID
AS
$BODY$
DECLARE
  county_id INTEGER;
BEGIN
  UPDATE country_2_language SET c2l_is_default = FALSE WHERE c2l_country_id = (SELECT c_id FROM country WHERE c_iso_2_alpha = p_country_iso);
  UPDATE country_2_language SET c2l_is_default = TRUE WHERE c2l_country_id = (SELECT c_id FROM country WHERE c_iso_2_alpha = p_country_iso) AND c2l_language_id = (SELECT l_id FROM language WHERE l_iso_code = p_lang_iso);
END
$BODY$
LANGUAGE plpgsql VOLATILE SECURITY DEFINER;