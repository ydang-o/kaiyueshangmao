-- ----------------------------
-- 抽奖配置表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_lottery_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `status` char(1) DEFAULT '0' COMMENT '状态（0：关闭；1：开启）',
  `cost_points` int(11) DEFAULT '0' COMMENT '单次抽奖消耗积分',
  `daily_limit` int(11) DEFAULT '1' COMMENT '每日抽奖次数上限',
  `no_prize_probability` double(10,2) DEFAULT '0.00' COMMENT '未中奖概率（百分比，如 20.5 代表 20.5%）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='抽奖配置表';

-- ----------------------------
-- 抽奖奖品表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_lottery_prize` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `config_id` bigint(20) NOT NULL COMMENT '配置ID',
  `prize_type` char(1) DEFAULT '0' COMMENT '奖品类型（0：实物/虚拟商品；1：积分）',
  `goods_id` varchar(64) DEFAULT NULL COMMENT '关联商品ID（当类型为0时）',
  `point_amount` int(11) DEFAULT '0' COMMENT '积分数量（当类型为1时）',
  `prize_name` varchar(100) DEFAULT NULL COMMENT '奖品名称',
  `prize_pic` varchar(255) DEFAULT NULL COMMENT '奖品图片',
  `probability` double(10,2) DEFAULT '0.00' COMMENT '中奖概率（百分比，如 10.5 代表 10.5%）',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_config_id` (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='抽奖奖品表';
