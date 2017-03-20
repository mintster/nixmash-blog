ALTER TABLE categories DROP wp_category_id;
ALTER TABLE posts DROP wp_post_id;
ALTER TABLE tags DROP wp_tag_id;

insert into post_meta select post_id, 'SUMMARY', '@awesomeblogger', '/x/pics/twitter120x120.jpg', 'na' from posts;

INSERT IGNORE INTO categories (category_id, category_value, is_active, is_default) SELECT 1, 'Uncategorized',1,1;
