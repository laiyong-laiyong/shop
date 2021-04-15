UPDATE  mall_product_category t set t.parentId = '0';
alter table mall_product_media add column `order` int(11) COMMENT '顺序'; 
alter table mall_product add column shelf varchar(2) COMMENT '是否上架凌云商城'; 