/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.dingyangmall.common.annotation.Excel;
import com.dingyangmall.framework.config.typehandler.ArrayStringTypeHandler;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import lombok.Generated;
import org.apache.ibatis.type.JdbcType;

@TableName(value="goods_spu")
public class GoodsSpu
extends Model<GoodsSpu> {
    private static final long serialVersionUID = 1L;
    @Excel(name="PK")
    @TableId(type=IdType.ASSIGN_ID)
    private String id;
    @Excel(name="spu\u7f16\u7801")
    private String spuCode;
    @Excel(name="spu\u540d\u5b57")
    private String name;
    @Excel(name="\u5356\u70b9")
    private String sellPoint;
    @Excel(name="\u63cf\u8ff0")
    private String description;
    @Excel(name="\u4e00\u7ea7\u5206\u7c7bID")
    private String categoryFirst;
    @Excel(name="\u4e8c\u7ea7\u5206\u7c7bID")
    private String categorySecond;
    @Excel(name="\u5546\u54c1\u4e3b\u56fe")
    @TableField(typeHandler=ArrayStringTypeHandler.class, jdbcType=JdbcType.VARCHAR)
    private String[] picUrls;
    @Excel(name="\u662f\u5426\u4e0a\u67b6\uff080\u5426 1\u662f\uff09")
    private String shelf;
    @Excel(name="\u6392\u5e8f\u5b57\u6bb5")
    private Integer sort;
    @Excel(name="\u9500\u552e\u4ef7\u683c")
    private BigDecimal salesPrice;
    @Excel(name="\u5e02\u573a\u4ef7")
    private BigDecimal marketPrice;
    @Excel(name="\u6210\u672c\u4ef7")
    private BigDecimal costPrice;
    @Excel(name="\u5e93\u5b58")
    private Integer stock;
    @Excel(name="\u9500\u91cf")
    private Integer saleNum;
    @Excel(name="\u5546\u54c1\u7c7b\u578b\uff080\uff1a\u666e\u901a\u5546\u54c1\uff1b1\uff1a\u865a\u62df\u5546\u54c1\uff1b2\uff1a\u5546\u54c1\u5238\uff09")
    private String goodsType;
    @Excel(name="\u5546\u54c1\u5238\u5b50\u7c7b\u578b\uff081\uff1a\u7ebf\u4e0a\u4f18\u60e0\u5238\uff1b2\uff1a\u7ebf\u4e0b\u6838\u9500\u5238\uff09")
    private String couponType;
    @Excel(name="\u79ef\u5206\u4ef7\u683c")
    private Integer integralPrice;
    private transient Integer integralPriceGt;
    @Excel(name="\u521b\u5efa\u65f6\u95f4")
    private LocalDateTime createTime;
    @Excel(name="\u6700\u540e\u66f4\u65b0\u65f6\u95f4")
    private LocalDateTime updateTime;
    @Excel(name="\u903b\u8f91\u5220\u9664\u6807\u8bb0\uff080\uff1a\u663e\u793a\uff1b1\uff1a\u9690\u85cf\uff09")
    private String delFlag;
    @Version
    private Integer version;

    @Generated
    public GoodsSpu() {
    }

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getSpuCode() {
        return this.spuCode;
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public String getSellPoint() {
        return this.sellPoint;
    }

    @Generated
    public String getDescription() {
        return this.description;
    }

    @Generated
    public String getCategoryFirst() {
        return this.categoryFirst;
    }

    @Generated
    public String getCategorySecond() {
        return this.categorySecond;
    }

    @Generated
    public String[] getPicUrls() {
        return this.picUrls;
    }

    @Generated
    public String getShelf() {
        return this.shelf;
    }

    @Generated
    public Integer getSort() {
        return this.sort;
    }

    @Generated
    public BigDecimal getSalesPrice() {
        return this.salesPrice;
    }

    @Generated
    public BigDecimal getMarketPrice() {
        return this.marketPrice;
    }

    @Generated
    public BigDecimal getCostPrice() {
        return this.costPrice;
    }

    @Generated
    public Integer getStock() {
        return this.stock;
    }

    @Generated
    public Integer getSaleNum() {
        return this.saleNum;
    }

    @Generated
    public String getGoodsType() {
        return this.goodsType;
    }

    @Generated
    public String getCouponType() {
        return this.couponType;
    }

    @Generated
    public Integer getIntegralPrice() {
        return this.integralPrice;
    }

    @Generated
    public Integer getIntegralPriceGt() {
        return this.integralPriceGt;
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
    public String getDelFlag() {
        return this.delFlag;
    }

    @Generated
    public Integer getVersion() {
        return this.version;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
    }

    @Generated
    public void setSpuCode(String spuCode) {
        this.spuCode = spuCode;
    }

    @Generated
    public void setName(String name) {
        this.name = name;
    }

    @Generated
    public void setSellPoint(String sellPoint) {
        this.sellPoint = sellPoint;
    }

    @Generated
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    public void setCategoryFirst(String categoryFirst) {
        this.categoryFirst = categoryFirst;
    }

    @Generated
    public void setCategorySecond(String categorySecond) {
        this.categorySecond = categorySecond;
    }

    @Generated
    public void setPicUrls(String[] picUrls) {
        this.picUrls = picUrls;
    }

    @Generated
    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

    @Generated
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Generated
    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    @Generated
    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    @Generated
    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    @Generated
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Generated
    public void setSaleNum(Integer saleNum) {
        this.saleNum = saleNum;
    }

    @Generated
    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    @Generated
    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    @Generated
    public void setIntegralPrice(Integer integralPrice) {
        this.integralPrice = integralPrice;
    }

    @Generated
    public void setIntegralPriceGt(Integer integralPriceGt) {
        this.integralPriceGt = integralPriceGt;
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
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Generated
    public void setVersion(Integer version) {
        this.version = version;
    }

    @Generated
    public String toString() {
        return "GoodsSpu(id=" + this.getId() + ", spuCode=" + this.getSpuCode() + ", name=" + this.getName() + ", sellPoint=" + this.getSellPoint() + ", description=" + this.getDescription() + ", categoryFirst=" + this.getCategoryFirst() + ", categorySecond=" + this.getCategorySecond() + ", picUrls=" + Arrays.deepToString(this.getPicUrls()) + ", shelf=" + this.getShelf() + ", sort=" + this.getSort() + ", salesPrice=" + String.valueOf(this.getSalesPrice()) + ", marketPrice=" + String.valueOf(this.getMarketPrice()) + ", costPrice=" + String.valueOf(this.getCostPrice()) + ", stock=" + this.getStock() + ", saleNum=" + this.getSaleNum() + ", goodsType=" + this.getGoodsType() + ", couponType=" + this.getCouponType() + ", integralPrice=" + this.getIntegralPrice() + ", integralPriceGt=" + this.getIntegralPriceGt() + ", createTime=" + String.valueOf(this.getCreateTime()) + ", updateTime=" + String.valueOf(this.getUpdateTime()) + ", delFlag=" + this.getDelFlag() + ", version=" + this.getVersion() + ")";
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GoodsSpu)) {
            return false;
        }
        GoodsSpu other = (GoodsSpu)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Integer this$sort = this.getSort();
        Integer other$sort = other.getSort();
        if (this$sort == null ? other$sort != null : !((Object)this$sort).equals(other$sort)) {
            return false;
        }
        Integer this$stock = this.getStock();
        Integer other$stock = other.getStock();
        if (this$stock == null ? other$stock != null : !((Object)this$stock).equals(other$stock)) {
            return false;
        }
        Integer this$saleNum = this.getSaleNum();
        Integer other$saleNum = other.getSaleNum();
        if (this$saleNum == null ? other$saleNum != null : !((Object)this$saleNum).equals(other$saleNum)) {
            return false;
        }
        Integer this$integralPrice = this.getIntegralPrice();
        Integer other$integralPrice = other.getIntegralPrice();
        if (this$integralPrice == null ? other$integralPrice != null : !((Object)this$integralPrice).equals(other$integralPrice)) {
            return false;
        }
        Integer this$version = this.getVersion();
        Integer other$version = other.getVersion();
        if (this$version == null ? other$version != null : !((Object)this$version).equals(other$version)) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$spuCode = this.getSpuCode();
        String other$spuCode = other.getSpuCode();
        if (this$spuCode == null ? other$spuCode != null : !this$spuCode.equals(other$spuCode)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$sellPoint = this.getSellPoint();
        String other$sellPoint = other.getSellPoint();
        if (this$sellPoint == null ? other$sellPoint != null : !this$sellPoint.equals(other$sellPoint)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) {
            return false;
        }
        String this$categoryFirst = this.getCategoryFirst();
        String other$categoryFirst = other.getCategoryFirst();
        if (this$categoryFirst == null ? other$categoryFirst != null : !this$categoryFirst.equals(other$categoryFirst)) {
            return false;
        }
        String this$categorySecond = this.getCategorySecond();
        String other$categorySecond = other.getCategorySecond();
        if (this$categorySecond == null ? other$categorySecond != null : !this$categorySecond.equals(other$categorySecond)) {
            return false;
        }
        if (!Arrays.deepEquals(this.getPicUrls(), other.getPicUrls())) {
            return false;
        }
        String this$shelf = this.getShelf();
        String other$shelf = other.getShelf();
        if (this$shelf == null ? other$shelf != null : !this$shelf.equals(other$shelf)) {
            return false;
        }
        BigDecimal this$salesPrice = this.getSalesPrice();
        BigDecimal other$salesPrice = other.getSalesPrice();
        if (this$salesPrice == null ? other$salesPrice != null : !((Object)this$salesPrice).equals(other$salesPrice)) {
            return false;
        }
        BigDecimal this$marketPrice = this.getMarketPrice();
        BigDecimal other$marketPrice = other.getMarketPrice();
        if (this$marketPrice == null ? other$marketPrice != null : !((Object)this$marketPrice).equals(other$marketPrice)) {
            return false;
        }
        BigDecimal this$costPrice = this.getCostPrice();
        BigDecimal other$costPrice = other.getCostPrice();
        if (this$costPrice == null ? other$costPrice != null : !((Object)this$costPrice).equals(other$costPrice)) {
            return false;
        }
        String this$goodsType = this.getGoodsType();
        String other$goodsType = other.getGoodsType();
        if (this$goodsType == null ? other$goodsType != null : !this$goodsType.equals(other$goodsType)) {
            return false;
        }
        String this$couponType = this.getCouponType();
        String other$couponType = other.getCouponType();
        if (this$couponType == null ? other$couponType != null : !this$couponType.equals(other$couponType)) {
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
        String this$delFlag = this.getDelFlag();
        String other$delFlag = other.getDelFlag();
        return !(this$delFlag == null ? other$delFlag != null : !this$delFlag.equals(other$delFlag));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GoodsSpu;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        Integer $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : ((Object)$sort).hashCode());
        Integer $stock = this.getStock();
        result = result * 59 + ($stock == null ? 43 : ((Object)$stock).hashCode());
        Integer $saleNum = this.getSaleNum();
        result = result * 59 + ($saleNum == null ? 43 : ((Object)$saleNum).hashCode());
        Integer $integralPrice = this.getIntegralPrice();
        result = result * 59 + ($integralPrice == null ? 43 : ((Object)$integralPrice).hashCode());
        Integer $version = this.getVersion();
        result = result * 59 + ($version == null ? 43 : ((Object)$version).hashCode());
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $spuCode = this.getSpuCode();
        result = result * 59 + ($spuCode == null ? 43 : $spuCode.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $sellPoint = this.getSellPoint();
        result = result * 59 + ($sellPoint == null ? 43 : $sellPoint.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        String $categoryFirst = this.getCategoryFirst();
        result = result * 59 + ($categoryFirst == null ? 43 : $categoryFirst.hashCode());
        String $categorySecond = this.getCategorySecond();
        result = result * 59 + ($categorySecond == null ? 43 : $categorySecond.hashCode());
        result = result * 59 + Arrays.deepHashCode(this.getPicUrls());
        String $shelf = this.getShelf();
        result = result * 59 + ($shelf == null ? 43 : $shelf.hashCode());
        BigDecimal $salesPrice = this.getSalesPrice();
        result = result * 59 + ($salesPrice == null ? 43 : ((Object)$salesPrice).hashCode());
        BigDecimal $marketPrice = this.getMarketPrice();
        result = result * 59 + ($marketPrice == null ? 43 : ((Object)$marketPrice).hashCode());
        BigDecimal $costPrice = this.getCostPrice();
        result = result * 59 + ($costPrice == null ? 43 : ((Object)$costPrice).hashCode());
        String $goodsType = this.getGoodsType();
        result = result * 59 + ($goodsType == null ? 43 : $goodsType.hashCode());
        String $couponType = this.getCouponType();
        result = result * 59 + ($couponType == null ? 43 : $couponType.hashCode());
        LocalDateTime $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        LocalDateTime $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        String $delFlag = this.getDelFlag();
        result = result * 59 + ($delFlag == null ? 43 : $delFlag.hashCode());
        return result;
    }
}

