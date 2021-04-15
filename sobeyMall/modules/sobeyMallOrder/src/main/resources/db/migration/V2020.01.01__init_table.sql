/*
 Navicat Premium Data Transfer

 Source Server         : HWSobeyMall
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : 121.36.103.17:3306
 Source Schema         : sobey-mall-service

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 26/08/2020 14:29:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for consume_fail_msg
-- ----------------------------
DROP TABLE IF EXISTS `consume_fail_msg`;
CREATE TABLE `consume_fail_msg`  (
  `uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `appId` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `userCode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `usage` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用量JSON字符串',
  `failReason` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `createDate` datetime NULL DEFAULT NULL,
  `manualProcessStatus` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '人工处理状态 0-未处理 1-已处理',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mall_bill
-- ----------------------------
DROP TABLE IF EXISTS `mall_bill`;
CREATE TABLE `mall_bill`  (
  `uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `totalAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '消费总额',
  `balAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '余额支付总额',
  `wxAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '微信支付总额',
  `zfbAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '支付宝支付总额',
  `voucherAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '代金券支付总额',
  `refundTotalAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '退款总额',
  `balRefundTotalAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '余额退款总额',
  `wxRefundAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '微信退款总额',
  `zfbRefundAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '支付宝退款总额',
  `limPriAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '限价总额',
  `billDate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账期',
  `createDate` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mall_bill_detail
-- ----------------------------
DROP TABLE IF EXISTS `mall_bill_detail`;
CREATE TABLE `mall_bill_detail`  (
  `uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `billUuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账单总表uuid',
  `personalBillUuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '个人账单总表的uuid',
  `siteCode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '站点',
  `accountId` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `productId` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品id',
  `productName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `billingMethod` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计费方式 1-包周期 2-按量',
  `billType` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账单类型 0-消费',
  `totalOrderAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '订单总金额',
  `totalPayAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '消费总金额',
  `balAmount` decimal(20, 8) NULL DEFAULT NULL,
  `wxAmount` decimal(20, 8) NULL DEFAULT NULL,
  `zfbAmount` decimal(20, 8) NULL DEFAULT NULL,
  `voucherAmount` decimal(20, 8) NULL DEFAULT NULL,
  `totalLmiPri` decimal(20, 8) NULL DEFAULT NULL COMMENT '限价总金额',
  `totalRefundAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '退款总金额',
  `createDate` datetime NULL DEFAULT NULL,
  `wxRefundAmount` decimal(20, 8) NULL DEFAULT NULL,
  `zfbRefundAmount` decimal(20, 8) NULL DEFAULT NULL,
  `balRefundTotalAmount` decimal(20, 8) NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mall_pack
-- ----------------------------
DROP TABLE IF EXISTS `mall_pack`;
CREATE TABLE `mall_pack`  (
  `uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `siteCode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '站点',
  `accountId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `packUuid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '套餐包id',
  `packCode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `packName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `productId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `productName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `duration` int(11) NULL DEFAULT NULL COMMENT '有效时长',
  `unit` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '有效时长单位',
  `createDate` datetime NULL DEFAULT NULL,
  `effectiveDate` datetime NULL DEFAULT NULL COMMENT '生效时间',
  `expireDate` datetime NULL DEFAULT NULL COMMENT '失效时间',
  `isEffective` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否生效 0-已过期 1-有效 2-已用完',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mall_pack_resource
-- ----------------------------
DROP TABLE IF EXISTS `mall_pack_resource`;
CREATE TABLE `mall_pack_resource`  (
  `uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `resourceId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源id',
  `resourceName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `optionId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '选项id',
  `optionName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '选项名称,保留字段',
  `mallPackId` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'mall_pack表的主键',
  `metricId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单价id',
  `size` decimal(20, 4) NULL DEFAULT NULL COMMENT '容量',
  `remainingSize` decimal(20, 4) NULL DEFAULT NULL COMMENT '剩余量',
  `sizeUnit` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '容量单位',
  `price` decimal(11, 2) NULL DEFAULT NULL COMMENT '资源价格',
  `createDate` datetime NULL DEFAULT NULL,
  `updateDate` datetime NULL DEFAULT NULL,
  `expireDate` datetime NULL DEFAULT NULL COMMENT '套餐包失效时间',
  PRIMARY KEY (`uuid`) USING BTREE,
  INDEX `mallPackId`(`mallPackId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mall_personal_bill
-- ----------------------------
DROP TABLE IF EXISTS `mall_personal_bill`;
CREATE TABLE `mall_personal_bill`  (
  `uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `siteCode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '站点',
  `accountId` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户Id',
  `account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `totalAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '消费总额',
  `balAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '余额支付总额',
  `wxAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '微信支付总额',
  `zfbAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '支付宝支付总额',
  `voucherAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '代金券支付总额',
  `refundTotalAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '退款总额',
  `balRefundTotalAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '余额退款总额',
  `wxRefundAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '微信退款总额',
  `zfbRefundAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '支付宝退款总额',
  `limPriAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '限价总额',
  `billDate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账期',
  `createDate` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mall_saleman
-- ----------------------------
DROP TABLE IF EXISTS `mall_saleman`;
CREATE TABLE `mall_saleman`  (
  `uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `userCode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '销售人员code',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `createDate` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `loginName` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录名',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '销售的名字',
  `siteCode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '销售的站点',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '销售表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mall_saleman_customer
-- ----------------------------
DROP TABLE IF EXISTS `mall_saleman_customer`;
CREATE TABLE `mall_saleman_customer`  (
  `uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `saleUserCode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '销售人员code',
  `customerUserCode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户code',
  `customerLoginName` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户的登录名',
  `createDate` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `createUserCode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者code',
  `siteCode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户的的站点code',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '销售和客户关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for metric_usage
-- ----------------------------
DROP TABLE IF EXISTS `metric_usage`;
CREATE TABLE `metric_usage`  (
  `uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `orderNo` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `requestId` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求id',
  `priceId` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单价id',
  `price` decimal(15, 4) NULL DEFAULT NULL COMMENT '单价',
  `value` decimal(15, 4) NULL DEFAULT NULL COMMENT '用量',
  `typeCode` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型编码 1-KB 2-MB 3-GB 4-秒 5-分钟 6-小时 7-次 8-其他/默认单位',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单价类型 eg:时长',
  `createTime` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for publish_fail_msg
-- ----------------------------
DROP TABLE IF EXISTS `publish_fail_msg`;
CREATE TABLE `publish_fail_msg`  (
  `uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `appId` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `userCode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户编码',
  `usage` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计费发送的用量信息,JSON字符串',
  `failReason` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '失败原因',
  `createDate` datetime NULL DEFAULT NULL,
  `manualProcessStatus` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '人工处理状态 0-未处理 1-已处理',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for service_info
-- ----------------------------
DROP TABLE IF EXISTS `service_info`;
CREATE TABLE `service_info`  (
  `uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `appId` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'app编号',
  `serviceNo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务编号',
  `serviceStatus` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务状态 0-关闭 1-正常',
  `productId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品id',
  `versionId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '版本编码',
  `productSpecs` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '存储前端自定义信息 json字符串',
  `specifications` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '存储前端自定义信息 json字符串',
  `message` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'html字符串',
  `siteCode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '站点',
  `accountId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户userCode',
  `account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `openType` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开通类型 1-包周期(按版本) 2-按量',
  `chargeCodes` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计费编码,JSON字符串[{\"id\":\"100\",\"typeCode\":\"1\",\"type\":\"按时长\"},{\"id\":\"101\",\"typeCode\":\"2\",\"type\":\"按流量\"}]',
  `createDate` datetime NULL DEFAULT NULL,
  `expireDate` datetime NULL DEFAULT NULL,
  `updateDate` datetime NULL DEFAULT NULL,
  `openUrl` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开通地址',
  `renewUrl` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '续费地址',
  `closeUrl` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关闭地址',
  `failReason` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '失败原因,记录商品开通，续费或关闭失败的原因',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for usage_statistics
-- ----------------------------
DROP TABLE IF EXISTS `usage_statistics`;
CREATE TABLE `usage_statistics`  (
  `uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `accountId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `productId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `appId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `metricId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `value` decimal(15, 6) NULL DEFAULT NULL,
  `typeCode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `createDate` datetime NULL DEFAULT NULL,
  `requestId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
