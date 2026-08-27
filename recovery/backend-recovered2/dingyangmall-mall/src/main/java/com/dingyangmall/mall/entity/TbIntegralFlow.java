/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import lombok.Generated;

@TableName(value="tb_integral_flow")
public class TbIntegralFlow
extends Model<TbIntegralFlow> {
    private static final long serialVersionUID = 1L;
    @TableId(type=IdType.AUTO)
    private Long id;
    private Long userId;
    private Integer operType;
    private Integer integralNum;
    private Long sourceUserId;
    private String businessId;
    private String remark;
    private LocalDateTime operTime;
    private LocalDateTime createTime;
    private String createBy;
    private Integer delFlag;

    @Generated
    public TbIntegralFlow() {
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public Long getUserId() {
        return this.userId;
    }

    @Generated
    public Integer getOperType() {
        return this.operType;
    }

    @Generated
    public Integer getIntegralNum() {
        return this.integralNum;
    }

    @Generated
    public Long getSourceUserId() {
        return this.sourceUserId;
    }

    @Generated
    public String getBusinessId() {
        return this.businessId;
    }

    @Generated
    public String getRemark() {
        return this.remark;
    }

    @Generated
    public LocalDateTime getOperTime() {
        return this.operTime;
    }

    @Generated
    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    @Generated
    public String getCreateBy() {
        return this.createBy;
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
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Generated
    public void setOperType(Integer operType) {
        this.operType = operType;
    }

    @Generated
    public void setIntegralNum(Integer integralNum) {
        this.integralNum = integralNum;
    }

    @Generated
    public void setSourceUserId(Long sourceUserId) {
        this.sourceUserId = sourceUserId;
    }

    @Generated
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    @Generated
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Generated
    public void setOperTime(LocalDateTime operTime) {
        this.operTime = operTime;
    }

    @Generated
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Generated
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Generated
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    @Generated
    public String toString() {
        return "TbIntegralFlow(id=" + this.getId() + ", userId=" + this.getUserId() + ", operType=" + this.getOperType() + ", integralNum=" + this.getIntegralNum() + ", sourceUserId=" + this.getSourceUserId() + ", businessId=" + this.getBusinessId() + ", remark=" + this.getRemark() + ", operTime=" + String.valueOf(this.getOperTime()) + ", createTime=" + String.valueOf(this.getCreateTime()) + ", createBy=" + this.getCreateBy() + ", delFlag=" + this.getDelFlag() + ")";
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TbIntegralFlow)) {
            return false;
        }
        TbIntegralFlow other = (TbIntegralFlow)o;
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
        Long this$userId = this.getUserId();
        Long other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Integer this$operType = this.getOperType();
        Integer other$operType = other.getOperType();
        if (this$operType == null ? other$operType != null : !((Object)this$operType).equals(other$operType)) {
            return false;
        }
        Integer this$integralNum = this.getIntegralNum();
        Integer other$integralNum = other.getIntegralNum();
        if (this$integralNum == null ? other$integralNum != null : !((Object)this$integralNum).equals(other$integralNum)) {
            return false;
        }
        Long this$sourceUserId = this.getSourceUserId();
        Long other$sourceUserId = other.getSourceUserId();
        if (this$sourceUserId == null ? other$sourceUserId != null : !((Object)this$sourceUserId).equals(other$sourceUserId)) {
            return false;
        }
        Integer this$delFlag = this.getDelFlag();
        Integer other$delFlag = other.getDelFlag();
        if (this$delFlag == null ? other$delFlag != null : !((Object)this$delFlag).equals(other$delFlag)) {
            return false;
        }
        String this$businessId = this.getBusinessId();
        String other$businessId = other.getBusinessId();
        if (this$businessId == null ? other$businessId != null : !this$businessId.equals(other$businessId)) {
            return false;
        }
        String this$remark = this.getRemark();
        String other$remark = other.getRemark();
        if (this$remark == null ? other$remark != null : !this$remark.equals(other$remark)) {
            return false;
        }
        LocalDateTime this$operTime = this.getOperTime();
        LocalDateTime other$operTime = other.getOperTime();
        if (this$operTime == null ? other$operTime != null : !((Object)this$operTime).equals(other$operTime)) {
            return false;
        }
        LocalDateTime this$createTime = this.getCreateTime();
        LocalDateTime other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
            return false;
        }
        String this$createBy = this.getCreateBy();
        String other$createBy = other.getCreateBy();
        return !(this$createBy == null ? other$createBy != null : !this$createBy.equals(other$createBy));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof TbIntegralFlow;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Long $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $operType = this.getOperType();
        result = result * 59 + ($operType == null ? 43 : ((Object)$operType).hashCode());
        Integer $integralNum = this.getIntegralNum();
        result = result * 59 + ($integralNum == null ? 43 : ((Object)$integralNum).hashCode());
        Long $sourceUserId = this.getSourceUserId();
        result = result * 59 + ($sourceUserId == null ? 43 : ((Object)$sourceUserId).hashCode());
        Integer $delFlag = this.getDelFlag();
        result = result * 59 + ($delFlag == null ? 43 : ((Object)$delFlag).hashCode());
        String $businessId = this.getBusinessId();
        result = result * 59 + ($businessId == null ? 43 : $businessId.hashCode());
        String $remark = this.getRemark();
        result = result * 59 + ($remark == null ? 43 : $remark.hashCode());
        LocalDateTime $operTime = this.getOperTime();
        result = result * 59 + ($operTime == null ? 43 : ((Object)$operTime).hashCode());
        LocalDateTime $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        String $createBy = this.getCreateBy();
        result = result * 59 + ($createBy == null ? 43 : $createBy.hashCode());
        return result;
    }
}

