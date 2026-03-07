-- ----------------------------
-- 首页轮播图表
-- 若表已存在且 pic_url 报错无默认值，可执行：ALTER TABLE tb_banner MODIFY COLUMN pic_url varchar(255) NOT NULL DEFAULT '';
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_banner` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(100) DEFAULT NULL COMMENT '标题',
  `pic_url` varchar(255) NOT NULL DEFAULT '' COMMENT '图片地址',
  `link_url` varchar(255) DEFAULT NULL COMMENT '跳转链接(商品ID或外部链接)',
  `link_type` char(1) DEFAULT '0' COMMENT '跳转类型(0:无跳转; 1:商品详情; 2:外部链接)',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `status` char(1) DEFAULT '1' COMMENT '状态(0:下架; 1:上架)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志(0:正常; 1:删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='首页轮播图';
