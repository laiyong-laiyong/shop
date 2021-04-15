/*
Navicat MySQL Data Transfer

Source Server         : 凌云商城测试环境
Source Server Version : 50725
Source Host           : 121.36.103.17:3306
Source Database       : sobey-mall-panda

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2020-08-25 16:57:15
*/

DROP TABLE IF EXISTS `mall_balance_recharge_contract`;
CREATE TABLE `mall_balance_recharge_contract`  (
  `id` bigint(255) NOT NULL AUTO_INCREMENT,
  `projectId` bigint(255) NOT NULL COMMENT '关联mall_balance_recharge的id字段',
  `service` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '服务',
  `serviceSize` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '预计使用量',
  `contract` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '折扣方式',
  `contractDate` datetime(0) NULL DEFAULT NULL COMMENT '折扣期限',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `mall_balance_recharge`;
CREATE TABLE `mall_balance_recharge`  (
  `id` bigint(255) NOT NULL AUTO_INCREMENT COMMENT '关联mall_balance_recharge_contract表的id字段',
  `shellId` int(11) NOT NULL COMMENT '申请id',
  `account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户',
  `createDate` datetime(0) NOT NULL COMMENT '申请时间',
  `projectId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目号',
  `projectName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目名称',
  `agreementId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '合同号',
  `way` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '考核方式',
  `cloudService` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '云服务（未发现对应字段）',
  `lingYunAc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '凌云账号',
  `lingYunId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '凌云ID',
  `seller` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '销售人员',
  `preSale` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '售前人员',
  `amount` decimal(9, 2) NOT NULL COMMENT '金额',
  `recharge` int(11) NOT NULL COMMENT '0充值/1额度',
  `checkFlag` int(2) UNSIGNED ZEROFILL NOT NULL COMMENT '0待确认/1已完成/2失败(再次处理仅翻转为3状态，不调用接口)/3手动处理/4退回',
  `checkMs` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '失败原因',
  `checkName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作账号',
  `backMs` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退回原因',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mall_product

