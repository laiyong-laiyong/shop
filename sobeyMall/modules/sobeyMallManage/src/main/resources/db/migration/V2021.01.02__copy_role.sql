/*
 Navicat Premium Data Transfer

 Source Server         : 这里是将要复制的角色
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : 121.36.103.17:3306
 Source Schema         : sobey-mall-manage

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 03/02/2021 17:15:04
*/
DROP TABLE IF EXISTS `mall_copy_role`;
CREATE TABLE `mall_copy_role`  (
  `uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色code(必须指定正确)',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of mall_copy_role
-- ----------------------------
INSERT INTO `mall_copy_role` VALUES ('1', 'lingyun_role_y09afbvxh3', '用户管理员权限(自动创建)', 'admin');
