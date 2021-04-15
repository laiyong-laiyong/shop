/*
 Navicat Premium Data Transfer

 Source Server         : HWSobeyMall
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : 121.36.103.17:3306
 Source Schema         : sobey-mall-msg

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 26/08/2020 14:31:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for mall_msg
-- ----------------------------
DROP TABLE IF EXISTS `mall_msg`;
CREATE TABLE `mall_msg`  (
  `uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `basicMsgType` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '基础消息类型',
  `subMsgType` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '子消息类型',
  `msgContent` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '消息内容',
  `accountId` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收人',
  `msgStatus` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息状态 1-已读 0-未读',
  `createTime` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mall_msg_basic_type
-- ----------------------------
DROP TABLE IF EXISTS `mall_msg_basic_type`;
CREATE TABLE `mall_msg_basic_type`  (
  `uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `code` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型编码',
  `desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编码说明',
  `createTime` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mall_msg_broadcast
-- ----------------------------
DROP TABLE IF EXISTS `mall_msg_broadcast`;
CREATE TABLE `mall_msg_broadcast`  (
  `uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `basicMsgType` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `subMsgType` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `msgContent` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `sendStatus` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送状态 0-未发送 1-已发送',
  `createTime` datetime NULL DEFAULT NULL,
  `sentTime` datetime NULL DEFAULT NULL COMMENT '发送时间',
  `expireTime` datetime NULL DEFAULT NULL COMMENT '到期时间',
  `updateTime` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mall_msg_cloud_provider
-- ----------------------------
DROP TABLE IF EXISTS `mall_msg_cloud_provider`;
CREATE TABLE `mall_msg_cloud_provider`  (
  `uuid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '云提供商名称',
  `secretKey` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sk',
  `accessKey` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `regionId` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `policyName` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `endpoint` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mall_msg_cloud_short_message
-- ----------------------------
DROP TABLE IF EXISTS `mall_msg_cloud_short_message`;
CREATE TABLE `mall_msg_cloud_short_message`  (
  `uuid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `cloudProvider` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '云提供商类别',
  `signName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '签名',
  `templateCode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板编码',
  `notifyType` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通知类型',
  `desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mall_msg_proclamation
-- ----------------------------
DROP TABLE IF EXISTS `mall_msg_proclamation`;
CREATE TABLE `mall_msg_proclamation`  (
  `uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `title` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `typeCode` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公告类型编码',
  `typeDesc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公告类型',
  `publishStatus` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发布状态 0-未发布 1-已发布 2-已撤销',
  `createTime` datetime NULL DEFAULT NULL,
  `publishTime` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `updateTime` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mall_msg_sub_type
-- ----------------------------
DROP TABLE IF EXISTS `mall_msg_sub_type`;
CREATE TABLE `mall_msg_sub_type`  (
  `uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `code` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型编码',
  `desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编码描述',
  `basicUuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父级类型id',
  `createTime` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mall_msg_template
-- ----------------------------
DROP TABLE IF EXISTS `mall_msg_template`;
CREATE TABLE `mall_msg_template`  (
  `uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板名称',
  `parentTypeCode` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父级消息模板类型编码 eg:0-系统消息',
  `parentTypeDesc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父级消息模板类型编码说明',
  `msgTypeCode` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息模板类型编码 eg:0-服务开通提醒',
  `msgTypeDesc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息模板类型编码说明',
  `msgTemplate` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '模板内容',
  `createTime` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mall_msg_template_placeholder
-- ----------------------------
DROP TABLE IF EXISTS `mall_msg_template_placeholder`;
CREATE TABLE `mall_msg_template_placeholder`  (
  `uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '占位符',
  `desc` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '占位符说明',
  `createTime` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mall_operations_personnel
-- ----------------------------
DROP TABLE IF EXISTS `mall_operations_personnel`;
CREATE TABLE `mall_operations_personnel`  (
  `uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `accountId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'userCode',
  `account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `phoneNum` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话号码',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `isOnDuty` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否值班',
  `lastModifyAccount` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后修改人',
  `createDate` datetime NULL DEFAULT NULL,
  `updateDate` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO `mall_msg_basic_type`(`uuid`, `code`, `desc`, `createTime`) VALUES ('1', '0', '系统类型消息', '2020-04-13 11:53:27');
INSERT INTO `mall_msg_basic_type`(`uuid`, `code`, `desc`, `createTime`) VALUES ('2', '1', '广播类型消息(发给所有人)', '2020-04-13 11:53:54');
INSERT INTO `mall_msg_basic_type`(`uuid`, `code`, `desc`, `createTime`) VALUES ('3', '2', '公告类型消息', '2020-04-14 11:58:29');

INSERT INTO `mall_msg_cloud_provider`(`uuid`, `name`, `secretKey`, `accessKey`, `regionId`, `policyName`, `endpoint`) VALUES ('1', 'Aliyun', '4nO5AIJOmDnOmfI1GitunFmvlbYn5u', 'LTAImFFrrcHa5W7c', 'cn-hangzhou', 'AliyunOSSFullAccess', 'http://oss-cn-beijing.aliyuncs.com');

INSERT INTO `mall_msg_cloud_short_message`(`uuid`, `cloudProvider`, `signName`, `templateCode`, `notifyType`, `desc`) VALUES ('1', 'Aliyun', '成都索贝数码科技', 'SMS_189613023', 'reminder_operator', '客户催单');
INSERT INTO `mall_msg_cloud_short_message`(`uuid`, `cloudProvider`, `signName`, `templateCode`, `notifyType`, `desc`) VALUES ('2', 'Aliyun', '成都索贝数码科技', 'SMS_189710835', 'reminder_customer', '通知客户有消息反馈');
INSERT INTO `mall_msg_cloud_short_message`(`uuid`, `cloudProvider`, `signName`, `templateCode`, `notifyType`, `desc`) VALUES ('3', 'Aliyun', '成都索贝数码科技', 'SMS_189711743', 'distributed_customer', '工单派发通知客户');
INSERT INTO `mall_msg_cloud_short_message`(`uuid`, `cloudProvider`, `signName`, `templateCode`, `notifyType`, `desc`) VALUES ('4', 'Aliyun', '成都索贝数码科技', 'SMS_189711706', 'distributed_operator', '派发工单通知运维');
INSERT INTO `mall_msg_cloud_short_message`(`uuid`, `cloudProvider`, `signName`, `templateCode`, `notifyType`, `desc`) VALUES ('5', 'Aliyun', '成都索贝数码科技', 'SMS_189712266', 'new_work_order', '新增工单通知运维');
INSERT INTO `mall_msg_cloud_short_message`(`uuid`, `cloudProvider`, `signName`, `templateCode`, `notifyType`, `desc`) VALUES ('6', 'Aliyun', '成都索贝数码科技', 'SMS_190283359', 'voucher_short_message', '发送代金券短信');
INSERT INTO `mall_msg_cloud_short_message`(`uuid`, `cloudProvider`, `signName`, `templateCode`, `notifyType`, `desc`) VALUES ('7', 'Aliyun', '成都索贝数码科技', 'SMS_195705394', 'arrears_short_message', '欠费通知管理人员');

INSERT INTO `mall_msg_sub_type`(`uuid`, `code`, `desc`, `basicUuid`, `createTime`) VALUES ('1', '-1', '欢迎信息', '1', '2020-04-13 14:21:17');
INSERT INTO `mall_msg_sub_type`(`uuid`, `code`, `desc`, `basicUuid`, `createTime`) VALUES ('10', '8', '系统公告', '3', '2020-04-14 11:59:41');
INSERT INTO `mall_msg_sub_type`(`uuid`, `code`, `desc`, `basicUuid`, `createTime`) VALUES ('11', '9', '活动通知', '3', '2020-04-14 11:59:59');
INSERT INTO `mall_msg_sub_type`(`uuid`, `code`, `desc`, `basicUuid`, `createTime`) VALUES ('12', '10', '新闻动态', '3', '2020-04-14 12:00:18');
INSERT INTO `mall_msg_sub_type`(`uuid`, `code`, `desc`, `basicUuid`, `createTime`) VALUES ('13', '11', '服务到期提醒', '1', '2020-04-27 16:42:39');
INSERT INTO `mall_msg_sub_type`(`uuid`, `code`, `desc`, `basicUuid`, `createTime`) VALUES ('14', '12', '代金券创建完成通知', '1', '2020-05-18 16:40:12');
INSERT INTO `mall_msg_sub_type`(`uuid`, `code`, `desc`, `basicUuid`, `createTime`) VALUES ('15', '13', '信用额度提醒', '1', '2020-07-06 17:01:04');
INSERT INTO `mall_msg_sub_type`(`uuid`, `code`, `desc`, `basicUuid`, `createTime`) VALUES ('16', '14', '欠费提醒(给管理人员)', '1', '2020-07-08 11:12:06');
INSERT INTO `mall_msg_sub_type`(`uuid`, `code`, `desc`, `basicUuid`, `createTime`) VALUES ('17', '15', '用户折扣确认', '1', '2020-07-14 14:20:54');
INSERT INTO `mall_msg_sub_type`(`uuid`, `code`, `desc`, `basicUuid`, `createTime`) VALUES ('2', '0', '商品开通通知', '1', '2020-04-13 14:21:43');
INSERT INTO `mall_msg_sub_type`(`uuid`, `code`, `desc`, `basicUuid`, `createTime`) VALUES ('3', '1', '商品续费通知', '1', '2020-04-13 14:22:06');
INSERT INTO `mall_msg_sub_type`(`uuid`, `code`, `desc`, `basicUuid`, `createTime`) VALUES ('4', '2', '商品关闭通知', '1', '2020-04-13 14:22:23');
INSERT INTO `mall_msg_sub_type`(`uuid`, `code`, `desc`, `basicUuid`, `createTime`) VALUES ('5', '3', '欠费提醒', '1', '2020-04-13 14:22:38');
INSERT INTO `mall_msg_sub_type`(`uuid`, `code`, `desc`, `basicUuid`, `createTime`) VALUES ('6', '4', '余额充值提醒', '1', '2020-04-13 14:22:56');
INSERT INTO `mall_msg_sub_type`(`uuid`, `code`, `desc`, `basicUuid`, `createTime`) VALUES ('7', '5', '工单待处理(运维)', '1', '2020-04-13 14:23:16');
INSERT INTO `mall_msg_sub_type`(`uuid`, `code`, `desc`, `basicUuid`, `createTime`) VALUES ('8', '6', '工单受理通知(用户)', '1', '2020-04-13 14:23:31');
INSERT INTO `mall_msg_sub_type`(`uuid`, `code`, `desc`, `basicUuid`, `createTime`) VALUES ('9', '7', '工单确认通知(用户)', '1', '2020-04-13 14:23:47');

INSERT INTO `mall_msg_template`(`uuid`, `name`, `parentTypeCode`, `parentTypeDesc`, `msgTypeCode`, `msgTypeDesc`, `msgTemplate`, `createTime`, `updateTime`) VALUES ('00bf7b0cadcf433281829c1b0880cfdb', '商品关闭通知模板', '0', '系统类型消息', '2', '商品关闭通知', '【服务停止通知】您好！${username}，您的${productName}服务已于${yyyy/MM/dd HH:mm:ss}到期关闭，服务已停用。谢谢您的支持！', '2020-04-09 11:36:05', '2020-04-21 16:08:59');
INSERT INTO `mall_msg_template`(`uuid`, `name`, `parentTypeCode`, `parentTypeDesc`, `msgTypeCode`, `msgTypeDesc`, `msgTemplate`, `createTime`, `updateTime`) VALUES ('6e16fa307e1349b4b6a127105de83c11', '账户欠费通知运维管理', '0', '系统类型消息', '14', '欠费提醒(给管理人员)', '【欠费提醒】用户${username}已欠费，余额为${balance}，该用户有${credits}的信用额度，信用额度使用完按量计费服务会关闭，7天后资源将陆续释放，请及时告知用户！', NULL, '2020-07-09 15:54:12');
INSERT INTO `mall_msg_template`(`uuid`, `name`, `parentTypeCode`, `parentTypeDesc`, `msgTypeCode`, `msgTypeDesc`, `msgTemplate`, `createTime`, `updateTime`) VALUES ('7eae0bd787fd4527a354f170868a5859', '余额充值提醒模板', '0', '系统类型消息', '4', '余额充值提醒', '【充值完成】您好！${username}，您已完成充值，充值金额${transactionAmount}，账户余额${balance}。\r', '2020-04-09 11:36:05', NULL);
INSERT INTO `mall_msg_template`(`uuid`, `name`, `parentTypeCode`, `parentTypeDesc`, `msgTypeCode`, `msgTypeDesc`, `msgTemplate`, `createTime`, `updateTime`) VALUES ('80e4a0cd87284bab849c479348d2dacd', '欢迎信息模板', '0', '系统类型消息', '-1', '欢迎信息', '${username}，欢迎您注册索贝凌云！\r', '2020-04-09 11:36:04', NULL);
INSERT INTO `mall_msg_template`(`uuid`, `name`, `parentTypeCode`, `parentTypeDesc`, `msgTypeCode`, `msgTypeDesc`, `msgTemplate`, `createTime`, `updateTime`) VALUES ('9f6ac772ef1548ddbc7a32253319ee62', '工单确认通知(用户)模板', '0', '系统类型消息', '7', '工单确认通知(用户)', '【工单确认通知】您好！${username}，您的工单${workOrderNumber}已完成处理，请确认并填写服务满意度评价，谢谢！工单将会在7天后自动关闭，如有其他问题，请联系客服人员或再次提交工单，感谢您的支持\n', '2020-04-09 11:36:06', '2020-04-24 16:55:14');
INSERT INTO `mall_msg_template`(`uuid`, `name`, `parentTypeCode`, `parentTypeDesc`, `msgTypeCode`, `msgTypeDesc`, `msgTemplate`, `createTime`, `updateTime`) VALUES ('a8c8c88a15b84b8793b882a7020daf83', '商品续费通知模板', '0', '系统类型消息', '1', '商品续费通知', '【续费成功】您好！${username}，您的${productName}服务已完成续费！剩余服务时长${remainingTime}天，到期时间更新至${yyyy/MM/dd HH:mm:ss}。\n', '2020-04-09 11:36:04', '2020-04-24 16:55:01');
INSERT INTO `mall_msg_template`(`uuid`, `name`, `parentTypeCode`, `parentTypeDesc`, `msgTypeCode`, `msgTypeDesc`, `msgTemplate`, `createTime`, `updateTime`) VALUES ('a8d854c122f942bc92a54c99775580fd', '服务到期提醒模板', '0', '系统类型消息', '11', '服务到期提醒', '【服务到期提醒】您好！${username}，您的${productName}服务将在${remainingTime}天后到期，到期时间${yyyy/MM/dd HH:mm:ss}，请及时续费！', NULL, '2020-04-27 16:49:57');
INSERT INTO `mall_msg_template`(`uuid`, `name`, `parentTypeCode`, `parentTypeDesc`, `msgTypeCode`, `msgTypeDesc`, `msgTemplate`, `createTime`, `updateTime`) VALUES ('c176b490f77a47458b864e5b642d676e', '欠费提醒模板', '0', '系统类型消息', '3', '欠费提醒', '【欠费提醒】您好！${username}，您已欠费，账户余额${balance}元。您有${credits}元的信用额度，若欠费超过信用额度，按需服务将被冻结，无法正常使用；7天后未续费的按需服务将会被陆续释放，数据不可恢复。 为了保证您的业务正常运行，请及时进行充值！', '2020-04-09 11:36:05', '2020-07-08 11:02:16');
INSERT INTO `mall_msg_template`(`uuid`, `name`, `parentTypeCode`, `parentTypeDesc`, `msgTypeCode`, `msgTypeDesc`, `msgTemplate`, `createTime`, `updateTime`) VALUES ('d535b112e25849a18700f2719099cacc', '工单待处理(运维)模板', '0', '系统类型消息', '5', '工单待处理(运维)', '【工单待处理提醒】工单${workOrderNumber}已新增或催单，请及时查看并处理！\n', '2020-04-09 11:36:05', '2020-04-24 16:31:20');
INSERT INTO `mall_msg_template`(`uuid`, `name`, `parentTypeCode`, `parentTypeDesc`, `msgTypeCode`, `msgTypeDesc`, `msgTemplate`, `createTime`, `updateTime`) VALUES ('d987dd7c3f2041fa862312a598e37a19', '工单受理通知(用户)模板', '0', '系统类型消息', '6', '工单受理通知(用户)', '【工单受理通知】您好！${username}，您的工单${workOrderNumber}已被受理，我们会尽快为您处理，请您耐心等待。感谢您的支持！\r', '2020-04-09 11:36:06', NULL);
INSERT INTO `mall_msg_template`(`uuid`, `name`, `parentTypeCode`, `parentTypeDesc`, `msgTypeCode`, `msgTypeDesc`, `msgTemplate`, `createTime`, `updateTime`) VALUES ('ecddf91e10024e33a3d3c11eb493b4af', '代金券通知', '0', '系统类型消息', '12', '代金券创建完成通知', '您好，${username}！您已收到凌云商城试用代金券码：${voucherCode}，请登录凌云商城充值代金券！\n代金券充值请参考： https://www.sobeylingyun.com/doc/voucher/Voucher_charging.pdf', NULL, '2020-07-23 17:08:34');
INSERT INTO `mall_msg_template`(`uuid`, `name`, `parentTypeCode`, `parentTypeDesc`, `msgTypeCode`, `msgTypeDesc`, `msgTemplate`, `createTime`, `updateTime`) VALUES ('f2e88b58efe04123a9f83eabd0067d7c', '商品开通通知模板', '0', '系统类型消息', '0', '商品开通通知', '【服务已开通】您好！${username}，您的${productName}产品已开通！', '2020-04-09 11:36:04', '2020-04-24 16:53:57');
INSERT INTO `mall_msg_template`(`uuid`, `name`, `parentTypeCode`, `parentTypeDesc`, `msgTypeCode`, `msgTypeDesc`, `msgTemplate`, `createTime`, `updateTime`) VALUES ('fd69300e9dd54f46a82d1daa527c4c85', '信用额度使用完提醒', '0', '系统类型消息', '13', '信用额度提醒', '【信用额度提醒】尊敬的${username}用户，您好，您的信用额度${credits}已使用完，按需服务将被冻结，无法正常使用；7天后未续费的按需服务将会被陆续释放，数据不可恢复。 为了保证您的业务正常运行，请及时进行充值！', NULL, '2020-07-08 11:07:25');

INSERT INTO `mall_msg_template_placeholder`(`uuid`, `value`, `desc`, `createTime`) VALUES ('1', '${username}', '用户名称', '2020-04-08 17:43:33');
INSERT INTO `mall_msg_template_placeholder`(`uuid`, `value`, `desc`, `createTime`) VALUES ('2', '${productName}', '商品名称', '2020-04-08 17:43:54');
INSERT INTO `mall_msg_template_placeholder`(`uuid`, `value`, `desc`, `createTime`) VALUES ('3', '${yyyy/MM/dd HH:mm:ss}', '服务到期时间', '2020-04-08 17:44:26');
INSERT INTO `mall_msg_template_placeholder`(`uuid`, `value`, `desc`, `createTime`) VALUES ('4', '${remainingTime}', '剩余时长(天)', '2020-04-08 17:44:53');
INSERT INTO `mall_msg_template_placeholder`(`uuid`, `value`, `desc`, `createTime`) VALUES ('5', '${balance}', '账户余额', '2020-04-08 17:45:12');
INSERT INTO `mall_msg_template_placeholder`(`uuid`, `value`, `desc`, `createTime`) VALUES ('6', '${transactionAmount}', '交易金额', '2020-04-08 17:45:30');
INSERT INTO `mall_msg_template_placeholder`(`uuid`, `value`, `desc`, `createTime`) VALUES ('7', '${workOrderNumber}', '工单号', '2020-04-08 17:46:01');
INSERT INTO `mall_msg_template_placeholder`(`uuid`, `value`, `desc`, `createTime`) VALUES ('8', '${voucherCode}', '代金券编码', '2020-05-18 16:43:12');
INSERT INTO `mall_msg_template_placeholder`(`uuid`, `value`, `desc`, `createTime`) VALUES ('9', '${credits}', '信用额度', '2020-07-06 09:57:26');

