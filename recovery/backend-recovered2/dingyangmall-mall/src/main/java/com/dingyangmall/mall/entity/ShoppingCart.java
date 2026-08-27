/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.dingyangmall.common.annotation.Excel;
import com.dingyangmall.mall.entity.GoodsSpu;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Generated;

@TableName(value="shopping_cart")
public class ShoppingCart
extends Model<ShoppingCart> {
    private static final long serialVersionUID = 1L;
    @Excel(name="PK")
    @TableId(type=IdType.ASSIGN_ID)
    private String id;
    @Excel(name="\u903b\u8f91\u5220\u9664\u6807\u8bb0")
    private String delFlag;
    @Excel(name="\u521b\u5efa\u65f6\u95f4")
    private LocalDateTime createTime;
    @Excel(name="\u6700\u540e\u66f4\u65b0\u65f6\u95f4")
    private LocalDateTime updateTime;
    @Excel(name="\u7528\u6237\u7f16\u53f7")
    private String userId;
    @Excel(name="spuId")
    private String spuId;
    @Excel(name="\u52a0\u5165\u65f6\u4ef7\u683c")
    private BigDecimal addPrice;
    @Excel(name="\u5546\u54c1\u8d2d\u4e70\u6570\u91cf")
    private Integer quantity;
    @Excel(name="\u52a0\u5165\u65f6\u7684spu\u540d\u5b57")
    private String spuName;
    @Excel(name="\u56fe\u7247")
    private String picUrl;
    @TableField(exist=false)
    private GoodsSpu goodsSpu;

    @Generated
    public ShoppingCart() {
    }

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getDelFlag() {
        return this.delFlag;
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
    public String getUserId() {
        return this.userId;
    }

    @Generated
    public String getSpuId() {
        return this.spuId;
    }

    @Generated
    public BigDecimal getAddPrice() {
        return this.addPrice;
    }

    @Generated
    public Integer getQuantity() {
        return this.quantity;
    }

    @Generated
    public String getSpuName() {
        return this.spuName;
    }

    @Generated
    public String getPicUrl() {
        return this.picUrl;
    }

    @Generated
    public GoodsSpu getGoodsSpu() {
        return this.goodsSpu;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
    }

    @Generated
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
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
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Generated
    public void setSpuId(String spuId) {
        this.spuId = spuId;
    }

    @Generated
    public void setAddPrice(BigDecimal addPrice) {
        this.addPrice = addPrice;
    }

    @Generated
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Generated
    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    @Generated
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    @Generated
    public void setGoodsSpu(GoodsSpu goodsSpu) {
        this.goodsSpu = goodsSpu;
    }

    @Generated
    public String toString() {
        return "ShoppingCart(id=" + this.getId() + ", delFlag=" + this.getDelFlag() + ", createTime=" + String.valueOf(this.getCreateTime()) + ", updateTime=" + String.valueOf(this.getUpdateTime()) + ", userId=" + this.getUserId() + ", spuId=" + this.getSpuId() + ", addPrice=" + String.valueOf(this.getAddPrice()) + ", quantity=" + this.getQuantity() + ", spuName=" + this.getSpuName() + ", picUrl=" + this.getPicUrl() + ", goodsSpu=" + String.valueOf(this.getGoodsSpu()) + ")";
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ShoppingCart)) {
            return false;
        }
        ShoppingCart other = (ShoppingCart)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Integer this$quantity = this.getQuantity();
        Integer other$quantity = other.getQuantity();
        if (this$quantity == null ? other$quantity != null : !((Object)this$quantity).equals(other$quantity)) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$delFlag = this.getDelFlag();
        String other$delFlag = other.getDelFlag();
        if (this$delFlag == null ? other$delFlag != null : !this$delFlag.equals(other$delFlag)) {
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
        String this$userId = this.getUserId();
        String other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !this$userId.equals(other$userId)) {
            return false;
        }
        String this$spuId = this.getSpuId();
        String other$spuId = other.getSpuId();
        if (this$spuId == null ? other$spuId != null : !this$spuId.equals(other$spuId)) {
            return false;
        }
        BigDecimal this$addPrice = this.getAddPrice();
        BigDecimal other$addPrice = other.getAddPrice();
        if (this$addPrice == null ? other$addPrice != null : !((Object)this$addPrice).equals(other$addPrice)) {
            return false;
        }
        String this$spuName = this.getSpuName();
        String other$spuName = other.getSpuName();
        if (this$spuName == null ? other$spuName != null : !this$spuName.equals(other$spuName)) {
            return false;
        }
        String this$picUrl = this.getPicUrl();
        String other$picUrl = other.getPicUrl();
        if (this$picUrl == null ? other$picUrl != null : !this$picUrl.equals(other$picUrl)) {
            return false;
        }
        GoodsSpu this$goodsSpu = this.getGoodsSpu();
        GoodsSpu other$goodsSpu = other.getGoodsSpu();
        return !(this$goodsSpu == null ? other$goodsSpu != null : !((Object)this$goodsSpu).equals(other$goodsSpu));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ShoppingCart;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        Integer $quantity = this.getQuantity();
        result = result * 59 + ($quantity == null ? 43 : ((Object)$quantity).hashCode());
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $delFlag = this.getDelFlag();
        result = result * 59 + ($delFlag == null ? 43 : $delFlag.hashCode());
        LocalDateTime $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        LocalDateTime $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        String $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : $userId.hashCode());
        String $spuId = this.getSpuId();
        result = result * 59 + ($spuId == null ? 43 : $spuId.hashCode());
        BigDecimal $addPrice = this.getAddPrice();
        result = result * 59 + ($addPrice == null ? 43 : ((Object)$addPrice).hashCode());
        String $spuName = this.getSpuName();
        result = result * 59 + ($spuName == null ? 43 : $spuName.hashCode());
        String $picUrl = this.getPicUrl();
        result = result * 59 + ($picUrl == null ? 43 : $picUrl.hashCode());
        GoodsSpu $goodsSpu = this.getGoodsSpu();
        result = result * 59 + ($goodsSpu == null ? 43 : ((Object)$goodsSpu).hashCode());
        return result;
    }
}

