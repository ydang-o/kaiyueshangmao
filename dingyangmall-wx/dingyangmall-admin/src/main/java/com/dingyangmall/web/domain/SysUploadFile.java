package com.dingyangmall.web.domain;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 上传文件表 sys_upload_file（文件存于 MySQL，不依赖 OSS/磁盘）
 */
@Data
@TableName("sys_upload_file")
public class SysUploadFile {

    @TableId(type = IdType.AUTO)
    private Long fileId;

    private String fileName;
    private String originalName;
    private String contentType;
    private Long fileSize;
    private byte[] content;
    private Date createTime;
}
