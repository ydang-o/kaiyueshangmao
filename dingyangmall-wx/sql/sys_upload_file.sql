-- 文件存储表（替代 OSS/本地磁盘，将文件存于 MySQL）
DROP TABLE IF EXISTS `sys_upload_file`;
CREATE TABLE `sys_upload_file` (
  `file_id` bigint NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `file_name` varchar(255) NOT NULL COMMENT '存储文件名',
  `original_name` varchar(255) DEFAULT NULL COMMENT '原始文件名',
  `content_type` varchar(100) DEFAULT NULL COMMENT '文件类型',
  `file_size` bigint DEFAULT 0 COMMENT '文件大小(字节)',
  `content` longblob COMMENT '文件内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='上传文件表(Mysql存储)';
