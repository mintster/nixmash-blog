/*
Navicat MariaDB Data Transfer

Source Server         : mariadb
Source Server Version : 100017
Source Host           : localhost:3306
Source Database       : dev_hibernate

Target Server Type    : MariaDB
Target Server Version : 100017
File Encoding         : 65001

Date: 2015-04-21 17:49:43
*/


/* ------------------------------------------------------------ */
-- Users
/* ------------------------------------------------------------ */

INSERT INTO users (user_id, email, username, password, first_name, last_name, enabled, account_expired, account_locked, credentials_expired, has_avatar, user_key, provider_id)
VALUES
  (1, 'admin@email.com', 'admin', '$2a$10$B9wQFSrr3bfQeUGqxtTDuut1.4YFcA/WFthZaGe1wtb1wgVW./Oiq', 'Admin', 'Jones',
      TRUE, FALSE, FALSE, FALSE, FALSE, '4L4Hr3skHYYMbjkQ', 'SITE');
INSERT INTO users (user_id, email, username, password, first_name, last_name, enabled, account_expired, account_locked, credentials_expired, has_avatar, user_key, provider_id)
VALUES
  (2, 'user@aol.com', 'user', '$2a$10$F2a2W8RtbD99xXd9xtwjbuI4zjSYe04kS.s0FyvQcAIDJfh/6jjLW', 'User', 'Charlie', TRUE,
      FALSE, FALSE, FALSE, FALSE, 'v7jXapyD6fbRyZvi', 'SITE');
INSERT INTO users (user_id, email, username, password, first_name, last_name, enabled, account_expired, account_locked, credentials_expired, has_avatar, user_key, provider_id)
VALUES (3, 'keith@aol.com', 'keith', '$2a$10$F2a2W8RtbD99xXd9xtwjbuI4zjSYe04kS.s0FyvQcAIDJfh/6jjLW', 'Keith', 'Obannon',
           TRUE, FALSE, FALSE, FALSE, FALSE, 'HuoPByrU0hC87gz8', 'SITE');
INSERT INTO users (user_id, email, username, password, first_name, last_name, enabled, account_expired, account_locked, credentials_expired, has_avatar, user_key, provider_id)
VALUES
  (4, 'erwin@aol.com', 'erwin', '$2a$10$F2a2W8RtbD99xXd9xtwjbuI4zjSYe04kS.s0FyvQcAIDJfh/6jjLW', 'Erwin', 'Lapote', TRUE,
      FALSE, FALSE, FALSE, FALSE, 'kSpaxKbYIL0a5Mma', 'SITE');
INSERT INTO users (user_id, email, username, password, first_name, last_name, enabled, account_expired, account_locked, credentials_expired, has_avatar, user_key, provider_id)
VALUES
  (5, 'jeremy@aol.com', 'jeremy', '$2a$10$F2a2W8RtbD99xXd9xtwjbuI4zjSYe04kS.s0FyvQcAIDJfh/6jjLW', 'Jeremy', 'Sloan',
      TRUE, FALSE, FALSE, FALSE, FALSE, 'xt4e25EoFgjx5CP2', 'SITE');
INSERT INTO users (user_id, email, username, password, first_name, last_name, enabled, account_expired, account_locked, credentials_expired, has_avatar, user_key, provider_id)
VALUES
  (6, 'scott@aol.com', 'scott', '$2a$10$F2a2W8RtbD99xXd9xtwjbuI4zjSYe04kS.s0FyvQcAIDJfh/6jjLW', 'Scott', 'Shoenberger',
      TRUE, FALSE, FALSE, FALSE, FALSE, 'Fx05XbWjPFECJZQP', 'SITE');
INSERT INTO users (user_id, email, username, password, first_name, last_name, enabled, account_expired, account_locked, credentials_expired, has_avatar, user_key, provider_id)
VALUES
  (7, 'tommy@aol.com', 'tommy', '$2a$10$F2a2W8RtbD99xXd9xtwjbuI4zjSYe04kS.s0FyvQcAIDJfh/6jjLW', 'Tommy', 'Twotone',
      FALSE, FALSE, FALSE, FALSE, FALSE, 'VYlGwj3HOi665LIa', 'SITE');

-- INSERT INTO user_data (user_id, lastlogin_datetime, created_datetime) VALUES
--   (1, current_timestamp(), current_timestamp());
-- INSERT INTO user_data (user_id, lastlogin_datetime, created_datetime, login_attempts, invited_by_id) VALUES
--   (7, current_timestamp(), current_timestamp(), 2, 0);
--

