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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/app/dynamic-code")
public class DynamicCodeApi {
 @Autowired private DynamicCodeService codeService; @Autowired private UmsMemberService memberService; @Autowired private TbCouponInfoService couponService;
 @GetMapping("/member") public AjaxResult member(){Long id=currentId(); if(id==null)return AjaxResult.error("未登录"); UmsMember m=memberService.getById(id); if(m==null)return AjaxResult.error("用户不存在"); if(StringUtils.isEmpty(m.getPhone()))return AjaxResult.error("用户未绑定手机号"); DynamicCodeDTO d=codeService.generateDynamicMemberCodeDTO(id,m.getMemberCode(),m.getPhone()); Map<String,Object> r=new HashMap<>();r.put("dynamicCode",d.getEncryptedCode());r.put("timestamp",d.getTimestamp());r.put("expireSeconds",d.getExpireSeconds());r.put("memberNickname",m.getNickname());r.put("memberCode",mask(m.getMemberCode(),2,2));r.put("phone",mask(m.getPhone(),3,4));return AjaxResult.success(r);}
 @GetMapping("/coupon/{couponId}") public AjaxResult coupon(@PathVariable Long couponId){Long id=currentId();if(id==null)return AjaxResult.error("未登录");UmsMember m=memberService.getById(id);TbCouponInfo c=couponService.getById(couponId);if(m==null||c==null)return AjaxResult.error("资源不存在");if(!id.equals(c.getUserId()))return AjaxResult.error("无权访问此优惠券");DynamicCodeDTO d=codeService.generateDynamicCouponCodeDTO(couponId,c.getCouponCode(),id,m.getPhone(),c.getGoodsName());Map<String,Object> r=new HashMap<>();r.put("couponId",couponId);r.put("dynamicCode",d.getEncryptedCode());r.put("timestamp",d.getTimestamp());r.put("expireSeconds",d.getExpireSeconds());r.put("goodsName",c.getGoodsName());r.put("goodsPic",c.getGoodsPic());r.put("validityEnd",c.getValidityEnd());r.put("couponCode",mask(c.getCouponCode(),3,3));return AjaxResult.success(r);}
 @GetMapping("/coupons") public AjaxResult coupons(){Long id=currentId();if(id==null)return AjaxResult.error("未登录");UmsMember m=memberService.getById(id);if(m==null)return AjaxResult.error("用户不存在");List<Map<String,Object>> out=new ArrayList<>();for(TbCouponInfo c:couponService.getUserCoupons(id,1)){DynamicCodeDTO d=codeService.generateDynamicCouponCodeDTO(c.getId(),c.getCouponCode(),id,m.getPhone(),c.getGoodsName());Map<String,Object> r=new HashMap<>();r.put("couponId",c.getId());r.put("dynamicCode",d.getEncryptedCode());r.put("timestamp",d.getTimestamp());r.put("expireSeconds",d.getExpireSeconds());r.put("goodsName",c.getGoodsName());r.put("goodsPic",c.getGoodsPic());r.put("validityEnd",c.getValidityEnd());r.put("couponCode",mask(c.getCouponCode(),3,3));out.add(r);}return AjaxResult.success(out);}
 private Long currentId(){try{String s=MemberUtils.getMemberId();return StringUtils.isEmpty(s)?null:Long.valueOf(s);}catch(Exception e){return null;}}
 private String mask(String s,int left,int right){if(StringUtils.isEmpty(s)||s.length()<=left+right)return "****";return s.substring(0,left)+"****"+s.substring(s.length()-right);}
}
