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

@TableName(value="tb_coupon_info")
public class TbCouponInfo
extends Model<TbCouponInfo> {
    private static final long serialVersionUID = 1L;
    @TableId(type=IdType.AUTO)
    private Long id;
    private String couponCode;
    private Long userId;
    private String goodsId;
    private String goodsName;
    private String goodsPic;
    private Integer integralPrice;
    private LocalDateTime validityStart;
    private LocalDateTime validityEnd;
    private Integer couponStatus;
    private LocalDateTime verifyTime;
    private Long verifyDealerId;
    private String verifyDealerName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;

    @Generated
    public TbCouponInfo() {
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getCouponCode() {
        return this.couponCode;
    }

    @Generated
    public Long getUserId() {
        return this.userId;
    }

    @Generated
    public String getGoodsId() {
        return this.goodsId;
    }

    @Generated
    public String getGoodsName() {
        return this.goodsName;
    }

    @Generated
    public String getGoodsPic() {
        return this.goodsPic;
    }

    @Generated
    public Integer getIntegralPrice() {
        return this.integralPrice;
    }

    @Generated
    public LocalDateTime getValidityStart() {
        return this.validityStart;
    }

    @Generated
    public LocalDateTime getValidityEnd() {
        return this.validityEnd;
    }

    @Generated
    public Integer getCouponStatus() {
        return this.couponStatus;
    }

    @Generated
    public LocalDateTime getVerifyTime() {
        return this.verifyTime;
    }

    @Generated
    public Long getVerifyDealerId() {
        return this.verifyDealerId;
    }

    @Generated
    public String getVerifyDealerName() {
        return this.verifyDealerName;
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
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    @Generated
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Generated
    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    @Generated
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    @Generated
    public void setGoodsPic(String goodsPic) {
        this.goodsPic = goodsPic;
    }

    @Generated
    public void setIntegralPrice(Integer integralPrice) {
        this.integralPrice = integralPrice;
    }

    @Generated
    public void setValidityStart(LocalDateTime validityStart) {
        this.validityStart = validityStart;
    }

    @Generated
    public void setValidityEnd(LocalDateTime validityEnd) {
        this.validityEnd = validityEnd;
    }

    @Generated
    public void setCouponStatus(Integer couponStatus) {
        this.couponStatus = couponStatus;
    }

    @Generated
    public void setVerifyTime(LocalDateTime verifyTime) {
        this.verifyTime = verifyTime;
    }

    @Generated
    public void setVerifyDealerId(Long verifyDealerId) {
        this.verifyDealerId = verifyDealerId;
    }

    @Generated
    public void setVerifyDealerName(String verifyDealerName) {
        this.verifyDealerName = verifyDealerName;
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
    public String toString() {
        return "TbCouponInfo(id=" + this.getId() + ", couponCode=" + this.getCouponCode() + ", userId=" + this.getUserId() + ", goodsId=" + this.getGoodsId() + ", goodsName=" + this.getGoodsName() + ", goodsPic=" + this.getGoodsPic() + ", integralPrice=" + this.getIntegralPrice() + ", validityStart=" + String.valueOf(this.getValidityStart()) + ", validityEnd=" + String.valueOf(this.getValidityEnd()) + ", couponStatus=" + this.getCouponStatus() + ", verifyTime=" + String.valueOf(this.getVerifyTime()) + ", verifyDealerId=" + this.getVerifyDealerId() + ", verifyDealerName=" + this.getVerifyDealerName() + ", createTime=" + String.valueOf(this.getCreateTime()) + ", updateTime=" + String.valueOf(this.getUpdateTime()) + ", createBy=" + this.getCreateBy() + ")";
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TbCouponInfo)) {
            return false;
        }
        TbCouponInfo other = (TbCouponInfo)o;
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
        Long this$userId = this.getUserId();
        Long other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Integer this$integralPrice = this.getIntegralPrice();
        Integer other$integralPrice = other.getIntegralPrice();
        if (this$integralPrice == null ? other$integralPrice != null : !((Object)this$integralPrice).equals(other$integralPrice)) {
            return false;
        }
        Integer this$couponStatus = this.getCouponStatus();
        Integer other$couponStatus = other.getCouponStatus();
        if (this$couponStatus == null ? other$couponStatus != null : !((Object)this$couponStatus).equals(other$couponStatus)) {
            return false;
        }
        Long this$verifyDealerId = this.getVerifyDealerId();
        Long other$verifyDealerId = other.getVerifyDealerId();
        if (this$verifyDealerId == null ? other$verifyDealerId != null : !((Object)this$verifyDealerId).equals(other$verifyDealerId)) {
            return false;
        }
        String this$couponCode = this.getCouponCode();
        String other$couponCode = other.getCouponCode();
        if (this$couponCode == null ? other$couponCode != null : !this$couponCode.equals(other$couponCode)) {
            return false;
        }
        String this$goodsId = this.getGoodsId();
        String other$goodsId = other.getGoodsId();
        if (this$goodsId == null ? other$goodsId != null : !this$goodsId.equals(other$goodsId)) {
            return false;
        }
        String this$goodsName = this.getGoodsName();
        String other$goodsName = other.getGoodsName();
        if (this$goodsName == null ? other$goodsName != null : !this$goodsName.equals(other$goodsName)) {
            return false;
        }
        String this$goodsPic = this.getGoodsPic();
        String other$goodsPic = other.getGoodsPic();
        if (this$goodsPic == null ? other$goodsPic != null : !this$goodsPic.equals(other$goodsPic)) {
            return false;
        }
        LocalDateTime this$validityStart = this.getValidityStart();
        LocalDateTime other$validityStart = other.getValidityStart();
        if (this$validityStart == null ? other$validityStart != null : !((Object)this$validityStart).equals(other$validityStart)) {
            return false;
        }
        LocalDateTime this$validityEnd = this.getValidityEnd();
        LocalDateTime other$validityEnd = other.getValidityEnd();
        if (this$validityEnd == null ? other$validityEnd != null : !((Object)this$validityEnd).equals(other$validityEnd)) {
            return false;
        }
        LocalDateTime this$verifyTime = this.getVerifyTime();
        LocalDateTime other$verifyTime = other.getVerifyTime();
        if (this$verifyTime == null ? other$verifyTime != null : !((Object)this$verifyTime).equals(other$verifyTime)) {
            return false;
        }
        String this$verifyDealerName = this.getVerifyDealerName();
        String other$verifyDealerName = other.getVerifyDealerName();
        if (this$verifyDealerName == null ? other$verifyDealerName != null : !this$verifyDealerName.equals(other$verifyDealerName)) {
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
        return !(this$createBy == null ? other$createBy != null : !this$createBy.equals(other$createBy));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof TbCouponInfo;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Long $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $integralPrice = this.getIntegralPrice();
        result = result * 59 + ($integralPrice == null ? 43 : ((Object)$integralPrice).hashCode());
        Integer $couponStatus = this.getCouponStatus();
        result = result * 59 + ($couponStatus == null ? 43 : ((Object)$couponStatus).hashCode());
        Long $verifyDealerId = this.getVerifyDealerId();
        result = result * 59 + ($verifyDealerId == null ? 43 : ((Object)$verifyDealerId).hashCode());
        String $couponCode = this.getCouponCode();
        result = result * 59 + ($couponCode == null ? 43 : $couponCode.hashCode());
        String $goodsId = this.getGoodsId();
        result = result * 59 + ($goodsId == null ? 43 : $goodsId.hashCode());
        String $goodsName = this.getGoodsName();
        result = result * 59 + ($goodsName == null ? 43 : $goodsName.hashCode());
        String $goodsPic = this.getGoodsPic();
        result = result * 59 + ($goodsPic == null ? 43 : $goodsPic.hashCode());
        LocalDateTime $validityStart = this.getValidityStart();
        result = result * 59 + ($validityStart == null ? 43 : ((Object)$validityStart).hashCode());
        LocalDateTime $validityEnd = this.getValidityEnd();
        result = result * 59 + ($validityEnd == null ? 43 : ((Object)$validityEnd).hashCode());
        LocalDateTime $verifyTime = this.getVerifyTime();
        result = result * 59 + ($verifyTime == null ? 43 : ((Object)$verifyTime).hashCode());
        String $verifyDealerName = this.getVerifyDealerName();
        result = result * 59 + ($verifyDealerName == null ? 43 : $verifyDealerName.hashCode());
        LocalDateTime $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        LocalDateTime $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        String $createBy = this.getCreateBy();
        result = result * 59 + ($createBy == null ? 43 : $createBy.hashCode());
        return result;
    }
}

