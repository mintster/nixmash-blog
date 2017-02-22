select count(*) as 'count',
    c.category_value as 'category' from post_category_ids pc inner JOIN posts p
    on pc.post_id = p.post_id
    INNER JOIN categories c on c.category_id = pc.category_id
GROUP BY pc.category_id ORDER BY count DESC;