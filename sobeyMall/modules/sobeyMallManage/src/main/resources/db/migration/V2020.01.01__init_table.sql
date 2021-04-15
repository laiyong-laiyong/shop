

DROP TABLE IF EXISTS `mall_manage_task_monitor`;
CREATE TABLE `mall_manage_task_monitor` (
  `uuid` varchar(64) NOT NULL,
  `processName` varchar(255) DEFAULT NULL COMMENT '流程名称',
  `processId` varchar(64) DEFAULT NULL COMMENT '流程编号',
  `taskName` varchar(255) DEFAULT NULL COMMENT '任务名称',
  `site` varchar(255) DEFAULT NULL COMMENT '租户code',
  `status` varchar(10) DEFAULT NULL COMMENT '任务状态',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  `processStartDate` datetime DEFAULT NULL COMMENT '流程开始时间',
  `processEndDate` datetime DEFAULT NULL COMMENT '流程结束时间',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;