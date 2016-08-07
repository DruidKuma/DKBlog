UPDATE country_2_language as c2l1 SET c2l_is_default = TRUE
WHERE c2l_country_id IN (SELECT c1.c_id FROM country c1 WHERE NOT EXISTS(SELECT c2l3.c2l_country_id
                                                                         FROM country_2_language c2l3
                                                                         WHERE c2l3.c2l_country_id = c1.c_id
                                                                               AND c2l3.c2l_is_default))
      AND c2l_language_id IN (SELECT c2l2.c2l_language_id FROM country_2_language c2l2 WHERE c2l2.c2l_country_id = c2l1.c2l_country_id LIMIT 1);

INSERT INTO country_2_language VALUES
  (
    (SELECT c_id FROM country WHERE c_name = 'Antarctica'),
    (SELECT l_id FROM language WHERE l_iso_code = 'en'),
    TRUE
  ),
  (
    (SELECT c_id FROM country WHERE c_name = 'Bhutan'),
    (SELECT l_id FROM language WHERE l_iso_code = 'en'),
    TRUE
  );