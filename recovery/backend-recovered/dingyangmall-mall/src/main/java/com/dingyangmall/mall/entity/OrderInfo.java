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
import com.dingyangmall.mall.entity.OrderItem;
import com.dingyangmall.mall.entity.OrderLogistics;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.enums.OrderInfoEnum;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Generated;
import org.springframework.format.annotation.DateTimeFormat;

@TableName(value="order_info")
public class OrderInfo
extends Model<OrderInfo> {
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
    @Excel(name="\u7528\u6237id")
    private String userId;
    @Excel(name="\u8ba2\u5355\u5355\u53f7")
    private String orderNo;
    @Excel(name="\u652f\u4ed8\u65b9\u5f0f")
    private String paymentWay;
    @Excel(name="\u662f\u5426\u652f\u4ed80\u3001\u672a\u652f\u4ed8 1\u3001\u5df2\u652f\u4ed8")
    private String isPay;
    @Excel(name="\u8ba2\u5355\u540d")
    private String name;
    @Excel(name="\u72b6\u60010\u3001\u5f85\u4ed8\u6b3e 1\u3001\u5f85\u53d1\u8d27 2\u3001\u5f85\u6536\u8d27 3\u3001\u5df2\u5b8c\u6210 4\u3001\u5df2\u5173\u95ed")
    private String status;
    @Excel(name="\u8fd0\u8d39\u91d1\u989d")
    private BigDecimal freightPrice;
    @Excel(name="\u9500\u552e\u91d1\u989d")
    private BigDecimal salesPrice;
    @Excel(name="\u652f\u4ed8\u91d1\u989d\uff08\u9500\u552e\u91d1\u989d+\u8fd0\u8d39\u91d1\u989d-\u79ef\u5206\u62b5\u6263\u91d1\u989d-\u7535\u5b50\u5238\u62b5\u6263\u91d1\u989d\uff09")
    private BigDecimal paymentPrice;
    @Excel(name="\u652f\u4ed8\u79ef\u5206")
    private Integer payIntegral;
    @Excel(name="\u4ed8\u6b3e\u65f6\u95f4")
    private LocalDateTime paymentTime;
    @Excel(name="\u53d1\u8d27\u65f6\u95f4")
    private LocalDateTime deliveryTime;
    @Excel(name="\u6536\u8d27\u65f6\u95f4")
    private LocalDateTime receiverTime;
    @Excel(name="\u6210\u4ea4\u65f6\u95f4")
    private LocalDateTime closingTime;
    @Excel(name="\u4e70\u5bb6\u7559\u8a00")
    private String userMessage;
    @Excel(name="\u652f\u4ed8\u4ea4\u6613ID")
    private String transactionId;
    @Excel(name="\u7269\u6d41id")
    private String logisticsId;
    @Excel(name="\u5907\u6ce8")
    private String remark;
    @TableField(exist=false)
    private List<OrderItem> listOrderItem;
    @TableField(exist=false)
    private List<String> userIdList;
    @TableField(exist=false)
    private Long outTime;
    @TableField(exist=false)
    private String statusDesc;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(exist=false)
    private LocalDateTime beginTime;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(exist=false)
    private LocalDateTime endTime;
    @TableField(exist=false)
    private OrderLogistics orderLogistics;
    @TableField(exist=false)
    private String logistics;
    @TableField(exist=false)
    private UmsMember userInfo;
    @Excel(name="\u7269\u6d41\u5355\u53f7")
    @TableField(exist=false)
    private String logisticsNo;

    public String getStatusDesc() {
        if ("0".equals(this.isPay) && this.status == null) {
            return "\u5f85\u4ed8\u6b3e";
        }
        if (this.status == null) {
            return null;
        }
        return OrderInfoEnum.valueOf(OrderInfoEnum.STATUS_PREFIX + "_" + this.status).getDesc();
    }

    @Generated
    public OrderInfo() {
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
    public String getOrderNo() {
        return this.orderNo;
    }

    @Generated
    public String getPaymentWay() {
        return this.paymentWay;
    }

    @Generated
    public String getIsPay() {
        return this.isPay;
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public String getStatus() {
        return this.status;
    }

    @Generated
    public BigDecimal getFreightPrice() {
        return this.freightPrice;
    }

    @Generated
    public BigDecimal getSalesPrice() {
        return this.salesPrice;
    }

    @Generated
    public BigDecimal getPaymentPrice() {
        return this.paymentPrice;
    }

    @Generated
    public Integer getPayIntegral() {
        return this.payIntegral;
    }

    @Generated
    public LocalDateTime getPaymentTime() {
        return this.paymentTime;
    }

    @Generated
    public LocalDateTime getDeliveryTime() {
        return this.deliveryTime;
    }

    @Generated
    public LocalDateTime getReceiverTime() {
        return this.receiverTime;
    }

    @Generated
    public LocalDateTime getClosingTime() {
        return this.closingTime;
    }

    @Generated
    public String getUserMessage() {
        return this.userMessage;
    }

    @Generated
    public String getTransactionId() {
        return this.transactionId;
    }

    @Generated
    public String getLogisticsId() {
        return this.logisticsId;
    }

    @Generated
    public String getRemark() {
        return this.remark;
    }

    @Generated
    public List<OrderItem> getListOrderItem() {
        return this.listOrderItem;
    }

    @Generated
    public List<String> getUserIdList() {
        return this.userIdList;
    }

    @Generated
    public Long getOutTime() {
        return this.outTime;
    }

    @Generated
    public LocalDateTime getBeginTime() {
        return this.beginTime;
    }

    @Generated
    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    @Generated
    public OrderLogistics getOrderLogistics() {
        return this.orderLogistics;
    }

    @Generated
    public String getLogistics() {
        return this.logistics;
    }

    @Generated
    public UmsMember getUserInfo() {
        return this.userInfo;
    }

    @Generated
    public String getLogisticsNo() {
        return this.logisticsNo;
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
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Generated
    public void setPaymentWay(String paymentWay) {
        this.paymentWay = paymentWay;
    }

    @Generated
    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }

    @Generated
    public void setName(String name) {
        this.name = name;
    }

    @Generated
    public void setStatus(String status) {
        this.status = status;
    }

    @Generated
    public void setFreightPrice(BigDecimal freightPrice) {
        this.freightPrice = freightPrice;
    }

    @Generated
    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    @Generated
    public void setPaymentPrice(BigDecimal paymentPrice) {
        this.paymentPrice = paymentPrice;
    }

    @Generated
    public void setPayIntegral(Integer payIntegral) {
        this.payIntegral = payIntegral;
    }

    @Generated
    public void setPaymentTime(LocalDateTime paymentTime) {
        this.paymentTime = paymentTime;
    }

    @Generated
    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    @Generated
    public void setReceiverTime(LocalDateTime receiverTime) {
        this.receiverTime = receiverTime;
    }

    @Generated
    public void setClosingTime(LocalDateTime closingTime) {
        this.closingTime = closingTime;
    }

    @Generated
    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    @Generated
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Generated
    public void setLogisticsId(String logisticsId) {
        this.logisticsId = logisticsId;
    }

    @Generated
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Generated
    public void setListOrderItem(List<OrderItem> listOrderItem) {
        this.listOrderItem = listOrderItem;
    }

    @Generated
    public void setUserIdList(List<String> userIdList) {
        this.userIdList = userIdList;
    }

    @Generated
    public void setOutTime(Long outTime) {
        this.outTime = outTime;
    }

    @Generated
    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    @Generated
    public void setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
    }

    @Generated
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Generated
    public void setOrderLogistics(OrderLogistics orderLogistics) {
        this.orderLogistics = orderLogistics;
    }

    @Generated
    public void setLogistics(String logistics) {
        this.logistics = logistics;
    }

    @Generated
    public void setUserInfo(UmsMember userInfo) {
        this.userInfo = userInfo;
    }

    @Generated
    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }

    @Generated
    public String toString() {
        return "OrderInfo(id=" + this.getId() + ", delFlag=" + this.getDelFlag() + ", createTime=" + String.valueOf(this.getCreateTime()) + ", updateTime=" + String.valueOf(this.getUpdateTime()) + ", userId=" + this.getUserId() + ", orderNo=" + this.getOrderNo() + ", paymentWay=" + this.getPaymentWay() + ", isPay=" + this.getIsPay() + ", name=" + this.getName() + ", status=" + this.getStatus() + ", freightPrice=" + String.valueOf(this.getFreightPrice()) + ", salesPrice=" + String.valueOf(this.getSalesPrice()) + ", paymentPrice=" + String.valueOf(this.getPaymentPrice()) + ", payIntegral=" + this.getPayIntegral() + ", paymentTime=" + String.valueOf(this.getPaymentTime()) + ", deliveryTime=" + String.valueOf(this.getDeliveryTime()) + ", receiverTime=" + String.valueOf(this.getReceiverTime()) + ", closingTime=" + String.valueOf(this.getClosingTime()) + ", userMessage=" + this.getUserMessage() + ", transactionId=" + this.getTransactionId() + ", logisticsId=" + this.getLogisticsId() + ", remark=" + this.getRemark() + ", listOrderItem=" + String.valueOf(this.getListOrderItem()) + ", userIdList=" + String.valueOf(this.getUserIdList()) + ", outTime=" + this.getOutTime() + ", statusDesc=" + this.getStatusDesc() + ", beginTime=" + String.valueOf(this.getBeginTime()) + ", endTime=" + String.valueOf(this.getEndTime()) + ", orderLogistics=" + String.valueOf(this.getOrderLogistics()) + ", logistics=" + this.getLogistics() + ", userInfo=" + String.valueOf(this.getUserInfo()) + ", logisticsNo=" + this.getLogisticsNo() + ")";
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OrderInfo)) {
            return false;
        }
        OrderInfo other = (OrderInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Integer this$payIntegral = this.getPayIntegral();
        Integer other$payIntegral = other.getPayIntegral();
        if (this$payIntegral == null ? other$payIntegral != null : !((Object)this$payIntegral).equals(other$payIntegral)) {
            return false;
        }
        Long this$outTime = this.getOutTime();
        Long other$outTime = other.getOutTime();
        if (this$outTime == null ? other$outTime != null : !((Object)this$outTime).equals(other$outTime)) {
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
        String this$orderNo = this.getOrderNo();
        String other$orderNo = other.getOrderNo();
        if (this$orderNo == null ? other$orderNo != null : !this$orderNo.equals(other$orderNo)) {
            return false;
        }
        String this$paymentWay = this.getPaymentWay();
        String other$paymentWay = other.getPaymentWay();
        if (this$paymentWay == null ? other$paymentWay != null : !this$paymentWay.equals(other$paymentWay)) {
            return false;
        }
        String this$isPay = this.getIsPay();
        String other$isPay = other.getIsPay();
        if (this$isPay == null ? other$isPay != null : !this$isPay.equals(other$isPay)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        BigDecimal this$freightPrice = this.getFreightPrice();
        BigDecimal other$freightPrice = other.getFreightPrice();
        if (this$freightPrice == null ? other$freightPrice != null : !((Object)this$freightPrice).equals(other$freightPrice)) {
            return false;
        }
        BigDecimal this$salesPrice = this.getSalesPrice();
        BigDecimal other$salesPrice = other.getSalesPrice();
        if (this$salesPrice == null ? other$salesPrice != null : !((Object)this$salesPrice).equals(other$salesPrice)) {
            return false;
        }
        BigDecimal this$paymentPrice = this.getPaymentPrice();
        BigDecimal other$paymentPrice = other.getPaymentPrice();
        if (this$paymentPrice == null ? other$paymentPrice != null : !((Object)this$paymentPrice).equals(other$paymentPrice)) {
            return false;
        }
        LocalDateTime this$paymentTime = this.getPaymentTime();
        LocalDateTime other$paymentTime = other.getPaymentTime();
        if (this$paymentTime == null ? other$paymentTime != null : !((Object)this$paymentTime).equals(other$paymentTime)) {
            return false;
        }
        LocalDateTime this$deliveryTime = this.getDeliveryTime();
        LocalDateTime other$deliveryTime = other.getDeliveryTime();
        if (this$deliveryTime == null ? other$deliveryTime != null : !((Object)this$deliveryTime).equals(other$deliveryTime)) {
            return false;
        }
        LocalDateTime this$receiverTime = this.getReceiverTime();
        LocalDateTime other$receiverTime = other.getReceiverTime();
        if (this$receiverTime == null ? other$receiverTime != null : !((Object)this$receiverTime).equals(other$receiverTime)) {
            return false;
        }
        LocalDateTime this$closingTime = this.getClosingTime();
        LocalDateTime other$closingTime = other.getClosingTime();
        if (this$closingTime == null ? other$closingTime != null : !((Object)this$closingTime).equals(other$closingTime)) {
            return false;
        }
        String this$userMessage = this.getUserMessage();
        String other$userMessage = other.getUserMessage();
        if (this$userMessage == null ? other$userMessage != null : !this$userMessage.equals(other$userMessage)) {
            return false;
        }
        String this$transactionId = this.getTransactionId();
        String other$transactionId = other.getTransactionId();
        if (this$transactionId == null ? other$transactionId != null : !this$transactionId.equals(other$transactionId)) {
            return false;
        }
        String this$logisticsId = this.getLogisticsId();
        String other$logisticsId = other.getLogisticsId();
        if (this$logisticsId == null ? other$logisticsId != null : !this$logisticsId.equals(other$logisticsId)) {
            return false;
        }
        String this$remark = this.getRemark();
        String other$remark = other.getRemark();
        if (this$remark == null ? other$remark != null : !this$remark.equals(other$remark)) {
            return false;
        }
        List<OrderItem> this$listOrderItem = this.getListOrderItem();
        List<OrderItem> other$listOrderItem = other.getListOrderItem();
        if (this$listOrderItem == null ? other$listOrderItem != null : !((Object)this$listOrderItem).equals(other$listOrderItem)) {
            return false;
        }
        List<String> this$userIdList = this.getUserIdList();
        List<String> other$userIdList = other.getUserIdList();
        if (this$userIdList == null ? other$userIdList != null : !((Object)this$userIdList).equals(other$userIdList)) {
            return false;
        }
        String this$statusDesc = this.getStatusDesc();
        String other$statusDesc = other.getStatusDesc();
        if (this$statusDesc == null ? other$statusDesc != null : !this$statusDesc.equals(other$statusDesc)) {
            return false;
        }
        LocalDateTime this$beginTime = this.getBeginTime();
        LocalDateTime other$beginTime = other.getBeginTime();
        if (this$beginTime == null ? other$beginTime != null : !((Object)this$beginTime).equals(other$beginTime)) {
            return false;
        }
        LocalDateTime this$endTime = this.getEndTime();
        LocalDateTime other$endTime = other.getEndTime();
        if (this$endTime == null ? other$endTime != null : !((Object)this$endTime).equals(other$endTime)) {
            return false;
        }
        OrderLogistics this$orderLogistics = this.getOrderLogistics();
        OrderLogistics other$orderLogistics = other.getOrderLogistics();
        if (this$orderLogistics == null ? other$orderLogistics != null : !((Object)this$orderLogistics).equals(other$orderLogistics)) {
            return false;
        }
        String this$logistics = this.getLogistics();
        String other$logistics = other.getLogistics();
        if (this$logistics == null ? other$logistics != null : !this$logistics.equals(other$logistics)) {
            return false;
        }
        UmsMember this$userInfo = this.getUserInfo();
        UmsMember other$userInfo = other.getUserInfo();
        if (this$userInfo == null ? other$userInfo != null : !((Object)this$userInfo).equals(other$userInfo)) {
            return false;
        }
        String this$logisticsNo = this.getLogisticsNo();
        String other$logisticsNo = other.getLogisticsNo();
        return !(this$logisticsNo == null ? other$logisticsNo != null : !this$logisticsNo.equals(other$logisticsNo));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof OrderInfo;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        Integer $payIntegral = this.getPayIntegral();
        result = result * 59 + ($payIntegral == null ? 43 : ((Object)$payIntegral).hashCode());
        Long $outTime = this.getOutTime();
        result = result * 59 + ($outTime == null ? 43 : ((Object)$outTime).hashCode());
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
        String $orderNo = this.getOrderNo();
        result = result * 59 + ($orderNo == null ? 43 : $orderNo.hashCode());
        String $paymentWay = this.getPaymentWay();
        result = result * 59 + ($paymentWay == null ? 43 : $paymentWay.hashCode());
        String $isPay = this.getIsPay();
        result = result * 59 + ($isPay == null ? 43 : $isPay.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        BigDecimal $freightPrice = this.getFreightPrice();
        result = result * 59 + ($freightPrice == null ? 43 : ((Object)$freightPrice).hashCode());
        BigDecimal $salesPrice = this.getSalesPrice();
        result = result * 59 + ($salesPrice == null ? 43 : ((Object)$salesPrice).hashCode());
        BigDecimal $paymentPrice = this.getPaymentPrice();
        result = result * 59 + ($paymentPrice == null ? 43 : ((Object)$paymentPrice).hashCode());
        LocalDateTime $paymentTime = this.getPaymentTime();
        result = result * 59 + ($paymentTime == null ? 43 : ((Object)$paymentTime).hashCode());
        LocalDateTime $deliveryTime = this.getDeliveryTime();
        result = result * 59 + ($deliveryTime == null ? 43 : ((Object)$deliveryTime).hashCode());
        LocalDateTime $receiverTime = this.getReceiverTime();
        result = result * 59 + ($receiverTime == null ? 43 : ((Object)$receiverTime).hashCode());
        LocalDateTime $closingTime = this.getClosingTime();
        result = result * 59 + ($closingTime == null ? 43 : ((Object)$closingTime).hashCode());
        String $userMessage = this.getUserMessage();
        result = result * 59 + ($userMessage == null ? 43 : $userMessage.hashCode());
        String $transactionId = this.getTransactionId();
        result = result * 59 + ($transactionId == null ? 43 : $transactionId.hashCode());
        String $logisticsId = this.getLogisticsId();
        result = result * 59 + ($logisticsId == null ? 43 : $logisticsId.hashCode());
        String $remark = this.getRemark();
        result = result * 59 + ($remark == null ? 43 : $remark.hashCode());
        List<OrderItem> $listOrderItem = this.getListOrderItem();
        result = result * 59 + ($listOrderItem == null ? 43 : ((Object)$listOrderItem).hashCode());
        List<String> $userIdList = this.getUserIdList();
        result = result * 59 + ($userIdList == null ? 43 : ((Object)$userIdList).hashCode());
        String $statusDesc = this.getStatusDesc();
        result = result * 59 + ($statusDesc == null ? 43 : $statusDesc.hashCode());
        LocalDateTime $beginTime = this.getBeginTime();
        result = result * 59 + ($beginTime == null ? 43 : ((Object)$beginTime).hashCode());
        LocalDateTime $endTime = this.getEndTime();
        result = result * 59 + ($endTime == null ? 43 : ((Object)$endTime).hashCode());
        OrderLogistics $orderLogistics = this.getOrderLogistics();
        result = result * 59 + ($orderLogistics == null ? 43 : ((Object)$orderLogistics).hashCode());
        String $logistics = this.getLogistics();
        result = result * 59 + ($logistics == null ? 43 : $logistics.hashCode());
        UmsMember $userInfo = this.getUserInfo();
        result = result * 59 + ($userInfo == null ? 43 : ((Object)$userInfo).hashCode());
        String $logisticsNo = this.getLogisticsNo();
        result = result * 59 + ($logisticsNo == null ? 43 : $logisticsNo.hashCode());
        return result;
    }
}

