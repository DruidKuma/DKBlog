INSERT INTO category(ct_name_key, ct_hex_color, ct_last_modified) VALUES
  ('tipsAndTricks', '#000', now());

INSERT INTO category_2_country(c2c_category_id, c2c_country_id) VALUES
  (1, 235);

INSERT INTO translation_groups(tg_name, tg_parent_group_id) VALUES ('components', NULL);

INSERT INTO translation_groups(tg_name, tg_parent_group_id) VALUES
  ('category', (SELECT tg_id FROM translation_groups WHERE tg_name = 'components'));

INSERT INTO translations(tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES
  ((SELECT tg_id FROM translation_groups WHERE tg_name = 'category'), 'tipsAndTricks', 'Tips & Tricks', 40, now()),
  ((SELECT tg_id FROM translation_groups WHERE tg_name = 'category'), 'tipsAndTricks', 'Типсы & Трики', 133, now());

