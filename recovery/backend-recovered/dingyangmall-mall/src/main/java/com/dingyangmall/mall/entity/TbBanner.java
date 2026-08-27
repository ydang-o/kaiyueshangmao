/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import lombok.Generated;

@TableName(value="tb_banner")
public class TbBanner
extends Model<TbBanner> {
    private static final long serialVersionUID = 1L;
    @TableId(type=IdType.AUTO)
    private Long id;
    private String title;
    private String picUrl;
    private String linkUrl;
    private String linkType;
    private Integer sort;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private String delFlag;

    @Generated
    public TbBanner() {
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getTitle() {
        return this.title;
    }

    @Generated
    public String getPicUrl() {
        return this.picUrl;
    }

    @Generated
    public String getLinkUrl() {
        return this.linkUrl;
    }

    @Generated
    public String getLinkType() {
        return this.linkType;
    }

    @Generated
    public Integer getSort() {
        return this.sort;
    }

    @Generated
    public String getStatus() {
        return this.status;
    }

    @Generated
    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    @Generated
    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }

    @Generated
    public String getDelFlag() {
        return this.delFlag;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setTitle(String title) {
        this.title = title;
    }

    @Generated
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    @Generated
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    @Generated
    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    @Generated
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Generated
    public void setStatus(String status) {
        this.status = status;
    }

    @Generated
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Generated
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Generated
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Generated
    public String toString() {
        return "TbBanner(id=" + this.getId() + ", title=" + this.getTitle() + ", picUrl=" + this.getPicUrl() + ", linkUrl=" + this.getLinkUrl() + ", linkType=" + this.getLinkType() + ", sort=" + this.getSort() + ", status=" + this.getStatus() + ", createTime=" + String.valueOf(this.getCreateTime()) + ", updateTime=" + String.valueOf(this.getUpdateTime()) + ", delFlag=" + this.getDelFlag() + ")";
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TbBanner)) {
            return false;
        }
        TbBanner other = (TbBanner)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Long this$id = this.getId();
        Long other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$sort = this.getSort();
        Integer other$sort = other.getSort();
        if (this$sort == null ? other$sort != null : !((Object)this$sort).equals(other$sort)) {
            return false;
        }
        String this$title = this.getTitle();
        String other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) {
            return false;
        }
        String this$picUrl = this.getPicUrl();
        String other$picUrl = other.getPicUrl();
        if (this$picUrl == null ? other$picUrl != null : !this$picUrl.equals(other$picUrl)) {
            return false;
        }
        String this$linkUrl = this.getLinkUrl();
        String other$linkUrl = other.getLinkUrl();
        if (this$linkUrl == null ? other$linkUrl != null : !this$linkUrl.equals(other$linkUrl)) {
            return false;
        }
        String this$linkType = this.getLinkType();
        String other$linkType = other.getLinkType();
        if (this$linkType == null ? other$linkType != null : !this$linkType.equals(other$linkType)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        LocalDateTime this$createTime = this.getCreateTime();
        LocalDateTime other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
            return false;
        }
        LocalDateTime this$updateTime = this.getUpdateTime();
        LocalDateTime other$updateTime = other.getUpdateTime();
        if (this$updateTime == null ? other$updateTime != null : !((Object)this$updateTime).equals(other$updateTime)) {
            return false;
        }
        String this$delFlag = this.getDelFlag();
        String other$delFlag = other.getDelFlag();
        return !(this$delFlag == null ? other$delFlag != null : !this$delFlag.equals(other$delFlag));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof TbBanner;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : ((Object)$sort).hashCode());
        String $title = this.getTitle();
        result = result * 59 + ($title == null ? 43 : $title.hashCode());
        String $picUrl = this.getPicUrl();
        result = result * 59 + ($picUrl == null ? 43 : $picUrl.hashCode());
        String $linkUrl = this.getLinkUrl();
        result = result * 59 + ($linkUrl == null ? 43 : $linkUrl.hashCode());
        String $linkType = this.getLinkType();
        result = result * 59 + ($linkType == null ? 43 : $linkType.hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        LocalDateTime $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        LocalDateTime $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        String $delFlag = this.getDelFlag();
        result = result * 59 + ($delFlag == null ? 43 : $delFlag.hashCode());
        return result;
    }
}