INSERT INTO user_data (user_id, lastlogin_datetime, created_datetime, login_attempts, invited_by_id) SELECT
                                                                        user_id,
                                                                       current_timestamp,
                                                                        current_timestamp, 0, 0
                                                                      FROM users;

UPDATE user_data
SET login_attempts = 2
WHERE user_id = 7;

/* ------------------------------------------------------------ */
-- Authorities
/* ------------------------------------------------------------ */

INSERT INTO authorities (authority_id, authority, is_locked) VALUES (1, 'ROLE_ADMIN', TRUE);
INSERT INTO authorities (authority_id, authority, is_locked) VALUES (2, 'ROLE_USER', TRUE);
INSERT INTO authorities (authority_id, authority, is_locked) VALUES (3, 'ROLE_POSTS', TRUE);

/* ------------------------------------------------------------ */
-- User_Authorities
/* ------------------------------------------------------------ */

INSERT INTO user_authorities (user_id, authority_id) VALUES (1, 2);
INSERT INTO user_authorities (user_id, authority_id) VALUES (1, 1);
INSERT INTO user_authorities (user_id, authority_id) VALUES (1, 3);
INSERT INTO user_authorities (user_id, authority_id) VALUES (2, 2);
INSERT INTO user_authorities (user_id, authority_id) VALUES (3, 2);
INSERT INTO user_authorities (user_id, authority_id) VALUES (3, 3);
INSERT INTO user_authorities (user_id, authority_id) VALUES (4, 2);
INSERT INTO user_authorities (user_id, authority_id) VALUES (5, 2);
INSERT INTO user_authorities (user_id, authority_id) VALUES (6, 2);
INSERT INTO user_authorities (user_id, authority_id) VALUES (7, 2);


INSERT INTO contacts (contact_id, first_name, last_name, birth_date, email, created_by_user, creation_time, modified_by_user, modification_time, version)
VALUES (1, 'Summer', 'Glass', '1968-08-05', 'vitae@egestasadui.net', 'admin', '2015-09-10 19:18:38.335', 'admin',
        '2015-09-10 19:18:38.335', 0),
  (2, 'Mikayla', 'Church', '1975-04-03', 'lobortis.Class@aliquam.org', 'admin', '2015-09-10 19:18:38.335', 'admin',
   '2015-09-10 19:18:38.335', 0),
  (3, 'Shaine', 'Brooks', '1971-08-24', 'vel.pede@metusVivamuseuismod.edu', 'admin', '2015-09-10 19:18:38.335', 'admin',
   '2015-09-10 19:18:38.335', 0),
  (4, 'Robin', 'Sullivan', '1961-09-09', 'purus.gravida@necleo.edu', 'admin', '2015-09-10 19:18:38.335', 'admin',
   '2015-09-10 19:18:38.335', 0),
  (5, 'Xantha', 'Kim', '1960-08-25', 'risus.Duis.a@velnisl.ca', 'admin', '2015-09-10 19:18:38.335', 'admin',
   '2015-09-10 19:18:38.335', 0),
  (6, 'Barry', 'Kirk', '1982-03-27', 'blandit.at@Maurisblanditenim.com', 'admin', '2015-09-10 19:18:38.335', 'admin',
   '2015-09-10 19:18:38.335', 0),
  (7, 'Tad', 'Robellaboy', '1972-08-08', 'In.lorem.Donec@Vivamusnisi.org', 'admin', '2015-09-10 19:18:38.335', 'admin',
   '2015-09-10 19:18:38.335', 0),
  (8, 'Finn', 'Robertorobo', '1974-05-27', 'aliquet@ornare.net', 'admin', '2015-09-10 19:18:38.335', 'admin',
   '2015-09-10 19:18:38.335', 0),
  (9, 'Ali', 'Calhoun', '1976-11-30', 'fermentum@nulla.co.uk', 'admin', '2015-09-10 19:18:38.335', 'admin',
   '2015-09-10 19:18:38.335', 0),
  (10, 'Alexandra', 'Hendricks', '1973-07-05', 'at.auctor@pellentesquemassalobortis.edu', 'admin',
   '2015-09-10 19:18:38.335', 'admin', '2015-09-10 19:18:38.335', 0);


