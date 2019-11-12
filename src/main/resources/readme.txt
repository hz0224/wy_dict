showdoc中的 page表中的字段 page_title 和 addtime 长度太小
插入数据过长时可能会报错，因此需要修改一下这两个字段的长度

ALTER TABLE page MODIFY COLUMN page_title VARCHAR(100);
ALTER TABLE page MODIFY COLUMN addtime bigint(20);