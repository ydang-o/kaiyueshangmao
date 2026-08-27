/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.api;

import com.dingyangmall.common.annotation.RepeatSubmit;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.core.domain.entity.SysUser;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.common.utils.crypto.AesEncryptUtils;
import com.dingyangmall.mall.entity.TbCouponInfo;
import com.dingyangmall.mall.entity.TbIntegralFlow;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.service.DynamicCodeService;
import com.dingyangmall.mall.service.TbCouponInfoService;
import com.dingyangmall.mall.service.TbIntegralFlowService;
import com.dingyangmall.mall.service.UmsMemberService;
import com.dingyangmall.mall.utils.MemberUtils;
import com.dingyangmall.system.service.ISysUserService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/ma/dealer"})
public class DealerScanApi {
    private static final Logger log = LoggerFactory.getLogger(DealerScanApi.class);
    @Autowired
    private UmsMemberService umsMemberService;
    @Autowired
    private TbIntegralFlowService integralFlowService;
    @Autowired
    private TbCouponInfoService couponInfoService;
    @Autowired
    private DynamicCodeService dynamicCodeService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ISysUserService sysUserService;
    private static final String VERIFY_CODE_PREFIX = "dealer:verify:";
    private static final String IDEMPOTENT_PREFIX = "dealer:grant:idempotent:";
    private static final long VERIFY_CODE_EXPIRE = 5L;
    private static final long IDEMPOTENT_EXPIRE = 30L;

    private Long resolveDealerMemberId(String dealerId) {
        if (StringUtils.isEmpty(dealerId)) {
            return null;
        }
        try {
            return Long.parseLong(dealerId);
        }
        catch (NumberFormatException e) {
            UmsMember member = this.umsMemberService.getByPhone(dealerId);
            if (member != null) {
                return member.getId();
            }
            return null;
        }
    }

    @GetMapping(value={"/identify"})
    public AjaxResult identifyUser(@RequestParam String memberCode) {
        String dealerId = MemberUtils.getMemberId();
        if (StringUtils.isEmpty(dealerId)) {
            return AjaxResult.error("\u672a\u767b\u5f55");
        }
        Long dealerMemberId = this.resolveDealerMemberId(dealerId);
        if (dealerMemberId == null) {
            return AjaxResult.error("\u767b\u5f55\u5df2\u8fc7\u671f\u6216\u7ecf\u9500\u5546\u8d26\u53f7\u4fe1\u606f\u5f02\u5e38\uff0c\u8bf7\u91cd\u65b0\u767b\u5f55");
        }
        AjaxResult permissionError = this.checkDealerLevelPermission(dealerMemberId);
        if (permissionError != null) {
            return permissionError;
        }
        UmsMember member = null;
        boolean isDynamicCode = false;
        try {
            String[] parts;
            String decryptedData = this.decryptDynamicCode(memberCode);
            if (decryptedData != null && (parts = decryptedData.split("\\|")).length >= 4) {
                long timestamp = Long.parseLong(parts[0]);
                String phoneNumber = parts[1];
                String originalCode = parts[2];
                member = this.umsMemberService.getByMemberCode(originalCode);
                if (member != null) {
                    boolean valid = this.dynamicCodeService.verifyDynamicMemberCode(memberCode, member.getId(), originalCode, phoneNumber);
                    if (!valid) {
                        return AjaxResult.error("\u52a8\u6001\u7801\u5df2\u8fc7\u671f\u6216\u65e0\u6548\uff0c\u8bf7\u5237\u65b0\u4f1a\u5458\u7801");
                    }
                    isDynamicCode = true;
                }
            }
        }
        catch (Exception decryptedData) {
            // empty catch block
        }
        if (member == null && (member = this.umsMemberService.getByMemberCode(memberCode)) == null) {
            member = this.umsMemberService.getByPhone(memberCode);
        }
        if (member == null) {
            return AjaxResult.error("\u65e0\u6548\u7684\u4f1a\u5458\u7801");
        }
        if (!isDynamicCode) {
            return AjaxResult.error("\u8bf7\u4f7f\u7528\u52a8\u6001\u4f1a\u5458\u7801\u8fdb\u884c\u626b\u63cf");
        }
        String verifyCode = this.generateVerifyCode();
        String redisKey = VERIFY_CODE_PREFIX + member.getId();
        this.redisTemplate.opsForValue().set(redisKey, verifyCode, 5L, TimeUnit.MINUTES);
        UmsMember dealer = (UmsMember)this.umsMemberService.getById(dealerMemberId);
        String dealerName = dealer != null ? dealer.getNickname() : "\u7ecf\u9500\u5546";
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("userId", String.valueOf(member.getId()));
        result.put("nickname", member.getNickname());
        result.put("phone", this.maskPhone(member.getPhone()));
        result.put("points", member.getPoints());
        result.put("level", member.getLevel());
        result.put("memberCode", this.maskMemberCode(member.getMemberCode()));
        result.put("needVerify", true);
        result.put("dealerName", dealerName);
        return AjaxResult.success(result);
    }