INSERT INTO contact_phones (contact_phone_id, contact_id, phone_type, phone_number)
VALUES (1, '1', 'Mobile', '1-113-753-8020'), (2, '1', 'Home', '1-996-507-0853'), (3, '2', 'Mobile', '1-407-100-1341'),
  (4, '2', 'Home', '1-285-981-2510'), (5, '3', 'Mobile', '1-274-311-9291'), (6, '3', 'Home', '1-499-112-9185'),
  (7, '4', 'Mobile', '1-234-628-6511'), (8, '4', 'Home', '1-560-178-3273'), (9, '5', 'Mobile', '1-430-941-9233'),
  (10, '5', 'Home', '1-271-831-8886');
INSERT INTO contact_phones (contact_phone_id, contact_id, phone_type, phone_number)
VALUES (11, '6', 'Mobile', '1-255-105-0103'), (12, '6', 'Home', '1-481-652-4155'),
  (13, '7', 'Mobile', '1-917-917-8478'),
  (14, '7', 'Home', '1-766-831-2271'), (15, '8', 'Mobile', '1-863-515-3218'), (16, '8', 'Home', '1-930-909-9849'),
  (17, '9', 'Mobile', '1-423-399-6903'), (18, '9', 'Home', '1-294-840-1996'), (19, '10', 'Mobile', '1-661-300-3848'),
  (20, '10', 'Home', '1-972-479-8970');

INSERT INTO hobbies (hobby_id, hobby_title) VALUES (1, 'Jogging');
INSERT INTO hobbies (hobby_id, hobby_title) VALUES (2, 'Movies');
INSERT INTO hobbies (hobby_id, hobby_title) VALUES (3, 'Programming');
INSERT INTO hobbies (hobby_id, hobby_title) VALUES (4, 'Reading');
INSERT INTO hobbies (hobby_id, hobby_title) VALUES (5, 'Swimming');

INSERT INTO contact_hobby_ids (contact_hobby_id, contact_id, hobby_id)
VALUES (1, 1, 1), (2, 1, 2), (3, 2, 3), (4, 2, 4), (5, 3, 5), (6, 3, 1), (7, 4, 2), (8, 4, 3), (9, 5, 4),
  (10, 5, 5);
INSERT INTO contact_hobby_ids (contact_hobby_id, contact_id, hobby_id)
VALUES (11, 6, 1), (12, 6, 2), (13, 7, 3), (14, 7, 4), (15, 8, 5), (16, 8, 1), (17, 9, 2), (18, 9, 3), (19, 10, 4),
  (20, 10, 5);

INSERT INTO site_options (option_id, option_name, option_value) VALUES ('1', 'siteName', 'My Site');
INSERT INTO site_options (option_id, option_name, option_value) VALUES ('2', 'siteDescription', 'My Site Description');
INSERT INTO site_options (option_id, option_name, option_value) VALUES ('3', 'addGoogleAnalytics', 'false');
INSERT INTO site_options (option_id, option_name, option_value)
VALUES ('4', 'googleAnalyticsTrackingId', 'UA-XXXXXX-7');
INSERT INTO site_options (option_id, option_name, option_value) VALUES ('5', 'integerProperty', '1');
INSERT INTO site_options (option_id, option_name, option_value) VALUES ('6', 'userRegistration', 'ADMINISTRATIVE_APPROVAL');

/* ------------------------------------------------------------ */
-- Posts
/* ------------------------------------------------------------ */

-- INSERT INTO posts (post_id, user_id, post_title, post_name, post_link, post_date, post_modified, post_type, display_type, is_published, post_content, post_source, post_image, click_count, likes_count, value_rating, version) VALUES (1, 1, 'Post One Title', 'post-one-title', 'http://nixmash.com/something', '2016-05-31 13:27:47', '2016-05-31 13:28:01', 'LINK', 'LINK', 1, 'Post One Content', 'nixmash.com', null, 0, 0, 0, 0);
-- INSERT INTO posts (post_id, user_id, post_title, post_name, post_link, post_date, post_modified, post_type, display_type, is_published, post_content, post_source, post_image, click_count, likes_count, value_rating, version) VALUES (2, 1, 'Post Two Title', 'post-two-title', 'http://stackoverflow.com/something', '2016-05-31 14:30:45', '2016-05-31 14:30:47', 'LINK', 'LINK', 1, 'Post Two Content', 'stackoverflow.com', null, 0, 0, 0, 0);

