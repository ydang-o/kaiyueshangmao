/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import lombok.Generated;

@TableName(value="tb_integral_rule")
public class TbIntegralRule
extends Model<TbIntegralRule> {
    private static final long serialVersionUID = 1L;
    @TableId(type=IdType.AUTO)
    private Long id;
    private Integer registerIntegral;
    private Integer firstRechargeIntegral;
    private Integer signIntegral;
    private Integer recommendIntegral;
    private Integer redPacketSwitch;
    @TableField(exist=false)
    private String createBy;
    private LocalDateTime createTime;
    @TableField(exist=false)
    private String updateBy;
    private LocalDateTime updateTime;
    private Integer delFlag;

    @Generated
    public TbIntegralRule() {
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public Integer getRegisterIntegral() {
        return this.registerIntegral;
    }

    @Generated
    public Integer getFirstRechargeIntegral() {
        return this.firstRechargeIntegral;
    }

    @Generated
    public Integer getSignIntegral() {
        return this.signIntegral;
    }

    @Generated
    public Integer getRecommendIntegral() {
        return this.recommendIntegral;
    }

    @Generated
    public Integer getRedPacketSwitch() {
        return this.redPacketSwitch;
    }

    @Generated
    public String getCreateBy() {
        return this.createBy;
    }

    @Generated
    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    @Generated
    public String getUpdateBy() {
        return this.updateBy;
    }

    @Generated
    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }

    @Generated
    public Integer getDelFlag() {
        return this.delFlag;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setRegisterIntegral(Integer registerIntegral) {
        this.registerIntegral = registerIntegral;
    }

    @Generated
    public void setFirstRechargeIntegral(Integer firstRechargeIntegral) {
        this.firstRechargeIntegral = firstRechargeIntegral;
    }

    @Generated
    public void setSignIntegral(Integer signIntegral) {
        this.signIntegral = signIntegral;
    }

    @Generated
    public void setRecommendIntegral(Integer recommendIntegral) {
        this.recommendIntegral = recommendIntegral;
    }

    @Generated
    public void setRedPacketSwitch(Integer redPacketSwitch) {
        this.redPacketSwitch = redPacketSwitch;
    }

    @Generated
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Generated
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Generated
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Generated
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Generated
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    @Generated
    public String toString() {
        return "TbIntegralRule(id=" + this.getId() + ", registerIntegral=" + this.getRegisterIntegral() + ", firstRechargeIntegral=" + this.getFirstRechargeIntegral() + ", signIntegral=" + this.getSignIntegral() + ", recommendIntegral=" + this.getRecommendIntegral() + ", redPacketSwitch=" + this.getRedPacketSwitch() + ", createBy=" + this.getCreateBy() + ", createTime=" + String.valueOf(this.getCreateTime()) + ", updateBy=" + this.getUpdateBy() + ", updateTime=" + String.valueOf(this.getUpdateTime()) + ", delFlag=" + this.getDelFlag() + ")";
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TbIntegralRule)) {
            return false;
        }
        TbIntegralRule other = (TbIntegralRule)o;
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
        Integer this$registerIntegral = this.getRegisterIntegral();
        Integer other$registerIntegral = other.getRegisterIntegral();
        if (this$registerIntegral == null ? other$registerIntegral != null : !((Object)this$registerIntegral).equals(other$registerIntegral)) {
            return false;
        }
        Integer this$firstRechargeIntegral = this.getFirstRechargeIntegral();
        Integer other$firstRechargeIntegral = other.getFirstRechargeIntegral();
        if (this$firstRechargeIntegral == null ? other$firstRechargeIntegral != null : !((Object)this$firstRechargeIntegral).equals(other$firstRechargeIntegral)) {
            return false;
        }
        Integer this$signIntegral = this.getSignIntegral();
        Integer other$signIntegral = other.getSignIntegral();
        if (this$signIntegral == null ? other$signIntegral != null : !((Object)this$signIntegral).equals(other$signIntegral)) {
            return false;
        }
        Integer this$recommendIntegral = this.getRecommendIntegral();
        Integer other$recommendIntegral = other.getRecommendIntegral();
        if (this$recommendIntegral == null ? other$recommendIntegral != null : !((Object)this$recommendIntegral).equals(other$recommendIntegral)) {
            return false;
        }
        Integer this$redPacketSwitch = this.getRedPacketSwitch();
        Integer other$redPacketSwitch = other.getRedPacketSwitch();
        if (this$redPacketSwitch == null ? other$redPacketSwitch != null : !((Object)this$redPacketSwitch).equals(other$redPacketSwitch)) {
            return false;
        }
        Integer this$delFlag = this.getDelFlag();
        Integer other$delFlag = other.getDelFlag();
        if (this$delFlag == null ? other$delFlag != null : !((Object)this$delFlag).equals(other$delFlag)) {
            return false;
        }
        String this$createBy = this.getCreateBy();
        String other$createBy = other.getCreateBy();
        if (this$createBy == null ? other$createBy != null : !this$createBy.equals(other$createBy)) {
            return false;
        }
        LocalDateTime this$createTime = this.getCreateTime();
        LocalDateTime other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
            return false;
        }
        String this$updateBy = this.getUpdateBy();
        String other$updateBy = other.getUpdateBy();
        if (this$updateBy == null ? other$updateBy != null : !this$updateBy.equals(other$updateBy)) {
            return false;
        }
        LocalDateTime this$updateTime = this.getUpdateTime();
        LocalDateTime other$updateTime = other.getUpdateTime();
        return !(this$updateTime == null ? other$updateTime != null : !((Object)this$updateTime).equals(other$updateTime));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof TbIntegralRule;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $registerIntegral = this.getRegisterIntegral();
        result = result * 59 + ($registerIntegral == null ? 43 : ((Object)$registerIntegral).hashCode());
        Integer $firstRechargeIntegral = this.getFirstRechargeIntegral();
        result = result * 59 + ($firstRechargeIntegral == null ? 43 : ((Object)$firstRechargeIntegral).hashCode());
        Integer $signIntegral = this.getSignIntegral();
        result = result * 59 + ($signIntegral == null ? 43 : ((Object)$signIntegral).hashCode());
        Integer $recommendIntegral = this.getRecommendIntegral();
        result = result * 59 + ($recommendIntegral == null ? 43 : ((Object)$recommendIntegral).hashCode());
        Integer $redPacketSwitch = this.getRedPacketSwitch();
        result = result * 59 + ($redPacketSwitch == null ? 43 : ((Object)$redPacketSwitch).hashCode());
        Integer $delFlag = this.getDelFlag();
        result = result * 59 + ($delFlag == null ? 43 : ((Object)$delFlag).hashCode());
        String $createBy = this.getCreateBy();
        result = result * 59 + ($createBy == null ? 43 : $createBy.hashCode());
        LocalDateTime $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        String $updateBy = this.getUpdateBy();
        result = result * 59 + ($updateBy == null ? 43 : $updateBy.hashCode());
        LocalDateTime $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        return result;
    }
}

