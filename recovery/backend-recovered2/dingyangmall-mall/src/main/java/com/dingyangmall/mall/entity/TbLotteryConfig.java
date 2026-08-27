/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.dingyangmall.mall.entity.TbLotteryPrize;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Generated;

@TableName(value="tb_lottery_config")
public class TbLotteryConfig
extends Model<TbLotteryConfig> {
    private static final long serialVersionUID = 1L;
    @TableId(type=IdType.AUTO)
    private Long id;
    private String status;
    private Integer costPoints;
    private Integer dailyLimit;
    private Double noPrizeProbability;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;
    private String updateBy;
    @TableField(exist=false)
    private List<TbLotteryPrize> prizeList;

    @Generated
    public TbLotteryConfig() {
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getStatus() {
        return this.status;
    }

    @Generated
    public Integer getCostPoints() {
        return this.costPoints;
    }

    @Generated
    public Integer getDailyLimit() {
        return this.dailyLimit;
    }

    @Generated
    public Double getNoPrizeProbability() {
        return this.noPrizeProbability;
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
    public String getCreateBy() {
        return this.createBy;
    }

    @Generated
    public String getUpdateBy() {
        return this.updateBy;
    }

    @Generated
    public List<TbLotteryPrize> getPrizeList() {
        return this.prizeList;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setStatus(String status) {
        this.status = status;
    }

    @Generated
    public void setCostPoints(Integer costPoints) {
        this.costPoints = costPoints;
    }

    @Generated
    public void setDailyLimit(Integer dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    @Generated
    public void setNoPrizeProbability(Double noPrizeProbability) {
        this.noPrizeProbability = noPrizeProbability;
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
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Generated
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Generated
    public void setPrizeList(List<TbLotteryPrize> prizeList) {
        this.prizeList = prizeList;
    }

    @Generated
    public String toString() {
        return "TbLotteryConfig(id=" + this.getId() + ", status=" + this.getStatus() + ", costPoints=" + this.getCostPoints() + ", dailyLimit=" + this.getDailyLimit() + ", noPrizeProbability=" + this.getNoPrizeProbability() + ", createTime=" + String.valueOf(this.getCreateTime()) + ", updateTime=" + String.valueOf(this.getUpdateTime()) + ", createBy=" + this.getCreateBy() + ", updateBy=" + this.getUpdateBy() + ", prizeList=" + String.valueOf(this.getPrizeList()) + ")";
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TbLotteryConfig)) {
            return false;
        }
        TbLotteryConfig other = (TbLotteryConfig)o;
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
        Integer this$costPoints = this.getCostPoints();
        Integer other$costPoints = other.getCostPoints();
        if (this$costPoints == null ? other$costPoints != null : !((Object)this$costPoints).equals(other$costPoints)) {
            return false;
        }
        Integer this$dailyLimit = this.getDailyLimit();
        Integer other$dailyLimit = other.getDailyLimit();
        if (this$dailyLimit == null ? other$dailyLimit != null : !((Object)this$dailyLimit).equals(other$dailyLimit)) {
            return false;
        }
        Double this$noPrizeProbability = this.getNoPrizeProbability();
        Double other$noPrizeProbability = other.getNoPrizeProbability();
        if (this$noPrizeProbability == null ? other$noPrizeProbability != null : !((Object)this$noPrizeProbability).equals(other$noPrizeProbability)) {
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
        String this$createBy = this.getCreateBy();
        String other$createBy = other.getCreateBy();
        if (this$createBy == null ? other$createBy != null : !this$createBy.equals(other$createBy)) {
            return false;
        }
        String this$updateBy = this.getUpdateBy();
        String other$updateBy = other.getUpdateBy();
        if (this$updateBy == null ? other$updateBy != null : !this$updateBy.equals(other$updateBy)) {
            return false;
        }
        List<TbLotteryPrize> this$prizeList = this.getPrizeList();
        List<TbLotteryPrize> other$prizeList = other.getPrizeList();
        return !(this$prizeList == null ? other$prizeList != null : !((Object)this$prizeList).equals(other$prizeList));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof TbLotteryConfig;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $costPoints = this.getCostPoints();
        result = result * 59 + ($costPoints == null ? 43 : ((Object)$costPoints).hashCode());
        Integer $dailyLimit = this.getDailyLimit();
        result = result * 59 + ($dailyLimit == null ? 43 : ((Object)$dailyLimit).hashCode());
        Double $noPrizeProbability = this.getNoPrizeProbability();
        result = result * 59 + ($noPrizeProbability == null ? 43 : ((Object)$noPrizeProbability).hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        LocalDateTime $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        LocalDateTime $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        String $createBy = this.getCreateBy();
        result = result * 59 + ($createBy == null ? 43 : $createBy.hashCode());
        String $updateBy = this.getUpdateBy();
        result = result * 59 + ($updateBy == null ? 43 : $updateBy.hashCode());
        List<TbLotteryPrize> $prizeList = this.getPrizeList();
        result = result * 59 + ($prizeList == null ? 43 : ((Object)$prizeList).hashCode());
        return result;
    }
}