-- INSERT INTO posts (post_id, user_id, post_title, post_name, post_link, post_date, post_modified, post_type, display_type, is_published, post_content, post_source, post_image, click_count, likes_count, value_rating, version)
-- VALUES (-1, 3, 'Not Yet Selected', 'not-yet-selected', NULL, '2016-03-28 17:40:18', '2016-03-28 17:40:18', 'POST',
--             'SINGLEPHOTO_POST', 0, 'This is a placemarker post', 'NA', NULL, 0, 0, 0, 0);
INSERT INTO posts (post_id, user_id, post_title, post_name, post_link, post_date, post_modified, post_type, display_type, is_published, post_content, post_source, post_image, click_count, likes_count, value_rating, version)
VALUES (1, 3, 'JavaScript · Bootstrap', 'javascript-bootstrap', 'http://getbootstrap.com/javascript/#carousel',
           '2016-06-06 15:30:34', '2016-06-06 15:30:34', 'LINK', 'LINK', 1,
           'Bootstrap, a sleek, intuitive, and powerful mobile first front-end framework...', 'getbootstrap.com', NULL,
        0, 2, 0, 0);
INSERT INTO posts (post_id, user_id, post_title, post_name, post_link, post_date, post_modified, post_type, display_type, is_published, post_content, post_source, post_image, click_count, likes_count, value_rating, version)
VALUES (2, 3, 'A Java collection of value pairs? (tuples?)', 'a-java-collection-of-value-pairs-tuples',
           'http://stackoverflow.com/questions/521171/a-java-collection-of-value-pairs-tuples', '2016-06-05 15:31:01',
           '2016-06-05 15:31:01', 'LINK', 'LINK_SUMMARY', 1, 'I like how Java has a Map...', 'stackoverflow.com',
        '/images/posts/stackoverflow.png', 0, 2, 0, 0);
INSERT INTO posts (post_id, user_id, post_title, post_name, post_link, post_date, post_modified, post_type, display_type, is_published, post_content, post_source, post_image, click_count, likes_count, value_rating, version)
VALUES (3, 3, 'Jsoup Parsing and Traversing Document and URL - JAVATIPS.INFO',
           'jsoup-parsing-and-traversing-document-and-url-javatips-info',
           'http://javatips.info/jsoup-parsing-and-traversing-document-and-url.html', '2016-06-04 15:31:38',
           '2016-06-04 15:31:38', 'LINK', 'LINK', 1, 'Prerequisites Development environment...', 'javatips.info', NULL,
        0, 1, 0, 0);
INSERT INTO posts (post_id, user_id, post_title, post_name, post_link, post_date, post_modified, post_type, display_type, is_published, post_content, post_source, post_image, click_count, likes_count, value_rating, version)
VALUES (4, 3, 'Content Negotiation using Spring MVC', 'content-negotiation-using-spring-mvc',
           'https://spring.io/blog/2013/05/`/content-negotiation-using-spring-mvc', '2016-06-03 15:32:28',
           '2016-06-03 15:32:28', 'LINK', 'LINK_SUMMARY', 1,
           '<p>There are two ways to generate output using Spring MVC...</p>', 'spring.io', '/images/posts/spring.png',
        0, 0, 0, 0);
INSERT INTO posts (post_id, user_id, post_title, post_name, post_link, post_date, post_modified, post_type, display_type, is_published, post_content, post_source, post_image, click_count, likes_count, value_rating, version)
VALUES (5, 3, 'Variations on JSON Key-Value Pairs in Spring MVC', 'variations-on-json-key-value-pairs-in-spring-mvc',
           'http://nixmash.com/java/variations-on-json-key-value-pairs-in-spring-mvc/', '2016-06-02 15:34:09',
           '2016-06-02 15:34:09', 'LINK', 'LINK_SUMMARY', 1,
           'The topic of this post is pretty lightweight. A bit of a lark, really...', 'nixmash.com',
        'http://nixmash.com/x/blog/2016/jsonpair0528c.png', 0, 0, 0, 0);
INSERT INTO posts (post_id, user_id, post_title, post_name, post_link, post_date, post_modified, post_type, display_type, is_published, post_content, post_source, post_image, click_count, likes_count, value_rating, version)
VALUES
  (6, 3, 'Freestanding Note Post', 'freestanding-note-post', NULL, '2016-06-01 17:40:18', '2016-06-01 17:40:18', 'POST',
      'POST', 1, 'The freestanding Note is a marvelous thing', 'NA', NULL, 0, 0, 0, 0);
