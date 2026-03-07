package com.dingyangmall.web.api;

import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.core.domain.entity.SysUser;
import com.dingyangmall.common.utils.SecurityUtils;
import com.dingyangmall.mall.entity.TbCouponInfo;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.service.TbCouponInfoService;
import com.dingyangmall.mall.service.TbIntegralFlowService;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.framework.web.service.SmsService;
import com.dingyangmall.mall.service.UmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 商家端扫码接口
 */
@RestController
@RequestMapping("/api/mall/merchant/scan")
public class MerchantScanApi {

    @Autowired
    private UmsMemberService umsMemberService;

    @Autowired
    private TbIntegralFlowService integralFlowService;

    @Autowired
    private TbCouponInfoService couponInfoService;

    @Autowired(required = false)
    private SmsService smsService;

    /** 为 true 时，商家赠送积分必须传 smsCode 且校验通过 */
    @Value("${mall.merchant.give-points-require-sms:false}")
    private boolean givePointsRequireSms;

    /**
     * 识别用户 (扫会员码)
     * @param memberCode 会员码
     */
    @GetMapping("/user/{memberCode}")
    public AjaxResult identifyUser(@PathVariable String memberCode) {
        UmsMember member = umsMemberService.getByMemberCode(memberCode);
        if (member == null) {
            member = umsMemberService.getByPhone(memberCode);
        }
        
        if (member == null) {
            return AjaxResult.error("无效的会员码或手机号");
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("userId", member.getId());
        result.put("nickname", member.getNickname());
        result.put("phone", member.getPhone());
        result.put("points", member.getPoints());
        result.put("level", member.getLevel());
        
        return AjaxResult.success(result);
    }

    /**
     * 商家赠送积分（可选短信验证：body 传 smsCode 时校验当前商家手机号验证码；
     * 若配置 mall.merchant.give-points-require-sms=true 则必须传 smsCode 并校验通过）
     */
    @PostMapping("/points")
    public AjaxResult givePoints(@RequestBody Map<String, Object> body) {
        String memberCode = (String) body.get("memberCode");
        Integer points = (Integer) body.get("points");
        String smsCode = body != null && body.get("smsCode") != null ? body.get("smsCode").toString().trim() : null;

        if (points == null || points <= 0) {
            return AjaxResult.error("积分数量必须大于0");
        }

        SysUser dealer = SecurityUtils.getLoginUser().getUser();
        String dealerPhone = dealer != null ? dealer.getPhonenumber() : null;
        if (givePointsRequireSms || (smsCode != null && !smsCode.isEmpty())) {
            if (smsService == null) {
                return AjaxResult.error("短信服务未配置，无法校验验证码");
            }
            if (StringUtils.isEmpty(dealerPhone)) {
                return AjaxResult.error("请先完善商家手机号后再赠送积分");
            }
            if (StringUtils.isEmpty(smsCode)) {
                return AjaxResult.error("请填写短信验证码");
            }
            try {
                smsService.validateSmsCode(dealerPhone, smsCode);
            } catch (Exception e) {
                return AjaxResult.error("验证码错误或已过期，请重新获取");
            }
        }
        
        UmsMember member = umsMemberService.getByMemberCode(memberCode);
        if (member == null) {
            member = umsMemberService.getByPhone(memberCode);
        }
        
        if (member == null) {
            return AjaxResult.error("无效的会员码或手机号");
        }
        
        String remark = "商家[" + (dealer != null ? dealer.getNickName() : "") + "]扫码赠送";
        integralFlowService.addPoints(member.getId(), points, 2, remark);
        
        return AjaxResult.success("赠送成功");
    }

    /**
     * 核销商品券 (扫券码)
     */
    @PostMapping("/coupon/verify")
    public AjaxResult verifyCoupon(@RequestBody Map<String, String> body) {
        String couponCode = body.get("couponCode");
        
        TbCouponInfo coupon = couponInfoService.getValidCouponByCode(couponCode);
        if (coupon == null) {
            return AjaxResult.error("无效或已过期的商品券");
        }
        
        // 获取当前操作的商家信息
        SysUser dealer = SecurityUtils.getLoginUser().getUser();
        
        boolean success = couponInfoService.verifyCoupon(coupon.getId(), dealer.getUserId(), dealer.getNickName());
        if (success) {
            return AjaxResult.success("核销成功");
        } else {
            return AjaxResult.error("核销失败");
        }
    }
}
