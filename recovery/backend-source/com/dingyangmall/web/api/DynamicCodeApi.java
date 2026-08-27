/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.dingyangmall.common.core.domain.AjaxResult
 *  com.dingyangmall.common.utils.StringUtils
 *  com.dingyangmall.mall.dto.DynamicCodeDTO
 *  com.dingyangmall.mall.entity.TbCouponInfo
 *  com.dingyangmall.mall.entity.UmsMember
 *  com.dingyangmall.mall.service.DynamicCodeService
 *  com.dingyangmall.mall.service.TbCouponInfoService
 *  com.dingyangmall.mall.service.UmsMemberService
 *  com.dingyangmall.mall.utils.MemberUtils
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.dingyangmall.web.api;

import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.mall.dto.DynamicCodeDTO;
import com.dingyangmall.mall.entity.TbCouponInfo;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.service.DynamicCodeService;
import com.dingyangmall.mall.service.TbCouponInfoService;
import com.dingyangmall.mall.service.UmsMemberService;
import com.dingyangmall.mall.utils.MemberUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/app/dynamic-code"})
public class DynamicCodeApi {
    @Autowired
    private DynamicCodeService dynamicCodeService;
    @Autowired
    private UmsMemberService umsMemberService;
    @Autowired
    private TbCouponInfoService couponInfoService;

    @GetMapping(value={"/member"})
    public AjaxResult getDynamicMemberCode() {
        String memberIdStr = MemberUtils.getMemberId();
        if (StringUtils.isEmpty((String)memberIdStr)) {
            return AjaxResult.error((String)"\u672a\u767b\u5f55");
        }
        try {
            Long memberId = Long.parseLong(memberIdStr);
            UmsMember member = (UmsMember)this.umsMemberService.getById((Serializable)memberId);
            if (member == null) {
                return AjaxResult.error((String)"\u7528\u6237\u4e0d\u5b58\u5728");
            }
            String phoneNumber = member.getPhone();
            if (StringUtils.isEmpty((String)phoneNumber)) {
                return AjaxResult.error((String)"\u7528\u6237\u672a\u7ed1\u5b9a\u624b\u673a\u53f7");
            }
            DynamicCodeDTO dynamicCodeDTO = this.dynamicCodeService.generateDynamicMemberCodeDTO(memberId, member.getMemberCode(), phoneNumber);
            HashMap<String, Object> result = new HashMap<String, Object>();
            result.put("dynamicCode", dynamicCodeDTO.getEncryptedCode());
            result.put("timestamp", dynamicCodeDTO.getTimestamp());
            result.put("expireSeconds", dynamicCodeDTO.getExpireSeconds());
            result.put("memberNickname", member.getNickname());
            result.put("memberCode", this.maskMemberCode(member.getMemberCode()));
            result.put("phone", this.maskPhone(phoneNumber));
            return AjaxResult.success(result);
        }
        catch (NumberFormatException e) {
            return AjaxResult.error((String)"\u65e0\u6548\u7684\u7528\u6237ID");
        }
    }

