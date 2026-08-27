/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dingyangmall.mall.entity.TbCouponInfo;
import java.util.List;

public interface TbCouponInfoService
extends IService<TbCouponInfo> {
    public TbCouponInfo getValidCouponByCode(String var1);

    public boolean verifyCoupon(Long var1, Long var2, String var3);

    public boolean verifyCouponByCode(String var1, Long var2, String var3);

    public List<TbCouponInfo> getUserCoupons(Long var1, Integer var2);

    public TbCouponInfo createCoupon(Long var1, String var2);

    public List<TbCouponInfo> distributeCoupon(Long var1, String var2, int var3, int var4);

    public TbCouponInfo getByCouponCode(String var1);

    public boolean verifyCouponById(Long var1, Long var2, String var3);

    public List<TbCouponInfo> getMemberValidCoupons(Long var1);
}

