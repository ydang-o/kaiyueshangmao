/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.dingyangmall.common.annotation.RepeatSubmit
 *  com.dingyangmall.common.core.domain.AjaxResult
 *  com.dingyangmall.common.core.domain.entity.SysUser
 *  com.dingyangmall.common.utils.StringUtils
 *  com.dingyangmall.framework.web.service.SmsService
 *  com.dingyangmall.mall.dto.PlaceOrderDTO
 *  com.dingyangmall.mall.dto.PlaceOrderGoodsDTO
 *  com.dingyangmall.mall.entity.GoodsSpu
 *  com.dingyangmall.mall.entity.OrderInfo
 *  com.dingyangmall.mall.entity.TbIntegralFlow
 *  com.dingyangmall.mall.entity.UmsMember
 *  com.dingyangmall.mall.service.GoodsSpuService
 *  com.dingyangmall.mall.service.OrderInfoService
 *  com.dingyangmall.mall.service.TbIntegralFlowService
 *  com.dingyangmall.mall.service.UmsMemberService
 *  com.dingyangmall.mall.utils.MemberUtils
 *  com.dingyangmall.system.service.ISysUserService
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.data.redis.core.RedisTemplate
 *  org.springframework.transaction.annotation.Transactional
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.dingyangmall.web.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.annotation.RepeatSubmit;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.core.domain.entity.SysUser;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.framework.web.service.SmsService;
import com.dingyangmall.mall.dto.PlaceOrderDTO;
import com.dingyangmall.mall.dto.PlaceOrderGoodsDTO;
import com.dingyangmall.mall.entity.GoodsSpu;
import com.dingyangmall.mall.entity.OrderInfo;
import com.dingyangmall.mall.entity.TbIntegralFlow;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.service.GoodsSpuService;
import com.dingyangmall.mall.service.OrderInfoService;
import com.dingyangmall.mall.service.TbIntegralFlowService;
import com.dingyangmall.mall.service.UmsMemberService;
import com.dingyangmall.mall.utils.MemberUtils;
import com.dingyangmall.system.service.ISysUserService;
import com.dingyangmall.web.entity.WxMaUser;
import com.dingyangmall.web.mapper.WxMaUserMapper;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/weixin/api/ma/integralflow", "/api/ma/integralflow"})
public class IntegralFlowApi {
    private static final Logger log = LoggerFactory.getLogger(IntegralFlowApi.class);
    @Autowired
    private TbIntegralFlowService integralFlowService;
    @Autowired
    private UmsMemberService umsMemberService;
    @Autowired
    private GoodsSpuService goodsSpuService;
    @Autowired(required=false)
    private WxMaUserMapper wxMaUserMapper;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private static final String VERIFY_CODE_PREFIX = "dealer:verify:";
    private static final long VERIFY_CODE_EXPIRE = 5L;