INSERT INTO posts (post_id, user_id, post_title, post_name, post_link, post_date, post_modified, post_type, display_type, is_published, post_content, post_source, post_image, click_count, likes_count, value_rating, version)
VALUES (7, 3, '100 Ways To Title Something', '100-ways-to-title-something', NULL, '2016-05-30 17:40:18',
           '2016-05-30 17:40:18', 'POST', 'POST', 1, 'This post title begins with 100', 'NA', NULL, 0, 0, 0, 0);
INSERT INTO posts (post_id, user_id, post_title, post_name, post_link, post_date, post_modified, post_type, display_type, is_published, post_content, post_source, post_image, click_count, likes_count, value_rating, version)
VALUES (8, 3, '200 Ways To Title Something', '200-ways-to-title-something', NULL, '2016-05-29 17:40:18',
           '2016-05-29 17:40:18', 'POST', 'POST', 1, 'This post title begins with 200', 'NA', NULL, 0, 0, 0, 0);
INSERT INTO posts (post_id, user_id, post_title, post_name, post_link, post_date, post_modified, post_type, display_type, is_published, post_content, post_source, post_image, click_count, likes_count, value_rating, version)
VALUES (9, 3, '1000 Ways To Title Something', '1000-ways-to-title-something', NULL, '2016-05-28 17:40:18',
           '2016-05-28 17:40:18', 'POST', 'POST', 1, 'This post title begins with 1000', 'NA', NULL, 0, 0, 0, 0);
INSERT INTO posts (post_id, user_id, post_title, post_name, post_link, post_date, post_modified, post_type, display_type, is_published, post_content, post_source, post_image, click_count, likes_count, value_rating, version)
VALUES (10, 3, 'Solr Rama', 'solr-rama', NULL, '2016-04-20 17:40:18',
            '2016-04-20 17:40:18', 'POST', 'POST', 1,
            '<p><strong>This is a post</strong> for <em>Solr Testing</em></p>', 'NA', NULL, 0, 0, 0, 0);
INSERT INTO posts (post_id, user_id, post_title, post_name, post_link, post_date, post_modified, post_type, display_type, is_published, post_content, post_source, post_image, click_count, likes_count, value_rating, version)
VALUES (11, 3, 'SinglePhoto Post', 'singlephoto-post', NULL, '2016-04-19 17:40:18',
            '2016-04-19 17:40:18', 'POST', 'SINGLEPHOTO_POST', 1,
            '<p><strong>This is a singlephoto post</strong> for <em>RSS Testing</em></p>', 'NA', NULL, 0, 0, 0, 0);
INSERT INTO posts (post_id, user_id, post_title, post_name, post_link, post_date, post_modified, post_type, display_type, is_published, post_content, post_source, post_image, click_count, likes_count, value_rating, version)
VALUES (12, 3, 'MultiPhoto Post', 'multiphoto-post', NULL, '2016-04-18 17:40:18',
            '2016-04-18 17:40:18', 'POST', 'MULTIPHOTO_POST', 1,
            '<p><strong>This is a multiphoto post</strong> for <em>RSS Testing</em></p>', 'NA', NULL, 0, 0, 0, 0);


INSERT INTO tags (tag_id, tag_value) VALUES (1, 'h2tagone');
INSERT INTO tags (tag_id, tag_value) VALUES (2, 'h2tagtwo');
INSERT INTO tags (tag_id, tag_value) VALUES (3, 'h2tagthree');
INSERT INTO tags (tag_id, tag_value) VALUES (4, 'h2tagfour');
INSERT INTO tags (tag_id, tag_value) VALUES (5, 'h2 Tag With Spaces');
INSERT INTO tags (tag_id, tag_value) VALUES (6, 'h2tagsix');

INSERT INTO categories (category_id, category_value, is_active, is_default) VALUES (1, 'Uncategorized', 1, 0);
INSERT INTO categories (category_id, category_value, is_active, is_default) VALUES (2, 'Java', 1, 1);
INSERT INTO categories (category_id, category_value, is_active, is_default) VALUES (3, 'Solr', 1, 0);
INSERT INTO categories (category_id, category_value, is_active, is_default) VALUES (4, 'PHP', 0, 0);
INSERT INTO categories (category_id, category_value, is_active, is_default) VALUES (5, 'Wannabe', 1, 0);
INSERT INTO categories (category_id, category_value, is_active, is_default) VALUES (6, 'Shorttimer', 1, 0);

