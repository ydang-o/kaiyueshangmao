package com.dingyangmall.system.domain;

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

    /** 存储文件名 */
    private String fileName;

    /** 原始文件名 */
    private String originalName;

    /** 文件类型 */
    private String contentType;

    /** 文件大小(字节) */
    private Long fileSize;

    /** 文件内容 */
    private byte[] content;

    /** 创建时间 */
    private Date createTime;
}
