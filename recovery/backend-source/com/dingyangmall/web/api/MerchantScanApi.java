/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.toolkit.Wrappers
 *  com.dingyangmall.common.annotation.RepeatSubmit
 *  com.dingyangmall.common.core.domain.AjaxResult
 *  com.dingyangmall.common.core.domain.entity.SysUser
 *  com.dingyangmall.common.utils.SecurityUtils
 *  com.dingyangmall.common.utils.StringUtils
 *  com.dingyangmall.framework.web.service.SmsService
 *  com.dingyangmall.mall.entity.TbCouponInfo
 *  com.dingyangmall.mall.entity.UmsMember
 *  com.dingyangmall.mall.service.TbCouponInfoService
 *  com.dingyangmall.mall.service.TbIntegralFlowService
 *  com.dingyangmall.mall.service.UmsMemberService
 *  com.dingyangmall.system.service.ISysUserService
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.data.redis.core.RedisTemplate
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.dingyangmall.web.api;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dingyangmall.common.annotation.RepeatSubmit;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.core.domain.entity.SysUser;
import com.dingyangmall.common.utils.SecurityUtils;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.framework.web.service.SmsService;
import com.dingyangmall.mall.entity.TbCouponInfo;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.service.TbCouponInfoService;
import com.dingyangmall.mall.service.TbIntegralFlowService;
import com.dingyangmall.mall.service.UmsMemberService;
import com.dingyangmall.system.service.ISysUserService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/mall/merchant/scan"})
public class MerchantScanApi {
    private static final Logger log = LoggerFactory.getLogger(MerchantScanApi.class);
    private static final String IDEMPOTENT_PREFIX = "merchant:grant:idempotent:";
    private static final long IDEMPOTENT_EXPIRE = 30L;
    @Autowired
    private UmsMemberService umsMemberService;
    @Autowired
    private TbIntegralFlowService integralFlowService;
    @Autowired
    private TbCouponInfoService couponInfoService;
    @Autowired(required=false)
    private SmsService smsService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Value(value="${mall.merchant.give-points-require-sms:false}")
    private boolean givePointsRequireSms;

    @GetMapping(value={"/user/{memberCode}"})
    public AjaxResult identifyUser(@PathVariable String memberCode) {
        UmsMember member = this.umsMemberService.getByMemberCode(memberCode);
        if (member == null) {
            member = this.umsMemberService.getByPhone(memberCode);
        }
        if (member == null) {
            return AjaxResult.error((String)"\u65e0\u6548\u7684\u4f1a\u5458\u7801\u6216\u624b\u673a\u53f7");
        }
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("userId", String.valueOf(member.getId()));
        result.put("nickname", member.getNickname());
        result.put("phone", member.getPhone());
        result.put("points", member.getPoints());
        result.put("level", member.getLevel());
        return AjaxResult.success(result);
    }

    @RepeatSubmit(interval=2000, message="\u8bf7\u52ff\u91cd\u590d\u63d0\u4ea4")
    @PostMapping(value={"/points"})
    public AjaxResult givePoints(@RequestBody Map<String, Object> body) {
        UmsMember member;
        SysUser dealer;
        String smsCode;
        String memberCode = (String)body.get("memberCode");
        Integer points = (Integer)body.get("points");
        String string = smsCode = body != null && body.get("smsCode") != null ? body.get("smsCode").toString().trim() : null;
        if (points == null || points <= 0) {
            return AjaxResult.error((String)"\u79ef\u5206\u6570\u91cf\u5fc5\u987b\u5927\u4e8e0");
        }
        try {
            dealer = SecurityUtils.getLoginUser().getUser();
        }
        catch (Exception e) {
            return AjaxResult.error((String)"\u672a\u767b\u5f55\u6216\u767b\u5f55\u5df2\u8fc7\u671f");
        }
        if (dealer == null) {
            return AjaxResult.error((String)"\u672a\u767b\u5f55\u6216\u767b\u5f55\u5df2\u8fc7\u671f");
        }
        String dealerPhone = dealer.getPhonenumber();
        if (this.givePointsRequireSms || smsCode != null && !smsCode.isEmpty()) {
            if (this.smsService == null) {
                return AjaxResult.error((String)"\u77ed\u4fe1\u670d\u52a1\u672a\u914d\u7f6e\uff0c\u65e0\u6cd5\u6821\u9a8c\u9a8c\u8bc1\u7801");
            }
            if (StringUtils.isEmpty((String)dealerPhone)) {
                return AjaxResult.error((String)"\u8bf7\u5148\u5b8c\u5584\u5546\u5bb6\u624b\u673a\u53f7\u540e\u518d\u8d60\u9001\u79ef\u5206");
            }
            if (StringUtils.isEmpty((String)smsCode)) {
                return AjaxResult.error((String)"\u8bf7\u586b\u5199\u77ed\u4fe1\u9a8c\u8bc1\u7801");
            }
            try {
                this.smsService.validateSmsCode(dealerPhone, smsCode);
            }
            catch (Exception e) {
                return AjaxResult.error((String)"\u9a8c\u8bc1\u7801\u9519\u8bef\u6216\u5df2\u8fc7\u671f\uff0c\u8bf7\u91cd\u65b0\u83b7\u53d6");
            }
        }
        if ((member = this.umsMemberService.getByMemberCode(memberCode)) == null) {
            member = this.umsMemberService.getByPhone(memberCode);
        }
        if (member == null) {
            return AjaxResult.error((String)"\u65e0\u6548\u7684\u4f1a\u5458\u7801\u6216\u624b\u673a\u53f7");
        }
        String remark = "\u5546\u5bb6[" + (dealer != null ? dealer.getNickName() : "") + "]\u626b\u7801\u8d60\u9001";
        if (dealer != null && !dealer.isAdmin()) {
            List dealerMemberList = this.umsMemberService.list((Wrapper)Wrappers.lambdaQuery().eq(UmsMember::getPhone, (Object)dealer.getPhonenumber()));
            if (dealerMemberList.isEmpty()) {
                return AjaxResult.error((String)"\u5f53\u524d\u5546\u5bb6\u672a\u7ed1\u5b9a\u4f1a\u5458\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458");
            }
            UmsMember dealerMember = (UmsMember)dealerMemberList.get(0);
            if (dealerMember.getPoints() == null || dealerMember.getPoints() < points) {
                return AjaxResult.error((String)("\u79ef\u5206\u4f59\u989d\u4e0d\u8db3\uff0c\u5f53\u524d\u4f59\u989d\uff1a" + (dealerMember.getPoints() != null ? dealerMember.getPoints() : 0) + "\uff0c\u9700\u8981\u53d1\u653e\uff1a" + points));
            }
            this.integralFlowService.addPoints(dealerMember.getId(), Integer.valueOf(-points.intValue()), Integer.valueOf(2), "\u626b\u7801\u8d60\u9001\u79ef\u5206\u7ed9\u4f1a\u5458[" + member.getNickname() + "]");
        }
        log.info("[\u5546\u5bb6\u8d60\u9001\u79ef\u5206] \u5f00\u59cb: \u5546\u5bb6={}, \u4f1a\u5458ID={}, \u79ef\u5206={}", new Object[]{dealer != null ? dealer.getNickName() : "\u672a\u77e5", member.getId(), points});
        this.integralFlowService.addPoints(member.getId(), points, Integer.valueOf(2), remark);
        log.info("[\u5546\u5bb6\u8d60\u9001\u79ef\u5206] \u5b8c\u6210: \u4f1a\u5458ID={}, \u79ef\u5206={}", (Object)member.getId(), (Object)points);
        return AjaxResult.success((String)"\u8d60\u9001\u6210\u529f");
    }

