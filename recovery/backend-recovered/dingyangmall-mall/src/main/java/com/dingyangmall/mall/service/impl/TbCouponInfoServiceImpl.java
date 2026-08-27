/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyangmall.mall.entity.GoodsSpu;
import com.dingyangmall.mall.entity.TbCouponInfo;
import com.dingyangmall.mall.mapper.TbCouponInfoMapper;
import com.dingyangmall.mall.service.GoodsSpuService;
import com.dingyangmall.mall.service.TbCouponInfoService;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TbCouponInfoServiceImpl
extends ServiceImpl<TbCouponInfoMapper, TbCouponInfo>
implements TbCouponInfoService {
    @Autowired
    private GoodsSpuService goodsSpuService;

    @Override
    public TbCouponInfo getValidCouponByCode(String couponCode) {
        return (TbCouponInfo)this.getOne((Wrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(TbCouponInfo::getCouponCode, couponCode)).eq(TbCouponInfo::getCouponStatus, 1)).ge(TbCouponInfo::getValidityEnd, LocalDateTime.now()));
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public boolean verifyCoupon(Long couponId, Long dealerId, String dealerName) {
        TbCouponInfo coupon = (TbCouponInfo)this.getById(couponId);
        if (coupon == null || coupon.getCouponStatus() != 1) {
            return false;
        }
        coupon.setCouponStatus(2);
        coupon.setVerifyTime(LocalDateTime.now());
        coupon.setVerifyDealerId(dealerId);
        coupon.setVerifyDealerName(dealerName);
        coupon.setUpdateTime(LocalDateTime.now());
        return this.updateById(coupon);
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public boolean verifyCouponByCode(String couponCode, Long dealerId, String dealerName) {
        TbCouponInfo coupon = this.getValidCouponByCode(couponCode);
        if (coupon == null) {
            return false;
        }
        coupon.setCouponStatus(2);
        coupon.setVerifyTime(LocalDateTime.now());
        coupon.setVerifyDealerId(dealerId);
        coupon.setVerifyDealerName(dealerName);
        coupon.setUpdateTime(LocalDateTime.now());
        return this.updateById(coupon);
    }

    @Override
    public List<TbCouponInfo> getUserCoupons(Long userId, Integer status) {
        return this.list((Wrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(TbCouponInfo::getUserId, userId)).eq(status != null, TbCouponInfo::getCouponStatus, (Object)status)).orderByDesc(TbCouponInfo::getCreateTime));
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public TbCouponInfo createCoupon(Long userId, String goodsId) {
        return this.createCoupon(userId, goodsId, 365);
    }

    public TbCouponInfo createCoupon(Long userId, String goodsId, int validityDays) {
        GoodsSpu goodsSpu = (GoodsSpu)this.goodsSpuService.getById((Serializable)((Object)goodsId));
        if (goodsSpu == null) {
            throw new RuntimeException("\u5546\u54c1\u4e0d\u5b58\u5728");
        }
        TbCouponInfo coupon = new TbCouponInfo();
        coupon.setUserId(userId);
        coupon.setGoodsId(goodsId);
        coupon.setGoodsName(goodsSpu.getName());
        coupon.setGoodsPic(goodsSpu.getPicUrls() != null && goodsSpu.getPicUrls().length > 0 ? goodsSpu.getPicUrls()[0] : "");
        coupon.setIntegralPrice(goodsSpu.getIntegralPrice());
        coupon.setCouponStatus(1);
        coupon.setValidityStart(LocalDateTime.now());
        coupon.setValidityEnd(LocalDateTime.now().plusDays(Math.max(1, validityDays)));
        coupon.setCreateTime(LocalDateTime.now());
        coupon.setUpdateTime(LocalDateTime.now());
        coupon.setCreateBy(String.valueOf(userId));
        this.save(coupon);
        String couponCode = "CP" + String.format("%08d", coupon.getId());
        coupon.setCouponCode(couponCode);
        this.updateById(coupon);
        return coupon;
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public List<TbCouponInfo> distributeCoupon(Long userId, String goodsId, int count, int validityDays) {
        ArrayList<TbCouponInfo> list = new ArrayList<TbCouponInfo>();
        for (int i = 0; i < Math.min(count, 100); ++i) {
            list.add(this.createCoupon(userId, goodsId, validityDays));
        }
        return list;
    }

    @Override
    public TbCouponInfo getByCouponCode(String couponCode) {
        return (TbCouponInfo)this.getOne((Wrapper)Wrappers.lambdaQuery().eq(TbCouponInfo::getCouponCode, couponCode));
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public boolean verifyCouponById(Long couponId, Long dealerId, String dealerName) {
        TbCouponInfo coupon = (TbCouponInfo)this.getById(couponId);
        if (coupon == null || coupon.getCouponStatus() != 1) {
            return false;
        }
        if (coupon.getValidityEnd() != null && coupon.getValidityEnd().isBefore(LocalDateTime.now())) {
            return false;
        }
        coupon.setCouponStatus(2);
        coupon.setVerifyTime(LocalDateTime.now());
        coupon.setVerifyDealerId(dealerId);
        coupon.setVerifyDealerName(dealerName);
        coupon.setUpdateTime(LocalDateTime.now());
        return this.updateById(coupon);
    }

    @Override
    public List<TbCouponInfo> getMemberValidCoupons(Long memberId) {
        return this.list((Wrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(TbCouponInfo::getUserId, memberId)).eq(TbCouponInfo::getCouponStatus, 1)).ge(TbCouponInfo::getValidityEnd, LocalDateTime.now())).orderByDesc(TbCouponInfo::getCreateTime));
    }
}

