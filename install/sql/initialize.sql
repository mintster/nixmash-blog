ALTER TABLE categories DROP wp_category_id;
ALTER TABLE posts DROP wp_post_id;
ALTER TABLE tags DROP wp_tag_id;

insert into post_meta select post_id, 'SUMMARY', '@awesomeblogger', '/x/pics/twitter120x120.jpg', 'na' from posts;

INSERT IGNORE INTO categories (category_id, category_value, is_active, is_default) SELECT 1, 'Uncategorized',1,1;

--  Remove duplicate categories as NixMash Blog supports a single category per post
-- Otherwise duplicate posts would be retrieved for display

DELETE a
FROM post_category_ids as a, post_category_ids as b
WHERE
          (a.post_id = b.post_id)
      AND (a.category_id <> b.category_id)
      AND a.post_category_id < b.post_category_id;

-- Nixmash.com specific update

update posts set post_content = REPLACE(post_content,'http://nixmash.com/links/?','#?');