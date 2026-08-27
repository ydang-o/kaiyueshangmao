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
import java.time.LocalDateTime;
import lombok.Generated;
import org.apache.ibatis.type.JdbcType;

@TableName(value="wx_msg")
public class WxMsg
extends Model<WxMsg> {
    private static final long serialVersionUID = 1L;
    @TableId(type=IdType.ASSIGN_ID)
    private String id;
    private String createId;
    private LocalDateTime createTime;
    private String updateId;
    private LocalDateTime updateTime;
    private String remark;
    private String delFlag;
    private String appName;
    private String appLogo;
    private String wxUserId;
    private String nickName;
    private String headimgUrl;
    private String type;
    private String repType;
    private String repEvent;
    private String repContent;
    private String repMediaId;
    private String repName;
    private String repDesc;
    private String repUrl;
    private String repHqUrl;
    @TableField(typeHandler=JsonTypeHandler.class, jdbcType=JdbcType.VARCHAR)
    private JSONObject content;
    private String repThumbMediaId;
    private String repThumbUrl;
    private Double repLocationX;
    private Double repLocationY;
    private Double repScale;
    private String readFlag;

    @Generated
    public WxMsg() {
    }

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getCreateId() {
        return this.createId;
    }

    @Generated
    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    @Generated
    public String getUpdateId() {
        return this.updateId;
    }

    @Generated
    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }

    @Generated
    public String getRemark() {
        return this.remark;
    }

    @Generated
    public String getDelFlag() {
        return this.delFlag;
    }

    @Generated
    public String getAppName() {
        return this.appName;
    }

    @Generated
    public String getAppLogo() {
        return this.appLogo;
    }

    @Generated
    public String getWxUserId() {
        return this.wxUserId;
    }

    @Generated
    public String getNickName() {
        return this.nickName;
    }

    @Generated
    public String getHeadimgUrl() {
        return this.headimgUrl;
    }

    @Generated
    public String getType() {
        return this.type;
    }

    @Generated
    public String getRepType() {
        return this.repType;
    }

    @Generated
    public String getRepEvent() {
        return this.repEvent;
    }

    @Generated
    public String getRepContent() {
        return this.repContent;
    }

    @Generated
    public String getRepMediaId() {
        return this.repMediaId;
    }

    @Generated
    public String getRepName() {
        return this.repName;
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
    public JSONObject getContent() {
        return this.content;
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
    public Double getRepLocationX() {
        return this.repLocationX;
    }

    @Generated
    public Double getRepLocationY() {
        return this.repLocationY;
    }

    @Generated
    public Double getRepScale() {
        return this.repScale;
    }

    @Generated
    public String getReadFlag() {
        return this.readFlag;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
    }

    @Generated
    public void setCreateId(String createId) {
        this.createId = createId;
    }

    @Generated
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Generated
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    @Generated
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Generated
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Generated
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Generated
    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Generated
    public void setAppLogo(String appLogo) {
        this.appLogo = appLogo;
    }

    @Generated
    public void setWxUserId(String wxUserId) {
        this.wxUserId = wxUserId;
    }

    @Generated
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Generated
    public void setHeadimgUrl(String headimgUrl) {
        this.headimgUrl = headimgUrl;
    }

    @Generated
    public void setType(String type) {
        this.type = type;
    }

    @Generated
    public void setRepType(String repType) {
        this.repType = repType;
    }

    @Generated
    public void setRepEvent(String repEvent) {
        this.repEvent = repEvent;
    }

    @Generated
    public void setRepContent(String repContent) {
        this.repContent = repContent;
    }

    @Generated
    public void setRepMediaId(String repMediaId) {
        this.repMediaId = repMediaId;
    }

    @Generated
    public void setRepName(String repName) {
        this.repName = repName;
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
    public void setContent(JSONObject content) {
        this.content = content;
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
    public void setRepLocationX(Double repLocationX) {
        this.repLocationX = repLocationX;
    }

    @Generated
    public void setRepLocationY(Double repLocationY) {
        this.repLocationY = repLocationY;
    }

    @Generated
    public void setRepScale(Double repScale) {
        this.repScale = repScale;
    }

    @Generated
    public void setReadFlag(String readFlag) {
        this.readFlag = readFlag;
    }

    @Generated
    public String toString() {
        return "WxMsg(id=" + this.getId() + ", createId=" + this.getCreateId() + ", createTime=" + String.valueOf(this.getCreateTime()) + ", updateId=" + this.getUpdateId() + ", updateTime=" + String.valueOf(this.getUpdateTime()) + ", remark=" + this.getRemark() + ", delFlag=" + this.getDelFlag() + ", appName=" + this.getAppName() + ", appLogo=" + this.getAppLogo() + ", wxUserId=" + this.getWxUserId() + ", nickName=" + this.getNickName() + ", headimgUrl=" + this.getHeadimgUrl() + ", type=" + this.getType() + ", repType=" + this.getRepType() + ", repEvent=" + this.getRepEvent() + ", repContent=" + this.getRepContent() + ", repMediaId=" + this.getRepMediaId() + ", repName=" + this.getRepName() + ", repDesc=" + this.getRepDesc() + ", repUrl=" + this.getRepUrl() + ", repHqUrl=" + this.getRepHqUrl() + ", content=" + String.valueOf(this.getContent()) + ", repThumbMediaId=" + this.getRepThumbMediaId() + ", repThumbUrl=" + this.getRepThumbUrl() + ", repLocationX=" + this.getRepLocationX() + ", repLocationY=" + this.getRepLocationY() + ", repScale=" + this.getRepScale() + ", readFlag=" + this.getReadFlag() + ")";
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WxMsg)) {
            return false;
        }
        WxMsg other = (WxMsg)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Double this$repLocationX = this.getRepLocationX();
        Double other$repLocationX = other.getRepLocationX();
        if (this$repLocationX == null ? other$repLocationX != null : !((Object)this$repLocationX).equals(other$repLocationX)) {
            return false;
        }
        Double this$repLocationY = this.getRepLocationY();
        Double other$repLocationY = other.getRepLocationY();
        if (this$repLocationY == null ? other$repLocationY != null : !((Object)this$repLocationY).equals(other$repLocationY)) {
            return false;
        }
        Double this$repScale = this.getRepScale();
        Double other$repScale = other.getRepScale();
        if (this$repScale == null ? other$repScale != null : !((Object)this$repScale).equals(other$repScale)) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$createId = this.getCreateId();
        String other$createId = other.getCreateId();
        if (this$createId == null ? other$createId != null : !this$createId.equals(other$createId)) {
            return false;
        }
        LocalDateTime this$createTime = this.getCreateTime();
        LocalDateTime other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
            return false;
        }
        String this$updateId = this.getUpdateId();
        String other$updateId = other.getUpdateId();
        if (this$updateId == null ? other$updateId != null : !this$updateId.equals(other$updateId)) {
            return false;
        }
        LocalDateTime this$updateTime = this.getUpdateTime();
        LocalDateTime other$updateTime = other.getUpdateTime();
        if (this$updateTime == null ? other$updateTime != null : !((Object)this$updateTime).equals(other$updateTime)) {
            return false;
        }
        String this$remark = this.getRemark();
        String other$remark = other.getRemark();
        if (this$remark == null ? other$remark != null : !this$remark.equals(other$remark)) {
            return false;
        }
        String this$delFlag = this.getDelFlag();
        String other$delFlag = other.getDelFlag();
        if (this$delFlag == null ? other$delFlag != null : !this$delFlag.equals(other$delFlag)) {
            return false;
        }
        String this$appName = this.getAppName();
        String other$appName = other.getAppName();
        if (this$appName == null ? other$appName != null : !this$appName.equals(other$appName)) {
            return false;
        }
        String this$appLogo = this.getAppLogo();
        String other$appLogo = other.getAppLogo();
        if (this$appLogo == null ? other$appLogo != null : !this$appLogo.equals(other$appLogo)) {
            return false;
        }
        String this$wxUserId = this.getWxUserId();
        String other$wxUserId = other.getWxUserId();
        if (this$wxUserId == null ? other$wxUserId != null : !this$wxUserId.equals(other$wxUserId)) {
            return false;
        }
        String this$nickName = this.getNickName();
        String other$nickName = other.getNickName();
        if (this$nickName == null ? other$nickName != null : !this$nickName.equals(other$nickName)) {
            return false;
        }
        String this$headimgUrl = this.getHeadimgUrl();
        String other$headimgUrl = other.getHeadimgUrl();
        if (this$headimgUrl == null ? other$headimgUrl != null : !this$headimgUrl.equals(other$headimgUrl)) {
            return false;
        }
        String this$type = this.getType();
        String other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) {
            return false;
        }
        String this$repType = this.getRepType();
        String other$repType = other.getRepType();
        if (this$repType == null ? other$repType != null : !this$repType.equals(other$repType)) {
            return false;
        }
        String this$repEvent = this.getRepEvent();
        String other$repEvent = other.getRepEvent();
        if (this$repEvent == null ? other$repEvent != null : !this$repEvent.equals(other$repEvent)) {
            return false;
        }
        String this$repContent = this.getRepContent();
        String other$repContent = other.getRepContent();
        if (this$repContent == null ? other$repContent != null : !this$repContent.equals(other$repContent)) {
            return false;
        }
        String this$repMediaId = this.getRepMediaId();
        String other$repMediaId = other.getRepMediaId();
        if (this$repMediaId == null ? other$repMediaId != null : !this$repMediaId.equals(other$repMediaId)) {
            return false;
        }
        String this$repName = this.getRepName();
        String other$repName = other.getRepName();
        if (this$repName == null ? other$repName != null : !this$repName.equals(other$repName)) {
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
        JSONObject this$content = this.getContent();
        JSONObject other$content = other.getContent();
        if (this$content == null ? other$content != null : !((Object)this$content).equals(other$content)) {
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
        String this$readFlag = this.getReadFlag();
        String other$readFlag = other.getReadFlag();
        return !(this$readFlag == null ? other$readFlag != null : !this$readFlag.equals(other$readFlag));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof WxMsg;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        Double $repLocationX = this.getRepLocationX();
        result = result * 59 + ($repLocationX == null ? 43 : ((Object)$repLocationX).hashCode());
        Double $repLocationY = this.getRepLocationY();
        result = result * 59 + ($repLocationY == null ? 43 : ((Object)$repLocationY).hashCode());
        Double $repScale = this.getRepScale();
        result = result * 59 + ($repScale == null ? 43 : ((Object)$repScale).hashCode());
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $createId = this.getCreateId();
        result = result * 59 + ($createId == null ? 43 : $createId.hashCode());
        LocalDateTime $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        String $updateId = this.getUpdateId();
        result = result * 59 + ($updateId == null ? 43 : $updateId.hashCode());
        LocalDateTime $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        String $remark = this.getRemark();
        result = result * 59 + ($remark == null ? 43 : $remark.hashCode());
        String $delFlag = this.getDelFlag();
        result = result * 59 + ($delFlag == null ? 43 : $delFlag.hashCode());
        String $appName = this.getAppName();
        result = result * 59 + ($appName == null ? 43 : $appName.hashCode());
        String $appLogo = this.getAppLogo();
        result = result * 59 + ($appLogo == null ? 43 : $appLogo.hashCode());
        String $wxUserId = this.getWxUserId();
        result = result * 59 + ($wxUserId == null ? 43 : $wxUserId.hashCode());
        String $nickName = this.getNickName();
        result = result * 59 + ($nickName == null ? 43 : $nickName.hashCode());
        String $headimgUrl = this.getHeadimgUrl();
        result = result * 59 + ($headimgUrl == null ? 43 : $headimgUrl.hashCode());
        String $type = this.getType();
        result = result * 59 + ($type == null ? 43 : $type.hashCode());
        String $repType = this.getRepType();
        result = result * 59 + ($repType == null ? 43 : $repType.hashCode());
        String $repEvent = this.getRepEvent();
        result = result * 59 + ($repEvent == null ? 43 : $repEvent.hashCode());
        String $repContent = this.getRepContent();
        result = result * 59 + ($repContent == null ? 43 : $repContent.hashCode());
        String $repMediaId = this.getRepMediaId();
        result = result * 59 + ($repMediaId == null ? 43 : $repMediaId.hashCode());
        String $repName = this.getRepName();
        result = result * 59 + ($repName == null ? 43 : $repName.hashCode());
        String $repDesc = this.getRepDesc();
        result = result * 59 + ($repDesc == null ? 43 : $repDesc.hashCode());
        String $repUrl = this.getRepUrl();
        result = result * 59 + ($repUrl == null ? 43 : $repUrl.hashCode());
        String $repHqUrl = this.getRepHqUrl();
        result = result * 59 + ($repHqUrl == null ? 43 : $repHqUrl.hashCode());
        JSONObject $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : ((Object)$content).hashCode());
        String $repThumbMediaId = this.getRepThumbMediaId();
        result = result * 59 + ($repThumbMediaId == null ? 43 : $repThumbMediaId.hashCode());
        String $repThumbUrl = this.getRepThumbUrl();
        result = result * 59 + ($repThumbUrl == null ? 43 : $repThumbUrl.hashCode());
        String $readFlag = this.getReadFlag();
        result = result * 59 + ($readFlag == null ? 43 : $readFlag.hashCode());
        return result;
    }
}

