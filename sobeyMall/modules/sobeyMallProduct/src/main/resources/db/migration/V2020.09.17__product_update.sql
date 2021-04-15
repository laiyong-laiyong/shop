

DROP TABLE IF EXISTS `mall_product_relation`;
CREATE TABLE `mall_product_relation` (
  `uuid` varchar(64) NOT NULL,
  `productId` varchar(255) DEFAULT NULL COMMENT '商品编号',
  `relationProductId` varchar(64) DEFAULT NULL COMMENT '关联商品编号',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;