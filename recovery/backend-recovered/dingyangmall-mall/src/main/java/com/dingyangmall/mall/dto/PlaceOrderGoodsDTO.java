/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.dto;

import com.dingyangmall.common.annotation.Excel;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Generated;

public class PlaceOrderGoodsDTO
implements Serializable {
    private static final long serialVersionUID = 1L;
    @Excel(name="\u5546\u54c1Id")
    private String spuId;
    @Excel(name="\u6570\u91cf")
    private Integer quantity;
    @Excel(name="\u652f\u4ed8\u91d1\u989d")
    private BigDecimal paymentPrice;
    @Excel(name="\u8fd0\u8d39\u91d1\u989d")
    private BigDecimal freightPrice;

    @Generated
    public PlaceOrderGoodsDTO() {
    }

    @Generated
    public String getSpuId() {
        return this.spuId;
    }

    @Generated
    public Integer getQuantity() {
        return this.quantity;
    }

    @Generated
    public BigDecimal getPaymentPrice() {
        return this.paymentPrice;
    }

    @Generated
    public BigDecimal getFreightPrice() {
        return this.freightPrice;
    }

    @Generated
    public void setSpuId(String spuId) {
        this.spuId = spuId;
    }

    @Generated
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Generated
    public void setPaymentPrice(BigDecimal paymentPrice) {
        this.paymentPrice = paymentPrice;
    }

    @Generated
    public void setFreightPrice(BigDecimal freightPrice) {
        this.freightPrice = freightPrice;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlaceOrderGoodsDTO)) {
            return false;
        }
        PlaceOrderGoodsDTO other = (PlaceOrderGoodsDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$quantity = this.getQuantity();
        Integer other$quantity = other.getQuantity();
        if (this$quantity == null ? other$quantity != null : !((Object)this$quantity).equals(other$quantity)) {
            return false;
        }
        String this$spuId = this.getSpuId();
        String other$spuId = other.getSpuId();
        if (this$spuId == null ? other$spuId != null : !this$spuId.equals(other$spuId)) {
            return false;
        }
        BigDecimal this$paymentPrice = this.getPaymentPrice();
        BigDecimal other$paymentPrice = other.getPaymentPrice();
        if (this$paymentPrice == null ? other$paymentPrice != null : !((Object)this$paymentPrice).equals(other$paymentPrice)) {
            return false;
        }
        BigDecimal this$freightPrice = this.getFreightPrice();
        BigDecimal other$freightPrice = other.getFreightPrice();
        return !(this$freightPrice == null ? other$freightPrice != null : !((Object)this$freightPrice).equals(other$freightPrice));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof PlaceOrderGoodsDTO;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $quantity = this.getQuantity();
        result = result * 59 + ($quantity == null ? 43 : ((Object)$quantity).hashCode());
        String $spuId = this.getSpuId();
        result = result * 59 + ($spuId == null ? 43 : $spuId.hashCode());
        BigDecimal $paymentPrice = this.getPaymentPrice();
        result = result * 59 + ($paymentPrice == null ? 43 : ((Object)$paymentPrice).hashCode());
        BigDecimal $freightPrice = this.getFreightPrice();
        result = result * 59 + ($freightPrice == null ? 43 : ((Object)$freightPrice).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "PlaceOrderGoodsDTO(spuId=" + this.getSpuId() + ", quantity=" + this.getQuantity() + ", paymentPrice=" + String.valueOf(this.getPaymentPrice()) + ", freightPrice=" + String.valueOf(this.getFreightPrice()) + ")";
    }
}

