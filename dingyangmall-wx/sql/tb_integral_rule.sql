-- 积分规则表（每日签到、注册赠送等依赖）
-- 执行前请确认已选择数据库：USE dingyangmall_ry;

CREATE TABLE IF NOT EXISTS `tb_integral_rule` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `register_integral` int NOT NULL DEFAULT 0 COMMENT '注册赠送积分',
  `first_recharge_integral` int NOT NULL DEFAULT 0 COMMENT '首充赠送积分',
  `sign_integral` int NOT NULL DEFAULT 10 COMMENT '签到赠送积分',
  `recommend_integral` int NOT NULL DEFAULT 20 COMMENT '推荐注册赠送积分',
  `red_packet_switch` tinyint NOT NULL DEFAULT 1 COMMENT '红包开关（0关闭 1开启）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='积分规则';

-- 插入默认规则（签到每天 10 积分）
INSERT INTO `tb_integral_rule` (`register_integral`, `first_recharge_integral`, `sign_integral`, `recommend_integral`, `red_packet_switch`)
SELECT 0, 50, 10, 20, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tb_integral_rule` LIMIT 1);
