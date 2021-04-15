/*
Navicat MySQL Data Transfer

Source Server         : 凌云商城测试环境
Source Server Version : 50725
Source Host           : 121.36.103.17:3306
Source Database       : sobey-mall-product-db-upgrade

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2020-08-25 16:57:15
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for mall_product
-- ----------------------------
DROP TABLE IF EXISTS `mall_product`;
CREATE TABLE `mall_product` (
  `code` varchar(255) DEFAULT NULL,
  `uuid` varchar(32) NOT NULL,
  `name` varchar(100) DEFAULT NULL COMMENT '商品名',
  `summary` varchar(255) DEFAULT NULL COMMENT '商品概述',
  `categoryId` varchar(32) DEFAULT NULL COMMENT '类别编号',
  `desc` varchar(1024) DEFAULT NULL COMMENT '商品说明',
  `price` decimal(20,3) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  `saleMode` varchar(32) DEFAULT NULL COMMENT '销售模式：包年，包月',
  `saleChannel` varchar(32) DEFAULT NULL COMMENT '销售渠道：直接销售，面议等',
  `saleType` varchar(32) DEFAULT NULL COMMENT '销售类型：公开销售，非公开销售',
  `priceDesc` varchar(1024) DEFAULT NULL COMMENT '定价说明',
  `renewInterface` varchar(1024) DEFAULT NULL COMMENT '续费接口',
  `closeInterface` varchar(1024) DEFAULT NULL COMMENT '关闭接口',
  `openInterface` varchar(1024) DEFAULT NULL COMMENT '开通接口',
  `chargingInterface` varchar(1024) DEFAULT NULL COMMENT '计费接口',
  `console` varchar(1024) DEFAULT NULL COMMENT '控制台地址',
  `mediaDir` varchar(255) DEFAULT NULL COMMENT '产品素材的主目录',
  `state` varchar(10) DEFAULT NULL COMMENT '商品状态',
  `resourceId` varchar(255) DEFAULT NULL COMMENT '商品对应的认证中心的资源Id',
  `resourceCode` varchar(255) DEFAULT NULL,
  `logo` varchar(32) DEFAULT NULL COMMENT '商品logo,关联media表中的mediaId',
  `masterGraph` varchar(32) DEFAULT NULL COMMENT '商品主图,关联media表中的mediaId',
  `banner` varchar(32) DEFAULT NULL COMMENT '商品横幅,关联media表中的mediaId',
  `scene` varchar(32) DEFAULT NULL COMMENT '应用场景图片,关联media表中的mediaId',
  `userGuide` varchar(32) DEFAULT NULL COMMENT '用户手册,关联media表中的mediaId',
  `developGuide` varchar(32) DEFAULT NULL COMMENT '开发手册,关联media表中的mediaId',
  `priceTable` varchar(32) DEFAULT NULL COMMENT '定价表,关联media表中的mediaId',
  `createUserCode` varchar(32) DEFAULT NULL,
  `destroyInterface` varchar(255) DEFAULT NULL,
  `hot` varchar(2) DEFAULT NULL,
  `newLaunch` varchar(2) DEFAULT NULL,
  `voucher` varchar(2) DEFAULT NULL COMMENT '是否支持代金券',
  `priceLimited` varchar(32) DEFAULT NULL COMMENT '限价表',
  `enablePackagesNotify` varchar(4) DEFAULT NULL COMMENT '是否启用套餐包通知',
  `notifyPackagesUrl` varchar(255) DEFAULT NULL COMMENT '套餐包通知url',
  `siteCode` varchar(40) DEFAULT NULL COMMENT '站点的code',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for mall_product_bright_spot
-- ----------------------------
DROP TABLE IF EXISTS `mall_product_bright_spot`;
CREATE TABLE `mall_product_bright_spot` (
  `uuid` varchar(32) NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '商品亮点名称',
  `desc` varchar(4000) DEFAULT NULL COMMENT '亮点描述',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  `productId` varchar(32) DEFAULT NULL COMMENT '所属商品编号',
  `order` int(11) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品亮点表';

-- ----------------------------
-- Table structure for mall_product_category
-- ----------------------------
DROP TABLE IF EXISTS `mall_product_category`;
CREATE TABLE `mall_product_category` (
  `uuid` varchar(32) NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `desc` varchar(4000) DEFAULT NULL,
  `parentId` varchar(255) DEFAULT NULL COMMENT '父类型编号',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for mall_product_cooperation_case
-- ----------------------------
DROP TABLE IF EXISTS `mall_product_cooperation_case`;
CREATE TABLE `mall_product_cooperation_case` (
  `uuid` varchar(32) NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '合作案例名称',
  `desc` varchar(4000) DEFAULT NULL COMMENT '合作案例描述',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  `productId` varchar(32) DEFAULT NULL COMMENT '所属商品编号',
  `order` int(11) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品合作案例表';

-- ----------------------------
-- Table structure for mall_product_custom_privilege
-- ----------------------------
DROP TABLE IF EXISTS `mall_product_custom_privilege`;
CREATE TABLE `mall_product_custom_privilege` (
  `uuid` varchar(32) NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '权限名称',
  `type` varchar(255) DEFAULT NULL COMMENT '权限类型',
  `desc` varchar(4000) DEFAULT NULL COMMENT '描述',
  `productId` varchar(32) DEFAULT NULL COMMENT '商品编号',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  `roleId` varchar(255) DEFAULT NULL COMMENT '对应认证中心里的角色id',
  `roleCode` varchar(255) DEFAULT NULL COMMENT '对应认证中心里的角色code',
  `resourceId` varchar(255) DEFAULT NULL COMMENT '对应认证中心里的资源Id',
  `resourceCode` varchar(255) DEFAULT NULL COMMENT '对应认证中心里的资源code',
  `order` int(11) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品自定义权限类型';

-- ----------------------------
-- Table structure for mall_product_discount
-- ----------------------------
DROP TABLE IF EXISTS `mall_product_discount`;
CREATE TABLE `mall_product_discount` (
  `uuid` varchar(32) NOT NULL,
  `userCode` varchar(255) DEFAULT NULL COMMENT '用户编号',
  `productId` varchar(32) DEFAULT NULL COMMENT '所属商品编号',
  `discount` decimal(20,4) DEFAULT NULL COMMENT '折扣',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  `expirationDate` datetime DEFAULT NULL COMMENT '过期时间',
  `status` varchar(255) DEFAULT NULL COMMENT '折扣状态',
  `salesman` varchar(20) DEFAULT NULL COMMENT '销售人员',
  `customer` varchar(20) DEFAULT NULL COMMENT '客户',
  `description` varchar(200) DEFAULT NULL COMMENT '备注',
  `requestUuid` varchar(32) DEFAULT NULL,
  `userLoginName` varchar(32) DEFAULT NULL COMMENT '用户名称',
  `userSiteCode` varchar(32) DEFAULT NULL COMMENT '用户的userSiteCode',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品折扣表';

-- ----------------------------
-- Table structure for mall_product_discount_request_info
-- ----------------------------
DROP TABLE IF EXISTS `mall_product_discount_request_info`;
CREATE TABLE `mall_product_discount_request_info` (
  `uuid` varchar(32) NOT NULL,
  `protocal` varchar(6000) DEFAULT NULL,
  `text` varchar(4000) DEFAULT NULL COMMENT '折扣请求信息',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  `protocalCode` varchar(255) DEFAULT NULL COMMENT '协议编号',
  `status` varchar(255) DEFAULT NULL COMMENT '协议状态',
  `operationType` varchar(255) DEFAULT NULL COMMENT '请求类型',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品折扣请求信息表';

-- ----------------------------
-- Table structure for mall_product_discount_statement
-- ----------------------------
DROP TABLE IF EXISTS `mall_product_discount_statement`;
CREATE TABLE `mall_product_discount_statement` (
  `uuid` varchar(32) NOT NULL,
  `text` varchar(8000) DEFAULT NULL COMMENT '协议模板',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品折扣声明表';

-- ----------------------------
-- Table structure for mall_product_failed_discount_msg
-- ----------------------------
DROP TABLE IF EXISTS `mall_product_failed_discount_msg`;
CREATE TABLE `mall_product_failed_discount_msg` (
  `uuid` varchar(32) NOT NULL,
  `msg` varchar(255) DEFAULT NULL COMMENT '失败的消息内容',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='失败的折扣消息内容';

-- ----------------------------
-- Table structure for mall_product_favorite
-- ----------------------------
DROP TABLE IF EXISTS `mall_product_favorite`;
CREATE TABLE `mall_product_favorite` (
  `uuid` varchar(32) NOT NULL,
  `userCode` varchar(100) DEFAULT NULL COMMENT '用户code',
  `productId` varchar(32) DEFAULT NULL COMMENT '所属商品编号',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品收藏';

-- ----------------------------
-- Table structure for mall_product_function
-- ----------------------------
DROP TABLE IF EXISTS `mall_product_function`;
CREATE TABLE `mall_product_function` (
  `uuid` varchar(32) NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '商品功能名称',
  `desc` varchar(4000) DEFAULT NULL COMMENT '功能描述',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  `productId` varchar(32) DEFAULT NULL COMMENT '所属商品编号',
  `order` int(11) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品功能表';

-- ----------------------------
-- Table structure for mall_product_media
-- ----------------------------
DROP TABLE IF EXISTS `mall_product_media`;
CREATE TABLE `mall_product_media` (
  `uuid` varchar(32) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL COMMENT '媒体类型(图片，视频)',
  `address` varchar(4000) DEFAULT NULL COMMENT '存储地址',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  `productId` varchar(32) DEFAULT NULL COMMENT '所属商品编号',
  `mediaId` varchar(32) DEFAULT NULL COMMENT '关联商品场景图，商品主图等的编号',
  PRIMARY KEY (`uuid`),
  KEY `mediaId` (`mediaId`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品素材表';

-- ----------------------------
-- Table structure for mall_product_metric
-- ----------------------------
DROP TABLE IF EXISTS `mall_product_metric`;
CREATE TABLE `mall_product_metric` (
  `uuid` varchar(32) NOT NULL,
  `name` varchar(100) DEFAULT NULL COMMENT '计费名称',
  `type` varchar(20) DEFAULT NULL COMMENT '计费类型',
  `price` decimal(20,4) DEFAULT NULL COMMENT '价格',
  `productId` varchar(32) DEFAULT NULL COMMENT '所属商品编号',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  `order` int(11) DEFAULT NULL,
  `limitedPrice` decimal(20,4) DEFAULT NULL COMMENT '限价',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品计费单价表';

-- ----------------------------
-- Table structure for mall_product_packages
-- ----------------------------
DROP TABLE IF EXISTS `mall_product_packages`;
CREATE TABLE `mall_product_packages` (
  `uuid` varchar(32) NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '套餐包名称',
  `code` varchar(255) DEFAULT NULL COMMENT '套餐包编码',
  `effectiveDuration` int(11) DEFAULT NULL COMMENT '有效时长',
  `unit` varchar(32) DEFAULT NULL COMMENT '时长单位',
  `desc` varchar(1000) DEFAULT NULL COMMENT '套餐包描述',
  `productId` varchar(32) DEFAULT NULL COMMENT '所属商品编号',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  `order` int(11) DEFAULT NULL COMMENT '套餐包页面顺序',
  `hide` varchar(10) DEFAULT NULL COMMENT '是否隐藏',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品套餐包表';

-- ----------------------------
-- Table structure for mall_product_packages_custom
-- ----------------------------
DROP TABLE IF EXISTS `mall_product_packages_custom`;
CREATE TABLE `mall_product_packages_custom` (
  `uuid` varchar(32) NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '自定义套餐包资源名称',
  `enable` varchar(255) DEFAULT NULL COMMENT '是否启用',
  `desc` varchar(1000) DEFAULT NULL COMMENT '规格描述',
  `packagesId` varchar(32) DEFAULT NULL COMMENT '所属套餐包编号',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  `order` int(11) DEFAULT NULL COMMENT '资源的页面顺序',
  `hide` varchar(10) DEFAULT NULL COMMENT '是否隐藏',
  `metricId` varchar(32) DEFAULT NULL COMMENT '关联按量计费的uuid',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品套餐包自定义规格表';

-- ----------------------------
-- Table structure for mall_product_packages_custom_option
-- ----------------------------
DROP TABLE IF EXISTS `mall_product_packages_custom_option`;
CREATE TABLE `mall_product_packages_custom_option` (
  `uuid` varchar(32) NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '套餐包选项名称',
  `price` decimal(20,4) DEFAULT NULL COMMENT '选项价格',
  `desc` varchar(1000) DEFAULT NULL COMMENT '描述',
  `customId` varchar(32) DEFAULT NULL COMMENT '自定义资源编号',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  `order` int(11) DEFAULT NULL COMMENT '页面顺序',
  `hide` varchar(10) DEFAULT NULL COMMENT '是否隐藏',
  `size` decimal(20,4) DEFAULT NULL COMMENT '容量大小',
  `sizeUnit` varchar(20) DEFAULT NULL COMMENT '容量单位',
  `limitedPrice` decimal(20,4) DEFAULT NULL COMMENT '限价',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品版本自定义规格选项';

-- ----------------------------
-- Table structure for mall_product_protocal
-- ----------------------------
DROP TABLE IF EXISTS `mall_product_protocal`;
CREATE TABLE `mall_product_protocal` (
  `uuid` varchar(32) NOT NULL,
  `text` mediumtext COMMENT '开通协议',
  `type` varchar(4) DEFAULT NULL COMMENT '协议类型',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品开通协议';

-- ----------------------------
-- Table structure for mall_product_version
-- ----------------------------
DROP TABLE IF EXISTS `mall_product_version`;
CREATE TABLE `mall_product_version` (
  `uuid` varchar(32) NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '版本名称',
  `code` varchar(255) DEFAULT NULL COMMENT '版本编码',
  `priceYear` decimal(20,4) DEFAULT NULL COMMENT '包年价格',
  `priceDay` decimal(20,4) DEFAULT NULL,
  `priceMonth` decimal(20,4) DEFAULT NULL COMMENT '包月价格',
  `desc` varchar(4000) DEFAULT NULL COMMENT '版本描述',
  `productId` varchar(32) DEFAULT NULL COMMENT '所属商品编号',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  `order` int(11) DEFAULT NULL,
  `hide` varchar(10) DEFAULT NULL COMMENT '是否隐藏',
  `limitedPriceDay` decimal(20,4) DEFAULT NULL COMMENT '按天限价',
  `limitedPriceMonth` decimal(20,4) DEFAULT NULL COMMENT '按月限价',
  `limitedPriceYear` decimal(20,4) DEFAULT NULL COMMENT '按年限价',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品版本表';

-- ----------------------------
-- Table structure for mall_product_version_custom
-- ----------------------------
DROP TABLE IF EXISTS `mall_product_version_custom`;
CREATE TABLE `mall_product_version_custom` (
  `uuid` varchar(32) NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '自定义规格名称',
  `enable` varchar(255) DEFAULT NULL COMMENT '是否启用',
  `desc` varchar(4000) DEFAULT NULL COMMENT '版本描述',
  `versionId` varchar(32) DEFAULT NULL COMMENT '所属版本编号',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  `order` int(11) DEFAULT NULL,
  `hide` varchar(10) DEFAULT NULL COMMENT '是否隐藏',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品版本自定义规格表';

-- ----------------------------
-- Table structure for mall_product_version_custom_option
-- ----------------------------
DROP TABLE IF EXISTS `mall_product_version_custom_option`;
CREATE TABLE `mall_product_version_custom_option` (
  `uuid` varchar(32) NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '选项名称',
  `priceYear` decimal(20,4) DEFAULT NULL COMMENT '选项价格',
  `priceDay` decimal(20,4) DEFAULT NULL,
  `priceMonth` decimal(20,4) DEFAULT NULL COMMENT '选项价格单位',
  `desc` varchar(4000) DEFAULT NULL COMMENT '描述',
  `customId` varchar(32) DEFAULT NULL COMMENT '自定义规格编号',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  `order` int(11) DEFAULT NULL COMMENT '顺序',
  `hide` varchar(10) DEFAULT NULL COMMENT '是否隐藏',
  `limitedPriceDay` decimal(20,4) DEFAULT NULL COMMENT '按天限价',
  `limitedPriceMonth` decimal(20,4) DEFAULT NULL COMMENT '按月限价',
  `limitedPriceYear` decimal(20,4) DEFAULT NULL COMMENT '按年限价',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品版本自定义规格选项';




INSERT INTO `mall_product_category` VALUES ('1', '媒体服务', null, null, null, null);
INSERT INTO `mall_product_category` VALUES ('2', '编辑服务', null, null, null, null);
INSERT INTO `mall_product_category` VALUES ('3', '视频基础服务', null, null, null, null);
INSERT INTO `mall_product_category` VALUES ('4', '智能服务', null, null, null, null);
INSERT INTO `mall_product_category` VALUES ('5', '数据服务', null, null, null, null);
INSERT INTO `mall_product_category` VALUES ('6', '媒体应用', null, null, null, null);