    @PostMapping(value={"/coupon/verify"})
    public AjaxResult verifyCoupon(@RequestBody Map<String, String> body) {
        SysUser dealer;
        String couponCode = body.get("couponCode");
        TbCouponInfo coupon = this.couponInfoService.getValidCouponByCode(couponCode);
        if (coupon == null) {
            return AjaxResult.error((String)"\u65e0\u6548\u6216\u5df2\u8fc7\u671f\u7684\u5546\u54c1\u5238");
        }
        try {
            dealer = SecurityUtils.getLoginUser().getUser();
        }
        catch (Exception e) {
            return AjaxResult.error((String)"\u672a\u767b\u5f55\u6216\u767b\u5f55\u5df2\u8fc7\u671f");
        }
        if (dealer == null) {
            return AjaxResult.error((String)"\u672a\u767b\u5f55\u6216\u767b\u5f55\u5df2\u8fc7\u671f");
        }
        boolean success = this.couponInfoService.verifyCoupon(coupon.getId(), dealer.getUserId(), dealer.getNickName());
        if (success) {
            return AjaxResult.success((String)"\u6838\u9500\u6210\u529f");
        }
        return AjaxResult.error((String)"\u6838\u9500\u5931\u8d25");
    }

    @PostMapping(value={"/change-password"})
    public AjaxResult changePassword(@RequestBody Map<String, String> body) {
        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");
        if (StringUtils.isEmpty((String)oldPassword)) {
            return AjaxResult.error((String)"\u8bf7\u8f93\u5165\u539f\u5bc6\u7801");
        }
        if (StringUtils.isEmpty((String)newPassword)) {
            return AjaxResult.error((String)"\u8bf7\u8f93\u5165\u65b0\u5bc6\u7801");
        }
        if (newPassword.length() < 6) {
            return AjaxResult.error((String)"\u65b0\u5bc6\u7801\u957f\u5ea6\u4e0d\u80fd\u5c11\u4e8e6\u4f4d");
        }
        if (oldPassword.equals(newPassword)) {
            return AjaxResult.error((String)"\u65b0\u5bc6\u7801\u4e0d\u80fd\u4e0e\u539f\u5bc6\u7801\u76f8\u540c");
        }
        SysUser currentUser = SecurityUtils.getLoginUser().getUser();
        if (currentUser == null || StringUtils.isEmpty((String)currentUser.getUserName())) {
            return AjaxResult.error((String)"\u672a\u767b\u5f55\u6216\u767b\u5f55\u5df2\u8fc7\u671f");
        }
        String userName = currentUser.getUserName();
        SysUser user = this.sysUserService.selectUserByUserName(userName);
        if (user == null) {
            return AjaxResult.error((String)"\u7528\u6237\u4e0d\u5b58\u5728");
        }
        if (!SecurityUtils.matchesPassword((String)oldPassword, (String)user.getPassword())) {
            return AjaxResult.error((String)"\u539f\u5bc6\u7801\u9519\u8bef");
        }
        String encryptedNewPassword = SecurityUtils.encryptPassword((String)newPassword);
        int result = this.sysUserService.resetUserPwd(userName, encryptedNewPassword);
        if (result > 0) {
            return AjaxResult.success((String)"\u5bc6\u7801\u4fee\u6539\u6210\u529f\uff0c\u8bf7\u91cd\u65b0\u767b\u5f55");
        }
        return AjaxResult.error((String)"\u5bc6\u7801\u4fee\u6539\u5931\u8d25\uff0c\u8bf7\u91cd\u8bd5");
    }
}