    private UmsMember getCurrentMember() {
        UmsMember member;
        String memberIdStr = MemberUtils.getMemberId();
        log.info("[\u79ef\u5206\u6d41\u6c34] getCurrentMember: memberIdStr={}", (Object)memberIdStr);
        if (StringUtils.isEmpty((String)memberIdStr)) {
            log.warn("[\u79ef\u5206\u6d41\u6c34] getCurrentMember: memberIdStr \u4e3a\u7a7a\uff0c\u8fd4\u56de null");
            return null;
        }
        try {
            Long memberId = Long.parseLong(memberIdStr);
            UmsMember member2 = (UmsMember)this.umsMemberService.getById((Serializable)memberId);
            if (member2 != null) {
                log.info("[\u79ef\u5206\u6d41\u6c34] getCurrentMember: App\u7aef\uff0c\u6309 ID \u627e\u5230\u4f1a\u5458 id={}, phone={}", (Object)member2.getId(), (Object)member2.getPhone());
                return member2;
            }
            log.warn("[\u79ef\u5206\u6d41\u6c34] getCurrentMember: App\u7aef\uff0cID={} \u627e\u4e0d\u5230\u5bf9\u5e94\u4f1a\u5458", (Object)memberId);
        }
        catch (NumberFormatException memberId) {
            // empty catch block
        }
        String phone = null;
        String nickname = null;
        String avatar = null;
        if (this.wxMaUserMapper != null) {
            WxMaUser wxUser = this.wxMaUserMapper.selectByOpenid(memberIdStr);
            if (wxUser != null) {
                phone = wxUser.getPhone();
                nickname = wxUser.getNickname();
                avatar = wxUser.getAvatarUrl();
                log.info("[\u79ef\u5206\u6d41\u6c34] getCurrentMember: \u5c0f\u7a0b\u5e8f\u7aef\uff0copenid={}, wx_user \u5b58\u5728, phone={}", (Object)memberIdStr, (Object)phone);
            } else {
                log.warn("[\u79ef\u5206\u6d41\u6c34] getCurrentMember: \u5c0f\u7a0b\u5e8f\u7aef\uff0copenid={}, wx_user \u8868\u4e2d\u65e0\u8bb0\u5f55", (Object)memberIdStr);
            }
        } else {
            log.warn("[\u79ef\u5206\u6d41\u6c34] getCurrentMember: wxMaUserMapper \u4e3a null");
        }
        if (StringUtils.isNotEmpty(phone)) {
            member = this.umsMemberService.getOrCreateByPhone(phone, nickname, avatar);
            log.info("[\u79ef\u5206\u6d41\u6c34] getCurrentMember: \u6309\u624b\u673a\u53f7\u627e\u5230\u4f1a\u5458 id={}, phone={}", (Object)(member != null ? member.getId() : null), (Object)phone);
            return member;
        }
        member = this.umsMemberService.getOrCreateByOpenid(memberIdStr, nickname, avatar);
        log.info("[\u79ef\u5206\u6d41\u6c34] getCurrentMember: \u6309 openid \u627e\u5230\u4f1a\u5458 id={}, memberCode={}", (Object)(member != null ? member.getId() : null), (Object)(member != null ? member.getMemberCode() : null));
        return member;
    }

