/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.entity;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.dingyangmall.framework.config.typehandler.JsonTypeHandler;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Generated;
import org.apache.ibatis.type.JdbcType;

@TableName(value="wx_menu")
public class WxMenu
extends Model<WxMenu> {
    private static final long serialVersionUID = 1L;
    @TableId(type=IdType.ASSIGN_ID)
    private String id;
    private String parentId;
    private Integer sort;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String delFlag;
    @NotNull(message="\u83dc\u5355\u7c7b\u578b\u4e0d\u80fd\u4e3a\u7a7a")
    private @NotNull(message="\u83dc\u5355\u7c7b\u578b\u4e0d\u80fd\u4e3a\u7a7a") String type;
    @NotNull(message="\u83dc\u5355\u540d\u4e0d\u80fd\u4e3a\u7a7a")
    private @NotNull(message="\u83dc\u5355\u540d\u4e0d\u80fd\u4e3a\u7a7a") String name;
    private String url;
    private String repMediaId;
    private String repType;
    private String repName;
    private String repContent;
    private String maAppId;
    private String maPagePath;
    private String repDesc;
    private String repUrl;
    private String repHqUrl;
    private String repThumbMediaId;
    private String repThumbUrl;
    @TableField(typeHandler=JsonTypeHandler.class, jdbcType=JdbcType.VARCHAR)
    private JSONObject content;

    @Generated
    public WxMenu() {
    }

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getParentId() {
        return this.parentId;
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
    public String getType() {
        return this.type;
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public String getUrl() {
        return this.url;
    }

    @Generated
    public String getRepMediaId() {
        return this.repMediaId;
    }

    @Generated
    public String getRepType() {
        return this.repType;
    }

    @Generated
    public String getRepName() {
        return this.repName;
    }

    @Generated
    public String getRepContent() {
        return this.repContent;
    }

    @Generated
    public String getMaAppId() {
        return this.maAppId;
    }

    @Generated
    public String getMaPagePath() {
        return this.maPagePath;
    }

    @Generated
    public String getRepDesc() {
        return this.repDesc;
    }

    @Generated
    public String getRepUrl() {
        return this.repUrl;
    }

    @Generated
    public String getRepHqUrl() {
        return this.repHqUrl;
    }

    @Generated
    public String getRepThumbMediaId() {
        return this.repThumbMediaId;
    }

    @Generated
    public String getRepThumbUrl() {
        return this.repThumbUrl;
    }

    @Generated
    public JSONObject getContent() {
        return this.content;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
    }

    @Generated
    public void setParentId(String parentId) {
        this.parentId = parentId;
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
    public void setType(String type) {
        this.type = type;
    }

    @Generated
    public void setName(String name) {
        this.name = name;
    }

    @Generated
    public void setUrl(String url) {
        this.url = url;
    }

    @Generated
    public void setRepMediaId(String repMediaId) {
        this.repMediaId = repMediaId;
    }

    @Generated
    public void setRepType(String repType) {
        this.repType = repType;
    }

    @Generated
    public void setRepName(String repName) {
        this.repName = repName;
    }

    @Generated
    public void setRepContent(String repContent) {
        this.repContent = repContent;
    }

    @Generated
    public void setMaAppId(String maAppId) {
        this.maAppId = maAppId;
    }

    @Generated
    public void setMaPagePath(String maPagePath) {
        this.maPagePath = maPagePath;
    }

    @Generated
    public void setRepDesc(String repDesc) {
        this.repDesc = repDesc;
    }

    @Generated
    public void setRepUrl(String repUrl) {
        this.repUrl = repUrl;
    }

    @Generated
    public void setRepHqUrl(String repHqUrl) {
        this.repHqUrl = repHqUrl;
    }

    @Generated
    public void setRepThumbMediaId(String repThumbMediaId) {
        this.repThumbMediaId = repThumbMediaId;
    }

    @Generated
    public void setRepThumbUrl(String repThumbUrl) {
        this.repThumbUrl = repThumbUrl;
    }

    @Generated
    public void setContent(JSONObject content) {
        this.content = content;
    }

    @Generated
    public String toString() {
        return "WxMenu(id=" + this.getId() + ", parentId=" + this.getParentId() + ", sort=" + this.getSort() + ", createTime=" + String.valueOf(this.getCreateTime()) + ", updateTime=" + String.valueOf(this.getUpdateTime()) + ", delFlag=" + this.getDelFlag() + ", type=" + this.getType() + ", name=" + this.getName() + ", url=" + this.getUrl() + ", repMediaId=" + this.getRepMediaId() + ", repType=" + this.getRepType() + ", repName=" + this.getRepName() + ", repContent=" + this.getRepContent() + ", maAppId=" + this.getMaAppId() + ", maPagePath=" + this.getMaPagePath() + ", repDesc=" + this.getRepDesc() + ", repUrl=" + this.getRepUrl() + ", repHqUrl=" + this.getRepHqUrl() + ", repThumbMediaId=" + this.getRepThumbMediaId() + ", repThumbUrl=" + this.getRepThumbUrl() + ", content=" + String.valueOf(this.getContent()) + ")";
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WxMenu)) {
            return false;
        }
        WxMenu other = (WxMenu)o;
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
        String this$parentId = this.getParentId();
        String other$parentId = other.getParentId();
        if (this$parentId == null ? other$parentId != null : !this$parentId.equals(other$parentId)) {
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
        if (this$delFlag == null ? other$delFlag != null : !this$delFlag.equals(other$delFlag)) {
            return false;
        }
        String this$type = this.getType();
        String other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$url = this.getUrl();
        String other$url = other.getUrl();
        if (this$url == null ? other$url != null : !this$url.equals(other$url)) {
            return false;
        }
        String this$repMediaId = this.getRepMediaId();
        String other$repMediaId = other.getRepMediaId();
        if (this$repMediaId == null ? other$repMediaId != null : !this$repMediaId.equals(other$repMediaId)) {
            return false;
        }
        String this$repType = this.getRepType();
        String other$repType = other.getRepType();
        if (this$repType == null ? other$repType != null : !this$repType.equals(other$repType)) {
            return false;
        }
        String this$repName = this.getRepName();
        String other$repName = other.getRepName();
        if (this$repName == null ? other$repName != null : !this$repName.equals(other$repName)) {
            return false;
        }
        String this$repContent = this.getRepContent();
        String other$repContent = other.getRepContent();
        if (this$repContent == null ? other$repContent != null : !this$repContent.equals(other$repContent)) {
            return false;
        }
        String this$maAppId = this.getMaAppId();
        String other$maAppId = other.getMaAppId();
        if (this$maAppId == null ? other$maAppId != null : !this$maAppId.equals(other$maAppId)) {
            return false;
        }
        String this$maPagePath = this.getMaPagePath();
        String other$maPagePath = other.getMaPagePath();
        if (this$maPagePath == null ? other$maPagePath != null : !this$maPagePath.equals(other$maPagePath)) {
            return false;
        }
        String this$repDesc = this.getRepDesc();
        String other$repDesc = other.getRepDesc();
        if (this$repDesc == null ? other$repDesc != null : !this$repDesc.equals(other$repDesc)) {
            return false;
        }
        String this$repUrl = this.getRepUrl();
        String other$repUrl = other.getRepUrl();
        if (this$repUrl == null ? other$repUrl != null : !this$repUrl.equals(other$repUrl)) {
            return false;
        }
        String this$repHqUrl = this.getRepHqUrl();
        String other$repHqUrl = other.getRepHqUrl();
        if (this$repHqUrl == null ? other$repHqUrl != null : !this$repHqUrl.equals(other$repHqUrl)) {
            return false;
        }
        String this$repThumbMediaId = this.getRepThumbMediaId();
        String other$repThumbMediaId = other.getRepThumbMediaId();
        if (this$repThumbMediaId == null ? other$repThumbMediaId != null : !this$repThumbMediaId.equals(other$repThumbMediaId)) {
            return false;
        }
        String this$repThumbUrl = this.getRepThumbUrl();
        String other$repThumbUrl = other.getRepThumbUrl();
        if (this$repThumbUrl == null ? other$repThumbUrl != null : !this$repThumbUrl.equals(other$repThumbUrl)) {
            return false;
        }
        JSONObject this$content = this.getContent();
        JSONObject other$content = other.getContent();
        return !(this$content == null ? other$content != null : !((Object)this$content).equals(other$content));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof WxMenu;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        Integer $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : ((Object)$sort).hashCode());
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $parentId = this.getParentId();
        result = result * 59 + ($parentId == null ? 43 : $parentId.hashCode());
        LocalDateTime $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        LocalDateTime $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        String $delFlag = this.getDelFlag();
        result = result * 59 + ($delFlag == null ? 43 : $delFlag.hashCode());
        String $type = this.getType();
        result = result * 59 + ($type == null ? 43 : $type.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $url = this.getUrl();
        result = result * 59 + ($url == null ? 43 : $url.hashCode());
        String $repMediaId = this.getRepMediaId();
        result = result * 59 + ($repMediaId == null ? 43 : $repMediaId.hashCode());
        String $repType = this.getRepType();
        result = result * 59 + ($repType == null ? 43 : $repType.hashCode());
        String $repName = this.getRepName();
        result = result * 59 + ($repName == null ? 43 : $repName.hashCode());
        String $repContent = this.getRepContent();
        result = result * 59 + ($repContent == null ? 43 : $repContent.hashCode());
        String $maAppId = this.getMaAppId();
        result = result * 59 + ($maAppId == null ? 43 : $maAppId.hashCode());
        String $maPagePath = this.getMaPagePath();
        result = result * 59 + ($maPagePath == null ? 43 : $maPagePath.hashCode());
        String $repDesc = this.getRepDesc();
        result = result * 59 + ($repDesc == null ? 43 : $repDesc.hashCode());
        String $repUrl = this.getRepUrl();
        result = result * 59 + ($repUrl == null ? 43 : $repUrl.hashCode());
        String $repHqUrl = this.getRepHqUrl();
        result = result * 59 + ($repHqUrl == null ? 43 : $repHqUrl.hashCode());
        String $repThumbMediaId = this.getRepThumbMediaId();
        result = result * 59 + ($repThumbMediaId == null ? 43 : $repThumbMediaId.hashCode());
        String $repThumbUrl = this.getRepThumbUrl();
        result = result * 59 + ($repThumbUrl == null ? 43 : $repThumbUrl.hashCode());
        JSONObject $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : ((Object)$content).hashCode());
        return result;
    }
}

