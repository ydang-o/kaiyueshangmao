/*
 * Decompiled with CFR.
 */
package com.dingyangmall.system.domain;

import com.dingyangmall.common.core.domain.BaseEntity;
import com.dingyangmall.common.xss.Xss;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SysNotice
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long noticeId;
    private String noticeTitle;
    private String noticeType;
    private String noticeContent;
    private String status;

    public Long getNoticeId() {
        return this.noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    @Xss(message="\u516c\u544a\u6807\u9898\u4e0d\u80fd\u5305\u542b\u811a\u672c\u5b57\u7b26")
    @NotBlank(message="\u516c\u544a\u6807\u9898\u4e0d\u80fd\u4e3a\u7a7a")
    @Size(min=0, max=50, message="\u516c\u544a\u6807\u9898\u4e0d\u80fd\u8d85\u8fc750\u4e2a\u5b57\u7b26")
    public @NotBlank(message="\u516c\u544a\u6807\u9898\u4e0d\u80fd\u4e3a\u7a7a") @Size(min=0, max=50, message="\u516c\u544a\u6807\u9898\u4e0d\u80fd\u8d85\u8fc750\u4e2a\u5b57\u7b26") String getNoticeTitle() {
        return this.noticeTitle;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getNoticeType() {
        return this.noticeType;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public String getNoticeContent() {
        return this.noticeContent;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("noticeId", this.getNoticeId()).append("noticeTitle", this.getNoticeTitle()).append("noticeType", this.getNoticeType()).append("noticeContent", this.getNoticeContent()).append("status", this.getStatus()).append("createBy", this.getCreateBy()).append("createTime", this.getCreateTime()).append("updateBy", this.getUpdateBy()).append("updateTime", this.getUpdateTime()).append("remark", this.getRemark()).toString();
    }
}