INSERT INTO post_category_ids (post_category_id, post_id, category_id) VALUES (1, 1, 1);
INSERT INTO post_category_ids (post_category_id, post_id, category_id) VALUES (2, 2, 1);
INSERT INTO post_category_ids (post_category_id, post_id, category_id) VALUES (3, 3, 2);
INSERT INTO post_category_ids (post_category_id, post_id, category_id) VALUES (4, 4, 2);
INSERT INTO post_category_ids (post_category_id, post_id, category_id) VALUES (5, 5, 2);
INSERT INTO post_category_ids (post_category_id, post_id, category_id) VALUES (6, 6, 2);
INSERT INTO post_category_ids (post_category_id, post_id, category_id) VALUES (7, 7, 2);
INSERT INTO post_category_ids (post_category_id, post_id, category_id) VALUES (8, 8, 6);
INSERT INTO post_category_ids (post_category_id, post_id, category_id) VALUES (9, 9, 3);
INSERT INTO post_category_ids (post_category_id, post_id, category_id) VALUES (10, 10, 2);
INSERT INTO post_category_ids (post_category_id, post_id, category_id) VALUES (11, 11, 2);
INSERT INTO post_category_ids (post_category_id, post_id, category_id) VALUES (12, 12, 2);

INSERT INTO post_tag_ids (post_tag_id, post_id, tag_id) VALUES (1, 1, 1);
INSERT INTO post_tag_ids (post_tag_id, post_id, tag_id) VALUES (2, 1, 2);
INSERT INTO post_tag_ids (post_tag_id, post_id, tag_id) VALUES (3, 1, 3);
INSERT INTO post_tag_ids (post_tag_id, post_id, tag_id) VALUES (4, 2, 4);
INSERT INTO post_tag_ids (post_tag_id, post_id, tag_id) VALUES (5, 2, 1);
INSERT INTO post_tag_ids (post_tag_id, post_id, tag_id) VALUES (6, 3, 1);
INSERT INTO post_tag_ids (post_tag_id, post_id, tag_id) VALUES (7, 4, 1);
INSERT INTO post_tag_ids (post_tag_id, post_id, tag_id) VALUES (8, 5, 1);
INSERT INTO post_tag_ids (post_tag_id, post_id, tag_id) VALUES (9, 5, 5);
INSERT INTO post_tag_ids (post_tag_id, post_id, tag_id) VALUES (10, 6, 2);
INSERT INTO post_tag_ids (post_tag_id, post_id, tag_id) VALUES (11, 10, 1);
INSERT INTO post_tag_ids (post_tag_id, post_id, tag_id) VALUES (12, 10, 2);
INSERT INTO post_tag_ids (post_tag_id, post_id, tag_id) VALUES (13, 10, 3);
INSERT INTO post_tag_ids (post_tag_id, post_id, tag_id) VALUES (14, 11, 3);
INSERT INTO post_tag_ids (post_tag_id, post_id, tag_id) VALUES (15, 12, 3);

INSERT INTO user_likes (like_id, user_id, item_id, content_type_id) VALUES (1, 3, 1, 1);
INSERT INTO user_likes (like_id, user_id, item_id, content_type_id) VALUES (2, 3, 2, 1);
INSERT INTO user_likes (like_id, user_id, item_id, content_type_id) VALUES (3, 3, 3, 1);
INSERT INTO user_likes (like_id, user_id, item_id, content_type_id) VALUES (4, 3, 10, 1);
INSERT INTO user_likes (like_id, user_id, item_id, content_type_id) VALUES (5, 2, 1, 1);
INSERT INTO user_likes (like_id, user_id, item_id, content_type_id) VALUES (6, 2, 2, 1);

INSERT INTO post_images (image_id, post_id, image_name, thumbnail_filename, filename, content_type, size, thumbnail_size, datetime_created)
VALUES
  (1, 1, 'WP_000993.jpg', 'f6dea6b8-87bf-42eb-a4b6-4e4c751f0d70-thumbnail.png',
   'f6dea6b8-87bf-42eb-a4b6-4e4c751f0d70.jpg',
   'image/jpeg', 1065071, 53726, '2016-08-03 13:49:09');
INSERT INTO post_images (image_id, post_id, image_name, thumbnail_filename, filename, content_type, size, thumbnail_size, datetime_created)
VALUES
  (2, 1, 'WP_000624.jpg', '49e5d232-56a9-4bf9-a9df-916508a4f540-thumbnail.png',
   '49e5d232-56a9-4bf9-a9df-916508a4f540.jpg',
   'image/jpeg', 580112, 40909, '2016-08-03 13:49:09');
