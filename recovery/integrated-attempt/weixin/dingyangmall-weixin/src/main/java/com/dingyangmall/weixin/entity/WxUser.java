/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.dingyangmall.common.sensitive.Sensitive;
import com.dingyangmall.common.sensitive.SensitiveTypeEnum;
import com.dingyangmall.framework.config.typehandler.ArrayLongTypeHandler;
import java.time.LocalDateTime;
import java.util.Arrays;
import lombok.Generated;
import org.apache.ibatis.type.JdbcType;

@TableName(value="wx_user")
public class WxUser
extends Model<WxUser> {
    private static final long serialVersionUID = 1L;
    @TableId(type=IdType.ASSIGN_ID)
    private String id;
    private String createId;
    private LocalDateTime createTime;
    private String updateId;
    private LocalDateTime updateTime;
    private String remark;
    private String delFlag;
    private String appType;
    private String subscribe;
    private String subscribeScene;
    private LocalDateTime subscribeTime;
    private Integer subscribeNum;
    private LocalDateTime cancelSubscribeTime;
    private String openId;
    private String nickName;
    private String sex;
    private String city;
    private String country;
    private String province;
    @Sensitive(type=SensitiveTypeEnum.MOBILE_PHONE)
    private String phone;
    private String language;
    private String headimgUrl;
    private String unionId;
    private String groupId;
    @TableField(typeHandler=ArrayLongTypeHandler.class, jdbcType=JdbcType.VARCHAR)
    private Long[] tagidList;
    private String qrSceneStr;
    private Double latitude;
    private Double longitude;
    @TableField(value="`precision`")
    private Double precision;
    private String sessionKey;

    @Generated
    public WxUser() {
    }

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getCreateId() {
        return this.createId;
    }

    @Generated
    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    @Generated
    public String getUpdateId() {
        return this.updateId;
    }

    @Generated
    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }

    @Generated
    public String getRemark() {
        return this.remark;
    }

    @Generated
    public String getDelFlag() {
        return this.delFlag;
    }

    @Generated
    public String getAppType() {
        return this.appType;
    }

    @Generated
    public String getSubscribe() {
        return this.subscribe;
    }

    @Generated
    public String getSubscribeScene() {
        return this.subscribeScene;
    }

    @Generated
    public LocalDateTime getSubscribeTime() {
        return this.subscribeTime;
    }

    @Generated
    public Integer getSubscribeNum() {
        return this.subscribeNum;
    }

    @Generated
    public LocalDateTime getCancelSubscribeTime() {
        return this.cancelSubscribeTime;
    }

    @Generated
    public String getOpenId() {
        return this.openId;
    }

    @Generated
    public String getNickName() {
        return this.nickName;
    }

    @Generated
    public String getSex() {
        return this.sex;
    }

    @Generated
    public String getCity() {
        return this.city;
    }

    @Generated
    public String getCountry() {
        return this.country;
    }

    @Generated
    public String getProvince() {
        return this.province;
    }

    @Generated
    public String getPhone() {
        return this.phone;
    }

    @Generated
    public String getLanguage() {
        return this.language;
    }

    @Generated
    public String getHeadimgUrl() {
        return this.headimgUrl;
    }

    @Generated
    public String getUnionId() {
        return this.unionId;
    }

    @Generated
    public String getGroupId() {
        return this.groupId;
    }

    @Generated
    public Long[] getTagidList() {
        return this.tagidList;
    }

    @Generated
    public String getQrSceneStr() {
        return this.qrSceneStr;
    }

    @Generated
    public Double getLatitude() {
        return this.latitude;
    }

    @Generated
    public Double getLongitude() {
        return this.longitude;
    }

    @Generated
    public Double getPrecision() {
        return this.precision;
    }

    @Generated
    public String getSessionKey() {
        return this.sessionKey;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
    }

    @Generated
    public void setCreateId(String createId) {
        this.createId = createId;
    }

    @Generated
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Generated
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    @Generated
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Generated
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Generated
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Generated
    public void setAppType(String appType) {
        this.appType = appType;
    }

    @Generated
    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    @Generated
    public void setSubscribeScene(String subscribeScene) {
        this.subscribeScene = subscribeScene;
    }

    @Generated
    public void setSubscribeTime(LocalDateTime subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    @Generated
    public void setSubscribeNum(Integer subscribeNum) {
        this.subscribeNum = subscribeNum;
    }

    @Generated
    public void setCancelSubscribeTime(LocalDateTime cancelSubscribeTime) {
        this.cancelSubscribeTime = cancelSubscribeTime;
    }

    @Generated
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @Generated
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Generated
    public void setSex(String sex) {
        this.sex = sex;
    }

    @Generated
    public void setCity(String city) {
        this.city = city;
    }

    @Generated
    public void setCountry(String country) {
        this.country = country;
    }

    @Generated
    public void setProvince(String province) {
        this.province = province;
    }

    @Generated
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Generated
    public void setLanguage(String language) {
        this.language = language;
    }

    @Generated
    public void setHeadimgUrl(String headimgUrl) {
        this.headimgUrl = headimgUrl;
    }

    @Generated
    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    @Generated
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Generated
    public void setTagidList(Long[] tagidList) {
        this.tagidList = tagidList;
    }

    @Generated
    public void setQrSceneStr(String qrSceneStr) {
        this.qrSceneStr = qrSceneStr;
    }

    @Generated
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Generated
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Generated
    public void setPrecision(Double precision) {
        this.precision = precision;
    }

    @Generated
    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    @Generated
    public String toString() {
        return "WxUser(id=" + this.getId() + ", createId=" + this.getCreateId() + ", createTime=" + String.valueOf(this.getCreateTime()) + ", updateId=" + this.getUpdateId() + ", updateTime=" + String.valueOf(this.getUpdateTime()) + ", remark=" + this.getRemark() + ", delFlag=" + this.getDelFlag() + ", appType=" + this.getAppType() + ", subscribe=" + this.getSubscribe() + ", subscribeScene=" + this.getSubscribeScene() + ", subscribeTime=" + String.valueOf(this.getSubscribeTime()) + ", subscribeNum=" + this.getSubscribeNum() + ", cancelSubscribeTime=" + String.valueOf(this.getCancelSubscribeTime()) + ", openId=" + this.getOpenId() + ", nickName=" + this.getNickName() + ", sex=" + this.getSex() + ", city=" + this.getCity() + ", country=" + this.getCountry() + ", province=" + this.getProvince() + ", phone=" + this.getPhone() + ", language=" + this.getLanguage() + ", headimgUrl=" + this.getHeadimgUrl() + ", unionId=" + this.getUnionId() + ", groupId=" + this.getGroupId() + ", tagidList=" + Arrays.deepToString(this.getTagidList()) + ", qrSceneStr=" + this.getQrSceneStr() + ", latitude=" + this.getLatitude() + ", longitude=" + this.getLongitude() + ", precision=" + this.getPrecision() + ", sessionKey=" + this.getSessionKey() + ")";
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WxUser)) {
            return false;
        }
        WxUser other = (WxUser)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Integer this$subscribeNum = this.getSubscribeNum();
        Integer other$subscribeNum = other.getSubscribeNum();
        if (this$subscribeNum == null ? other$subscribeNum != null : !((Object)this$subscribeNum).equals(other$subscribeNum)) {
            return false;
        }
        Double this$latitude = this.getLatitude();
        Double other$latitude = other.getLatitude();
        if (this$latitude == null ? other$latitude != null : !((Object)this$latitude).equals(other$latitude)) {
            return false;
        }
        Double this$longitude = this.getLongitude();
        Double other$longitude = other.getLongitude();
        if (this$longitude == null ? other$longitude != null : !((Object)this$longitude).equals(other$longitude)) {
            return false;
        }
        Double this$precision = this.getPrecision();
        Double other$precision = other.getPrecision();
        if (this$precision == null ? other$precision != null : !((Object)this$precision).equals(other$precision)) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$createId = this.getCreateId();
        String other$createId = other.getCreateId();
        if (this$createId == null ? other$createId != null : !this$createId.equals(other$createId)) {
            return false;
        }
        LocalDateTime this$createTime = this.getCreateTime();
        LocalDateTime other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
            return false;
        }
        String this$updateId = this.getUpdateId();
        String other$updateId = other.getUpdateId();
        if (this$updateId == null ? other$updateId != null : !this$updateId.equals(other$updateId)) {
            return false;
        }
        LocalDateTime this$updateTime = this.getUpdateTime();
        LocalDateTime other$updateTime = other.getUpdateTime();
        if (this$updateTime == null ? other$updateTime != null : !((Object)this$updateTime).equals(other$updateTime)) {
            return false;
        }
        String this$remark = this.getRemark();
        String other$remark = other.getRemark();
        if (this$remark == null ? other$remark != null : !this$remark.equals(other$remark)) {
            return false;
        }
        String this$delFlag = this.getDelFlag();
        String other$delFlag = other.getDelFlag();
        if (this$delFlag == null ? other$delFlag != null : !this$delFlag.equals(other$delFlag)) {
            return false;
        }
        String this$appType = this.getAppType();
        String other$appType = other.getAppType();
        if (this$appType == null ? other$appType != null : !this$appType.equals(other$appType)) {
            return false;
        }
        String this$subscribe = this.getSubscribe();
        String other$subscribe = other.getSubscribe();
        if (this$subscribe == null ? other$subscribe != null : !this$subscribe.equals(other$subscribe)) {
            return false;
        }
        String this$subscribeScene = this.getSubscribeScene();
        String other$subscribeScene = other.getSubscribeScene();
        if (this$subscribeScene == null ? other$subscribeScene != null : !this$subscribeScene.equals(other$subscribeScene)) {
            return false;
        }
        LocalDateTime this$subscribeTime = this.getSubscribeTime();
        LocalDateTime other$subscribeTime = other.getSubscribeTime();
        if (this$subscribeTime == null ? other$subscribeTime != null : !((Object)this$subscribeTime).equals(other$subscribeTime)) {
            return false;
        }
        LocalDateTime this$cancelSubscribeTime = this.getCancelSubscribeTime();
        LocalDateTime other$cancelSubscribeTime = other.getCancelSubscribeTime();
        if (this$cancelSubscribeTime == null ? other$cancelSubscribeTime != null : !((Object)this$cancelSubscribeTime).equals(other$cancelSubscribeTime)) {
            return false;
        }
        String this$openId = this.getOpenId();
        String other$openId = other.getOpenId();
        if (this$openId == null ? other$openId != null : !this$openId.equals(other$openId)) {
            return false;
        }
        String this$nickName = this.getNickName();
        String other$nickName = other.getNickName();
        if (this$nickName == null ? other$nickName != null : !this$nickName.equals(other$nickName)) {
            return false;
        }
        String this$sex = this.getSex();
        String other$sex = other.getSex();
        if (this$sex == null ? other$sex != null : !this$sex.equals(other$sex)) {
            return false;
        }
        String this$city = this.getCity();
        String other$city = other.getCity();
        if (this$city == null ? other$city != null : !this$city.equals(other$city)) {
            return false;
        }
        String this$country = this.getCountry();
        String other$country = other.getCountry();
        if (this$country == null ? other$country != null : !this$country.equals(other$country)) {
            return false;
        }
        String this$province = this.getProvince();
        String other$province = other.getProvince();
        if (this$province == null ? other$province != null : !this$province.equals(other$province)) {
            return false;
        }
        String this$phone = this.getPhone();
        String other$phone = other.getPhone();
        if (this$phone == null ? other$phone != null : !this$phone.equals(other$phone)) {
            return false;
        }
        String this$language = this.getLanguage();
        String other$language = other.getLanguage();
        if (this$language == null ? other$language != null : !this$language.equals(other$language)) {
            return false;
        }
        String this$headimgUrl = this.getHeadimgUrl();
        String other$headimgUrl = other.getHeadimgUrl();
        if (this$headimgUrl == null ? other$headimgUrl != null : !this$headimgUrl.equals(other$headimgUrl)) {
            return false;
        }
        String this$unionId = this.getUnionId();
        String other$unionId = other.getUnionId();
        if (this$unionId == null ? other$unionId != null : !this$unionId.equals(other$unionId)) {
            return false;
        }
        String this$groupId = this.getGroupId();
        String other$groupId = other.getGroupId();
        if (this$groupId == null ? other$groupId != null : !this$groupId.equals(other$groupId)) {
            return false;
        }
        if (!Arrays.deepEquals(this.getTagidList(), other.getTagidList())) {
            return false;
        }
        String this$qrSceneStr = this.getQrSceneStr();
        String other$qrSceneStr = other.getQrSceneStr();
        if (this$qrSceneStr == null ? other$qrSceneStr != null : !this$qrSceneStr.equals(other$qrSceneStr)) {
            return false;
        }
        String this$sessionKey = this.getSessionKey();
        String other$sessionKey = other.getSessionKey();
        return !(this$sessionKey == null ? other$sessionKey != null : !this$sessionKey.equals(other$sessionKey));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof WxUser;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        Integer $subscribeNum = this.getSubscribeNum();
        result = result * 59 + ($subscribeNum == null ? 43 : ((Object)$subscribeNum).hashCode());
        Double $latitude = this.getLatitude();
        result = result * 59 + ($latitude == null ? 43 : ((Object)$latitude).hashCode());
        Double $longitude = this.getLongitude();
        result = result * 59 + ($longitude == null ? 43 : ((Object)$longitude).hashCode());
        Double $precision = this.getPrecision();
        result = result * 59 + ($precision == null ? 43 : ((Object)$precision).hashCode());
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $createId = this.getCreateId();
        result = result * 59 + ($createId == null ? 43 : $createId.hashCode());
        LocalDateTime $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        String $updateId = this.getUpdateId();
        result = result * 59 + ($updateId == null ? 43 : $updateId.hashCode());
        LocalDateTime $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        String $remark = this.getRemark();
        result = result * 59 + ($remark == null ? 43 : $remark.hashCode());
        String $delFlag = this.getDelFlag();
        result = result * 59 + ($delFlag == null ? 43 : $delFlag.hashCode());
        String $appType = this.getAppType();
        result = result * 59 + ($appType == null ? 43 : $appType.hashCode());
        String $subscribe = this.getSubscribe();
        result = result * 59 + ($subscribe == null ? 43 : $subscribe.hashCode());
        String $subscribeScene = this.getSubscribeScene();
        result = result * 59 + ($subscribeScene == null ? 43 : $subscribeScene.hashCode());
        LocalDateTime $subscribeTime = this.getSubscribeTime();
        result = result * 59 + ($subscribeTime == null ? 43 : ((Object)$subscribeTime).hashCode());
        LocalDateTime $cancelSubscribeTime = this.getCancelSubscribeTime();
        result = result * 59 + ($cancelSubscribeTime == null ? 43 : ((Object)$cancelSubscribeTime).hashCode());
        String $openId = this.getOpenId();
        result = result * 59 + ($openId == null ? 43 : $openId.hashCode());
        String $nickName = this.getNickName();
        result = result * 59 + ($nickName == null ? 43 : $nickName.hashCode());
        String $sex = this.getSex();
        result = result * 59 + ($sex == null ? 43 : $sex.hashCode());
        String $city = this.getCity();
        result = result * 59 + ($city == null ? 43 : $city.hashCode());
        String $country = this.getCountry();
        result = result * 59 + ($country == null ? 43 : $country.hashCode());
        String $province = this.getProvince();
        result = result * 59 + ($province == null ? 43 : $province.hashCode());
        String $phone = this.getPhone();
        result = result * 59 + ($phone == null ? 43 : $phone.hashCode());
        String $language = this.getLanguage();
        result = result * 59 + ($language == null ? 43 : $language.hashCode());
        String $headimgUrl = this.getHeadimgUrl();
        result = result * 59 + ($headimgUrl == null ? 43 : $headimgUrl.hashCode());
        String $unionId = this.getUnionId();
        result = result * 59 + ($unionId == null ? 43 : $unionId.hashCode());
        String $groupId = this.getGroupId();
        result = result * 59 + ($groupId == null ? 43 : $groupId.hashCode());
        result = result * 59 + Arrays.deepHashCode(this.getTagidList());
        String $qrSceneStr = this.getQrSceneStr();
        result = result * 59 + ($qrSceneStr == null ? 43 : $qrSceneStr.hashCode());
        String $sessionKey = this.getSessionKey();
        result = result * 59 + ($sessionKey == null ? 43 : $sessionKey.hashCode());
        return result;
    }
}