    @PostMapping(value={"/exchange"})
    @Transactional(rollbackFor={Exception.class})
    public AjaxResult exchange(@RequestBody IntegralExchangeDTO dto) {
        Integer userPoints;
        UmsMember member = this.getCurrentMember();
        if (member == null) {
            return AjaxResult.error((String)"\u8bf7\u5148\u767b\u5f55");
        }
        if (dto == null || StringUtils.isBlank((CharSequence)dto.getSpuId()) || dto.getQuantity() == null || dto.getQuantity() <= 0) {
            return AjaxResult.error((String)"\u53c2\u6570\u9519\u8bef");
        }
        GoodsSpu goods = (GoodsSpu)this.goodsSpuService.getById((Serializable)((Object)dto.getSpuId()));
        if (goods == null) {
            return AjaxResult.error((String)"\u5546\u54c1\u4e0d\u5b58\u5728");
        }
        if (goods.getStock() == null || goods.getStock() <= 0) {
            return AjaxResult.error((String)"\u5546\u54c1\u5e93\u5b58\u4e0d\u8db3");
        }
        Integer needPoints = dto.getIntegralAmount();
        if (needPoints == null || needPoints <= 0) {
            if (goods.getIntegralPrice() != null && goods.getIntegralPrice() > 0) {
                needPoints = goods.getIntegralPrice();
            } else {
                return AjaxResult.error((String)"\u8be5\u5546\u54c1\u4e0d\u652f\u6301\u79ef\u5206\u5151\u6362");
            }
        }
        if ((userPoints = Integer.valueOf(member.getPoints() != null ? member.getPoints() : 0)) < needPoints) {
            return AjaxResult.error((String)"\u79ef\u5206\u4e0d\u8db3");
        }
        if (StringUtils.isBlank((CharSequence)dto.getAddressId())) {
            return AjaxResult.error((String)"\u8bf7\u9009\u62e9\u6536\u8d27\u5730\u5740");
        }
        PlaceOrderDTO placeOrderDTO = new PlaceOrderDTO();
        String phone = member.getPhone();
        if (StringUtils.isBlank((CharSequence)phone) || phone.startsWith("999")) {
            return AjaxResult.error((String)"\u8bf7\u5148\u7ed1\u5b9a\u624b\u673a\u53f7");
        }
        placeOrderDTO.setUserId(phone);
        placeOrderDTO.setPaymentWay("4");
        placeOrderDTO.setIsPay(Boolean.valueOf(true));
        placeOrderDTO.setPayIntegral(needPoints);
        placeOrderDTO.setUserAddressId(dto.getAddressId());
        PlaceOrderGoodsDTO goodsDTO = new PlaceOrderGoodsDTO();
        goodsDTO.setSpuId(dto.getSpuId());
        goodsDTO.setQuantity(dto.getQuantity());
        goodsDTO.setPaymentPrice(BigDecimal.ZERO);
        goodsDTO.setFreightPrice(BigDecimal.ZERO);
        placeOrderDTO.setSkus(Collections.singletonList(goodsDTO));
        OrderInfo orderInfo = this.orderInfoService.orderSub(placeOrderDTO);
        if (orderInfo == null) {
            return AjaxResult.error((String)"\u8ba2\u5355\u521b\u5efa\u5931\u8d25");
        }
        String orderNoSuffix = ",\u8ba2\u5355\u53f7:" + orderInfo.getOrderNo();
        String remarkPrefix = "\u5151\u6362\u5546\u54c1:";
        String goodsName = goods.getName() != null ? goods.getName() : "";
        int maxNameLen = 200 - remarkPrefix.length() - orderNoSuffix.length();
        if (maxNameLen < 0) {
            maxNameLen = 0;
        }
        if (goodsName.length() > maxNameLen) {
            goodsName = goodsName.substring(0, maxNameLen);
        }
        this.integralFlowService.addPoints(member.getId(), Integer.valueOf(-needPoints.intValue()), Integer.valueOf(1), remarkPrefix + goodsName + orderNoSuffix);
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("orderNo", orderInfo.getOrderNo());
        data.put("orderId", orderInfo.getId());
        data.put("goodsName", goods.getName());
        data.put("points", needPoints);
        data.put("status", orderInfo.getStatus());
        return AjaxResult.success((String)"\u5151\u6362\u6210\u529f", data);
    }

    @GetMapping(value={"/points"})
    public AjaxResult getUserPoints() {
        UmsMember member = this.getCurrentMember();
        if (member == null) {
            return AjaxResult.error((String)"\u8bf7\u5148\u767b\u5f55");
        }
        HashMap<String, Integer> data = new HashMap<String, Integer>();
        data.put("points", member.getPoints() != null ? member.getPoints() : 0);
        return AjaxResult.success(data);
    }

    @GetMapping(value={"/list"})
    public AjaxResult list(@RequestParam(defaultValue="1") Integer page, @RequestParam(defaultValue="20") Integer pageSize) {
        UmsMember member = this.getCurrentMember();
        if (member == null) {
            return AjaxResult.error((String)"\u8bf7\u5148\u767b\u5f55");
        }
        Page pageParam = new Page((long)page.intValue(), (long)pageSize.intValue());
        IPage result = ((LambdaQueryChainWrapper)((LambdaQueryChainWrapper)this.integralFlowService.lambdaQuery().eq(TbIntegralFlow::getUserId, (Object)member.getId())).orderByDesc(TbIntegralFlow::getOperTime)).page((IPage)pageParam);
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("records", result.getRecords());
        data.put("total", result.getTotal());
        data.put("current", result.getCurrent());
        data.put("pages", result.getPages());
        return AjaxResult.success(data);
    }

