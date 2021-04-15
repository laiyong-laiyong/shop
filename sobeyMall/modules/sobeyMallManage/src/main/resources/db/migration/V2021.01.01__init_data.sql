/*
 Navicat Premium Data Transfer

 Source Server         : 凌云商城测试环境
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : 121.36.103.17:3306
 Source Schema         : sobey-mall-manage

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 03/02/2021 17:15:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for mall_init_data_permissions
-- ----------------------------
DROP TABLE IF EXISTS `mall_init_data_permissions`;
CREATE TABLE `mall_init_data_permissions`  (
  `uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `roleCode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色code',
  `resourceCode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源code',
  `permissions` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限',
  `siteCode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '站点code',
  `success` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否成功执行(yes,no)',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mall_init_data_permissions
-- ----------------------------
INSERT INTO `mall_init_data_permissions` VALUES ('1', 'lingyun_role_ucgbprp725', 'lingyun_oty4grwhqk', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('10', 'lingyun_role_y09afbvxh3', 'lingyun_k7lnftcf5c', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('11', 'lingyun_role_y09afbvxh3', 'lingyun_no5firtb2o', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('12', 'lingyun_role_y09afbvxh3', 'lingyun_zjff3vg3gj', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('13', 'lingyun_role_y09afbvxh3', 'lingyun_ed8gesf5gk', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('14', 'lingyun_role_y09afbvxh3', 'lingyun_oe5dqo82vz', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('15', 'lingyun_role_y09afbvxh3', 'lingyun_ypnntctdjq', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('16', 'lingyun_role_y09afbvxh3', 'lingyun_4qbpcfg4eu', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('17', 'lingyun_role_y09afbvxh3', 'lingyun_bz282v21q8', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('18', 'lingyun_role_p36n761vos', 'lingyun_hdxq4qf29g', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('19', 'lingyun_role_p36n761vos', 'lingyun_z25a51klnq', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('2', 'lingyun_role_jfmwq891fq', 'lingyun_eya2ad1mp8', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('20', 'lingyun_role_p36n761vos', 'lingyun_q7i1jh4rfe', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('21', 'lingyun_role_p36n761vos', 'lingyun_tflleg7ebp', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('22', 'lingyun_role_g6110pq9nf', 'lingyun_zse50hqkj0', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('23', 'lingyun_role_g6110pq9nf', 'lingyun_z25a51klnq', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('24', 'lingyun_role_g6110pq9nf', 'lingyun_j2ir55p4nj', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('25', 'lingyun_role_g6110pq9nf', 'lingyun_dfyorgrl8a', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('26', 'lingyun_role_g6110pq9nf', 'lingyun_gbcsmygovj', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('27', 'lingyun_role_g6110pq9nf', 'lingyun_at8zotavhp', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('28', 'lingyun_role_g6110pq9nf', 'lingyun_tflleg7ebp', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('29', 'lingyun_role_g6110pq9nf', 'lingyun_xh7n42k5u9', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('3', 'lingyun_role_jfmwq891fq', 'lingyun_r4jxm0ux2l', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('30', 'lingyun_role_g6110pq9nf', 'lingyun_wzn29f2921', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('31', 'lingyun_role_g6110pq9nf', 'lingyun_jdcn8sz4og', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('32', 'lingyun_role_g6110pq9nf', 'lingyun_q7i1jh4rfe', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('33', 'lingyun_role_g6110pq9nf', 'lingyun_y9fkeiuthu', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('34', 'lingyun_role_g6110pq9nf', 'lingyun_j3woanp78q', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('35', 'lingyun_role_g6110pq9nf', 'lingyun_pqgkeukg4z', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('36', 'lingyun_role_g6110pq9nf', 'lingyun_7ljt5zo24c', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('37', 'lingyun_role_g6110pq9nf', 'lingyun_hw8swnwmds', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('38', 'lingyun_role_g6110pq9nf', 'lingyun_grhi2hpsp5', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('39', 'lingyun_role_g6110pq9nf', 'lingyun_tr5y5p4sev', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('4', 'lingyun_role_jfmwq891fq', 'lingyun_e5nnciwyql', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('40', 'lingyun_role_g6110pq9nf', 'lingyun_qp46auw1dn', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('41', 'lingyun_role_g6110pq9nf', 'lingyun_68dw51pi86', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('42', 'lingyun_role_4rn85cwi4p', 'lingyun_k7lnftcf5c', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('43', 'lingyun_role_4rn85cwi4p', 'lingyun_no5firtb2o', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('44', 'lingyun_role_4rn85cwi4p', 'lingyun_zjff3vg3gj', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('45', 'lingyun_role_4rn85cwi4p', 'lingyun_ed8gesf5gk', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('46', 'lingyun_role_4rn85cwi4p', 'lingyun_oe5dqo82vz', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('47', 'lingyun_role_4rn85cwi4p', 'lingyun_ypnntctdjq', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('48', 'lingyun_role_4rn85cwi4p', 'lingyun_4qbpcfg4eu', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('49', 'lingyun_role_4rn85cwi4p', 'lingyun_bz282v21q8', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('5', 'lingyun_role_y09afbvxh3', 'lingyun_w4kok53itl', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('50', 'lingyun_role_4rn85cwi4p', 'lingyun_w4kok53itl', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('51', 'lingyun_role_4rn85cwi4p', 'lingyun_4hgo57m4h3', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('52', 'lingyun_role_4rn85cwi4p', 'lingyun_n2a3br3xxg', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('53', 'lingyun_role_4rn85cwi4p', 'lingyun_i78mivkp8b', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('54', 'lingyun_role_4rn85cwi4p', 'lingyun_3fnhoa6i54', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('55', 'lingyun_role_4rn85cwi4p', 'lingyun_hdxq4qf29g', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('56', 'lingyun_role_4rn85cwi4p', 'lingyun_lqwoctda2n', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('57', 'lingyun_role_4rn85cwi4p', 'lingyun_serjd3qmbe', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('6', 'lingyun_role_y09afbvxh3', 'lingyun_4hgo57m4h3', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('7', 'lingyun_role_y09afbvxh3', 'lingyun_n2a3br3xxg', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('8', 'lingyun_role_y09afbvxh3', 'lingyun_i78mivkp8b', '*', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_permissions` VALUES ('9', 'lingyun_role_y09afbvxh3', 'lingyun_3fnhoa6i54', '*', 'sobeyLingYunMall', NULL);

-- ----------------------------
-- Table structure for mall_init_data_resource
-- ----------------------------
DROP TABLE IF EXISTS `mall_init_data_resource`;
CREATE TABLE `mall_init_data_resource`  (
  `uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源名称',
  `shown` int(11) NULL DEFAULT NULL COMMENT '是否显示',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源code',
  `clientCode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户端code',
  `siteCode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '站点code',
  `parentId` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父节点编号',
  `resourceId` bigint(20) NULL DEFAULT NULL COMMENT '认证中心资源id',
  `desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `success` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否成功执行(yes,no)',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mall_init_data_resource
-- ----------------------------
INSERT INTO `mall_init_data_resource` VALUES ('1', '商品权限', 1, 'lingyun_qzvjbrof7e', 'Lingyun_mall', 'sobeyLingYunMall', '46', 3, NULL, NULL);
INSERT INTO `mall_init_data_resource` VALUES ('10', '广播管理', 1, 'lingyun_dfyorgrl8a', 'Lingyun_mall', 'sobeyLingYunMall', '7', 39, '对广播进行发布.删除等操作,对广播相应的管理', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('11', '消息模板', 1, 'lingyun_gbcsmygovj', 'Lingyun_mall', 'sobeyLingYunMall', '7', 40, '对站内消息模板的修改增删等管理', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('12', '交易管理', 1, 'lingyun_at8zotavhp', 'Lingyun_mall', 'sobeyLingYunMall', '2', 5, '商城订单\\服务等相应管理', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('13', '全部服务', 1, 'lingyun_q7i1jh4rfe', 'Lingyun_mall', 'sobeyLingYunMall', '12', 6, '所有用户开通的服务信息', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('14', '订单查询', 1, 'lingyun_tflleg7ebp', 'Lingyun_mall', 'sobeyLingYunMall', '12', 7, '所有用户的订单管理', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('15', '发票管理', 1, 'lingyun_y9fkeiuthu', 'Lingyun_mall', 'sobeyLingYunMall', '12', 8, '对用户申请的发票管理', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('16', '系统管理', 1, 'lingyun_iv9jcl069q', 'Lingyun_mall', 'sobeyLingYunMall', '2', 12, '用户,角色,资源权限的管理', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('17', '用户管理', 1, 'lingyun_j3woanp78q', 'Lingyun_mall', 'sobeyLingYunMall', '16', 13, '凌云商城的所有注册用户管理', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('18', '角色管理', 1, 'lingyun_pqgkeukg4z', 'Lingyun_mall', 'sobeyLingYunMall', '16', 14, '商城角色的管理', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('19', '资源管理', 1, 'lingyun_7gsfdnan22', 'Lingyun_mall', 'sobeyLingYunMall', '16', 15, '对所有资源权限的管理', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('2', '管理平台', 1, 'lingyun_ehhbnvl69i', 'Lingyun_mall', 'sobeyLingYunMall', '46', 4, '凌云商城管理平台', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('20', '用户中心', 1, 'lingyun_xyuy74687f', 'Lingyun_mall', 'sobeyLingYunMall', '46', 43, '商城用户中心相关权限', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('21', '用户设置', 1, 'lingyun_w4kok53itl', 'Lingyun_mall', 'sobeyLingYunMall', '20', 44, '用户信息设置', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('22', '用户信息', 1, 'lingyun_4hgo57m4h3', 'Lingyun_mall', 'sobeyLingYunMall', '21', 45, '用户注册相关信息', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('23', '安全设置', 1, 'lingyun_n2a3br3xxg', 'Lingyun_mall', 'sobeyLingYunMall', '21', 46, '用户登录密码等安全信息设置', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('24', '费用中心', 1, 'lingyun_bz282v21q8', 'Lingyun_mall', 'sobeyLingYunMall', '20', 47, '用户余额,订单等相关信息管理', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('25', '余额管理', 1, 'lingyun_i78mivkp8b', 'Lingyun_mall', 'sobeyLingYunMall', '24', 48, '用户余额查看,充值等相关管理', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('26', '我的订单', 1, 'lingyun_3fnhoa6i54', 'Lingyun_mall', 'sobeyLingYunMall', '24', 49, '用户的订单管理', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('27', '发票管理', 1, 'lingyun_99k9i1kheo', 'Lingyun_mall', 'sobeyLingYunMall', '34', 20, '发票管理', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('28', '我的服务', 1, 'lingyun_no5firtb2o', 'Lingyun_mall', 'sobeyLingYunMall', '20', 53, '用户开通的服务管理', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('29', '工单管理', 1, 'lingyun_zjff3vg3gj', 'Lingyun_mall', 'sobeyLingYunMall', '20', 54, '用户提交工单和工单列表管理', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('3', '平台状态', 1, 'lingyun_2xsiewmxi4', 'Lingyun_mall', 'sobeyLingYunMall', '2', 18, '管理平台状态监控', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('30', '我的工单', 1, 'lingyun_oe5dqo82vz', 'Lingyun_mall', 'sobeyLingYunMall', '29', 55, '展示用户的工单,查看工单处理情况等信息', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('31', '提交工单', 1, 'lingyun_ed8gesf5gk', 'Lingyun_mall', 'sobeyLingYunMall', '29', 56, '用户提交工单的功能', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('32', '消息箱', 1, 'lingyun_ypnntctdjq', 'Lingyun_mall', 'sobeyLingYunMall', '20', 57, '查看消息内容', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('33', '用户组', 1, 'lingyun_serjd3qmbe', 'Lingyun_mall', 'sobeyLingYunMall', '47', 60, '用户的权限查看及相关设置管理', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('34', '财务管理', 1, 'lingyun_5u6le54k3r', 'Lingyun_mall', 'sobeyLingYunMall', '2', 19, '财务管理权限', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('35', '我的套餐', 1, 'lingyun_4qbpcfg4eu', 'Lingyun_mall', 'sobeyLingYunMall', '20', 58, '我的套餐', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('36', '发票管理', 1, 'lingyun_k7lnftcf5c', 'Lingyun_mall', 'sobeyLingYunMall', '24', 50, '用户的发票索取等相关管理', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('37', '套餐管理', 1, 'lingyun_wzn29f2921', 'Lingyun_mall', 'sobeyLingYunMall', '12', 9, '套餐管理', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('38', '折扣管理', 1, 'lingyun_ieneuj343n', 'Lingyun_mall', 'sobeyLingYunMall', '34', 21, '用户折扣管理', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('39', '商品定价', 1, 'lingyun_ktddhxwgpj', 'Lingyun_mall', 'sobeyLingYunMall', '24', 51, '商品定价 图片展示！', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('4', '商品中心', 1, 'lingyun_eya2ad1mp8', 'Lingyun_mall', 'sobeyLingYunMall', '2', 63, '商品发布与管理权限', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('40', '账单', 1, 'lingyun_9a46m8ecuu', 'Lingyun_mall', 'sobeyLingYunMall', '24', 52, '用户消费账单统计', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('41', '账户管理', 1, 'lingyun_7ljt5zo24c', 'Lingyun_mall', 'sobeyLingYunMall', '34', 22, '账户管理', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('42', '余额充值', 1, 'lingyun_z6x1hqndb7', 'Lingyun_mall', 'sobeyLingYunMall', '2', 27, '余额充值', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('43', '销售中心', 1, 'lingyun_bb00tjojyc', 'Lingyun_mall', 'sobeyLingYunMall', '2', 28, '销售中心', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('44', '在售商品一览', 1, 'lingyun_ccgjjq7t6p', 'Lingyun_mall', 'sobeyLingYunMall', '43', 29, '正在销售的商品总览', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('45', '我的销售数据', 1, 'lingyun_aau4rszjmx', 'Lingyun_mall', 'sobeyLingYunMall', '43', 30, '我的销售数据', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('46', '索贝凌云商城', 1, 'lingyun_oty4grwhqk', 'Lingyun_mall', 'sobeyLingYunMall', '0', 2, NULL, NULL);
INSERT INTO `mall_init_data_resource` VALUES ('47', '子账号管理', 1, 'lingyun_hdxq4qf29g', 'Lingyun_mall', 'sobeyLingYunMall', '20', 59, '用户创建子账号等相关管理', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('48', '子账号列表', 1, 'lingyun_lqwoctda2n', 'Lingyun_mall', 'sobeyLingYunMall', '47', 61, '子账号列表展示,可创建子账号,设置子账号分组', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('49', '代金券管理 ', 1, 'lingyun_aasm7kjyii', 'Lingyun_mall', 'sobeyLingYunMall', '34', 23, '代金券管理权限', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('5', '发布商品', 1, 'lingyun_e5nnciwyql', 'Lingyun_mall', 'sobeyLingYunMall', '4', 25, '可以发布商品', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('50', '我的客户', 1, 'lingyun_tbg2q4lqx6', 'Lingyun_mall', 'sobeyLingYunMall', '43', 31, '销售对应的客户查询', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('51', '销售管理', 1, 'lingyun_6nt8kzy4zc', 'Lingyun_mall', 'sobeyLingYunMall', '43', 32, '销售的利润及成本查询', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('52', '账单管理', 1, 'lingyun_jdcn8sz4og', 'Lingyun_mall', 'sobeyLingYunMall', '12', 10, '用户账单、阅读统计相关', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('53', '计费失败', 1, 'lingyun_xh7n42k5u9', 'Lingyun_mall', 'sobeyLingYunMall', '12', 11, '统计并处理计费失败的交易', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('54', '运维监控', 1, 'lingyun_hw8swnwmds', 'Lingyun_mall', 'sobeyLingYunMall', '2', 33, '运维监控', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('55', '任务监控', 1, 'lingyun_qp46auw1dn', 'Lingyun_mall', 'sobeyLingYunMall', '54', 34, '任务监控', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('56', '执行器监控', 1, 'lingyun_tr5y5p4sev', 'Lingyun_mall', 'sobeyLingYunMall', '54', 35, '执行器监控', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('57', '引擎监控', 1, 'lingyun_68dw51pi86', 'Lingyun_mall', 'sobeyLingYunMall', '54', 36, '引擎监控', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('58', '租户监控', 1, 'lingyun_grhi2hpsp5', 'Lingyun_mall', 'sobeyLingYunMall', '54', 37, '租户监控', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('59', '应用管理', 1, 'lingyun_fn50jheyrd', 'Lingyun_mall', 'sobeyLingYunMall', '16', 16, NULL, NULL);
INSERT INTO `mall_init_data_resource` VALUES ('6', '商品管理', 1, 'lingyun_r4jxm0ux2l', 'Lingyun_mall', 'sobeyLingYunMall', '4', 62, '商品修改\\删除等管理', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('60', '系统注册', 1, 'lingyun_4n8pu2t79r', 'Lingyun_mall', 'sobeyLingYunMall', '16', 17, '系统注册', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('7', '服务管理', 1, 'lingyun_zse50hqkj0', 'Lingyun_mall', 'sobeyLingYunMall', '2', 38, '消息工单等相关服务管理', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('8', '工单管理', 1, 'lingyun_z25a51klnq', 'Lingyun_mall', 'sobeyLingYunMall', '7', 41, '对商城用户中心提交的工单进行派发等相应管理', NULL);
INSERT INTO `mall_init_data_resource` VALUES ('9', '公告管理', 1, 'lingyun_j2ir55p4nj', 'Lingyun_mall', 'sobeyLingYunMall', '7', 42, '在商城发布公告,删除公告等操作,对公告相应的管理', NULL);

-- ----------------------------
-- Table structure for mall_init_data_role
-- ----------------------------
DROP TABLE IF EXISTS `mall_init_data_role`;
CREATE TABLE `mall_init_data_role`  (
  `uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源名称',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源code',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `clientCode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户端code',
  `siteCode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '站点code',
  `success` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否成功执行(yes,no)',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mall_init_data_role
-- ----------------------------
INSERT INTO `mall_init_data_role` VALUES ('1', 'lgYnSpAdministrator', 'lingyun_role_ucgbprp725', '商城所有权限', 'Lingyun_mall', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_role` VALUES ('2', '商品管理权限', 'lingyun_role_jfmwq891fq', '后台上架商品权限', 'Lingyun_mall', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_role` VALUES ('3', '用户中心权限', 'lingyun_role_4rn85cwi4p', '商城用户基础权限', 'Lingyun_mall', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_role` VALUES ('4', 'admin', 'lingyun_role_y09afbvxh3', '用户管理员权限', 'Lingyun_mall', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_role` VALUES ('5', '默认接收工单消息人员', 'lingyun_role_p36n761vos', '提交工单时或催单时收到消息提醒', 'Lingyun_mall', 'sobeyLingYunMall', NULL);
INSERT INTO `mall_init_data_role` VALUES ('6', '运维人员', 'lingyun_role_g6110pq9nf', '', 'Lingyun_mall', 'sobeyLingYunMall', NULL);

-- ----------------------------
-- Table structure for mall_init_data_user
-- ----------------------------
DROP TABLE IF EXISTS `mall_init_data_user`;
CREATE TABLE `mall_init_data_user`  (
  `uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `loginName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名/登录名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户密码',
  `nickName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `rootUserFlag` tinyint(4) NULL DEFAULT NULL COMMENT '是否为根级父账号',
  `siteCode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '站点code',
  `phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `success` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否成功执行(yes,no)',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mall_init_data_user
-- ----------------------------
INSERT INTO `mall_init_data_user` VALUES ('1', 'lyadmin', '$2a$10$zmFL15IXLe7fzdT/9UoI.emzHgkN91UWCzpMJVPd8GLqEbY3v1Ouu', 'lyadmin', 1, 'sobeyLingYunMall', NULL, NULL);

-- ----------------------------
-- Table structure for mall_init_data_user_role
-- ----------------------------
DROP TABLE IF EXISTS `mall_init_data_user_role`;
CREATE TABLE `mall_init_data_user_role`  (
  `uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `userId` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '自己的用户表的id',
  `userCode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户中心返回的code',
  `roleCode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色code',
  `success` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否成功执行(yes,no)',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mall_init_data_user_role
-- ----------------------------
INSERT INTO `mall_init_data_user_role` VALUES ('1', '1', '76e7a38e34db4bfc943c5c34ca7fae70', 'lingyun_role_ucgbprp725', NULL);

SET FOREIGN_KEY_CHECKS = 1;
