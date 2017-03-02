
-- ----------------------------
-- Table structure for authorities
-- ----------------------------
CREATE TABLE authorities (
  authority_id bigint(20) NOT NULL AUTO_INCREMENT,
  authority varchar(50) NOT NULL,
  is_locked tinyint(1) NOT NULL,
  PRIMARY KEY (authority_id)
);


-- ----------------------------
-- Table structure for users
-- ----------------------------
CREATE TABLE users (
  user_id bigint(20) NOT NULL AUTO_INCREMENT,
  username varchar(50) NOT NULL,
  email varchar(150) NOT NULL,
  first_name varchar(40) NOT NULL,
  last_name varchar(40) NOT NULL,
  enabled tinyint(1) NOT NULL,
  account_expired tinyint(1) NOT NULL,
  account_locked tinyint(1) NOT NULL,
  credentials_expired tinyint(1) NOT NULL,
  has_avatar tinyint(1) NOT NULL,
  user_key varchar(25) NOT NULL DEFAULT '0000000000000000',
  provider_id varchar(25) NOT NULL DEFAULT 'SITE',
  password varchar(255) NOT NULL,
  version int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (user_id)
);

-- ----------------------------
-- Table structure for hobbies
-- ----------------------------
CREATE TABLE hobbies (
  hobby_id bigint(20) NOT NULL AUTO_INCREMENT,
  hobby_title varchar(20) NOT NULL,
  PRIMARY KEY (hobby_id)
);


-- ----------------------------
-- Table structure for contacts
-- ----------------------------
CREATE TABLE contacts (
  contact_id bigint(20) NOT NULL AUTO_INCREMENT,
  first_name varchar(40) NOT NULL,
  last_name varchar(40) NOT NULL,
  birth_date date DEFAULT NULL,
  email varchar(100) NOT NULL,
  created_by_user varchar(50) NOT NULL,
  creation_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  modified_by_user varchar(50) NOT NULL,
  modification_time timestamp NULL DEFAULT NULL,
  version int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (contact_id)
);


-- ----------------------------
-- Table structure for contact_hobby_ids
-- ----------------------------
DROP TABLE IF EXISTS contact_hobby_ids;
CREATE TABLE contact_hobby_ids (
  contact_hobby_id bigint(20) NOT NULL AUTO_INCREMENT,
  contact_id bigint(20) NOT NULL,
  hobby_id bigint(20) NOT NULL,
  PRIMARY KEY (contact_hobby_id),
  CONSTRAINT fk_hobby_contact_id FOREIGN KEY (contact_id) REFERENCES contacts (contact_id) ON DELETE CASCADE,
  CONSTRAINT fk_hobby_hobby_id FOREIGN KEY (hobby_id) REFERENCES hobbies (hobby_id)
);

-- ----------------------------
-- Table structure for contact_phones
-- ----------------------------
CREATE TABLE contact_phones (
  contact_phone_id bigint(20) NOT NULL AUTO_INCREMENT,
  contact_id bigint(20) NOT NULL,
  phone_type varchar(20) NOT NULL,
  phone_number varchar(20) NOT NULL,
  version int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (contact_phone_id),
  CONSTRAINT fk_contact_phones_contact_id FOREIGN KEY (contact_id) REFERENCES contacts (contact_id)
);

-- ----------------------------
-- Table structure for site_options
-- ----------------------------
CREATE TABLE site_options (
  option_id bigint(20) NOT NULL AUTO_INCREMENT,
  option_name varchar(50) NOT NULL,
  option_value varchar(5000),
  PRIMARY KEY (option_id)
);

-- ----------------------------
-- Table structure for user_authorities
-- ----------------------------
CREATE TABLE user_authorities (
  user_id bigint(20) NOT NULL,
  authority_id bigint(20) NOT NULL,
  UNIQUE KEY uc_user_authorities (user_id,authority_id),
  CONSTRAINT fk_user_authorities_1 FOREIGN KEY (user_id) REFERENCES users (user_id),
  CONSTRAINT fk_user_authorities_2 FOREIGN KEY (authority_id) REFERENCES authorities (authority_id)
);