    @RepeatSubmit(interval=2000, message="\u8bf7\u52ff\u91cd\u590d\u63d0\u4ea4")
    @PostMapping(value={"/grant"})
    public AjaxResult grant(@RequestBody Map<String, Object> body) {
        Long dealerMemberId;
        UmsMember member;
        String remark;
        Integer points;
        block37: {
            String dealerId = MemberUtils.getMemberId();
            if (StringUtils.isEmpty((String)dealerId)) {
                return AjaxResult.error((String)"\u672a\u767b\u5f55");
            }
            String memberCode = (String)body.get("memberCode");
            String userIdStr = body.get("userId") != null ? String.valueOf(body.get("userId")) : null;
            points = null;
            Object pointsObj = body.get("points");
            if (pointsObj instanceof Number) {
                points = ((Number)pointsObj).intValue();
            } else if (pointsObj != null) {
                try {
                    points = Integer.parseInt(String.valueOf(pointsObj));
                }
                catch (NumberFormatException e) {
                    return AjaxResult.error((String)"\u79ef\u5206\u6570\u91cf\u683c\u5f0f\u9519\u8bef");
                }
            }
            remark = (String)body.get("remark");
            String verifyCode = (String)body.get("verifyCode");
            String smsCode = (String)body.get("smsCode");
            if (points == null || points <= 0) {
                return AjaxResult.error((String)"\u53d1\u653e\u79ef\u5206\u5fc5\u987b\u5927\u4e8e0");
            }
            member = null;
            boolean isAdminGrant = false;
            if (StringUtils.isNotEmpty((String)userIdStr)) {
                SysUser operator;
                Long operatorId;
                block36: {
                    operatorId = null;
                    try {
                        operatorId = Long.parseLong(dealerId);
                    }
                    catch (NumberFormatException ignore) {
                        UmsMember opMember = this.umsMemberService.getByPhone(dealerId);
                        if (opMember == null) break block36;
                        operatorId = null;
                    }
                }
                SysUser sysUser = operator = operatorId != null ? this.sysUserService.selectUserById(operatorId) : null;
                if (operator == null) {
                    return AjaxResult.error((String)"\u65e0\u6cd5\u786e\u8ba4\u64cd\u4f5c\u4eba\u8eab\u4efd\uff0c\u65e0\u6cd5\u53d1\u653e");
                }
                if (StringUtils.isEmpty((String)smsCode)) {
                    return AjaxResult.error((String)"\u8bf7\u8f93\u5165\u77ed\u4fe1\u9a8c\u8bc1\u7801");
                }
                String operatorPhone = operator.getPhonenumber();
                if (StringUtils.isEmpty((String)operatorPhone)) {
                    return AjaxResult.error((String)"\u5f53\u524d\u8d26\u53f7\u672a\u8bbe\u7f6e\u624b\u673a\u53f7\uff0c\u65e0\u6cd5\u8fdb\u884c\u77ed\u4fe1\u9a8c\u8bc1\uff0c\u8bf7\u5148\u7ed1\u5b9a\u624b\u673a\u53f7");
                }
                try {
                    this.smsService.validateSmsCode(operatorPhone, smsCode);
                }
                catch (Exception e) {
                    return AjaxResult.error((String)"\u9a8c\u8bc1\u7801\u9519\u8bef\u6216\u5df2\u8fc7\u671f");
                }
                try {
                    Long userId = Long.parseLong(userIdStr);
                    SysUser sysUser2 = this.sysUserService.selectUserById(userId);
                    if (sysUser2 != null && StringUtils.isNotEmpty((String)sysUser2.getPhonenumber()) && (member = this.umsMemberService.getByPhone(sysUser2.getPhonenumber())) != null) {
                        isAdminGrant = true;
                    }
                    if (member == null) {
                        return AjaxResult.error((String)"\u672a\u627e\u5230\u8be5\u7528\u6237\u5bf9\u5e94\u7684\u4f1a\u5458\u4fe1\u606f\uff0c\u8bf7\u786e\u4fdd\u7528\u6237\u5df2\u7ed1\u5b9a\u624b\u673a\u53f7");
                    }
                }
                catch (NumberFormatException e) {
                    return AjaxResult.error((String)"\u65e0\u6548\u7684\u7528\u6237ID");
                }
            }
            if (member == null && StringUtils.isNotEmpty((String)memberCode)) {
                String storedCode;
                member = this.umsMemberService.getByMemberCode(memberCode);
                if (member == null) {
                    member = this.umsMemberService.getByPhone(memberCode);
                }
                if (member == null) {
                    return AjaxResult.error((String)"\u65e0\u6548\u7684\u4f1a\u5458\u7801\u6216\u624b\u673a\u53f7");
                }
                if (StringUtils.isEmpty((String)verifyCode)) {
                    return AjaxResult.error((String)"\u8bf7\u8f93\u5165\u9a8c\u8bc1\u7801");
                }
                String redisKey = VERIFY_CODE_PREFIX + member.getId();
                Object codeObj = this.redisTemplate.opsForValue().get((Object)redisKey);
                String string = storedCode = codeObj != null ? codeObj.toString() : null;
                if (StringUtils.isEmpty((String)storedCode)) {
                    return AjaxResult.error((String)"\u9a8c\u8bc1\u7801\u5df2\u8fc7\u671f\uff0c\u8bf7\u91cd\u65b0\u626b\u7801");
                }
                if (!storedCode.equals(verifyCode)) {
                    return AjaxResult.error((String)"\u9a8c\u8bc1\u7801\u9519\u8bef");
                }
                this.redisTemplate.delete((Object)redisKey);
            }
            if (member == null) {
                return AjaxResult.error((String)"\u672a\u627e\u5230\u4f1a\u5458\u4fe1\u606f\uff0c\u8bf7\u63d0\u4f9b\u6709\u6548\u7684\u7528\u6237ID\u6216\u4f1a\u5458\u7801");
            }
            dealerMemberId = null;
            try {
                dealerMemberId = Long.parseLong(dealerId);
            }
            catch (NumberFormatException e) {
                UmsMember m = this.umsMemberService.getByPhone(dealerId);
                if (m == null) break block37;
                dealerMemberId = m.getId();
            }
        }
        String operatorName = "\u7ba1\u7406\u5458";
        boolean isDealer = false;
        SysUser currentUser = null;
        try {
            if (dealerMemberId != null && (currentUser = this.sysUserService.selectUserById(dealerMemberId)) != null) {
                String string = operatorName = StringUtils.isNotEmpty((String)currentUser.getNickName()) ? currentUser.getNickName() : currentUser.getUserName();
                if (currentUser.getDealerLevel() != null && currentUser.getDealerLevel() > 0 && !currentUser.isAdmin()) {
                    isDealer = true;
                    UmsMember dealer = (UmsMember)this.umsMemberService.getById((Serializable)dealerMemberId);
                    if (dealer != null && StringUtils.isNotEmpty((String)dealer.getNickname())) {
                        operatorName = dealer.getNickname();
                    }
                }
            }
        }
        catch (Exception dealer) {
            // empty catch block
        }
        if (currentUser != null && !currentUser.isAdmin()) {
            UmsMember dealerMember = this.umsMemberService.getByPhone(currentUser.getPhonenumber());
            if (dealerMember == null) {
                return AjaxResult.error((String)"\u5f53\u524d\u7ecf\u9500\u5546\u672a\u7ed1\u5b9a\u4f1a\u5458\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458");
            }
            if (dealerMember.getPoints() == null || dealerMember.getPoints() < points) {
                return AjaxResult.error((String)("\u79ef\u5206\u4f59\u989d\u4e0d\u8db3\uff0c\u5f53\u524d\u4f59\u989d\uff1a" + (dealerMember.getPoints() != null ? dealerMember.getPoints() : 0) + "\uff0c\u9700\u8981\u53d1\u653e\uff1a" + points));
            }
            this.integralFlowService.addPoints(dealerMember.getId(), Integer.valueOf(-points.intValue()), Integer.valueOf(2), "\u53d1\u653e\u79ef\u5206\u7ed9\u4f1a\u5458[" + member.getNickname() + "]");
        }
        Object finalRemark = StringUtils.isNotEmpty((String)remark) ? remark : "[" + operatorName + "]\u53d1\u653e\u79ef\u5206";
        log.info("[\u7ba1\u7406\u7aef\u53d1\u653e\u79ef\u5206] \u5f00\u59cb: \u64cd\u4f5c\u4eba={}, \u4f1a\u5458ID={}, \u79ef\u5206={}", new Object[]{operatorName, member.getId(), points});
        this.integralFlowService.addPoints(member.getId(), points, Integer.valueOf(2), (String)finalRemark);
        log.info("[\u7ba1\u7406\u7aef\u53d1\u653e\u79ef\u5206] \u5b8c\u6210: \u4f1a\u5458ID={}, \u79ef\u5206={}", (Object)member.getId(), (Object)points);
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("memberId", member.getId());
        result.put("memberNickname", member.getNickname());
        result.put("grantPoints", points);
        result.put("message", "\u53d1\u653e\u6210\u529f");
        return AjaxResult.success(result);
    }

