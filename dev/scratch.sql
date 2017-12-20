select count(*) as 'count',
    c.category_value as 'category' from post_category_ids pc inner JOIN posts p
    on pc.post_id = p.post_id
    INNER JOIN categories c on c.category_id = pc.category_id
GROUP BY pc.category_id ORDER BY count DESC;


update post_meta set twitter_image='/x/pics/twitter120x120.png', twitter_description='na' where twitter_card = 'SUMMARY';

TRUNCATE TABLE tags;

INSERT INTO site_images
  (image_filename, image_description, image_author, source_url, common_license, banner_image, is_active)
  VALUES
    ('yellowleaves', 'Maple Sunset', 'Nicholas Erwin', 'https://flic.kr/p/214P6Rz', 1, 1,1);

INSERT INTO nixmashdb.site_images (site_image_id, image_filename, image_description, image_author, source_url, common_license, banner_image, is_active, is_current, day_of_year) VALUES (39, 'brattlemarket', 'Brattleboro Farmers Market', 'Putneypics', 'https://flic.kr/p/Zg6U4o', 1, 1, 1, 0, 341);
