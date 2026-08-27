/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.dingyangmall.common.annotation.Excel;
import java.time.LocalDateTime;
import lombok.Generated;

@TableName(value="goods_category")
public class GoodsCategory
extends Model<GoodsCategory> {
    private static final long serialVersionUID = 1L;
    @Excel(name="PK")
    @TableId(type=IdType.ASSIGN_ID)
    private String id;
    @Excel(name="1\uff1a\u5f00\u542f\uff1b0\uff1a\u5173\u95ed")
    private String enable;
    @Excel(name="\u7236\u5206\u7c7b\u7f16\u53f7")
    private String parentId;
    @Excel(name="\u540d\u79f0")
    private String name;
    @Excel(name="\u63cf\u8ff0")
    private String description;
    @Excel(name="\u56fe\u7247")
    private String picUrl;
    @Excel(name="\u6392\u5e8f")
    private Integer sort;
    @Excel(name="\u521b\u5efa\u65f6\u95f4")
    private LocalDateTime createTime;
    @Excel(name="\u6700\u540e\u66f4\u65b0\u65f6\u95f4")
    private LocalDateTime updateTime;
    @Excel(name="\u903b\u8f91\u5220\u9664\u6807\u8bb0")
    private String delFlag;

    @Generated
    public GoodsCategory() {
    }

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getEnable() {
        return this.enable;
    }

    @Generated
    public String getParentId() {
        return this.parentId;
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public String getDescription() {
        return this.description;
    }

    @Generated
    public String getPicUrl() {
        return this.picUrl;
    }

    @Generated
    public Integer getSort() {
        return this.sort;
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
    public void setId(String id) {
        this.id = id;
    }

    @Generated
    public void setEnable(String enable) {
        this.enable = enable;
    }

    @Generated
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Generated
    public void setName(String name) {
        this.name = name;
    }

    @Generated
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    @Generated
    public void setSort(Integer sort) {
        this.sort = sort;
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
        return "GoodsCategory(id=" + this.getId() + ", enable=" + this.getEnable() + ", parentId=" + this.getParentId() + ", name=" + this.getName() + ", description=" + this.getDescription() + ", picUrl=" + this.getPicUrl() + ", sort=" + this.getSort() + ", createTime=" + String.valueOf(this.getCreateTime()) + ", updateTime=" + String.valueOf(this.getUpdateTime()) + ", delFlag=" + this.getDelFlag() + ")";
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GoodsCategory)) {
            return false;
        }
        GoodsCategory other = (GoodsCategory)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Integer this$sort = this.getSort();
        Integer other$sort = other.getSort();
        if (this$sort == null ? other$sort != null : !((Object)this$sort).equals(other$sort)) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$enable = this.getEnable();
        String other$enable = other.getEnable();
        if (this$enable == null ? other$enable != null : !this$enable.equals(other$enable)) {
            return false;
        }
        String this$parentId = this.getParentId();
        String other$parentId = other.getParentId();
        if (this$parentId == null ? other$parentId != null : !this$parentId.equals(other$parentId)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) {
            return false;
        }
        String this$picUrl = this.getPicUrl();
        String other$picUrl = other.getPicUrl();
        if (this$picUrl == null ? other$picUrl != null : !this$picUrl.equals(other$picUrl)) {
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
        return other instanceof GoodsCategory;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        Integer $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : ((Object)$sort).hashCode());
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $enable = this.getEnable();
        result = result * 59 + ($enable == null ? 43 : $enable.hashCode());
        String $parentId = this.getParentId();
        result = result * 59 + ($parentId == null ? 43 : $parentId.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        String $picUrl = this.getPicUrl();
        result = result * 59 + ($picUrl == null ? 43 : $picUrl.hashCode());
        LocalDateTime $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        LocalDateTime $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        String $delFlag = this.getDelFlag();
        result = result * 59 + ($delFlag == null ? 43 : $delFlag.hashCode());
        return result;
    }
}