    public static class IntegralExchangeDTO {
        private String spuId;
        private Integer quantity;
        private Integer payType;
        private Integer integralAmount;
        private String addressId;

        @Generated
        public IntegralExchangeDTO() {
        }

        @Generated
        public String getSpuId() {
            return this.spuId;
        }

        @Generated
        public Integer getQuantity() {
            return this.quantity;
        }

        @Generated
        public Integer getPayType() {
            return this.payType;
        }

        @Generated
        public Integer getIntegralAmount() {
            return this.integralAmount;
        }

        @Generated
        public String getAddressId() {
            return this.addressId;
        }

        @Generated
        public void setSpuId(String spuId) {
            this.spuId = spuId;
        }

        @Generated
        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        @Generated
        public void setPayType(Integer payType) {
            this.payType = payType;
        }

        @Generated
        public void setIntegralAmount(Integer integralAmount) {
            this.integralAmount = integralAmount;
        }

        @Generated
        public void setAddressId(String addressId) {
            this.addressId = addressId;
        }

        @Generated
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof IntegralExchangeDTO)) {
                return false;
            }
            IntegralExchangeDTO other = (IntegralExchangeDTO)o;
            if (!other.canEqual(this)) {
                return false;
            }
            Integer this$quantity = this.getQuantity();
            Integer other$quantity = other.getQuantity();
            if (this$quantity == null ? other$quantity != null : !((Object)this$quantity).equals(other$quantity)) {
                return false;
            }
            Integer this$payType = this.getPayType();
            Integer other$payType = other.getPayType();
            if (this$payType == null ? other$payType != null : !((Object)this$payType).equals(other$payType)) {
                return false;
            }
            Integer this$integralAmount = this.getIntegralAmount();
            Integer other$integralAmount = other.getIntegralAmount();
            if (this$integralAmount == null ? other$integralAmount != null : !((Object)this$integralAmount).equals(other$integralAmount)) {
                return false;
            }
            String this$spuId = this.getSpuId();
            String other$spuId = other.getSpuId();
            if (this$spuId == null ? other$spuId != null : !this$spuId.equals(other$spuId)) {
                return false;
            }
            String this$addressId = this.getAddressId();
            String other$addressId = other.getAddressId();
            return !(this$addressId == null ? other$addressId != null : !this$addressId.equals(other$addressId));
        }

        @Generated
        protected boolean canEqual(Object other) {
            return other instanceof IntegralExchangeDTO;
        }

        @Generated
        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            Integer $quantity = this.getQuantity();
            result = result * 59 + ($quantity == null ? 43 : ((Object)$quantity).hashCode());
            Integer $payType = this.getPayType();
            result = result * 59 + ($payType == null ? 43 : ((Object)$payType).hashCode());
            Integer $integralAmount = this.getIntegralAmount();
            result = result * 59 + ($integralAmount == null ? 43 : ((Object)$integralAmount).hashCode());
            String $spuId = this.getSpuId();
            result = result * 59 + ($spuId == null ? 43 : $spuId.hashCode());
            String $addressId = this.getAddressId();
            result = result * 59 + ($addressId == null ? 43 : $addressId.hashCode());
            return result;
        }

        @Generated
        public String toString() {
            return "IntegralFlowApi.IntegralExchangeDTO(spuId=" + this.getSpuId() + ", quantity=" + this.getQuantity() + ", payType=" + this.getPayType() + ", integralAmount=" + this.getIntegralAmount() + ", addressId=" + this.getAddressId() + ")";
        }
    }
}

