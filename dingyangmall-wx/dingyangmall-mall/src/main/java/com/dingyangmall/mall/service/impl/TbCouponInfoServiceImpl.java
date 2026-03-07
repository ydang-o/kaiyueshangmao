package com.dingyangmall.mall.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyangmall.mall.entity.TbCouponInfo;
import com.dingyangmall.mall.mapper.TbCouponInfoMapper;
import com.dingyangmall.mall.service.TbCouponInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import com.dingyangmall.mall.entity.GoodsSpu;
import com.dingyangmall.mall.service.GoodsSpuService;
import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 商品券信息 服务实现类
 *
 * @author dingyangmall
 * @date 2026-02-12
 */
@Service
public class TbCouponInfoServiceImpl extends ServiceImpl<TbCouponInfoMapper, TbCouponInfo> implements TbCouponInfoService {

    @Autowired
    private GoodsSpuService goodsSpuService;

    @Override
    public TbCouponInfo getValidCouponByCode(String couponCode) {
        return getOne(Wrappers.<TbCouponInfo>lambdaQuery()
                .eq(TbCouponInfo::getCouponCode, couponCode)
                .eq(TbCouponInfo::getCouponStatus, 1) // 1-未使用
                .ge(TbCouponInfo::getValidityEnd, LocalDateTime.now())); // 未过期
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean verifyCoupon(Long couponId, Long dealerId, String dealerName) {
        TbCouponInfo coupon = getById(couponId);
        if (coupon == null || coupon.getCouponStatus() != 1) {
            return false;
        }
        
        coupon.setCouponStatus(2); // 2-已使用
        coupon.setVerifyTime(LocalDateTime.now());
        coupon.setVerifyDealerId(dealerId);
        coupon.setVerifyDealerName(dealerName);
        coupon.setUpdateTime(LocalDateTime.now());
        
        return updateById(coupon);
    }

    @Override
    public java.util.List<TbCouponInfo> getUserCoupons(Long userId, Integer status) {
        return list(Wrappers.<TbCouponInfo>lambdaQuery()
                .eq(TbCouponInfo::getUserId, userId)
                .eq(status != null, TbCouponInfo::getCouponStatus, status)
                .orderByDesc(TbCouponInfo::getCreateTime));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TbCouponInfo createCoupon(Long userId, String goodsId) {
        return createCoupon(userId, goodsId, 365);
    }

    /**
     * 创建商品券（可指定有效天数）
     */
    public TbCouponInfo createCoupon(Long userId, String goodsId, int validityDays) {
        GoodsSpu goodsSpu = goodsSpuService.getById(goodsId);
        if (goodsSpu == null) {
            throw new RuntimeException("商品不存在");
        }
        TbCouponInfo coupon = new TbCouponInfo();
        coupon.setUserId(userId);
        coupon.setCouponCode(IdUtil.simpleUUID().substring(0, 10).toUpperCase());
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
        save(coupon);
        return coupon;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public java.util.List<TbCouponInfo> distributeCoupon(Long userId, String goodsId, int count, int validityDays) {
        java.util.List<TbCouponInfo> list = new java.util.ArrayList<>();
        for (int i = 0; i < Math.min(count, 100); i++) { // 单次最多100张
            list.add(createCoupon(userId, goodsId, validityDays));
        }
        return list;
    }
}
