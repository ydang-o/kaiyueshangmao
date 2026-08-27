package com.dingyangmall.web.api;

import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.mall.entity.TbCouponInfo;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.service.TbCouponInfoService;
import com.dingyangmall.mall.service.TbIntegralFlowService;
import com.dingyangmall.mall.service.UmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/ma/dealer")
public class DealerScanApi {
    @Autowired private UmsMemberService memberService;
    @Autowired private TbIntegralFlowService flowService;
    @Autowired private TbCouponInfoService couponService;

    @GetMapping("/identify")
    public AjaxResult identify(@RequestParam String memberCode) {
        UmsMember member = findMember(memberCode);
        if (member == null) return AjaxResult.error("无效的会员码或手机号");
        Map<String,Object> data = new HashMap<>(); data.put("userId",member.getId()); data.put("nickname",member.getNickname()); data.put("phone",member.getPhone()); data.put("points",member.getPoints()); data.put("memberCode",member.getMemberCode()); return AjaxResult.success(data);
    }
    @GetMapping("/verify-code")
    public AjaxResult verifyCode(@RequestParam String memberCode) {
        UmsMember member=findMember(memberCode); if(member==null)return AjaxResult.error("无效的会员码或手机号");
        String code=String.format("%06d",new Random().nextInt(1000000)); Map<String,Object> data=new HashMap<>(); data.put("verifyCode",code); data.put("memberId",member.getId()); data.put("phone",maskPhone(member.getPhone())); return AjaxResult.success(data);
    }
    @PostMapping("/grant-points")
    public AjaxResult grant(@RequestBody Map<String,Object> body) { return changePoints(body, true); }
    @PostMapping("/deduct-points")
    public AjaxResult deduct(@RequestBody Map<String,Object> body) { return changePoints(body, false); }
    private AjaxResult changePoints(Map<String,Object> body, boolean grant) {
        if(body==null)return AjaxResult.error("参数不能为空"); UmsMember member=findMember(String.valueOf(body.get("memberCode"))); if(member==null && body.get("userId")!=null) member=memberService.getById(String.valueOf(body.get("userId"))); if(member==null)return AjaxResult.error("会员不存在");
        Number n=body.get("points") instanceof Number?(Number)body.get("points"):null; int points=n==null?0:n.intValue(); if(points<=0)return AjaxResult.error("积分数量必须大于0"); if(!grant && (member.getPoints()==null||member.getPoints()<points))return AjaxResult.error("积分余额不足"); flowService.addPoints(member.getId(),grant?points:-points,grant?2:6,String.valueOf(body.getOrDefault("remark",grant?"经销商赠送积分":"经销商扣减积分"))); return AjaxResult.success("操作成功");
    }
    @GetMapping("/coupons") public AjaxResult coupons(@RequestParam String memberCode){UmsMember m=findMember(memberCode); if(m==null)return AjaxResult.error("会员不存在"); return AjaxResult.success(couponService.getUserCoupons(m.getId(),1));}
    @PostMapping("/verify-coupon") public AjaxResult verifyCoupon(@RequestBody Map<String,String> body){TbCouponInfo c=couponService.getValidCouponByCode(body==null?null:body.get("couponCode")); if(c==null)return AjaxResult.error("无效或已过期的商品券"); return couponService.verifyCoupon(c.getId(),null,"经销商")?AjaxResult.success("核销成功"):AjaxResult.error("核销失败");}
    private UmsMember findMember(String value){if(StringUtils.isEmpty(value)||"null".equals(value))return null; UmsMember m=memberService.getByMemberCode(value); return m!=null?m:memberService.getByPhone(value);}
    private String maskPhone(String p){return p!=null&&p.length()==11?p.substring(0,3)+"****"+p.substring(7):"***********";}
}
