-- 商品券/代金券表（我的优惠券、发放、核销等依赖）
-- 执行前请确认已选择数据库：USE dingyangmall_ry;

CREATE TABLE IF NOT EXISTS `tb_coupon_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键-券ID',
  `coupon_code` varchar(32) NOT NULL COMMENT '券码-唯一',
  `user_id` bigint NOT NULL COMMENT '持有用户ID（ums_member.id）',
  `goods_id` varchar(64) NOT NULL DEFAULT '' COMMENT '关联商品ID（goods_spu.id，商品券类型 goods_type=2）',
  `goods_name` varchar(200) NOT NULL DEFAULT '' COMMENT '商品名称-冗余',
  `goods_pic` varchar(255) NOT NULL DEFAULT '' COMMENT '商品图片-冗余',
  `integral_price` int NOT NULL DEFAULT 0 COMMENT '兑换积分价格-冗余',
  `validity_start` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '有效期开始',
  `validity_end` datetime NOT NULL COMMENT '有效期结束',
  `coupon_status` tinyint NOT NULL DEFAULT 1 COMMENT '券状态：1未使用 2已使用 3已过期',
  `verify_time` datetime DEFAULT NULL COMMENT '核销时间',
  `verify_dealer_id` bigint DEFAULT NULL COMMENT '核销经销商ID',
  `verify_dealer_name` varchar(64) NOT NULL DEFAULT '' COMMENT '核销经销商名称-冗余',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_coupon_code` (`coupon_code`),
  KEY `idx_user_status` (`user_id`, `coupon_status`),
  KEY `idx_validity_end` (`validity_end`),
  KEY `idx_goods_id` (`goods_id`),
  KEY `idx_verify_dealer_id` (`verify_dealer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品券/代金券';
