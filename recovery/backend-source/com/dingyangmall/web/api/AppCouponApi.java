/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.dingyangmall.common.core.domain.AjaxResult
 *  com.dingyangmall.common.utils.StringUtils
 *  com.dingyangmall.mall.entity.TbCouponInfo
 *  com.dingyangmall.mall.entity.UmsMember
 *  com.dingyangmall.mall.service.TbCouponInfoService
 *  com.dingyangmall.mall.service.UmsMemberService
 *  com.dingyangmall.mall.utils.MemberUtils
 *  lombok.Generated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.dingyangmall.web.api;

import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.mall.entity.TbCouponInfo;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.service.TbCouponInfoService;
import com.dingyangmall.mall.service.UmsMemberService;
import com.dingyangmall.mall.utils.MemberUtils;
import com.dingyangmall.web.entity.WxMaUser;
import com.dingyangmall.web.mapper.WxMaUserMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import lombok.Generated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/app/coupon", "/api/ma/coupon", "/weixin/api/ma/coupon"})
public class AppCouponApi {
    private final TbCouponInfoService couponInfoService;
    private final WxMaUserMapper wxMaUserMapper;
    private final UmsMemberService umsMemberService;

    private Long resolveToUserId(String memberIdStr) {
        if (StringUtils.isEmpty((String)memberIdStr)) {
            return null;
        }
        try {
            return Long.parseLong(memberIdStr);
        }
        catch (NumberFormatException numberFormatException) {
            WxMaUser wxUser = this.wxMaUserMapper.selectByOpenid(memberIdStr);
            if (wxUser == null || StringUtils.isEmpty((String)wxUser.getPhone())) {
                return null;
            }
            UmsMember member = this.umsMemberService.getOrCreateByPhone(wxUser.getPhone(), wxUser.getNickname(), wxUser.getAvatarUrl());
            return member != null ? member.getId() : null;
        }
    }

    @GetMapping(value={"/my"})
    public AjaxResult getMyCoupons(@RequestParam(required=false) Integer status) {
        String memberIdStr = MemberUtils.getMemberId();
        if (StringUtils.isEmpty((String)memberIdStr)) {
            return AjaxResult.error((String)"\u672a\u767b\u5f55");
        }
        Long userId = this.resolveToUserId(memberIdStr);
        if (userId == null) {
            return AjaxResult.success(Collections.emptyList());
        }
        List list = this.couponInfoService.getUserCoupons(userId, status);
        ArrayList resultList = new ArrayList();
        for (TbCouponInfo coupon : list) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("id", coupon.getId());
            item.put("couponCode", this.maskCouponCode(coupon.getCouponCode()));
            item.put("goodsId", coupon.getGoodsId());
            item.put("goodsName", coupon.getGoodsName());
            item.put("goodsPic", coupon.getGoodsPic());
            item.put("integralPrice", coupon.getIntegralPrice());
            item.put("validityStart", coupon.getValidityStart());
            item.put("validityEnd", coupon.getValidityEnd());
            item.put("couponStatus", coupon.getCouponStatus());
            item.put("verifyTime", coupon.getVerifyTime());
            item.put("verifyDealerId", coupon.getVerifyDealerId());
            item.put("verifyDealerName", coupon.getVerifyDealerName());
            item.put("createTime", coupon.getCreateTime());
            resultList.add(item);
        }
        return AjaxResult.success(resultList);
    }

    private String maskCouponCode(String couponCode) {
        if (StringUtils.isEmpty((String)couponCode) || couponCode.length() <= 6) {
            return "******";
        }
        return couponCode.substring(0, 3) + "******" + couponCode.substring(couponCode.length() - 3);
    }

    @Generated
    public AppCouponApi(TbCouponInfoService couponInfoService, WxMaUserMapper wxMaUserMapper, UmsMemberService umsMemberService) {
        this.couponInfoService = couponInfoService;
        this.wxMaUserMapper = wxMaUserMapper;
        this.umsMemberService = umsMemberService;
    }
}

