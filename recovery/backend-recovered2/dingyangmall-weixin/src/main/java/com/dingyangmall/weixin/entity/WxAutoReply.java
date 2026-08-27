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

@TableName(value="wx_auto_reply")
public class WxAutoReply
extends Model<WxAutoReply> {
    private static final long serialVersionUID = 1L;
    @TableId(type=IdType.ASSIGN_ID)
    private String id;
    private String createId;
    private LocalDateTime createTime;
    private String updateId;
    private LocalDateTime updateTime;
    private String remark;
    private String delFlag;
    @NotNull(message="\u7c7b\u578b\u4e0d\u80fd\u4e3a\u7a7a")
    private @NotNull(message="\u7c7b\u578b\u4e0d\u80fd\u4e3a\u7a7a") String type;
    private String reqKey;
    private String reqType;
    @NotNull(message="\u56de\u590d\u6d88\u606f\u7c7b\u578b\u4e0d\u80fd\u4e3a\u7a7a")
    private @NotNull(message="\u56de\u590d\u6d88\u606f\u7c7b\u578b\u4e0d\u80fd\u4e3a\u7a7a") String repType;
    private String repMate;
    private String repContent;
    private String repName;
    private String repMediaId;
    private String repDesc;
    private String repUrl;
    private String repHqUrl;
    private String repThumbMediaId;
    private String repThumbUrl;
    @TableField(typeHandler=JsonTypeHandler.class, jdbcType=JdbcType.VARCHAR)
    private JSONObject content;

    @Generated
    public WxAutoReply() {
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
    public String getType() {
        return this.type;
    }

    @Generated
    public String getReqKey() {
        return this.reqKey;
    }

    @Generated
    public String getReqType() {
        return this.reqType;
    }

    @Generated
    public String getRepType() {
        return this.repType;
    }

    @Generated
    public String getRepMate() {
        return this.repMate;
    }

    @Generated
    public String getRepContent() {
        return this.repContent;
    }

    @Generated
    public String getRepName() {
        return this.repName;
    }

    @Generated
    public String getRepMediaId() {
        return this.repMediaId;
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
    public void setType(String type) {
        this.type = type;
    }

    @Generated
    public void setReqKey(String reqKey) {
        this.reqKey = reqKey;
    }

    @Generated
    public void setReqType(String reqType) {
        this.reqType = reqType;
    }

    @Generated
    public void setRepType(String repType) {
        this.repType = repType;
    }

    @Generated
    public void setRepMate(String repMate) {
        this.repMate = repMate;
    }

    @Generated
    public void setRepContent(String repContent) {
        this.repContent = repContent;
    }

    @Generated
    public void setRepName(String repName) {
        this.repName = repName;
    }

    @Generated
    public void setRepMediaId(String repMediaId) {
        this.repMediaId = repMediaId;
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
        return "WxAutoReply(id=" + this.getId() + ", createId=" + this.getCreateId() + ", createTime=" + String.valueOf(this.getCreateTime()) + ", updateId=" + this.getUpdateId() + ", updateTime=" + String.valueOf(this.getUpdateTime()) + ", remark=" + this.getRemark() + ", delFlag=" + this.getDelFlag() + ", type=" + this.getType() + ", reqKey=" + this.getReqKey() + ", reqType=" + this.getReqType() + ", repType=" + this.getRepType() + ", repMate=" + this.getRepMate() + ", repContent=" + this.getRepContent() + ", repName=" + this.getRepName() + ", repMediaId=" + this.getRepMediaId() + ", repDesc=" + this.getRepDesc() + ", repUrl=" + this.getRepUrl() + ", repHqUrl=" + this.getRepHqUrl() + ", repThumbMediaId=" + this.getRepThumbMediaId() + ", repThumbUrl=" + this.getRepThumbUrl() + ", content=" + String.valueOf(this.getContent()) + ")";
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WxAutoReply)) {
            return false;
        }
        WxAutoReply other = (WxAutoReply)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
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
        String this$type = this.getType();
        String other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) {
            return false;
        }
        String this$reqKey = this.getReqKey();
        String other$reqKey = other.getReqKey();
        if (this$reqKey == null ? other$reqKey != null : !this$reqKey.equals(other$reqKey)) {
            return false;
        }
        String this$reqType = this.getReqType();
        String other$reqType = other.getReqType();
        if (this$reqType == null ? other$reqType != null : !this$reqType.equals(other$reqType)) {
            return false;
        }
        String this$repType = this.getRepType();
        String other$repType = other.getRepType();
        if (this$repType == null ? other$repType != null : !this$repType.equals(other$repType)) {
            return false;
        }
        String this$repMate = this.getRepMate();
        String other$repMate = other.getRepMate();
        if (this$repMate == null ? other$repMate != null : !this$repMate.equals(other$repMate)) {
            return false;
        }
        String this$repContent = this.getRepContent();
        String other$repContent = other.getRepContent();
        if (this$repContent == null ? other$repContent != null : !this$repContent.equals(other$repContent)) {
            return false;
        }
        String this$repName = this.getRepName();
        String other$repName = other.getRepName();
        if (this$repName == null ? other$repName != null : !this$repName.equals(other$repName)) {
            return false;
        }
        String this$repMediaId = this.getRepMediaId();
        String other$repMediaId = other.getRepMediaId();
        if (this$repMediaId == null ? other$repMediaId != null : !this$repMediaId.equals(other$repMediaId)) {
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
        return other instanceof WxAutoReply;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
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
        String $type = this.getType();
        result = result * 59 + ($type == null ? 43 : $type.hashCode());
        String $reqKey = this.getReqKey();
        result = result * 59 + ($reqKey == null ? 43 : $reqKey.hashCode());
        String $reqType = this.getReqType();
        result = result * 59 + ($reqType == null ? 43 : $reqType.hashCode());
        String $repType = this.getRepType();
        result = result * 59 + ($repType == null ? 43 : $repType.hashCode());
        String $repMate = this.getRepMate();
        result = result * 59 + ($repMate == null ? 43 : $repMate.hashCode());
        String $repContent = this.getRepContent();
        result = result * 59 + ($repContent == null ? 43 : $repContent.hashCode());
        String $repName = this.getRepName();
        result = result * 59 + ($repName == null ? 43 : $repName.hashCode());
        String $repMediaId = this.getRepMediaId();
        result = result * 59 + ($repMediaId == null ? 43 : $repMediaId.hashCode());
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

