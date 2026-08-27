/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.dto;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.dingyangmall.common.annotation.Excel;
import com.dingyangmall.mall.dto.PlaceOrderGoodsDTO;
import com.dingyangmall.mall.entity.ShoppingCart;
import java.util.List;
import lombok.Generated;

public class PlaceOrderDTO
extends Model<ShoppingCart> {
    private static final long serialVersionUID = 1L;
    @Excel(name="\u652f\u4ed8\u65b9\u5f0f1\u3001\u8d27\u5230\u4ed8\u6b3e\uff1b2\u3001\u5728\u7ebf\u652f\u4ed8")
    private String paymentWay;
    @Excel(name="\u914d\u9001\u65b9\u5f0f")
    private String deliveryWay;
    @Excel(name="\u4ed8\u6b3e\u65b9\u5f0f")
    private String paymentType;
    @Excel(name="\u4e70\u5bb6\u7559\u8a00")
    private String userMessage;
    @Excel(name="\u7528\u6237id")
    private String userId;
    @Excel(name="\u7528\u6237\u6536\u8d27\u5730\u5740ID")
    private String userAddressId;
    @Excel(name="\u8ba2\u5355\u7c7b\u578b")
    private String orderType;
    @Excel(name="\u5546\u54c1")
    private List<PlaceOrderGoodsDTO> skus;
    private Boolean isPay;
    private Integer payIntegral;

    @Generated
    public PlaceOrderDTO() {
    }

    @Generated
    public String getPaymentWay() {
        return this.paymentWay;
    }

    @Generated
    public String getDeliveryWay() {
        return this.deliveryWay;
    }

    @Generated
    public String getPaymentType() {
        return this.paymentType;
    }

    @Generated
    public String getUserMessage() {
        return this.userMessage;
    }

    @Generated
    public String getUserId() {
        return this.userId;
    }

    @Generated
    public String getUserAddressId() {
        return this.userAddressId;
    }

    @Generated
    public String getOrderType() {
        return this.orderType;
    }

    @Generated
    public List<PlaceOrderGoodsDTO> getSkus() {
        return this.skus;
    }

    @Generated
    public Boolean getIsPay() {
        return this.isPay;
    }

    @Generated
    public Integer getPayIntegral() {
        return this.payIntegral;
    }

    @Generated
    public void setPaymentWay(String paymentWay) {
        this.paymentWay = paymentWay;
    }

    @Generated
    public void setDeliveryWay(String deliveryWay) {
        this.deliveryWay = deliveryWay;
    }

    @Generated
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    @Generated
    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    @Generated
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Generated
    public void setUserAddressId(String userAddressId) {
        this.userAddressId = userAddressId;
    }

    @Generated
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    @Generated
    public void setSkus(List<PlaceOrderGoodsDTO> skus) {
        this.skus = skus;
    }

    @Generated
    public void setIsPay(Boolean isPay) {
        this.isPay = isPay;
    }

    @Generated
    public void setPayIntegral(Integer payIntegral) {
        this.payIntegral = payIntegral;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlaceOrderDTO)) {
            return false;
        }
        PlaceOrderDTO other = (PlaceOrderDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Boolean this$isPay = this.getIsPay();
        Boolean other$isPay = other.getIsPay();
        if (this$isPay == null ? other$isPay != null : !((Object)this$isPay).equals(other$isPay)) {
            return false;
        }
        Integer this$payIntegral = this.getPayIntegral();
        Integer other$payIntegral = other.getPayIntegral();
        if (this$payIntegral == null ? other$payIntegral != null : !((Object)this$payIntegral).equals(other$payIntegral)) {
            return false;
        }
        String this$paymentWay = this.getPaymentWay();
        String other$paymentWay = other.getPaymentWay();
        if (this$paymentWay == null ? other$paymentWay != null : !this$paymentWay.equals(other$paymentWay)) {
            return false;
        }
        String this$deliveryWay = this.getDeliveryWay();
        String other$deliveryWay = other.getDeliveryWay();
        if (this$deliveryWay == null ? other$deliveryWay != null : !this$deliveryWay.equals(other$deliveryWay)) {
            return false;
        }
        String this$paymentType = this.getPaymentType();
        String other$paymentType = other.getPaymentType();
        if (this$paymentType == null ? other$paymentType != null : !this$paymentType.equals(other$paymentType)) {
            return false;
        }
        String this$userMessage = this.getUserMessage();
        String other$userMessage = other.getUserMessage();
        if (this$userMessage == null ? other$userMessage != null : !this$userMessage.equals(other$userMessage)) {
            return false;
        }
        String this$userId = this.getUserId();
        String other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !this$userId.equals(other$userId)) {
            return false;
        }
        String this$userAddressId = this.getUserAddressId();
        String other$userAddressId = other.getUserAddressId();
        if (this$userAddressId == null ? other$userAddressId != null : !this$userAddressId.equals(other$userAddressId)) {
            return false;
        }
        String this$orderType = this.getOrderType();
        String other$orderType = other.getOrderType();
        if (this$orderType == null ? other$orderType != null : !this$orderType.equals(other$orderType)) {
            return false;
        }
        List<PlaceOrderGoodsDTO> this$skus = this.getSkus();
        List<PlaceOrderGoodsDTO> other$skus = other.getSkus();
        return !(this$skus == null ? other$skus != null : !((Object)this$skus).equals(other$skus));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof PlaceOrderDTO;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Boolean $isPay = this.getIsPay();
        result = result * 59 + ($isPay == null ? 43 : ((Object)$isPay).hashCode());
        Integer $payIntegral = this.getPayIntegral();
        result = result * 59 + ($payIntegral == null ? 43 : ((Object)$payIntegral).hashCode());
        String $paymentWay = this.getPaymentWay();
        result = result * 59 + ($paymentWay == null ? 43 : $paymentWay.hashCode());
        String $deliveryWay = this.getDeliveryWay();
        result = result * 59 + ($deliveryWay == null ? 43 : $deliveryWay.hashCode());
        String $paymentType = this.getPaymentType();
        result = result * 59 + ($paymentType == null ? 43 : $paymentType.hashCode());
        String $userMessage = this.getUserMessage();
        result = result * 59 + ($userMessage == null ? 43 : $userMessage.hashCode());
        String $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : $userId.hashCode());
        String $userAddressId = this.getUserAddressId();
        result = result * 59 + ($userAddressId == null ? 43 : $userAddressId.hashCode());
        String $orderType = this.getOrderType();
        result = result * 59 + ($orderType == null ? 43 : $orderType.hashCode());
        List<PlaceOrderGoodsDTO> $skus = this.getSkus();
        result = result * 59 + ($skus == null ? 43 : ((Object)$skus).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "PlaceOrderDTO(paymentWay=" + this.getPaymentWay() + ", deliveryWay=" + this.getDeliveryWay() + ", paymentType=" + this.getPaymentType() + ", userMessage=" + this.getUserMessage() + ", userId=" + this.getUserId() + ", userAddressId=" + this.getUserAddressId() + ", orderType=" + this.getOrderType() + ", skus=" + String.valueOf(this.getSkus()) + ", isPay=" + this.getIsPay() + ", payIntegral=" + this.getPayIntegral() + ")";
    }
}

