/* ------------------------------------------------------------------------------

        Script to create schema in NixMash Spring Blog MySQL Destination
        WordPress NixMash Spring Migrator Version v1.0.0

--------------------------------------------------------------------------- */

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for authorities
-- ----------------------------
DROP TABLE IF EXISTS `authorities`;
CREATE TABLE `authorities` (
  `authority_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `authority` varchar(50) NOT NULL,
  `is_locked` tinyint(1) NOT NULL,
  PRIMARY KEY (`authority_id`),
  UNIQUE KEY `uc_authorities` (`authority`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE users
(
  user_id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  email VARCHAR(150) NOT NULL,
  first_name VARCHAR(40) NOT NULL,
  last_name VARCHAR(40) NOT NULL,
  enabled TINYINT(1) NOT NULL,
  account_expired TINYINT(1) NOT NULL,
  account_locked TINYINT(1) NOT NULL,
  credentials_expired TINYINT(1) NOT NULL,
  has_avatar TINYINT(1) NOT NULL,
  user_key VARCHAR(25) DEFAULT '0000000000000000' NOT NULL,
  provider_id VARCHAR(25) DEFAULT 'SITE' NOT NULL,
  password VARCHAR(255) NOT NULL,
  created_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  approved_datetime TIMESTAMP,
  version INT(11) DEFAULT '0' NOT NULL
);
CREATE UNIQUE INDEX users_user_key_uindex ON users (user_key);

-- ----------------------------
-- Table structure for user_data
-- ----------------------------
DROP TABLE IF EXISTS `user_data`;
CREATE TABLE user_data
(
  user_id BIGINT(20) PRIMARY KEY NOT NULL,
  login_attempts INT(11) DEFAULT '0' NOT NULL,
  lastlogin_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  created_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  approved_datetime TIMESTAMP NULL,
  invited_datetime TIMESTAMP NULL,
  accepted_datetime TIMESTAMP NULL,
  invited_by_id BIGINT(20) DEFAULT '0' NOT NULL,
  ip VARCHAR(25),
  CONSTRAINT user_data_users_fk FOREIGN KEY (user_id)
  REFERENCES users (user_id)
);
CREATE UNIQUE INDEX user_data_user_id_uindex ON user_data (user_id);

-- ----------------------------
-- Table structure for user_authorities
-- ----------------------------
DROP TABLE IF EXISTS `user_authorities`;
CREATE TABLE `user_authorities` (
  `user_id` bigint(20) NOT NULL,
  `authority_id` bigint(20) NOT NULL,
  UNIQUE KEY `uc_user_authorities` (`user_id`,`authority_id`),
  KEY `fk_user_authorities_2` (`authority_id`),
  CONSTRAINT `fk_user_authorities_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `fk_user_authorities_2` FOREIGN KEY (`authority_id`) REFERENCES `authorities` (`authority_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- ----------------------------
-- Table structure for user_profiles
-- ----------------------------
DROP TABLE IF EXISTS `user_profiles`;
CREATE TABLE `user_profiles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `address2` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `zip` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- ----------------------------
-- Table structure for userconnection
-- ----------------------------
DROP TABLE IF EXISTS `userconnection`;
CREATE TABLE `userconnection` (
  `userId` varchar(255) NOT NULL,
  `providerId` varchar(255) NOT NULL,
  `providerUserId` varchar(255) NOT NULL DEFAULT '',
  `rank` int(11) NOT NULL,
  `displayName` varchar(255) DEFAULT NULL,
  `profileUrl` varchar(512) DEFAULT NULL,
  `imageUrl` varchar(512) DEFAULT NULL,
  `accessToken` varchar(255) NOT NULL,
  `secret` varchar(255) DEFAULT NULL,
  `refreshToken` varchar(255) DEFAULT NULL,
  `expireTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`userId`,`providerId`,`providerUserId`),
  UNIQUE KEY `UserConnectionRank` (`userId`,`providerId`,`rank`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE UNIQUE INDEX UserConnectionProviderUser ON UserConnection(providerId, providerUserId);

-- ----------------------------
-- Table structure for site_options
-- ----------------------------
DROP TABLE IF EXISTS `site_options`;
CREATE TABLE `site_options`
(
  `option_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `option_name` VARCHAR(50) NOT NULL,
  `option_value` TEXT,
  PRIMARY KEY (`option_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

CREATE UNIQUE INDEX SiteOptionsOptionId ON site_options (option_id);

-- ----------------------------
-- Table structure for posts
-- ----------------------------
DROP TABLE IF EXISTS posts;
CREATE TABLE posts
(
  post_id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  user_id BIGINT(20) NOT NULL,
  post_title VARCHAR(200) NOT NULL,
  post_name VARCHAR(200) NOT NULL,
  post_link VARCHAR(255),
  post_date TIMESTAMP DEFAULT '0000-00-00 00:00:00' NOT NULL,
  post_modified TIMESTAMP DEFAULT '0000-00-00 00:00:00' NOT NULL,
  post_type VARCHAR(20) DEFAULT 'LINK' NOT NULL,
  display_type VARCHAR(20) DEFAULT 'LINK' NOT NULL,
  is_published TINYINT(1) DEFAULT '0' NOT NULL,
  post_content TEXT NOT NULL,
  post_source VARCHAR(50) DEFAULT 'NA' NOT NULL,
  post_image VARCHAR(200),
  click_count INT(11) DEFAULT '0' NOT NULL,
  likes_count INT(11) DEFAULT '0' NOT NULL,
  value_rating INT(11) DEFAULT '0' NOT NULL,
  version INT(11) DEFAULT '0' NOT NULL,
  wp_post_id BIGINT(20) DEFAULT '0',
  CONSTRAINT posts_users_user_id_fk FOREIGN KEY (user_id) REFERENCES users (user_id)
) ENGINE=InnoDB DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE UNIQUE INDEX posts_post_id_uindex ON posts (post_id);
CREATE INDEX posts_users_user_id_fk ON posts (user_id);

-- ----------------------------
-- Set posts.post_content table for proper encoding on HTML on WP import
-- ----------------------------

# ALTER TABLE posts MODIFY COLUMN post_content TEXT
# CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL;

-- ----------------------------
-- Table structure for tags
-- ----------------------------

DROP TABLE IF EXISTS `tags`;
CREATE TABLE `tags` (
  `tag_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tag_value` varchar(50) NOT NULL,
  `wp_tag_id` BIGINT(20),
  PRIMARY KEY (`tag_id`),
  UNIQUE KEY `tags_tag_id_uindex` (`tag_id`),
  UNIQUE KEY `tags_tag_value_uindex` (`tag_value`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for categories
-- ----------------------------

DROP TABLE IF EXISTS categories;
CREATE TABLE categories
(
  category_id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  category_value VARCHAR(50) NOT NULL,
  wp_category_id BIGINT(20),
  is_active tinyint(1) NOT NULL DEFAULT '1',
  is_default tinyint(1) NOT NULL DEFAULT '0'
);
CREATE UNIQUE INDEX categories_category_id_uindex ON categories (category_id);

-- ----------------------------
-- Table structure for post_tag_ids
-- ----------------------------

DROP TABLE IF EXISTS `post_tag_ids`;
CREATE TABLE `post_tag_ids` (
  `post_tag_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `post_id` bigint(20) NOT NULL,
  `tag_id` bigint(20) NOT NULL,
  PRIMARY KEY (`post_tag_id`),
  KEY `fk_posts_post_id` (`post_id`),
  KEY `fk_tags_tag_id` (`tag_id`),
  CONSTRAINT `fk_posts_post_id` FOREIGN KEY (`post_id`) REFERENCES `posts` (`post_id`),
  CONSTRAINT `fk_tags_tag_id` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for post_category_ids
-- ----------------------------

DROP TABLE IF EXISTS post_category_ids;
CREATE TABLE post_category_ids
(
  post_category_id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  post_id BIGINT(20) NOT NULL,
  category_id BIGINT(20) NOT NULL,
  CONSTRAINT fk_categories_post_id FOREIGN KEY (post_id) REFERENCES posts (post_id),
  CONSTRAINT fk_categories_category_id FOREIGN KEY (category_id) REFERENCES categories (category_id)
);
CREATE INDEX categories_category_id_uindex ON post_category_ids (category_id);
CREATE INDEX categories_post_id_uindex ON post_category_ids (post_id);

-- ----------------------------
-- Table structure for post_meta
-- ----------------------------

DROP TABLE IF EXISTS post_meta;
CREATE TABLE post_meta
(
  post_id BIGINT(20) PRIMARY KEY NOT NULL,
  twitter_card VARCHAR(25) NOT NULL,
  twitter_creator VARCHAR(50),
  twitter_image VARCHAR(200),
  twitter_description VARCHAR(500),
  CONSTRAINT post_meta_posts_post_id_fk FOREIGN KEY (post_id) REFERENCES posts (post_id)
);
CREATE UNIQUE INDEX post_metadata_post_id_uindex ON post_meta (post_id);


-- ----------------------------
-- Table structure for user_likes
-- ----------------------------

DROP TABLE IF EXISTS user_likes;
CREATE TABLE user_likes
(
  like_id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  user_id BIGINT(20) NOT NULL,
  item_id BIGINT(20) NOT NULL,
  content_type_id INT(11) DEFAULT '1' NOT NULL,
  CONSTRAINT fk_likes_user_id FOREIGN KEY (user_id) REFERENCES users (user_id)
);
CREATE INDEX like_content_type_index ON user_likes (content_type_id);
CREATE UNIQUE INDEX like_ids_index ON user_likes (like_id);
CREATE INDEX like_item_id_index ON user_likes (item_id);
CREATE INDEX like_user_id_index ON user_likes (user_id);

-- ----------------------------
-- Table structure for post_images
-- ----------------------------

DROP TABLE IF EXISTS post_images;
CREATE TABLE post_images
(
  image_id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  post_id BIGINT(20) NOT NULL,
  image_name VARCHAR(255),
  thumbnail_filename VARCHAR(255),
  filename VARCHAR(255),
  content_type VARCHAR(50),
  size BIGINT(20),
  thumbnail_size BIGINT(20),
  datetime_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
CREATE UNIQUE INDEX post_images_image_id_uindex ON post_images (image_id);
CREATE INDEX post_images_post_id_index ON post_images (post_id);

-- ----------------------------
-- Table structure for post_images
-- ----------------------------

DROP TABLE IF EXISTS user_tokens;
CREATE TABLE user_tokens
(
  token_id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  user_id BIGINT(20) NOT NULL,
  token VARCHAR(255),
  token_expiration TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
CREATE UNIQUE INDEX user_tokens_token_id_uindex ON user_tokens (token_id);
CREATE UNIQUE INDEX user_tokens_user_id_uindex ON user_tokens (user_id);


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
CREATE UNIQUE INDEX UNIQUE_KEY_UN ON batch_job_execution_seq (UNIQUE_KEY);


CREATE TABLE batch_job_seq
(
  ID BIGINT(20) NOT NULL,
  UNIQUE_KEY CHAR(1) NOT NULL
);
CREATE UNIQUE INDEX UNIQUE_KEY_UN ON batch_job_seq (UNIQUE_KEY);
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
CREATE UNIQUE INDEX UNIQUE_KEY_UN ON batch_step_execution_seq (UNIQUE_KEY);

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

-- ------------------------------------------------------------------------------------
-- Github Stats Table
-- ------------------------------------------------------------------------------------

CREATE TABLE github_stats
(
  stat_id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  stat_date DATE NOT NULL,
  followers INT(11) DEFAULT '0' NOT NULL,
  subscribers INT(11) DEFAULT '0' NOT NULL,
  stars INT(11) DEFAULT '0' NOT NULL,
  forks INT(11) DEFAULT '0' NOT NULL
);
CREATE UNIQUE INDEX github_stats_stat_id_uindex ON github_stats (stat_id);
CREATE UNIQUE INDEX github_stats_stat_date_uindex ON github_stats (stat_date);


-- ----------------------------
-- Insert into authorities
-- ----------------------------
INSERT INTO `authorities` VALUES ('1', 'ROLE_ADMIN','1');
INSERT INTO `authorities` VALUES ('2', 'ROLE_USER','1');
INSERT INTO `authorities` VALUES ('3', 'ROLE_POSTS','1');

-- ----------------------------
-- Insert into users
-- ----------------------------
INSERT INTO `users` VALUES ('1', 'ken', 'ken@aol.com', 'Ken', 'Watts', '1', '0', '0', '0',  '0', 'TUuOypJ5pPI0ksqi', 'SITE', '$2a$10$F2a2W8RtbD99xXd9xtwjbuI4zjSYe04kS.s0FyvQcAIDJfh/6jjLW', CURRENT_TIMESTAMP, NULL, '0');

INSERT INTO user_data (user_id) SELECT user_id from users;

-- ----------------------------
-- Insert into user_authorities
-- ----------------------------
INSERT INTO `user_authorities` VALUES ('1', '1');
INSERT INTO `user_authorities` VALUES ('1', '2');
INSERT INTO `user_authorities` VALUES ('1', '3');

-- ----------------------------
-- Insert into site_options
-- ----------------------------
INSERT INTO `site_options` VALUES ('1', 'siteName', 'My Site');
INSERT INTO `site_options` VALUES ('2', 'siteDescription', 'My Site Description');
INSERT INTO `site_options` VALUES ('3', 'addGoogleAnalytics', 'false');
INSERT INTO `site_options` VALUES ('4', 'googleAnalyticsTrackingId', 'UA-XXXXXX-7');
INSERT INTO `site_options` VALUES ('5', 'userRegistration', 'EMAIL_VERIFICATION');