INSERT INTO post_images (image_id, post_id, image_name, thumbnail_filename, filename, content_type, size, thumbnail_size, datetime_created)
VALUES
  (3, 2, 'WP_000931.jpg', '000e045d-781b-4979-9a00-37692f8d33cf-thumbnail.png',
   '000e045d-781b-4979-9a00-37692f8d33cf.jpg',
   'image/jpeg', 908532, 51092, '2016-08-03 14:58:45');
INSERT INTO post_images (image_id, post_id, image_name, thumbnail_filename, filename, content_type, size, thumbnail_size, datetime_created) VALUES (4, 12, 'cats0306b.png', '48e0c504-859a-461d-a400-b7f137149e0c-thumbnail.png', '48e0c504-859a-461d-a400-b7f137149e0c.png', 'image/png', 83704, 12534, '2017-03-08 14:15:51');
INSERT INTO post_images (image_id, post_id, image_name, thumbnail_filename, filename, content_type, size, thumbnail_size, datetime_created) VALUES (5, 12, 'cats0306a.png', 'a51d98b9-db84-43ed-b41d-1e3a2a3c561f-thumbnail.png', 'a51d98b9-db84-43ed-b41d-1e3a2a3c561f.png', 'image/png', 70978, 9372, '2017-03-08 14:15:51');
INSERT INTO post_images (image_id, post_id, image_name, thumbnail_filename, filename, content_type, size, thumbnail_size, datetime_created) VALUES (6, 11, 'shrimp.jpg', '42ac213f-e813-43d6-884e-c083f62b4f59-thumbnail.png', '42ac213f-e813-43d6-884e-c083f62b4f59.jpg', 'image/jpeg', 252122, 38418, '2017-03-08 15:06:07');

INSERT INTO flashcard_categories (category_id, category) VALUES (1, 'category one');
INSERT INTO flashcard_categories (category_id, category) VALUES (2, 'category two');

INSERT INTO flashcard_slides (slide_id, category_id, slide_image, slide_content, datetime_created, post_id)
VALUES (1, 1, 'slideone.jpg', 'slide one', '2016-08-11 15:01:46', 1);
INSERT INTO flashcard_slides (slide_id, category_id, slide_image, slide_content, datetime_created, post_id)
VALUES (2, 1, 'slidetwo.jpg', 'slide two', '2016-08-11 15:02:11', 2);
INSERT INTO flashcard_slides (slide_id, category_id, slide_image, slide_content, datetime_created, post_id)
VALUES (3, 1, 'slidethree.jpg', 'slide three', '2016-08-11 15:02:30', 3);

INSERT INTO github_stats (forks, stars, subscribers, followers, stat_date) VALUES (20, 100, 25, 18, '2016-12-01');
INSERT INTO github_stats (forks, stars, subscribers, followers, stat_date) VALUES (21, 102, 27, 18, '2016-12-02');
INSERT INTO github_stats (forks, stars, subscribers, followers, stat_date) VALUES (22, 105, 31, 19, '2016-12-03');

insert into post_meta select post_id, 'SUMMARY', '@awesomeblogger', '/x/pics/twitter120x120.jpg', 'na'
                      from posts;

update post_meta set twitter_card = 'SUMMARY_LARGE_IMAGE' where post_id = 10;

update post_meta set twitter_card = 'NONE' where post_id = 9;

INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (-1, 'test.jpg', 'Test Image', 'FlickrDude', 'https://flic.kr/p/RW27MT', 1, 1, 0, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (1, 'churchstreet.jpg', 'Church Street', 'BostonTx', 'https://flic.kr/p/RW27MT', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (2, 'currentimage.jpg', 'A Vermont Country Home', 'Nicholas Erwin', 'https://flic.kr/p/9JyTPG', 1, 1, 1,1, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (3, 'saab.jpg', 'A Junkyard Saab', 'Nicholas Erwin', 'https://flic.kr/p/q14oYv', 1, 1, 0, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (4, 'magichat.jpg', 'A Magic Hat Truck', 'Paige Bollman', 'https://flic.kr/p/Shh8Du', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (5, 'reservoir.jpg', 'Waterbury Reservoir', 'Nicholas Erwin', 'https://flic.kr/p/atrauN', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (6, 'farmhouse.jpg', 'A Country Farm House', 'Stanley Zimny', 'https://flic.kr/p/pAMnYj', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (7, 'malletsbay.jpg', 'Mallet''s Bay', 'Nicholas Erwin', 'https://flic.kr/p/pxKoo9', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (8, 'walkway.jpg', 'A Fall Walkway', 'Eric Konon', 'https://flic.kr/p/pvqqLr', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (9, 'stratton.jpg', 'A Stratton Hiking Scene', 'Nate Merrill', 'https://flic.kr/p/oGBHth', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (10, 'jennysfarm.jpg', 'Jenny''s Farm', 'Lonnie Janzen', 'https://flic.kr/p/oZSkd7', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (11, 'manchester.jpg', 'Manchester Fall Foliage', 'Lonnie Janzen', 'https://flic.kr/p/oKpXQ4', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (12, 'woodstockinn.jpg', 'The Woodstock Inn', 'Craig T', 'https://flic.kr/p/pk4R5Q', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (13, 'winterpath.jpg', 'A Burlington Winter Path', 'Scott McCracken', 'https://flic.kr/p/m9t3NM', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (14, 'doll.jpg', 'A Shelburne Museum Doll', 'Peter Eimon', 'https://flic.kr/p/nLo1yG', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (15, 'lakebtv.jpg', 'Lake Champlaign from Burlington', 'Shannon McGee', 'https://flic.kr/p/brkJBG', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (16, 'uvmstreet.jpg', 'University of Vermont', 'Ben W', 'https://flic.kr/p/nBwDwi', 1, 1, 0, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (17, 'putneylogs.jpg', 'Putney Logs', 'Putneypics', 'https://flic.kr/p/iD5ao7', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (18, 'warrenslopes.jpg', 'Warren Ski Slopes', 'Pinneyshaun', 'https://flic.kr/p/iNP3zt', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (19, 'stowechurch.jpg', 'Stowe, Vermont', 'Leslee_atFlickr', 'https://flic.kr/p/RNP5Sf', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (20, 'uvmstreettower.jpg', 'University of Vermont', 'Matthew D. Britt', 'https://flic.kr/p/p19wXr', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (21, 'benandjerrys.jpg', 'Ben and Jerrys', 'Matt Lancashire', 'https://flic.kr/p/p4MzgC', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (22, 'snowyhilltop.jpg', 'A Snowy Hilltop', 'Scott McCracken', 'https://flic.kr/p/iMF1Yf', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (23, 'swampygreen.jpg', 'A Green Mountain Scene', 'Adrian Scottow', 'https://flic.kr/p/SWaWzF', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (24, 'crabapple.jpg', 'Stop Being Such a Crabapple', 'Nicholas Erwin', 'https://flic.kr/p/UX331p', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (25, 'causeway.jpg', 'Colchester Causeway', 'kai.bates', 'https://flic.kr/p/Vma1oX', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (26, 'ferry.jpg', 'Charlotte Ferry', 'Mike', 'https://flic.kr/p/WxvVzz', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (27, 'burlingtonfromwater.jpg', 'Burlington from Lake Champlain', 'Mike', 'https://flic.kr/p/Vgeys9', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (28, 'btvresistance.jpg', '#BTVResistance', 'kai.bates', 'https://flic.kr/p/Wo1TER', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (29, 'benjerrycups.jpg', 'Ben and Jerry Cups', 'Erin', 'https://flic.kr/p/W2sW65', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (30, 'magichatcups.jpg', 'Magic Hat Cups', 'Paige Bollman', 'https://flic.kr/p/Shh82s', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (31, 'swanton.jpg', 'Swanton
', 'B.2010', 'https://flic.kr/p/Xoi95S', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (32, 'reststop.jpg', 'Rest Stop', 'Nicholas Erwin', 'https://flic.kr/p/YaFtSi', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (33, 'rochester.jpg', 'Rochester, VT', 'Philip N Young', 'https://flic.kr/p/XZqoiU', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (34, 'lakeecho.jpg', 'Lake Echo', 'Philip N Young', 'https://flic.kr/p/XRiM3t', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (35, 'rte100n.jpg', 'View from Rte 100', 'Philip N Young', 'https://flic.kr/p/WMRgQ1', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (36, 'peaceful.jpg', 'Peaceful', 'Nicholas Erwin', 'https://flic.kr/p/ZF9cLF', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (37, 'lakechamplaindock', 'Lake Champlain', 'John Truong', 'https://flic.kr/p/YYcSTb', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (38, 'danville', 'Danville', 'damian entwistle', 'https://flic.kr/p/ZFjdJN', 1, 1, 1, 0, 1);
INSERT INTO site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (39, 'brattlemarket', 'Brattleboro Farmers Market', 'Putneypics', 'https://flic.kr/p/Zg6U4o', 1, 1, 1, 0, 1);