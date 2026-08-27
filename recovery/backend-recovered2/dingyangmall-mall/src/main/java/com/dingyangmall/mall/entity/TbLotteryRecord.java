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

@TableName(value="tb_lottery_record")
public class TbLotteryRecord
extends Model<TbLotteryRecord> {
    private static final long serialVersionUID = 1L;
    @TableId(type=IdType.AUTO)
    private Long id;
    private Long userId;
    private String isWin;
    private Long prizeId;
    private String prizeName;
    private String prizeType;
    private Integer costPoints;
    private String grantStatus;
    private String businessId;
    private LocalDateTime createTime;
    private Long configId;

    @Generated
    public TbLotteryRecord() {
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
    public String getIsWin() {
        return this.isWin;
    }

    @Generated
    public Long getPrizeId() {
        return this.prizeId;
    }

    @Generated
    public String getPrizeName() {
        return this.prizeName;
    }

    @Generated
    public String getPrizeType() {
        return this.prizeType;
    }

    @Generated
    public Integer getCostPoints() {
        return this.costPoints;
    }

    @Generated
    public String getGrantStatus() {
        return this.grantStatus;
    }

    @Generated
    public String getBusinessId() {
        return this.businessId;
    }

    @Generated
    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    @Generated
    public Long getConfigId() {
        return this.configId;
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
    public void setIsWin(String isWin) {
        this.isWin = isWin;
    }

    @Generated
    public void setPrizeId(Long prizeId) {
        this.prizeId = prizeId;
    }

    @Generated
    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    @Generated
    public void setPrizeType(String prizeType) {
        this.prizeType = prizeType;
    }

    @Generated
    public void setCostPoints(Integer costPoints) {
        this.costPoints = costPoints;
    }

    @Generated
    public void setGrantStatus(String grantStatus) {
        this.grantStatus = grantStatus;
    }

    @Generated
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    @Generated
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Generated
    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    @Generated
    public String toString() {
        return "TbLotteryRecord(id=" + this.getId() + ", userId=" + this.getUserId() + ", isWin=" + this.getIsWin() + ", prizeId=" + this.getPrizeId() + ", prizeName=" + this.getPrizeName() + ", prizeType=" + this.getPrizeType() + ", costPoints=" + this.getCostPoints() + ", grantStatus=" + this.getGrantStatus() + ", businessId=" + this.getBusinessId() + ", createTime=" + String.valueOf(this.getCreateTime()) + ", configId=" + this.getConfigId() + ")";
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TbLotteryRecord)) {
            return false;
        }
        TbLotteryRecord other = (TbLotteryRecord)o;
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
        Long this$prizeId = this.getPrizeId();
        Long other$prizeId = other.getPrizeId();
        if (this$prizeId == null ? other$prizeId != null : !((Object)this$prizeId).equals(other$prizeId)) {
            return false;
        }
        Integer this$costPoints = this.getCostPoints();
        Integer other$costPoints = other.getCostPoints();
        if (this$costPoints == null ? other$costPoints != null : !((Object)this$costPoints).equals(other$costPoints)) {
            return false;
        }
        Long this$configId = this.getConfigId();
        Long other$configId = other.getConfigId();
        if (this$configId == null ? other$configId != null : !((Object)this$configId).equals(other$configId)) {
            return false;
        }
        String this$isWin = this.getIsWin();
        String other$isWin = other.getIsWin();
        if (this$isWin == null ? other$isWin != null : !this$isWin.equals(other$isWin)) {
            return false;
        }
        String this$prizeName = this.getPrizeName();
        String other$prizeName = other.getPrizeName();
        if (this$prizeName == null ? other$prizeName != null : !this$prizeName.equals(other$prizeName)) {
            return false;
        }
        String this$prizeType = this.getPrizeType();
        String other$prizeType = other.getPrizeType();
        if (this$prizeType == null ? other$prizeType != null : !this$prizeType.equals(other$prizeType)) {
            return false;
        }
        String this$grantStatus = this.getGrantStatus();
        String other$grantStatus = other.getGrantStatus();
        if (this$grantStatus == null ? other$grantStatus != null : !this$grantStatus.equals(other$grantStatus)) {
            return false;
        }
        String this$businessId = this.getBusinessId();
        String other$businessId = other.getBusinessId();
        if (this$businessId == null ? other$businessId != null : !this$businessId.equals(other$businessId)) {
            return false;
        }
        LocalDateTime this$createTime = this.getCreateTime();
        LocalDateTime other$createTime = other.getCreateTime();
        return !(this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof TbLotteryRecord;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Long $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Long $prizeId = this.getPrizeId();
        result = result * 59 + ($prizeId == null ? 43 : ((Object)$prizeId).hashCode());
        Integer $costPoints = this.getCostPoints();
        result = result * 59 + ($costPoints == null ? 43 : ((Object)$costPoints).hashCode());
        Long $configId = this.getConfigId();
        result = result * 59 + ($configId == null ? 43 : ((Object)$configId).hashCode());
        String $isWin = this.getIsWin();
        result = result * 59 + ($isWin == null ? 43 : $isWin.hashCode());
        String $prizeName = this.getPrizeName();
        result = result * 59 + ($prizeName == null ? 43 : $prizeName.hashCode());
        String $prizeType = this.getPrizeType();
        result = result * 59 + ($prizeType == null ? 43 : $prizeType.hashCode());
        String $grantStatus = this.getGrantStatus();
        result = result * 59 + ($grantStatus == null ? 43 : $grantStatus.hashCode());
        String $businessId = this.getBusinessId();
        result = result * 59 + ($businessId == null ? 43 : $businessId.hashCode());
        LocalDateTime $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        return result;
    }
}