-- ----------------------------
-- Table structure for user_profiles
-- ----------------------------
CREATE TABLE user_profiles (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  address varchar(255) DEFAULT NULL,
  address2 varchar(255) DEFAULT NULL,
  city varchar(255) DEFAULT NULL,
  phone varchar(255) DEFAULT NULL,
  state varchar(255) DEFAULT NULL,
  zip varchar(10) DEFAULT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for user_data
-- ----------------------------
CREATE TABLE user_data
(
  user_id BIGINT(20) NOT NULL,
  login_attempts INT(11) DEFAULT '0' NOT NULL,
  lastlogin_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  created_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  approved_datetime TIMESTAMP NULL,
  invited_datetime TIMESTAMP NULL,
  accepted_datetime TIMESTAMP NULL,
  invited_by_id BIGINT(20) DEFAULT '0' NOT NULL,
  ip VARCHAR(25),
  UNIQUE INDEX user_data_user_id_uindex (user_id),
  PRIMARY KEY (user_id),
  CONSTRAINT user_data_users_fk FOREIGN KEY (user_id) REFERENCES users (user_id)
);

-- ----------------------------
-- Table structure for userconnection
-- ----------------------------
CREATE TABLE userconnection (
  userId varchar(255) NOT NULL,
  providerId varchar(255) NOT NULL,
  providerUserId varchar(255) NOT NULL DEFAULT '',
  rank int(11) NOT NULL,
  displayName varchar(255) DEFAULT NULL,
  profileUrl varchar(512) DEFAULT NULL,
  imageUrl varchar(512) DEFAULT NULL,
  accessToken varchar(255) NOT NULL,
  secret varchar(255) DEFAULT NULL,
  refreshToken varchar(255) DEFAULT NULL,
  expireTime bigint(20) DEFAULT NULL,
  PRIMARY KEY (userId,providerId,providerUserId)
);

-- ----------------------------
-- Table structure for posts
-- ----------------------------
DROP TABLE IF EXISTS posts;
CREATE TABLE posts (
  post_id bigint(20) NOT NULL AUTO_INCREMENT,
  user_id bigint(20) NOT NULL,
  post_title varchar(200) NOT NULL,
  post_name varchar(200) NOT NULL,
  post_link varchar(255) DEFAULT NULL,
  post_date timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  post_modified timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  post_type varchar(20) NOT NULL DEFAULT 'LINK',
  display_type varchar(20) NOT NULL DEFAULT 'LINK',
  is_published tinyint(1) NOT NULL DEFAULT '0',
  post_content varchar(5000) NOT NULL,
  post_source varchar(50) DEFAULT 'NA',
  post_image varchar(200) DEFAULT NULL,
  click_count int(11) NOT NULL DEFAULT '0',
  likes_count int(11) NOT NULL DEFAULT '0',
  value_rating int(11) NOT NULL DEFAULT '0',
  version int(11) NOT NULL DEFAULT '0',
  UNIQUE KEY posts_post_id_uindex (post_id),
  UNIQUE KEY posts_post_name_pk (post_name),
  PRIMARY KEY (post_id),
  CONSTRAINT posts_users_user_id_fk FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE tags
(
  tag_id BIGINT(20) NOT NULL AUTO_INCREMENT,
  tag_value VARCHAR(50) NOT NULL,
  PRIMARY KEY (tag_id)
);

CREATE TABLE post_tag_ids
(
  post_tag_id BIGINT(20) NOT NULL AUTO_INCREMENT,
  post_id BIGINT(20) NOT NULL,
  tag_id BIGINT(20) NOT NULL,
  PRIMARY KEY (post_tag_id),
  CONSTRAINT fk_posts_post_id FOREIGN KEY (post_id) REFERENCES posts (post_id),
  CONSTRAINT fk_tags_tag_id FOREIGN KEY (tag_id) REFERENCES tags (tag_id)
);

DROP TABLE IF EXISTS categories;
CREATE TABLE categories
(
  category_id BIGINT(20) NOT NULL AUTO_INCREMENT,
  category_value VARCHAR(50) NOT NULL,
  wp_category_id BIGINT(20) NOT NULL,
  is_active tinyint(1) NOT NULL DEFAULT '1',
  is_default tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (category_id)
);

CREATE TABLE post_category_ids
(
  post_category_id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  post_id BIGINT(20) NOT NULL,
  category_id BIGINT(20) NOT NULL,
  CONSTRAINT fk_categories_post_id FOREIGN KEY (post_id) REFERENCES posts (post_id),
  CONSTRAINT fk_categories_category_id FOREIGN KEY (category_id) REFERENCES categories (category_id)
);
CREATE INDEX fk_categories_category_id ON post_category_ids (category_id);
CREATE INDEX fk_categories_post_id ON post_category_ids (post_id);

CREATE TABLE user_likes (
  like_id bigint(20) NOT NULL AUTO_INCREMENT,
  user_id bigint(20) DEFAULT NULL,
  item_id bigint(20) DEFAULT NULL,
  content_type_id int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (like_id),
  UNIQUE KEY like_ids_index (like_id),
  CONSTRAINT fk_likes_user_id FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE post_images (
  image_id bigint(20) NOT NULL AUTO_INCREMENT,
  post_id bigint(20) DEFAULT NOT NULL,
  image_name varchar(255) DEFAULT NULL,
  thumbnail_filename varchar(255) DEFAULT NULL,
  filename varchar(255) DEFAULT NULL,
  content_type varchar(50) DEFAULT NULL,
  size bigint(20) DEFAULT NULL,
  thumbnail_size bigint(20) DEFAULT NULL,
  datetime_created timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (image_id),
  UNIQUE KEY post_images_image_id_uindex (image_id)
);

CREATE TABLE user_tokens (
  token_id bigint(20) NOT NULL AUTO_INCREMENT,
  user_id bigint(20) NOT NULL,
  token varchar(255) DEFAULT NULL,
  token_expiration timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (token_id),
  UNIQUE KEY user_tokens_token_id_uindex (token_id),
  UNIQUE KEY user_tokens_user_id_uindex (user_id)
);

CREATE TABLE github_stats
(
  stat_id BIGINT(20) NOT NULL AUTO_INCREMENT,
  stat_date DATE NOT NULL,
  followers INT(11) DEFAULT '0' NOT NULL,
  subscribers INT(11) DEFAULT '0' NOT NULL,
  stars INT(11) DEFAULT '0' NOT NULL,
  forks INT(11) DEFAULT '0' NOT NULL,
  PRIMARY KEY (stat_id),
  UNIQUE KEY github_stats_stat_id_uindex (stat_id),
  UNIQUE KEY github_stats_stat_date_uindex (stat_date)
);

DROP TABLE IF EXISTS flashcard_categories;
CREATE TABLE flashcard_categories (
  category_id bigint(20) NOT NULL AUTO_INCREMENT,
  category varchar(255) DEFAULT NULL,
  PRIMARY KEY (category_id),
  UNIQUE KEY flashcard_categories_category_id_uindex (category_id)
);

DROP TABLE IF EXISTS flashcard_slides;
CREATE TABLE flashcard_slides (
  slide_id bigint(20) NOT NULL AUTO_INCREMENT,
  category_id bigint(20) NOT NULL,
  post_id bigint(20) NOT NULL DEFAULT -1,
  slide_image varchar(255) DEFAULT NULL,
  slide_content varchar(2147483647),
  datetime_created timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (slide_id),
  UNIQUE KEY flashcard_slides_slide_id_uindex (slide_id),
  CONSTRAINT fk_flashcard_slides_categories FOREIGN KEY (category_id) REFERENCES flashcard_categories (category_id),
  CONSTRAINT fk_flashcard_slides_posts FOREIGN KEY (post_id) REFERENCES posts (post_id)
);

DROP VIEW IF EXISTS v_flashcards;
create VIEW v_flashcards AS
  select s.slide_id as slide_id,
         s.category_id as category_id,
         s.slide_image as slide_image,
         s.slide_content as slide_content,
         s.datetime_created as datetime_created,
         c.category  as category,
          p.post_title as post_title,
          p.post_id as post_id
  from flashcard_slides s inner join flashcard_categories c on s.category_id = c.category_id inner join posts p on s.post_id = p.post_id;

-- ------------------------------------------------------------------------------------
-- BEGIN SPRING BATCH FROM FRAMEWORK
-- ------------------------------------------------------------------------------------

CREATE TABLE batch_job_instance
(
  JOB_INSTANCE_ID BIGINT(20) PRIMARY KEY NOT NULL,
  VERSION BIGINT(20),
  JOB_NAME VARCHAR(100) NOT NULL,
  JOB_KEY VARCHAR(32) NOT NULL
);
CREATE UNIQUE INDEX JOB_INST_UN ON batch_job_instance (JOB_NAME, JOB_KEY);
CREATE TABLE batch_job_execution
(
  JOB_EXECUTION_ID BIGINT(20) PRIMARY KEY NOT NULL,
  VERSION BIGINT(20),
  JOB_INSTANCE_ID BIGINT(20) NOT NULL,
  CREATE_TIME DATETIME NOT NULL,
  START_TIME DATETIME,
  END_TIME DATETIME,
  STATUS VARCHAR(10),
  EXIT_CODE VARCHAR(2500),
  EXIT_MESSAGE VARCHAR(2500),
  LAST_UPDATED DATETIME,
  JOB_CONFIGURATION_LOCATION VARCHAR(2500),
  CONSTRAINT JOB_INST_EXEC_FK FOREIGN KEY (JOB_INSTANCE_ID) REFERENCES batch_job_instance (JOB_INSTANCE_ID)
);
CREATE INDEX JOB_INST_EXEC_FK ON batch_job_execution (JOB_INSTANCE_ID);
CREATE TABLE batch_job_execution_context
(
  JOB_EXECUTION_ID BIGINT(20) PRIMARY KEY NOT NULL,
  SHORT_CONTEXT VARCHAR(2500) NOT NULL,
  SERIALIZED_CONTEXT TEXT,
  CONSTRAINT JOB_EXEC_CTX_FK FOREIGN KEY (JOB_EXECUTION_ID) REFERENCES batch_job_execution (JOB_EXECUTION_ID)
);
CREATE TABLE batch_job_execution_params
(
  JOB_EXECUTION_ID BIGINT(20) NOT NULL,
  TYPE_CD VARCHAR(6) NOT NULL,
  KEY_NAME VARCHAR(100) NOT NULL,
  STRING_VAL VARCHAR(250),
  DATE_VAL DATETIME,
  LONG_VAL BIGINT(20),
  DOUBLE_VAL DOUBLE,
  IDENTIFYING CHAR(1) NOT NULL,
  CONSTRAINT JOB_EXEC_PARAMS_FK FOREIGN KEY (JOB_EXECUTION_ID) REFERENCES batch_job_execution (JOB_EXECUTION_ID)
);
CREATE INDEX JOB_EXEC_PARAMS_FK ON batch_job_execution_params (JOB_EXECUTION_ID);
CREATE TABLE batch_job_execution_seq
(
  ID BIGINT(20) NOT NULL,
  UNIQUE_KEY CHAR(1) NOT NULL
);
CREATE UNIQUE INDEX UNIQUE_KEY_EXEC_UN ON batch_job_execution_seq (UNIQUE_KEY);


CREATE TABLE batch_job_seq
(
  ID BIGINT(20) NOT NULL,
  UNIQUE_KEY CHAR(1) NOT NULL
);
CREATE UNIQUE INDEX UNIQUE_KEY_SEQ_UN ON batch_job_seq (UNIQUE_KEY);
CREATE TABLE batch_step_execution
(
  STEP_EXECUTION_ID BIGINT(20) PRIMARY KEY NOT NULL,
  VERSION BIGINT(20) NOT NULL,
  STEP_NAME VARCHAR(100) NOT NULL,
  JOB_EXECUTION_ID BIGINT(20) NOT NULL,
  START_TIME DATETIME NOT NULL,
  END_TIME DATETIME,
  STATUS VARCHAR(10),
  COMMIT_COUNT BIGINT(20),
  READ_COUNT BIGINT(20),
  FILTER_COUNT BIGINT(20),
  WRITE_COUNT BIGINT(20),
  READ_SKIP_COUNT BIGINT(20),
  WRITE_SKIP_COUNT BIGINT(20),
  PROCESS_SKIP_COUNT BIGINT(20),
  ROLLBACK_COUNT BIGINT(20),
  EXIT_CODE VARCHAR(2500),
  EXIT_MESSAGE VARCHAR(2500),
  LAST_UPDATED DATETIME,
  CONSTRAINT JOB_EXEC_STEP_FK FOREIGN KEY (JOB_EXECUTION_ID) REFERENCES batch_job_execution (JOB_EXECUTION_ID)
);
CREATE INDEX JOB_EXEC_STEP_FK ON batch_step_execution (JOB_EXECUTION_ID);
CREATE TABLE batch_step_execution_context
(
  STEP_EXECUTION_ID BIGINT(20) PRIMARY KEY NOT NULL,
  SHORT_CONTEXT VARCHAR(2500) NOT NULL,
  SERIALIZED_CONTEXT TEXT,
  CONSTRAINT STEP_EXEC_CTX_FK FOREIGN KEY (STEP_EXECUTION_ID) REFERENCES batch_step_execution (STEP_EXECUTION_ID)
);
CREATE TABLE batch_step_execution_seq
(
  ID BIGINT(20) NOT NULL,
  UNIQUE_KEY CHAR(1) NOT NULL
);
CREATE UNIQUE INDEX UNIQUE_KEY_SETP_UN ON batch_step_execution_seq (UNIQUE_KEY);

-- ------------------------------------------------------------------------------------
-- END SPRING BATCH FROM FRAMEWORK
-- ------------------------------------------------------------------------------------

-- ------------------------------------------------------------------------------------
-- Batch View for BatchJob Reports
-- ------------------------------------------------------------------------------------

CREATE VIEW v_batch_job_report AS
  SELECT
    batch_job_execution.JOB_INSTANCE_ID AS JOB_INSTANCE_ID,
    batch_job_instance.JOB_NAME         AS JOB_NAME,
    batch_job_execution.START_TIME      AS START_TIME,
    batch_job_execution.END_TIME        AS END_TIME,
    batch_job_execution.STATUS          AS STATUS,
    batch_job_execution.EXIT_CODE       AS EXIT_CODE,
    batch_job_execution.EXIT_MESSAGE    AS EXIT_MESSAGE
  FROM (batch_job_execution
    JOIN batch_job_instance ON ((batch_job_execution.JOB_INSTANCE_ID =
                                 batch_job_instance.JOB_INSTANCE_ID)));


