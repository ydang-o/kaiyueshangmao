/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.dingyangmall.common.annotation.Excel;
import java.time.LocalDateTime;
import lombok.Generated;

@TableName(value="user_address")
public class UserAddress
extends Model<UserAddress> {
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
    @Excel(name="\u6536\u8d27\u4eba\u540d\u5b57")
    private String userName;
    @Excel(name="\u90ae\u7f16")
    private String postalCode;
    @Excel(name="\u7701\u540d")
    private String provinceName;
    @Excel(name="\u5e02\u540d")
    private String cityName;
    @Excel(name="\u533a\u540d")
    private String countyName;
    @Excel(name="\u8be6\u60c5\u5730\u5740")
    private String detailInfo;
    @Excel(name="\u7535\u8bdd\u53f7\u7801")
    private String telNum;
    @Excel(name="\u662f\u5426\u9ed8\u8ba4")
    private String isDefault;

    @Generated
    public UserAddress() {
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
    public String getUserName() {
        return this.userName;
    }

    @Generated
    public String getPostalCode() {
        return this.postalCode;
    }

    @Generated
    public String getProvinceName() {
        return this.provinceName;
    }

    @Generated
    public String getCityName() {
        return this.cityName;
    }

    @Generated
    public String getCountyName() {
        return this.countyName;
    }

    @Generated
    public String getDetailInfo() {
        return this.detailInfo;
    }

    @Generated
    public String getTelNum() {
        return this.telNum;
    }

    @Generated
    public String getIsDefault() {
        return this.isDefault;
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
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Generated
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Generated
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    @Generated
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Generated
    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    @Generated
    public void setDetailInfo(String detailInfo) {
        this.detailInfo = detailInfo;
    }

    @Generated
    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    @Generated
    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    @Generated
    public String toString() {
        return "UserAddress(id=" + this.getId() + ", delFlag=" + this.getDelFlag() + ", createTime=" + String.valueOf(this.getCreateTime()) + ", updateTime=" + String.valueOf(this.getUpdateTime()) + ", userId=" + this.getUserId() + ", userName=" + this.getUserName() + ", postalCode=" + this.getPostalCode() + ", provinceName=" + this.getProvinceName() + ", cityName=" + this.getCityName() + ", countyName=" + this.getCountyName() + ", detailInfo=" + this.getDetailInfo() + ", telNum=" + this.getTelNum() + ", isDefault=" + this.getIsDefault() + ")";
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserAddress)) {
            return false;
        }
        UserAddress other = (UserAddress)o;
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
        String this$userId = this.getUserId();
        String other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !this$userId.equals(other$userId)) {
            return false;
        }
        String this$userName = this.getUserName();
        String other$userName = other.getUserName();
        if (this$userName == null ? other$userName != null : !this$userName.equals(other$userName)) {
            return false;
        }
        String this$postalCode = this.getPostalCode();
        String other$postalCode = other.getPostalCode();
        if (this$postalCode == null ? other$postalCode != null : !this$postalCode.equals(other$postalCode)) {
            return false;
        }
        String this$provinceName = this.getProvinceName();
        String other$provinceName = other.getProvinceName();
        if (this$provinceName == null ? other$provinceName != null : !this$provinceName.equals(other$provinceName)) {
            return false;
        }
        String this$cityName = this.getCityName();
        String other$cityName = other.getCityName();
        if (this$cityName == null ? other$cityName != null : !this$cityName.equals(other$cityName)) {
            return false;
        }
        String this$countyName = this.getCountyName();
        String other$countyName = other.getCountyName();
        if (this$countyName == null ? other$countyName != null : !this$countyName.equals(other$countyName)) {
            return false;
        }
        String this$detailInfo = this.getDetailInfo();
        String other$detailInfo = other.getDetailInfo();
        if (this$detailInfo == null ? other$detailInfo != null : !this$detailInfo.equals(other$detailInfo)) {
            return false;
        }
        String this$telNum = this.getTelNum();
        String other$telNum = other.getTelNum();
        if (this$telNum == null ? other$telNum != null : !this$telNum.equals(other$telNum)) {
            return false;
        }
        String this$isDefault = this.getIsDefault();
        String other$isDefault = other.getIsDefault();
        return !(this$isDefault == null ? other$isDefault != null : !this$isDefault.equals(other$isDefault));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof UserAddress;
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
        String $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : $userId.hashCode());
        String $userName = this.getUserName();
        result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
        String $postalCode = this.getPostalCode();
        result = result * 59 + ($postalCode == null ? 43 : $postalCode.hashCode());
        String $provinceName = this.getProvinceName();
        result = result * 59 + ($provinceName == null ? 43 : $provinceName.hashCode());
        String $cityName = this.getCityName();
        result = result * 59 + ($cityName == null ? 43 : $cityName.hashCode());
        String $countyName = this.getCountyName();
        result = result * 59 + ($countyName == null ? 43 : $countyName.hashCode());
        String $detailInfo = this.getDetailInfo();
        result = result * 59 + ($detailInfo == null ? 43 : $detailInfo.hashCode());
        String $telNum = this.getTelNum();
        result = result * 59 + ($telNum == null ? 43 : $telNum.hashCode());
        String $isDefault = this.getIsDefault();
        result = result * 59 + ($isDefault == null ? 43 : $isDefault.hashCode());
        return result;
    }
}

