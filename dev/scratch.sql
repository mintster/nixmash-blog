select count(*) as 'count',
    c.category_value as 'category' from post_category_ids pc inner JOIN posts p
    on pc.post_id = p.post_id
    INNER JOIN categories c on c.category_id = pc.category_id
GROUP BY pc.category_id ORDER BY count DESC;


update post_meta set twitter_image='/x/pics/twitter120x120.png', twitter_description='na' where twitter_card = 'SUMMARY';

TRUNCATE TABLE tags;

INSERT INTO site_images
  (image_filename, image_description, image_author, source_url, common_license, banner_image)
  VALUES
    ('churchstreet', 'Church Street', 'BostonTx', 'https://flic.kr/p/RW27MT', 1, 1);
