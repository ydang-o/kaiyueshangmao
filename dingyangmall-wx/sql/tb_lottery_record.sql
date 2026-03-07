-- 抽奖记录表（管理端 /mall/lottery/record/page 等依赖）
-- 执行前请确认已选择数据库：USE dingyangmall_ry;

CREATE TABLE IF NOT EXISTS `tb_lottery_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID（ums_member.id）',
  `is_win` char(1) DEFAULT NULL COMMENT '是否中奖（0：未中奖；1：已中奖）',
  `prize_id` bigint DEFAULT NULL COMMENT '奖品ID',
  `prize_name` varchar(200) DEFAULT NULL COMMENT '奖品名称',
  `prize_type` varchar(20) DEFAULT NULL COMMENT '奖品类型（0：实物/虚拟商品；1：积分）',
  `cost_points` int DEFAULT NULL COMMENT '消耗积分',
  `grant_status` char(1) DEFAULT NULL COMMENT '发放状态（0：待发放；1：已发放）',
  `business_id` varchar(64) DEFAULT NULL COMMENT '关联业务ID（如订单ID、优惠券ID）',
  `create_time` datetime DEFAULT NULL COMMENT '抽奖时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='抽奖记录';
