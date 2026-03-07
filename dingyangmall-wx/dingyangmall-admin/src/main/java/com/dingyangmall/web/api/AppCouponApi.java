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
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * C端优惠券接口
 * 小程序 token 中 memberId 为 openid 时，通过 openid→wx_user→ums_member 解析为 userId 再查券
 *
 * @author dingyangmall
 * @date 2026-02-13
 */
@RestController
@RequestMapping(value = { "/app/coupon", "/api/ma/coupon", "/weixin/api/ma/coupon" })
@AllArgsConstructor
public class AppCouponApi {

    private final TbCouponInfoService couponInfoService;
    private final WxMaUserMapper wxMaUserMapper;
    private final UmsMemberService umsMemberService;

    /**
     * 将 token 中的 memberId（openid 或数字 id）解析为 ums_member.id
     */
    private Long resolveToUserId(String memberIdStr) {
        if (StringUtils.isEmpty(memberIdStr)) return null;
        try {
            return Long.parseLong(memberIdStr);
        } catch (NumberFormatException e) {
            // memberId 为 openid
        }
        WxMaUser wxUser = wxMaUserMapper.selectByOpenid(memberIdStr);
        if (wxUser == null || StringUtils.isEmpty(wxUser.getPhone())) return null;
        UmsMember member = umsMemberService.getOrCreateByPhone(
                wxUser.getPhone(), wxUser.getNickname(), wxUser.getAvatarUrl());
        return member != null ? member.getId() : null;
    }

    /**
     * 获取我的优惠券/代金券列表（移动端）
     * @param status 状态：1-未使用 2-已使用 3-已过期；不传查全部
     */
    @GetMapping("/my")
    public AjaxResult getMyCoupons(@RequestParam(required = false) Integer status) {
        String memberIdStr = MemberUtils.getMemberId();
        if (StringUtils.isEmpty(memberIdStr)) {
            return AjaxResult.error("未登录");
        }
        Long userId = resolveToUserId(memberIdStr);
        if (userId == null) {
            return AjaxResult.success(Collections.emptyList());
        }
        List<TbCouponInfo> list = couponInfoService.getUserCoupons(userId, status);
        return AjaxResult.success(list);
    }
}
