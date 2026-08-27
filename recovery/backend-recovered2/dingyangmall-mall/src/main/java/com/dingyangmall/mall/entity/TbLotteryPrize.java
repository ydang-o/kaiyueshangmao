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

@TableName(value="tb_lottery_prize")
public class TbLotteryPrize
extends Model<TbLotteryPrize> {
    private static final long serialVersionUID = 1L;
    @TableId(type=IdType.AUTO)
    private Long id;
    private Long configId;
    private String prizeType;
    private String goodsId;
    private Integer pointAmount;
    private String prizeName;
    private String prizePic;
    private Double probability;
    private Integer sortOrder;
    private LocalDateTime createTime;

    @Generated
    public TbLotteryPrize() {
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public Long getConfigId() {
        return this.configId;
    }

    @Generated
    public String getPrizeType() {
        return this.prizeType;
    }

    @Generated
    public String getGoodsId() {
        return this.goodsId;
    }

    @Generated
    public Integer getPointAmount() {
        return this.pointAmount;
    }

    @Generated
    public String getPrizeName() {
        return this.prizeName;
    }

    @Generated
    public String getPrizePic() {
        return this.prizePic;
    }

    @Generated
    public Double getProbability() {
        return this.probability;
    }

    @Generated
    public Integer getSortOrder() {
        return this.sortOrder;
    }

    @Generated
    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    @Generated
    public void setPrizeType(String prizeType) {
        this.prizeType = prizeType;
    }

    @Generated
    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    @Generated
    public void setPointAmount(Integer pointAmount) {
        this.pointAmount = pointAmount;
    }

    @Generated
    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    @Generated
    public void setPrizePic(String prizePic) {
        this.prizePic = prizePic;
    }

    @Generated
    public void setProbability(Double probability) {
        this.probability = probability;
    }

    @Generated
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Generated
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Generated
    public String toString() {
        return "TbLotteryPrize(id=" + this.getId() + ", configId=" + this.getConfigId() + ", prizeType=" + this.getPrizeType() + ", goodsId=" + this.getGoodsId() + ", pointAmount=" + this.getPointAmount() + ", prizeName=" + this.getPrizeName() + ", prizePic=" + this.getPrizePic() + ", probability=" + this.getProbability() + ", sortOrder=" + this.getSortOrder() + ", createTime=" + String.valueOf(this.getCreateTime()) + ")";
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TbLotteryPrize)) {
            return false;
        }
        TbLotteryPrize other = (TbLotteryPrize)o;
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
        Long this$configId = this.getConfigId();
        Long other$configId = other.getConfigId();
        if (this$configId == null ? other$configId != null : !((Object)this$configId).equals(other$configId)) {
            return false;
        }
        Integer this$pointAmount = this.getPointAmount();
        Integer other$pointAmount = other.getPointAmount();
        if (this$pointAmount == null ? other$pointAmount != null : !((Object)this$pointAmount).equals(other$pointAmount)) {
            return false;
        }
        Double this$probability = this.getProbability();
        Double other$probability = other.getProbability();
        if (this$probability == null ? other$probability != null : !((Object)this$probability).equals(other$probability)) {
            return false;
        }
        Integer this$sortOrder = this.getSortOrder();
        Integer other$sortOrder = other.getSortOrder();
        if (this$sortOrder == null ? other$sortOrder != null : !((Object)this$sortOrder).equals(other$sortOrder)) {
            return false;
        }
        String this$prizeType = this.getPrizeType();
        String other$prizeType = other.getPrizeType();
        if (this$prizeType == null ? other$prizeType != null : !this$prizeType.equals(other$prizeType)) {
            return false;
        }
        String this$goodsId = this.getGoodsId();
        String other$goodsId = other.getGoodsId();
        if (this$goodsId == null ? other$goodsId != null : !this$goodsId.equals(other$goodsId)) {
            return false;
        }
        String this$prizeName = this.getPrizeName();
        String other$prizeName = other.getPrizeName();
        if (this$prizeName == null ? other$prizeName != null : !this$prizeName.equals(other$prizeName)) {
            return false;
        }
        String this$prizePic = this.getPrizePic();
        String other$prizePic = other.getPrizePic();
        if (this$prizePic == null ? other$prizePic != null : !this$prizePic.equals(other$prizePic)) {
            return false;
        }
        LocalDateTime this$createTime = this.getCreateTime();
        LocalDateTime other$createTime = other.getCreateTime();
        return !(this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof TbLotteryPrize;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Long $configId = this.getConfigId();
        result = result * 59 + ($configId == null ? 43 : ((Object)$configId).hashCode());
        Integer $pointAmount = this.getPointAmount();
        result = result * 59 + ($pointAmount == null ? 43 : ((Object)$pointAmount).hashCode());
        Double $probability = this.getProbability();
        result = result * 59 + ($probability == null ? 43 : ((Object)$probability).hashCode());
        Integer $sortOrder = this.getSortOrder();
        result = result * 59 + ($sortOrder == null ? 43 : ((Object)$sortOrder).hashCode());
        String $prizeType = this.getPrizeType();
        result = result * 59 + ($prizeType == null ? 43 : $prizeType.hashCode());
        String $goodsId = this.getGoodsId();
        result = result * 59 + ($goodsId == null ? 43 : $goodsId.hashCode());
        String $prizeName = this.getPrizeName();
        result = result * 59 + ($prizeName == null ? 43 : $prizeName.hashCode());
        String $prizePic = this.getPrizePic();
        result = result * 59 + ($prizePic == null ? 43 : $prizePic.hashCode());
        LocalDateTime $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        return result;
    }
}

