
insert into mall_msg_sub_type (uuid,code,`desc`,basicUuid,createTime) value ('18','16','热门推荐','3','2020-10-14 10:12:45');

DROP TABLE IF EXISTS `mall_promote`;
CREATE TABLE `mall_promote`  (
  `uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL UNIQUE COMMENT '编码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `createDate` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

alter table mall_msg_proclamation add column promotes varchar(255);