    @GetMapping(value={"/coupon/{couponId}"})
    public AjaxResult getDynamicCouponCode(@PathVariable Long couponId) {
        String memberIdStr = MemberUtils.getMemberId();
        if (StringUtils.isEmpty((String)memberIdStr)) {
            return AjaxResult.error((String)"\u672a\u767b\u5f55");
        }
        try {
            Long userId = Long.parseLong(memberIdStr);
            UmsMember member = (UmsMember)this.umsMemberService.getById((Serializable)userId);
            if (member == null) {
                return AjaxResult.error((String)"\u7528\u6237\u4e0d\u5b58\u5728");
            }
            String phoneNumber = member.getPhone();
            if (StringUtils.isEmpty((String)phoneNumber)) {
                return AjaxResult.error((String)"\u7528\u6237\u672a\u7ed1\u5b9a\u624b\u673a\u53f7");
            }
            TbCouponInfo coupon = (TbCouponInfo)this.couponInfoService.getById((Serializable)couponId);
            if (coupon == null) {
                return AjaxResult.error((String)"\u4f18\u60e0\u5238\u4e0d\u5b58\u5728");
            }
            if (!coupon.getUserId().equals(userId)) {
                return AjaxResult.error((String)"\u65e0\u6743\u8bbf\u95ee\u6b64\u4f18\u60e0\u5238");
            }
            if (coupon.getCouponStatus() != 1) {
                return AjaxResult.error((String)"\u4f18\u60e0\u5238\u5df2\u4f7f\u7528\u6216\u5df2\u8fc7\u671f");
            }
            DynamicCodeDTO dynamicCodeDTO = this.dynamicCodeService.generateDynamicCouponCodeDTO(couponId, coupon.getCouponCode(), userId, phoneNumber, coupon.getGoodsName());
            HashMap<String, Object> result = new HashMap<String, Object>();
            result.put("dynamicCode", dynamicCodeDTO.getEncryptedCode());
            result.put("timestamp", dynamicCodeDTO.getTimestamp());
            result.put("expireSeconds", dynamicCodeDTO.getExpireSeconds());
            result.put("couponId", couponId);
            result.put("goodsName", coupon.getGoodsName());
            result.put("couponCode", this.maskCouponCode(coupon.getCouponCode()));
            result.put("phone", this.maskPhone(phoneNumber));
            return AjaxResult.success(result);
        }
        catch (NumberFormatException e) {
            return AjaxResult.error((String)"\u65e0\u6548\u7684\u7528\u6237ID");
        }
    }

    @GetMapping(value={"/coupons"})
    public AjaxResult getDynamicCouponCodes() {
        String memberIdStr = MemberUtils.getMemberId();
        if (StringUtils.isEmpty((String)memberIdStr)) {
            return AjaxResult.error((String)"\u672a\u767b\u5f55");
        }
        try {
            Long userId = Long.parseLong(memberIdStr);
            UmsMember member = (UmsMember)this.umsMemberService.getById((Serializable)userId);
            if (member == null) {
                return AjaxResult.error((String)"\u7528\u6237\u4e0d\u5b58\u5728");
            }
            String phoneNumber = member.getPhone();
            if (StringUtils.isEmpty((String)phoneNumber)) {
                return AjaxResult.error((String)"\u7528\u6237\u672a\u7ed1\u5b9a\u624b\u673a\u53f7");
            }
            List coupons = this.couponInfoService.getUserCoupons(userId, Integer.valueOf(1));
            ArrayList resultList = new ArrayList();
            for (TbCouponInfo coupon : coupons) {
                DynamicCodeDTO dynamicCodeDTO = this.dynamicCodeService.generateDynamicCouponCodeDTO(coupon.getId(), coupon.getCouponCode(), userId, phoneNumber, coupon.getGoodsName());
                HashMap<String, Object> item = new HashMap<String, Object>();
                item.put("couponId", coupon.getId());
                item.put("dynamicCode", dynamicCodeDTO.getEncryptedCode());
                item.put("timestamp", dynamicCodeDTO.getTimestamp());
                item.put("expireSeconds", dynamicCodeDTO.getExpireSeconds());
                item.put("goodsName", coupon.getGoodsName());
                item.put("goodsPic", coupon.getGoodsPic());
                item.put("validityEnd", coupon.getValidityEnd());
                item.put("couponCode", this.maskCouponCode(coupon.getCouponCode()));
                resultList.add(item);
            }
            return AjaxResult.success(resultList);
        }
        catch (NumberFormatException e) {
            return AjaxResult.error((String)"\u65e0\u6548\u7684\u7528\u6237ID");
        }
    }

    private String maskMemberCode(String memberCode) {
        if (StringUtils.isEmpty((String)memberCode) || memberCode.length() <= 4) {
            return "****";
        }
        return memberCode.substring(0, 2) + "****" + memberCode.substring(memberCode.length() - 2);
    }

    private String maskCouponCode(String couponCode) {
        if (StringUtils.isEmpty((String)couponCode) || couponCode.length() <= 6) {
            return "******";
        }
        return couponCode.substring(0, 3) + "******" + couponCode.substring(couponCode.length() - 3);
    }

    private String maskPhone(String phone) {
        if (StringUtils.isEmpty((String)phone) || phone.length() != 11) {
            return "***********";
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }
}

