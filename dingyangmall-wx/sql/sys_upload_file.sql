-- ----------------------------
-- 上传文件表（文件存于 MySQL，供通用上传、轮播图等使用）
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sys_upload_file` (
  `file_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `file_name` varchar(255) DEFAULT NULL COMMENT '存储文件名',
  `original_name` varchar(255) DEFAULT NULL COMMENT '原始文件名',
  `content_type` varchar(100) DEFAULT NULL COMMENT '文件类型',
  `file_size` bigint(20) DEFAULT NULL COMMENT '文件大小(字节)',
  `content` longblob COMMENT '文件内容',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='上传文件表';
