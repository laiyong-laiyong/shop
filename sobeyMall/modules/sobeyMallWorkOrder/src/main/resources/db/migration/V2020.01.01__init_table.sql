/*
Navicat MySQL Data Transfer

Source Server         : 凌云商城测试环境
Source Server Version : 50725
Source Host           : 121.36.103.17:3306
Source Database       : sobey-mall-work-order-db-upgrade

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2020-08-25 18:17:07
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for mall_work_order
-- ----------------------------
DROP TABLE IF EXISTS `mall_work_order`;
CREATE TABLE `mall_work_order` (
  `uuid` varchar(32) NOT NULL,
  `name` varchar(100) DEFAULT NULL COMMENT '工单名称',
  `categoryId` varchar(32) DEFAULT NULL COMMENT '类别编号',
  `desc` varchar(2048) DEFAULT NULL COMMENT '工单说明',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  `state` varchar(32) DEFAULT NULL COMMENT '工单状态',
  `telephone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(20) DEFAULT NULL COMMENT '联系邮箱',
  `mediaDir` varchar(255) DEFAULT NULL COMMENT '素材主目录',
  `level` varchar(10) DEFAULT NULL COMMENT '工单级别',
  `mediaId` varchar(32) DEFAULT NULL COMMENT '素材编号(这里只是为了和dialogue表统一,其值等于uuid)',
  `contactStartTime` datetime DEFAULT NULL COMMENT '联系开始时间',
  `contactEndTime` datetime DEFAULT NULL COMMENT '联系结束时间',
  `handlerCode` varchar(640) DEFAULT NULL COMMENT '处理者编码',
  `createUserCode` varchar(32) DEFAULT NULL COMMENT '创建者编码',
  `evaluate` varchar(2000) DEFAULT NULL COMMENT '评价内容',
  `closeDate` datetime DEFAULT NULL COMMENT '工单关闭或撤销时间',
  `productId` varchar(32) DEFAULT NULL COMMENT '商品编号',
  `deleteFlag` int(11) DEFAULT NULL,
  `siteCode` varchar(40) DEFAULT NULL COMMENT '站点的code',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单';

-- ----------------------------
-- Table structure for mall_work_order_category
-- ----------------------------
DROP TABLE IF EXISTS `mall_work_order_category`;
CREATE TABLE `mall_work_order_category` (
  `uuid` varchar(32) NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `desc` varchar(4000) DEFAULT NULL,
  `parentId` varchar(255) DEFAULT NULL COMMENT '父类型编号',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单类别';

-- ----------------------------
-- Table structure for mall_work_order_dialogue
-- ----------------------------
DROP TABLE IF EXISTS `mall_work_order_dialogue`;
CREATE TABLE `mall_work_order_dialogue` (
  `uuid` varchar(32) NOT NULL,
  `from` varchar(100) DEFAULT NULL COMMENT '讲话人的编号',
  `workOrderId` varchar(32) DEFAULT NULL COMMENT '工单编号',
  `context` varchar(2048) DEFAULT NULL COMMENT '谈话内容',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  `mediaId` varchar(32) DEFAULT NULL COMMENT '素材编号',
  `to` varchar(100) DEFAULT NULL COMMENT '听话人的编号',
  PRIMARY KEY (`uuid`),
  KEY `workOrderId` (`workOrderId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单对话表';

-- ----------------------------
-- Table structure for mall_work_order_evaluate
-- ----------------------------
DROP TABLE IF EXISTS `mall_work_order_evaluate`;
CREATE TABLE `mall_work_order_evaluate` (
  `uuid` varchar(32) NOT NULL,
  `workOrderId` varchar(32) DEFAULT NULL COMMENT '工单编号',
  `evaluateCategoryId` varchar(32) DEFAULT NULL COMMENT '评价类别编号',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单评价表';

-- ----------------------------
-- Table structure for mall_work_order_evaluate_label_category
-- ----------------------------
DROP TABLE IF EXISTS `mall_work_order_evaluate_label_category`;
CREATE TABLE `mall_work_order_evaluate_label_category` (
  `uuid` varchar(32) NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '评价类别',
  `starId` varchar(32) DEFAULT NULL COMMENT '星级标签',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单评价标签类别表';

-- ----------------------------
-- Table structure for mall_work_order_evaluate_star_category
-- ----------------------------
DROP TABLE IF EXISTS `mall_work_order_evaluate_star_category`;
CREATE TABLE `mall_work_order_evaluate_star_category` (
  `uuid` varchar(32) NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '评价类别',
  `desc` varchar(255) DEFAULT NULL COMMENT '描述',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单评价星级类别表';

-- ----------------------------
-- Table structure for mall_work_order_media
-- ----------------------------
DROP TABLE IF EXISTS `mall_work_order_media`;
CREATE TABLE `mall_work_order_media` (
  `uuid` varchar(32) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL COMMENT '媒体类型(图片，视频)',
  `address` varchar(4000) DEFAULT NULL COMMENT '存储地址',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  `workOrderId` varchar(32) DEFAULT NULL COMMENT '工单编号',
  `mediaId` varchar(32) DEFAULT NULL COMMENT '关联工单表，dialogue表的mediaId字段',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单素材表';




-- ----------------------------
-- Records of mall_work_order_category
-- ----------------------------
INSERT INTO `mall_work_order_category` VALUES ('1', '点播类', null, null, null, null);



-- ----------------------------
-- Records of mall_work_order_evaluate_label_category
-- ----------------------------
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('1', '处理结果不满意', '1001', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('10', '产品易用性差', '1002', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('11', '解决及时性一般', '1003', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('12', '服务态度一般', '1003', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('13', '处理结果一般', '1003', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('14', '响应速度一般', '1003', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('15', '业务水平一般', '1003', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('16', '产品稳定性一般', '1003', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('17', '产品易用性一般', '1003', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('18', '解决很及时', '1004', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('19', '服务态度好', '1004', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('2', '响应速度慢', '1001', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('20', '处理结果满意', '1004', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('21', '响应速度快', '1004', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('22', '业务水平强', '1004', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('23', '产品很稳定', '1004', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('24', '产品易用性好', '1004', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('25', '解决很及时', '1005', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('26', '服务态度好', '1005', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('27', '处理结果满意', '1005', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('28', '响应速度快', '1005', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('29', '业务水平强', '1005', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('3', '业务水平差', '1001', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('30', '产品很稳定', '1005', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('31', '产品易用性好', '1005', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('4', '产品稳定性差', '1001', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('5', '产品易用性差', '1001', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('6', '处理结果不满意', '1002', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('7', '响应速度慢', '1002', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('8', '业务水平差', '1002', null, null);
INSERT INTO `mall_work_order_evaluate_label_category` VALUES ('9', '产品稳定性差', '1002', null, null);

-- ----------------------------
-- Table structure for mall_work_order_evaluate_star_category
-- ----------------------------


-- ----------------------------
-- Records of mall_work_order_evaluate_star_category
-- ----------------------------
INSERT INTO `mall_work_order_evaluate_star_category` VALUES ('1001', '一星', '生气', null, null);
INSERT INTO `mall_work_order_evaluate_star_category` VALUES ('1002', '二星', '失望', null, null);
INSERT INTO `mall_work_order_evaluate_star_category` VALUES ('1003', '三星', '一般', null, null);
INSERT INTO `mall_work_order_evaluate_star_category` VALUES ('1004', '四星', '还不错', null, null);
INSERT INTO `mall_work_order_evaluate_star_category` VALUES ('1005', '五星', '满意', null, null);