    @GetMapping(value={"/verify-code"})
    public AjaxResult getVerifyCode(@RequestParam String memberCode) {
        String verifyCode;
        UmsMember member = this.findMemberByCode(memberCode);
        if (member == null) {
            return AjaxResult.error("\u65e0\u6548\u7684\u4f1a\u5458\u7801");
        }
        String redisKey = VERIFY_CODE_PREFIX + member.getId();
        Object codeObj = this.redisTemplate.opsForValue().get(redisKey);
        String string = verifyCode = codeObj != null ? codeObj.toString() : null;
        if (StringUtils.isEmpty(verifyCode)) {
            verifyCode = this.generateVerifyCode();
            this.redisTemplate.opsForValue().set(redisKey, verifyCode, 5L, TimeUnit.MINUTES);
        }
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("verifyCode", verifyCode);
        result.put("expireMinutes", 5L);
        return AjaxResult.success(result);
    }

    @RepeatSubmit(interval=2000, message="\u8bf7\u52ff\u91cd\u590d\u63d0\u4ea4")
    @PostMapping(value={"/grant-points"})
    public AjaxResult grantPoints(@RequestBody Map<String, Object> body) {
        List<SysUser> sysUsers;
        String dealerName;
        String storedCode;
        log.info("[\u7ecf\u9500\u5546\u53d1\u653e] grantPoints \u88ab\u8c03\u7528, body={}", (Object)body);
        String dealerId = MemberUtils.getMemberId();
        if (StringUtils.isEmpty(dealerId)) {
            return AjaxResult.error("\u672a\u767b\u5f55");
        }
        Long dealerMemberId = this.resolveDealerMemberId(dealerId);
        if (dealerMemberId == null) {
            return AjaxResult.error("\u767b\u5f55\u5df2\u8fc7\u671f\u6216\u7ecf\u9500\u5546\u8d26\u53f7\u4fe1\u606f\u5f02\u5e38\uff0c\u8bf7\u91cd\u65b0\u767b\u5f55");
        }
        AjaxResult permissionError = this.checkDealerLevelPermission(dealerMemberId);
        if (permissionError != null) {
            return permissionError;
        }
        String memberCode = (String)body.get("memberCode");
        String memberIdStr = body.get("memberId") != null ? String.valueOf(body.get("memberId")) : null;
        Integer points = (Integer)body.get("points");
        String remark = (String)body.get("remark");
        String verifyCode = (String)body.get("verifyCode");
        if (points == null || points <= 0) {
            return AjaxResult.error("\u53d1\u653e\u79ef\u5206\u5fc5\u987b\u5927\u4e8e0");
        }
        if (StringUtils.isEmpty(verifyCode)) {
            return AjaxResult.error("\u8bf7\u8f93\u5165\u9a8c\u8bc1\u7801");
        }
        UmsMember member = null;
        if (StringUtils.isNotEmpty(memberIdStr)) {
            try {
                member = (UmsMember)this.umsMemberService.getById(Long.valueOf(Long.parseLong(memberIdStr)));
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        if (member == null && StringUtils.isNotEmpty(memberCode)) {
            member = this.findMemberByCode(memberCode);
        }
        if (member == null) {
            return AjaxResult.error("\u65e0\u6548\u7684\u4f1a\u5458\u7801\u6216\u624b\u673a\u53f7");
        }
        String idempotentKey = IDEMPOTENT_PREFIX + member.getId() + ":" + verifyCode;
        Boolean isNew = this.redisTemplate.opsForValue().setIfAbsent(idempotentKey, "1", 30L, TimeUnit.SECONDS);
        if (Boolean.FALSE.equals(isNew)) {
            return AjaxResult.error("\u8be5\u7b14\u53d1\u653e\u6b63\u5728\u5904\u7406\u4e2d\uff0c\u8bf7\u52ff\u91cd\u590d\u64cd\u4f5c");
        }
        String redisKey = VERIFY_CODE_PREFIX + member.getId();
        Object codeObj = this.redisTemplate.opsForValue().get(redisKey);
        String string = storedCode = codeObj != null ? codeObj.toString() : null;
        if (StringUtils.isEmpty(storedCode)) {
            this.redisTemplate.delete(idempotentKey);
            return AjaxResult.error("\u9a8c\u8bc1\u7801\u5df2\u8fc7\u671f\uff0c\u8bf7\u91cd\u65b0\u626b\u7801");
        }
        if (!storedCode.equals(verifyCode)) {
            this.redisTemplate.delete(idempotentKey);
            return AjaxResult.error("\u9a8c\u8bc1\u7801\u9519\u8bef");
        }
        this.redisTemplate.delete(redisKey);
        final UmsMember dealer = (UmsMember)this.umsMemberService.getById(dealerMemberId);
        String string2 = dealerName = dealer != null ? dealer.getNickname() : "\u7ecf\u9500\u5546";
        if (dealer != null && StringUtils.isNotEmpty(dealer.getPhone()) && (sysUsers = this.sysUserService.selectUserList(new SysUser(){
            {
                this.setPhonenumber(dealer.getPhone());
            }
        })) != null && !sysUsers.isEmpty()) {
            SysUser dealerUser = sysUsers.get(0);
            Integer dealerPoints = dealerUser.getDealerPoints();
            if (dealerPoints == null) {
                dealerPoints = 0;
            }
            if (dealerPoints < points) {
                return AjaxResult.error("\u7ecf\u9500\u5546\u79ef\u5206\u4f59\u989d\u4e0d\u8db3\uff0c\u5f53\u524d\u4f59\u989d\uff1a" + dealerPoints + "\uff0c\u9700\u8981\u53d1\u653e\uff1a" + points);
            }
            dealerUser.setDealerPoints(dealerPoints - points);
            this.sysUserService.updateUser(dealerUser);
            TbIntegralFlow issuerFlow = new TbIntegralFlow();
            issuerFlow.setUserId(dealerUser.getUserId());
            issuerFlow.setIntegralNum(-points.intValue());
            issuerFlow.setOperType(2);
            issuerFlow.setSourceUserId(member.getId());
            issuerFlow.setRemark("\u53d1\u653e\u79ef\u5206\u7ed9\u4f1a\u5458[" + member.getNickname() + "]\uff0c\u6263\u51cf\u7ecf\u9500\u5546\u4f59\u989d");
            issuerFlow.setOperTime(LocalDateTime.now());
            issuerFlow.setCreateTime(LocalDateTime.now());
            issuerFlow.setCreateBy(dealerUser.getUserName());
            this.integralFlowService.save(issuerFlow);
        }
        Object finalRemark = StringUtils.isNotEmpty(remark) ? remark : "\u7ecf\u9500\u5546[" + dealerName + "]\u53d1\u653e\u79ef\u5206";
        log.info("[\u7ecf\u9500\u5546\u53d1\u653e] \u9a8c\u8bc1\u901a\u8fc7, \u5f00\u59cb\u53d1\u653e\u79ef\u5206: \u4f1a\u5458ID={}, \u79ef\u5206={}, \u7ecf\u9500\u5546={}", member.getId(), points, dealerName);
        this.integralFlowService.addPoints(member.getId(), points, 2, (String)finalRemark);
        log.info("[\u7ecf\u9500\u5546\u53d1\u653e] \u53d1\u653e\u5b8c\u6210: \u4f1a\u5458ID={}, \u79ef\u5206={}", (Object)member.getId(), (Object)points);
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("memberId", member.getId());
        result.put("memberNickname", member.getNickname());
        result.put("grantPoints", points);
        result.put("message", "\u53d1\u653e\u6210\u529f");
        return AjaxResult.success(result);
    }

    @PostMapping(value={"/deduct-points"})
    public AjaxResult deductPoints(@RequestBody Map<String, Object> body) {
        int currentPoints;
        String storedCode;
        String dealerId = MemberUtils.getMemberId();
        if (StringUtils.isEmpty(dealerId)) {
            return AjaxResult.error("\u672a\u767b\u5f55");
        }
        Long dealerMemberId = this.resolveDealerMemberId(dealerId);
        if (dealerMemberId == null) {
            return AjaxResult.error("\u767b\u5f55\u5df2\u8fc7\u671f\u6216\u7ecf\u9500\u5546\u8d26\u53f7\u4fe1\u606f\u5f02\u5e38\uff0c\u8bf7\u91cd\u65b0\u767b\u5f55");
        }
        AjaxResult permissionError = this.checkDealerLevelPermission(dealerMemberId);
        if (permissionError != null) {
            return permissionError;
        }
        String memberCode = (String)body.get("memberCode");
        String memberIdStr = body.get("memberId") != null ? String.valueOf(body.get("memberId")) : null;
        Integer points = (Integer)body.get("points");
        String remark = (String)body.get("remark");
        String verifyCode = (String)body.get("verifyCode");
        if (points == null || points <= 0) {
            return AjaxResult.error("\u6263\u51cf\u79ef\u5206\u5fc5\u987b\u5927\u4e8e0");
        }
        if (StringUtils.isEmpty(verifyCode)) {
            return AjaxResult.error("\u8bf7\u8f93\u5165\u9a8c\u8bc1\u7801");
        }
        UmsMember member = null;
        if (StringUtils.isNotEmpty(memberIdStr)) {
            try {
                member = (UmsMember)this.umsMemberService.getById(Long.valueOf(Long.parseLong(memberIdStr)));
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        if (member == null && StringUtils.isNotEmpty(memberCode)) {
            member = this.findMemberByCode(memberCode);
        }
        if (member == null) {
            return AjaxResult.error("\u65e0\u6548\u7684\u4f1a\u5458\u7801\u6216\u624b\u673a\u53f7");
        }
        String idempotentKey = IDEMPOTENT_PREFIX + member.getId() + ":" + verifyCode;
        Boolean isNew = this.redisTemplate.opsForValue().setIfAbsent(idempotentKey, "1", 30L, TimeUnit.SECONDS);
        if (Boolean.FALSE.equals(isNew)) {
            return AjaxResult.error("\u8be5\u7b14\u6263\u51cf\u6b63\u5728\u5904\u7406\u4e2d\uff0c\u8bf7\u52ff\u91cd\u590d\u64cd\u4f5c");
        }
        String redisKey = VERIFY_CODE_PREFIX + member.getId();
        Object codeObj = this.redisTemplate.opsForValue().get(redisKey);
        String string = storedCode = codeObj != null ? codeObj.toString() : null;
        if (StringUtils.isEmpty(storedCode)) {
            this.redisTemplate.delete(idempotentKey);
            return AjaxResult.error("\u9a8c\u8bc1\u7801\u5df2\u8fc7\u671f\uff0c\u8bf7\u91cd\u65b0\u626b\u7801");
        }
        if (!storedCode.equals(verifyCode)) {
            this.redisTemplate.delete(idempotentKey);
            return AjaxResult.error("\u9a8c\u8bc1\u7801\u9519\u8bef");
        }
        this.redisTemplate.delete(redisKey);
        int n = currentPoints = member.getPoints() != null ? member.getPoints() : 0;
        if (currentPoints < points) {
            return AjaxResult.error("\u4f1a\u5458\u79ef\u5206\u4e0d\u8db3\uff0c\u5f53\u524d\u79ef\u5206\uff1a" + currentPoints);
        }
        UmsMember dealer = (UmsMember)this.umsMemberService.getById(dealerMemberId);
        String dealerName = dealer != null ? dealer.getNickname() : "\u7ecf\u9500\u5546";
        Object deductRemark = StringUtils.isNotEmpty(remark) ? remark : "\u7ecf\u9500\u5546[" + dealerName + "]\u6263\u51cf\u79ef\u5206";
        String grantRemark = "\u4ece\u4f1a\u5458[" + member.getNickname() + "]\u8f6c\u5165\u79ef\u5206";
        this.integralFlowService.addPoints(member.getId(), -points.intValue(), 6, (String)deductRemark);
        this.integralFlowService.addPoints(dealerMemberId, points, 7, grantRemark);
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("memberId", member.getId());
        result.put("memberNickname", member.getNickname());
        result.put("deductPoints", points);
        result.put("remainPoints", currentPoints - points);
        result.put("dealerPoints", (dealer.getPoints() != null ? dealer.getPoints() : 0) + points);
        result.put("message", "\u6263\u51cf\u6210\u529f");
        return AjaxResult.success(result);
    }

    @GetMapping(value={"/coupons"})
    public AjaxResult getMemberCoupons(@RequestParam String memberCode) {
        String dealerId = MemberUtils.getMemberId();
        if (StringUtils.isEmpty(dealerId)) {
            return AjaxResult.error("\u672a\u767b\u5f55");
        }
        Long dealerMemberId = this.resolveDealerMemberId(dealerId);
        if (dealerMemberId == null) {
            return AjaxResult.error("\u767b\u5f55\u5df2\u8fc7\u671f\u6216\u7ecf\u9500\u5546\u8d26\u53f7\u4fe1\u606f\u5f02\u5e38\uff0c\u8bf7\u91cd\u65b0\u767b\u5f55");
        }
        AjaxResult permissionError = this.checkDealerLevelPermission(dealerMemberId);
        if (permissionError != null) {
            return permissionError;
        }
        UmsMember member = this.findMemberByCode(memberCode);
        if (member == null) {
            return AjaxResult.error("\u65e0\u6548\u7684\u4f1a\u5458\u7801\u6216\u624b\u673a\u53f7");
        }
        List<TbCouponInfo> coupons = this.couponInfoService.getMemberValidCoupons(member.getId());
        ArrayList resultList = new ArrayList();
        for (TbCouponInfo coupon : coupons) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("couponId", coupon.getId());
            item.put("goodsName", coupon.getGoodsName());
            item.put("goodsPic", coupon.getGoodsPic());
            item.put("validityEnd", coupon.getValidityEnd());
            item.put("integralPrice", coupon.getIntegralPrice());
            item.put("couponCode", this.maskCouponCode(coupon.getCouponCode()));
            resultList.add(item);
        }
        return AjaxResult.success(resultList);
    }

    @PostMapping(value={"/verify-coupon"})
    public AjaxResult verifyCoupon(@RequestBody Map<String, String> body) {
        String dealerId = MemberUtils.getMemberId();
        if (StringUtils.isEmpty(dealerId)) {
            return AjaxResult.error("\u672a\u767b\u5f55");
        }
        Long dealerMemberId = this.resolveDealerMemberId(dealerId);
        if (dealerMemberId == null) {
            return AjaxResult.error("\u767b\u5f55\u5df2\u8fc7\u671f\u6216\u7ecf\u9500\u5546\u8d26\u53f7\u4fe1\u606f\u5f02\u5e38\uff0c\u8bf7\u91cd\u65b0\u767b\u5f55");
        }
        AjaxResult permissionError = this.checkDealerLevelPermission(dealerMemberId);
        if (permissionError != null) {
            return permissionError;
        }
        String couponCode = body.get("couponCode");
        if (StringUtils.isEmpty(couponCode)) {
            return AjaxResult.error("\u5238\u7801\u4e0d\u80fd\u4e3a\u7a7a");
        }
        UmsMember dealer = (UmsMember)this.umsMemberService.getById(dealerMemberId);
        String dealerName = dealer != null ? dealer.getNickname() : "\u7ecf\u9500\u5546";
        boolean isDynamicCode = false;
        TbCouponInfo coupon = null;
        try {
            String[] parts;
            String decryptedData = this.decryptDynamicCode(couponCode);
            if (decryptedData != null && (parts = decryptedData.split("\\|")).length >= 5) {
                long timestamp = Long.parseLong(parts[0]);
                String phoneNumber = parts[1];
                Long couponIdFromCode = Long.parseLong(parts[2]);
                String originalCode = parts[3];
                coupon = this.couponInfoService.getByCouponCode(originalCode);
                if (coupon != null) {
                    boolean valid = this.dynamicCodeService.verifyDynamicCouponCode(couponCode, coupon.getId(), coupon.getUserId(), originalCode, phoneNumber);
                    if (!valid) {
                        return AjaxResult.error("\u52a8\u6001\u7801\u5df2\u8fc7\u671f\u6216\u65e0\u6548");
                    }
                    isDynamicCode = true;
                }
            }
        }
        catch (Exception decryptedData) {
            // empty catch block
        }
        if (!isDynamicCode) {
            return AjaxResult.error("\u8bf7\u4f7f\u7528\u52a8\u6001\u4e8c\u7ef4\u7801\u8fdb\u884c\u6838\u9500");
        }
        if (coupon == null) {
            return AjaxResult.error("\u65e0\u6548\u7684\u5238\u7801");
        }
        boolean success = this.couponInfoService.verifyCouponById(coupon.getId(), dealerMemberId, dealerName);
        if (success) {
            HashMap<String, String> result = new HashMap<String, String>();
            result.put("message", "\u6838\u9500\u6210\u529f");
            result.put("goodsName", coupon.getGoodsName());
            result.put("memberNickname", coupon.getUserId() != null ? ((UmsMember)this.umsMemberService.getById(coupon.getUserId())).getNickname() : "");
            return AjaxResult.success(result);
        }
        return AjaxResult.error("\u6838\u9500\u5931\u8d25\uff0c\u5238\u7801\u65e0\u6548\u6216\u5df2\u8fc7\u671f");
    }

    private String decryptDynamicCode(String code) {
        try {
            return AesEncryptUtils.decrypt(code);
        }
        catch (Exception e) {
            return null;
        }
    }

    private UmsMember findMemberByCode(String memberCode) {
        if (StringUtils.isEmpty(memberCode)) {
            return null;
        }
        try {
            String[] parts;
            String decryptedData = this.decryptDynamicCode(memberCode);
            if (decryptedData != null && (parts = decryptedData.split("\\|")).length >= 4) {
                String phoneNumber = parts[1];
                String originalCode = parts[2];
                UmsMember member = this.umsMemberService.getByMemberCode(originalCode);
                if (member != null) {
                    boolean valid = this.dynamicCodeService.verifyDynamicMemberCode(memberCode, member.getId(), originalCode, phoneNumber);
                    if (!valid) {
                        log.warn("[findMemberByCode] \u52a8\u6001\u7801\u9a8c\u8bc1\u5931\u8d25, memberId={}", (Object)member.getId());
                        return null;
                    }
                    return member;
                }
            }
        }
        catch (Exception decryptedData) {
            // empty catch block
        }
        UmsMember member = this.umsMemberService.getByMemberCode(memberCode);
        if (member != null) {
            return member;
        }
        return this.umsMemberService.getByPhone(memberCode);
    }

    private String generateVerifyCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; ++i) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    private String maskPhone(String phone) {
        if (StringUtils.isEmpty(phone) || phone.length() != 11) {
            return "****";
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    private String maskMemberCode(String memberCode) {
        if (StringUtils.isEmpty(memberCode) || memberCode.length() <= 4) {
            return "****";
        }
        return memberCode.substring(0, 2) + "****" + memberCode.substring(memberCode.length() - 2);
    }

    private String maskCouponCode(String couponCode) {
        if (StringUtils.isEmpty(couponCode) || couponCode.length() <= 6) {
            return "******";
        }
        return couponCode.substring(0, 3) + "******" + couponCode.substring(couponCode.length() - 3);
    }

    private AjaxResult checkDealerLevelPermission(Long dealerMemberId) {
        try {
            UmsMember dealerMember = (UmsMember)this.umsMemberService.getById(dealerMemberId);
            if (dealerMember == null || StringUtils.isEmpty(dealerMember.getPhone())) {
                return AjaxResult.error("\u7ecf\u9500\u5546\u8d26\u53f7\u4fe1\u606f\u5f02\u5e38");
            }
            SysUser sysUser = this.sysUserService.selectUserByUserName(dealerMember.getPhone());
            if (sysUser == null) {
                sysUser = this.sysUserService.selectUserByPhoneNumber(dealerMember.getPhone());
            }
            if (sysUser == null || sysUser.getDealerLevel() == null) {
                return AjaxResult.error("\u5f53\u524d\u8d26\u53f7\u65e0\u7ecf\u9500\u5546\u6743\u9650");
            }
            if (sysUser.getDealerLevel() < 1 || sysUser.getDealerLevel() > 3) {
                return AjaxResult.error("\u4ec5\u7ecf\u9500\u5546\u53ef\u4f7f\u7528\u6b64\u529f\u80fd");
            }
            return null;
        }
        catch (Exception e) {
            return AjaxResult.error("\u7ecf\u9500\u5546\u6743\u9650\u6821\u9a8c\u5931\u8d25");
        }
    }
}

