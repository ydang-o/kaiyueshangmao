/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.dingyangmall.common.annotation.Excel;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Generated;

@TableName(value="order_item")
public class OrderItem
extends Model<OrderItem> {
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
    @Excel(name="\u8ba2\u5355\u7f16\u53f7")
    private String orderId;
    @Excel(name="\u5546\u54c1Id")
    private String spuId;
    @Excel(name="\u5546\u54c1\u540d")
    private String spuName;
    @Excel(name="\u56fe\u7247")
    private String picUrl;
    @Excel(name="\u5546\u54c1\u6570\u91cf")
    private Integer quantity;
    @Excel(name="\u8d2d\u4e70\u5355\u4ef7")
    private BigDecimal salesPrice;
    @Excel(name="\u8fd0\u8d39\u91d1\u989d")
    private BigDecimal freightPrice;
    @Excel(name="\u652f\u4ed8\u91d1\u989d\uff08\u8d2d\u4e70\u5355\u4ef7*\u5546\u54c1\u6570\u91cf+\u8fd0\u8d39\u91d1\u989d\uff09")
    private BigDecimal paymentPrice;
    @Excel(name="\u5907\u6ce8")
    private String remark;
    @Excel(name="\u72b6\u60010\uff1a\u6b63\u5e38\uff1b1\uff1a\u9000\u6b3e\u4e2d\uff1b2:\u62d2\u7edd\u9000\u6b3e\uff1b3\uff1a\u540c\u610f\u9000\u6b3e")
    private String status;
    @Excel(name="\u662f\u5426\u9000\u6b3e0:\u5426 1\uff1a\u662f")
    private String isRefund;

    @Generated
    public OrderItem() {
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
    public String getOrderId() {
        return this.orderId;
    }

    @Generated
    public String getSpuId() {
        return this.spuId;
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
    public Integer getQuantity() {
        return this.quantity;
    }

    @Generated
    public BigDecimal getSalesPrice() {
        return this.salesPrice;
    }

    @Generated
    public BigDecimal getFreightPrice() {
        return this.freightPrice;
    }

    @Generated
    public BigDecimal getPaymentPrice() {
        return this.paymentPrice;
    }

    @Generated
    public String getRemark() {
        return this.remark;
    }

    @Generated
    public String getStatus() {
        return this.status;
    }

    @Generated
    public String getIsRefund() {
        return this.isRefund;
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
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Generated
    public void setSpuId(String spuId) {
        this.spuId = spuId;
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
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Generated
    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    @Generated
    public void setFreightPrice(BigDecimal freightPrice) {
        this.freightPrice = freightPrice;
    }

    @Generated
    public void setPaymentPrice(BigDecimal paymentPrice) {
        this.paymentPrice = paymentPrice;
    }

    @Generated
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Generated
    public void setStatus(String status) {
        this.status = status;
    }

    @Generated
    public void setIsRefund(String isRefund) {
        this.isRefund = isRefund;
    }

    @Generated
    public String toString() {
        return "OrderItem(id=" + this.getId() + ", delFlag=" + this.getDelFlag() + ", createTime=" + String.valueOf(this.getCreateTime()) + ", updateTime=" + String.valueOf(this.getUpdateTime()) + ", orderId=" + this.getOrderId() + ", spuId=" + this.getSpuId() + ", spuName=" + this.getSpuName() + ", picUrl=" + this.getPicUrl() + ", quantity=" + this.getQuantity() + ", salesPrice=" + String.valueOf(this.getSalesPrice()) + ", freightPrice=" + String.valueOf(this.getFreightPrice()) + ", paymentPrice=" + String.valueOf(this.getPaymentPrice()) + ", remark=" + this.getRemark() + ", status=" + this.getStatus() + ", isRefund=" + this.getIsRefund() + ")";
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OrderItem)) {
            return false;
        }
        OrderItem other = (OrderItem)o;
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
        String this$orderId = this.getOrderId();
        String other$orderId = other.getOrderId();
        if (this$orderId == null ? other$orderId != null : !this$orderId.equals(other$orderId)) {
            return false;
        }
        String this$spuId = this.getSpuId();
        String other$spuId = other.getSpuId();
        if (this$spuId == null ? other$spuId != null : !this$spuId.equals(other$spuId)) {
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
        BigDecimal this$salesPrice = this.getSalesPrice();
        BigDecimal other$salesPrice = other.getSalesPrice();
        if (this$salesPrice == null ? other$salesPrice != null : !((Object)this$salesPrice).equals(other$salesPrice)) {
            return false;
        }
        BigDecimal this$freightPrice = this.getFreightPrice();
        BigDecimal other$freightPrice = other.getFreightPrice();
        if (this$freightPrice == null ? other$freightPrice != null : !((Object)this$freightPrice).equals(other$freightPrice)) {
            return false;
        }
        BigDecimal this$paymentPrice = this.getPaymentPrice();
        BigDecimal other$paymentPrice = other.getPaymentPrice();
        if (this$paymentPrice == null ? other$paymentPrice != null : !((Object)this$paymentPrice).equals(other$paymentPrice)) {
            return false;
        }
        String this$remark = this.getRemark();
        String other$remark = other.getRemark();
        if (this$remark == null ? other$remark != null : !this$remark.equals(other$remark)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        String this$isRefund = this.getIsRefund();
        String other$isRefund = other.getIsRefund();
        return !(this$isRefund == null ? other$isRefund != null : !this$isRefund.equals(other$isRefund));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof OrderItem;
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
        String $orderId = this.getOrderId();
        result = result * 59 + ($orderId == null ? 43 : $orderId.hashCode());
        String $spuId = this.getSpuId();
        result = result * 59 + ($spuId == null ? 43 : $spuId.hashCode());
        String $spuName = this.getSpuName();
        result = result * 59 + ($spuName == null ? 43 : $spuName.hashCode());
        String $picUrl = this.getPicUrl();
        result = result * 59 + ($picUrl == null ? 43 : $picUrl.hashCode());
        BigDecimal $salesPrice = this.getSalesPrice();
        result = result * 59 + ($salesPrice == null ? 43 : ((Object)$salesPrice).hashCode());
        BigDecimal $freightPrice = this.getFreightPrice();
        result = result * 59 + ($freightPrice == null ? 43 : ((Object)$freightPrice).hashCode());
        BigDecimal $paymentPrice = this.getPaymentPrice();
        result = result * 59 + ($paymentPrice == null ? 43 : ((Object)$paymentPrice).hashCode());
        String $remark = this.getRemark();
        result = result * 59 + ($remark == null ? 43 : $remark.hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        String $isRefund = this.getIsRefund();
        result = result * 59 + ($isRefund == null ? 43 : $isRefund.hashCode());
        return result;
    }
}

