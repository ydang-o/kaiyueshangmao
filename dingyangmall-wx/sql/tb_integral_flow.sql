-- 积分流水表（签到记录、积分变动等依赖）
-- 执行前请确认已选择数据库：USE dingyangmall_ry;

CREATE TABLE IF NOT EXISTS `tb_integral_flow` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户ID（ums_member.id）',
  `oper_type` tinyint NOT NULL COMMENT '操作类型：1平台分发 2上级赠送 3首充 4注册 5每日签到 6推荐注册 7红包 8抽奖',
  `integral_num` int NOT NULL COMMENT '积分数量',
  `source_user_id` bigint DEFAULT NULL COMMENT '来源用户ID',
  `business_id` varchar(64) DEFAULT NULL COMMENT '业务ID',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `oper_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
  `del_flag` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_oper_type` (`oper_type`),
  KEY `idx_oper_time` (`oper_time`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='积分流水';
