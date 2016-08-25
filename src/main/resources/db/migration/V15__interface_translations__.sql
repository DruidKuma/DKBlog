DROP TABLE IF EXISTS translations;
DROP TABLE IF EXISTS translation_groups;

CREATE TABLE translation_groups (
  tg_id integer NOT NULL,
  tg_name text,
  tg_parent_group_id integer
);
ALTER TABLE translation_groups OWNER TO postgres;

CREATE SEQUENCE translation_groups_tg_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER TABLE translation_groups_tg_id_seq OWNER TO postgres;

ALTER SEQUENCE translation_groups_tg_id_seq OWNED BY translation_groups.tg_id;


CREATE TABLE translations (
  tr_id integer NOT NULL,
  tr_group_id integer,
  tr_key text,
  tr_value text,
  tr_language_id integer,
  tr_last_modified timestamp without time zone
);

ALTER TABLE translations OWNER TO postgres;

CREATE SEQUENCE translations_tr_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER TABLE translations_tr_id_seq OWNER TO postgres;

ALTER SEQUENCE translations_tr_id_seq OWNED BY translations.tr_id;

ALTER TABLE ONLY translation_groups ALTER COLUMN tg_id SET DEFAULT nextval('translation_groups_tg_id_seq'::regclass);
ALTER TABLE ONLY translations ALTER COLUMN tr_id SET DEFAULT nextval('translations_tr_id_seq'::regclass);

INSERT INTO translation_groups (tg_id, tg_name, tg_parent_group_id) VALUES (1, 'layout', NULL);
INSERT INTO translation_groups (tg_id, tg_name, tg_parent_group_id) VALUES (2, 'sidebar', 1);
INSERT INTO translation_groups (tg_id, tg_name, tg_parent_group_id) VALUES (3, 'mainTitle', 2);
INSERT INTO translation_groups (tg_id, tg_name, tg_parent_group_id) VALUES (4, 'menu', 1);
INSERT INTO translation_groups (tg_id, tg_name, tg_parent_group_id) VALUES (5, 'postList', NULL);
INSERT INTO translation_groups (tg_id, tg_name, tg_parent_group_id) VALUES (6, 'filters', 5);
INSERT INTO translation_groups (tg_id, tg_name, tg_parent_group_id) VALUES (7, 'components', NULL);
INSERT INTO translation_groups (tg_id, tg_name, tg_parent_group_id) VALUES (8, 'category', 7);
INSERT INTO translation_groups (tg_id, tg_name, tg_parent_group_id) VALUES (10, 'testGroup', 7);
INSERT INTO translation_groups (tg_id, tg_name, tg_parent_group_id) VALUES (13, 'heading', 1);

SELECT pg_catalog.setval('translation_groups_tg_id_seq', 13, true);


INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (1, 2, 'publishedPosts', 'Published Posts', 40, '2016-07-25 13:40:52.153');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (2, 3, 'domain', 'NET', 40, '2016-07-25 13:40:52.166');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (3, 3, 'name', 'Tyres', 40, '2016-07-25 13:40:52.173');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (5, 4, 'searchPlaceholder', 'Search', 40, '2016-07-25 13:40:52.193');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (6, 4, 'copyright', '2016 © DruidKuma', 40, '2016-07-25 13:40:52.2');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (7, 5, 'newPost', 'New Post', 40, '2016-07-25 13:40:52.209');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (8, 6, 'show', 'Show', 40, '2016-07-25 13:40:52.217');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (9, 6, 'published', 'Published', 40, '2016-07-25 13:40:52.223');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (10, 6, 'sort', 'Sort', 40, '2016-07-25 13:40:52.228');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (11, 6, 'category', 'Category', 40, '2016-07-25 13:40:52.234');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (13, 2, 'publishedPosts', 'Все посты', 133, '2016-07-25 13:40:52.282');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (14, 3, 'domain', 'RU', 133, '2016-07-25 13:40:52.291');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (15, 3, 'name', 'Shiny', 133, '2016-07-25 13:40:52.296');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (16, 2, 'dashboard', 'Главная', 133, '2016-07-25 13:40:52.3');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (17, 4, 'searchPlaceholder', 'Поиск', 133, '2016-07-25 13:40:52.309');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (18, 4, 'copyright', '2016 © DruidKuma', 133, '2016-07-25 13:40:52.312');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (19, 5, 'newPost', 'Новый пост', 133, '2016-07-25 13:40:52.321');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (20, 6, 'show', 'Показать', 133, '2016-07-25 13:40:52.332');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (21, 6, 'published', 'Опубликован', 133, '2016-07-25 13:40:52.339');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (22, 6, 'sort', 'Сортировка', 133, '2016-07-25 13:40:52.346');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (23, 6, 'category', 'Категория', 133, '2016-07-25 13:40:52.353');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (24, 5, 'allPostsTitle', 'Все посты', 133, '2016-07-25 13:40:52.359');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (40, 2, 'questionsAndAnswers', 'Questions & Answers', 40, '2016-08-25 15:10:35.043');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (41, 2, 'mediaGallery', 'Media Gallery', 40, '2016-08-25 15:10:48.623');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (42, 2, 'userManagement', 'User Management', 40, '2016-08-25 15:11:05.464');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (43, 2, 'settings', 'Settings', 40, '2016-08-25 15:11:13.712');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (44, 2, 'i18nPanel', 'I18N Panel', 40, '2016-08-25 15:11:26.223');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (45, 2, 'systemSettings', 'System Settings', 40, '2016-08-25 15:11:37.151');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (46, 2, 'accountSettings', 'Account Settings', 40, '2016-08-25 15:12:00.383');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (47, 2, 'cronJobs', 'Cron Jobs', 40, '2016-08-25 15:12:13.35');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (48, 2, 'navigation', 'Navigation', 40, '2016-08-25 15:12:53.662');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (51, 13, 'i18nPanel', 'I18N Panel', 40, '2016-08-25 19:30:39.281');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (31, 8, 'testCategory', 'Сало та Горилка', 169, '2016-08-15 18:35:14.073');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (30, 8, 'testCategory', 'Test Category', 40, '2016-08-15 18:35:14.073');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (26, 8, 'tipsAndTricks', 'Типсы & Трики', 133, '2016-08-16 12:18:36.893');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (25, 8, 'tipsAndTricks', 'Tips & Tricks', 40, '2016-08-16 12:18:36.893');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (34, 8, 'tipsAndTricks', 'Tipps und Tricken', 51, '2016-08-16 12:18:36.893');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (27, 8, 'tipsAndTricks', 'Сало и Горилка', 169, '2016-08-16 12:18:36.893');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (35, 8, 'mazafucka', 'Mazafucka', 40, '2016-08-16 12:18:51.248');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (36, 8, 'mazafucka', 'Категория', 169, '2016-08-16 12:18:51.249');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (4, 2, 'dashboard', 'Dashboard', 40, '2016-08-25 14:28:40.431');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (12, 5, 'allPostsTitle', 'All Posts', 40, '2016-08-25 14:44:00.431');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (38, 2, 'categories', 'Categories', 40, '2016-08-25 14:48:40.338');
INSERT INTO translations (tr_id, tr_group_id, tr_key, tr_value, tr_language_id, tr_last_modified) VALUES (39, 2, 'comments', 'Comments', 40, '2016-08-25 15:10:16.681');

SELECT pg_catalog.setval('translations_tr_id_seq', 51, true);


ALTER TABLE ONLY translation_groups
ADD CONSTRAINT translation_groups_pkey PRIMARY KEY (tg_id);

ALTER TABLE ONLY translations
ADD CONSTRAINT translations_pkey PRIMARY KEY (tr_id);

ALTER TABLE ONLY translation_groups
ADD CONSTRAINT translation_groups_tg_parent_group_id_fkey FOREIGN KEY (tg_parent_group_id) REFERENCES translation_groups(tg_id);

ALTER TABLE ONLY translations
ADD CONSTRAINT translations_tr_group_id_fkey FOREIGN KEY (tr_group_id) REFERENCES translation_groups(tg_id);

ALTER TABLE ONLY translations
ADD CONSTRAINT translations_tr_language_id_fkey FOREIGN KEY (tr_language_id) REFERENCES language(l_id);