/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.entity;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.dingyangmall.common.annotation.Excel;
import com.dingyangmall.mall.enums.OrderLogisticsEnum;
import java.time.LocalDateTime;
import lombok.Generated;

@TableName(value="order_logistics")
public class OrderLogistics
extends Model<OrderLogistics> {
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
    @Excel(name="\u90ae\u7f16")
    private String postalCode;
    @Excel(name="\u6536\u8d27\u4eba\u540d\u5b57")
    private String userName;
    @Excel(name="\u7535\u8bdd\u53f7\u7801")
    private String telNum;
    @Excel(name="\u8be6\u7ec6\u5730\u5740")
    private String address;
    @Excel(name="\u7269\u6d41\u5546\u5bb6")
    private String logistics;
    @TableField(exist=false)
    private String logisticsDesc;
    private String logisticsNo;
    private String status;
    @TableField(exist=false)
    private String statusDesc;
    private String isCheck;
    private String message;

    public String getLogisticsDesc() {
        if (this.logistics == null) {
            return null;
        }
        try {
            return OrderLogisticsEnum.valueOf(OrderLogisticsEnum.LOGISTICS_PREFIX + "_" + StrUtil.swapCase(this.logistics)).getDesc();
        }
        catch (IllegalArgumentException e) {
            return this.logistics;
        }
    }

    public String getStatusDesc() {
        if (this.status == null) {
            return null;
        }
        try {
            return OrderLogisticsEnum.valueOf(OrderLogisticsEnum.STATUS_PREFIX + "_" + this.status).getDesc();
        }
        catch (IllegalArgumentException e) {
            return this.status;
        }
    }

    @Generated
    public OrderLogistics() {
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
    public String getPostalCode() {
        return this.postalCode;
    }

    @Generated
    public String getUserName() {
        return this.userName;
    }

    @Generated
    public String getTelNum() {
        return this.telNum;
    }

    @Generated
    public String getAddress() {
        return this.address;
    }

    @Generated
    public String getLogistics() {
        return this.logistics;
    }

    @Generated
    public String getLogisticsNo() {
        return this.logisticsNo;
    }

    @Generated
    public String getStatus() {
        return this.status;
    }

    @Generated
    public String getIsCheck() {
        return this.isCheck;
    }

    @Generated
    public String getMessage() {
        return this.message;
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
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Generated
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Generated
    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    @Generated
    public void setAddress(String address) {
        this.address = address;
    }

    @Generated
    public void setLogistics(String logistics) {
        this.logistics = logistics;
    }

    @Generated
    public void setLogisticsDesc(String logisticsDesc) {
        this.logisticsDesc = logisticsDesc;
    }

    @Generated
    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }

    @Generated
    public void setStatus(String status) {
        this.status = status;
    }

    @Generated
    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    @Generated
    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }

    @Generated
    public void setMessage(String message) {
        this.message = message;
    }

    @Generated
    public String toString() {
        return "OrderLogistics(id=" + this.getId() + ", delFlag=" + this.getDelFlag() + ", createTime=" + String.valueOf(this.getCreateTime()) + ", updateTime=" + String.valueOf(this.getUpdateTime()) + ", postalCode=" + this.getPostalCode() + ", userName=" + this.getUserName() + ", telNum=" + this.getTelNum() + ", address=" + this.getAddress() + ", logistics=" + this.getLogistics() + ", logisticsDesc=" + this.getLogisticsDesc() + ", logisticsNo=" + this.getLogisticsNo() + ", status=" + this.getStatus() + ", statusDesc=" + this.getStatusDesc() + ", isCheck=" + this.getIsCheck() + ", message=" + this.getMessage() + ")";
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OrderLogistics)) {
            return false;
        }
        OrderLogistics other = (OrderLogistics)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
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
        String this$postalCode = this.getPostalCode();
        String other$postalCode = other.getPostalCode();
        if (this$postalCode == null ? other$postalCode != null : !this$postalCode.equals(other$postalCode)) {
            return false;
        }
        String this$userName = this.getUserName();
        String other$userName = other.getUserName();
        if (this$userName == null ? other$userName != null : !this$userName.equals(other$userName)) {
            return false;
        }
        String this$telNum = this.getTelNum();
        String other$telNum = other.getTelNum();
        if (this$telNum == null ? other$telNum != null : !this$telNum.equals(other$telNum)) {
            return false;
        }
        String this$address = this.getAddress();
        String other$address = other.getAddress();
        if (this$address == null ? other$address != null : !this$address.equals(other$address)) {
            return false;
        }
        String this$logistics = this.getLogistics();
        String other$logistics = other.getLogistics();
        if (this$logistics == null ? other$logistics != null : !this$logistics.equals(other$logistics)) {
            return false;
        }
        String this$logisticsDesc = this.getLogisticsDesc();
        String other$logisticsDesc = other.getLogisticsDesc();
        if (this$logisticsDesc == null ? other$logisticsDesc != null : !this$logisticsDesc.equals(other$logisticsDesc)) {
            return false;
        }
        String this$logisticsNo = this.getLogisticsNo();
        String other$logisticsNo = other.getLogisticsNo();
        if (this$logisticsNo == null ? other$logisticsNo != null : !this$logisticsNo.equals(other$logisticsNo)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        String this$statusDesc = this.getStatusDesc();
        String other$statusDesc = other.getStatusDesc();
        if (this$statusDesc == null ? other$statusDesc != null : !this$statusDesc.equals(other$statusDesc)) {
            return false;
        }
        String this$isCheck = this.getIsCheck();
        String other$isCheck = other.getIsCheck();
        if (this$isCheck == null ? other$isCheck != null : !this$isCheck.equals(other$isCheck)) {
            return false;
        }
        String this$message = this.getMessage();
        String other$message = other.getMessage();
        return !(this$message == null ? other$message != null : !this$message.equals(other$message));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof OrderLogistics;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $delFlag = this.getDelFlag();
        result = result * 59 + ($delFlag == null ? 43 : $delFlag.hashCode());
        LocalDateTime $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        LocalDateTime $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        String $postalCode = this.getPostalCode();
        result = result * 59 + ($postalCode == null ? 43 : $postalCode.hashCode());
        String $userName = this.getUserName();
        result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
        String $telNum = this.getTelNum();
        result = result * 59 + ($telNum == null ? 43 : $telNum.hashCode());
        String $address = this.getAddress();
        result = result * 59 + ($address == null ? 43 : $address.hashCode());
        String $logistics = this.getLogistics();
        result = result * 59 + ($logistics == null ? 43 : $logistics.hashCode());
        String $logisticsDesc = this.getLogisticsDesc();
        result = result * 59 + ($logisticsDesc == null ? 43 : $logisticsDesc.hashCode());
        String $logisticsNo = this.getLogisticsNo();
        result = result * 59 + ($logisticsNo == null ? 43 : $logisticsNo.hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        String $statusDesc = this.getStatusDesc();
        result = result * 59 + ($statusDesc == null ? 43 : $statusDesc.hashCode());
        String $isCheck = this.getIsCheck();
        result = result * 59 + ($isCheck == null ? 43 : $isCheck.hashCode());
        String $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        return result;
    }
}

