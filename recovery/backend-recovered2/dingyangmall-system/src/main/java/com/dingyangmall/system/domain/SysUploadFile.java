/*
 * Decompiled with CFR.
 */
package com.dingyangmall.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Arrays;
import java.util.Date;
import lombok.Generated;

@TableName(value="sys_upload_file")
public class SysUploadFile {
    @TableId(type=IdType.AUTO)
    private Long fileId;
    private String fileName;
    private String originalName;
    private String contentType;
    private Long fileSize;
    private byte[] content;
    private Date createTime;

    @Generated
    public SysUploadFile() {
    }

    @Generated
    public Long getFileId() {
        return this.fileId;
    }

    @Generated
    public String getFileName() {
        return this.fileName;
    }

    @Generated
    public String getOriginalName() {
        return this.originalName;
    }

    @Generated
    public String getContentType() {
        return this.contentType;
    }

    @Generated
    public Long getFileSize() {
        return this.fileSize;
    }

    @Generated
    public byte[] getContent() {
        return this.content;
    }

    @Generated
    public Date getCreateTime() {
        return this.createTime;
    }

    @Generated
    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    @Generated
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Generated
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    @Generated
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Generated
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    @Generated
    public void setContent(byte[] content) {
        this.content = content;
    }

    @Generated
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysUploadFile)) {
            return false;
        }
        SysUploadFile other = (SysUploadFile)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$fileId = this.getFileId();
        Long other$fileId = other.getFileId();
        if (this$fileId == null ? other$fileId != null : !((Object)this$fileId).equals(other$fileId)) {
            return false;
        }
        Long this$fileSize = this.getFileSize();
        Long other$fileSize = other.getFileSize();
        if (this$fileSize == null ? other$fileSize != null : !((Object)this$fileSize).equals(other$fileSize)) {
            return false;
        }
        String this$fileName = this.getFileName();
        String other$fileName = other.getFileName();
        if (this$fileName == null ? other$fileName != null : !this$fileName.equals(other$fileName)) {
            return false;
        }
        String this$originalName = this.getOriginalName();
        String other$originalName = other.getOriginalName();
        if (this$originalName == null ? other$originalName != null : !this$originalName.equals(other$originalName)) {
            return false;
        }
        String this$contentType = this.getContentType();
        String other$contentType = other.getContentType();
        if (this$contentType == null ? other$contentType != null : !this$contentType.equals(other$contentType)) {
            return false;
        }
        if (!Arrays.equals(this.getContent(), other.getContent())) {
            return false;
        }
        Date this$createTime = this.getCreateTime();
        Date other$createTime = other.getCreateTime();
        return !(this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof SysUploadFile;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $fileId = this.getFileId();
        result = result * 59 + ($fileId == null ? 43 : ((Object)$fileId).hashCode());
        Long $fileSize = this.getFileSize();
        result = result * 59 + ($fileSize == null ? 43 : ((Object)$fileSize).hashCode());
        String $fileName = this.getFileName();
        result = result * 59 + ($fileName == null ? 43 : $fileName.hashCode());
        String $originalName = this.getOriginalName();
        result = result * 59 + ($originalName == null ? 43 : $originalName.hashCode());
        String $contentType = this.getContentType();
        result = result * 59 + ($contentType == null ? 43 : $contentType.hashCode());
        result = result * 59 + Arrays.hashCode(this.getContent());
        Date $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "SysUploadFile(fileId=" + this.getFileId() + ", fileName=" + this.getFileName() + ", originalName=" + this.getOriginalName() + ", contentType=" + this.getContentType() + ", fileSize=" + this.getFileSize() + ", content=" + Arrays.toString(this.getContent()) + ", createTime=" + String.valueOf(this.getCreateTime()) + ")";
    }
}

