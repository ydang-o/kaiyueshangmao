/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.api;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.mall.config.MallConfigProperties;
import com.dingyangmall.mall.dto.PlaceOrderDTO;
import com.dingyangmall.mall.dto.PlaceOrderGoodsDTO;
import com.dingyangmall.mall.entity.GoodsSpu;
import com.dingyangmall.mall.entity.OrderInfo;
import com.dingyangmall.mall.entity.OrderItem;
import com.dingyangmall.mall.entity.OrderLogistics;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.enums.OrderInfoEnum;
import com.dingyangmall.mall.service.GoodsSpuService;
import com.dingyangmall.mall.service.Kuaidi100QueryService;
import com.dingyangmall.mall.service.OrderInfoService;
import com.dingyangmall.mall.service.OrderLogisticsService;
import com.dingyangmall.mall.service.TbIntegralFlowService;
import com.dingyangmall.mall.service.UmsMemberService;
import com.dingyangmall.mall.utils.MemberUtils;
import com.dingyangmall.web.entity.WxMaUser;
import com.dingyangmall.web.mapper.WxMaUserMapper;
import com.dingyangmall.weixin.config.WxPayConfiguration;
import com.dingyangmall.weixin.constant.MyReturnCode;
import com.dingyangmall.weixin.entity.WxUser;
import com.dingyangmall.weixin.utils.LocalDateTimeUtils;
import com.dingyangmall.weixin.utils.WxMaUtil;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/weixin/api/ma/orderinfo", "/api/ma/orderinfo"})
public class OrderInfoApi {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(OrderInfoApi.class);
    private final OrderInfoService orderInfoService;
    private final OrderLogisticsService orderLogisticsService;
    private final Kuaidi100QueryService kuaidi100QueryService;
    private final TbIntegralFlowService integralFlowService;
    private final UmsMemberService umsMemberService;
    private final GoodsSpuService goodsSpuService;
    private final MallConfigProperties mallConfigProperties;
    private final WxMaUserMapper wxMaUserMapper;

    @GetMapping(value={"/page"})
    public AjaxResult getOrderInfoPage(Page page, OrderInfo orderInfo) {
        String memberIdStr = MemberUtils.getMemberId();
        if (StringUtils.isEmpty(memberIdStr)) {
            return AjaxResult.error("\u8bf7\u5148\u767b\u5f55");
        }
        UserIdInfo userIdInfo = this.getUserIdInfo(memberIdStr);
        if (!userIdInfo.hasPhone) {
            return AjaxResult.error(400, "\u8bf7\u5148\u7ed1\u5b9a\u624b\u673a\u53f7");
        }
        orderInfo.setUserIdList(userIdInfo.userIdList);
        return AjaxResult.success(this.orderInfoService.page2(page, orderInfo));
    }

    private UserIdInfo getUserIdInfo(String memberIdStr) {
        UserIdInfo info;
        block5: {
            info = new UserIdInfo();
            try {
                Long memberId = Long.parseLong(memberIdStr);
                UmsMember member = (UmsMember)this.umsMemberService.getById(memberId);
                if (member != null) {
                    info.userIdList.add(memberIdStr);
                    if (StringUtils.isNotBlank(member.getPhone()) && !member.getPhone().startsWith("999")) {
                        info.userIdList.add(member.getPhone());
                        info.hasPhone = true;
                    }
                    if (StringUtils.isNotBlank(member.getMemberCode()) && !member.getMemberCode().startsWith("oAw")) {
                        info.userIdList.add(member.getMemberCode());
                    }
                }
            }
            catch (NumberFormatException e) {
                WxMaUser wxUser;
                info.userIdList.add(memberIdStr);
                if (this.wxMaUserMapper == null || (wxUser = this.wxMaUserMapper.selectByOpenid(memberIdStr)) == null || !StringUtils.isNotBlank(wxUser.getPhone())) break block5;
                info.userIdList.add(wxUser.getPhone());
                info.hasPhone = true;
            }
        }
        return info;
    }

    private String getCurrentUserPhone() {
        WxMaUser wxUser;
        String memberIdStr = MemberUtils.getMemberId();
        if (StringUtils.isEmpty(memberIdStr)) {
            return null;
        }
        try {
            Long memberId = Long.parseLong(memberIdStr);
            UmsMember member = (UmsMember)this.umsMemberService.getById(memberId);
            if (member != null && StringUtils.isNotBlank(member.getPhone()) && !member.getPhone().startsWith("999")) {
                return member.getPhone();
            }
        }
        catch (NumberFormatException memberId) {
            // empty catch block
        }
        if (this.wxMaUserMapper != null && (wxUser = this.wxMaUserMapper.selectByOpenid(memberIdStr)) != null && StringUtils.isNotBlank(wxUser.getPhone())) {
            return wxUser.getPhone();
        }
        return null;
    }

    @GetMapping(value={"/{id}"})
    public AjaxResult getById(@PathVariable(value="id") String id) {
        return AjaxResult.success(this.orderInfoService.getById2((Serializable)((Object)id)));
    }

    @PostMapping
    public AjaxResult save(@RequestBody PlaceOrderDTO placeOrderDTO) {
        String spuId;
        GoodsSpu goods;
        String memberId = MemberUtils.getMemberId();
        placeOrderDTO.setUserId(memberId);
        boolean isIntegralOrder = false;
        Integer needPoints = 0;
        List<PlaceOrderGoodsDTO> skus = placeOrderDTO.getSkus();
        if (skus != null && !skus.isEmpty() && (goods = (GoodsSpu)this.goodsSpuService.getById((Serializable)((Object)(spuId = skus.get(0).getSpuId())))) != null && goods.getIntegralPrice() != null && goods.getIntegralPrice() > 0) {
            isIntegralOrder = true;
            needPoints = goods.getIntegralPrice() * skus.get(0).getQuantity();
        }
        if (isIntegralOrder) {
            UmsMember member = (UmsMember)this.umsMemberService.getById(Long.valueOf(Long.parseLong(memberId)));
            if (member == null) {
                return AjaxResult.error("\u7528\u6237\u4e0d\u5b58\u5728");
            }
            Integer userPoints = member.getPoints() != null ? member.getPoints() : 0;
            if (userPoints < needPoints) {
                return AjaxResult.error("\u79ef\u5206\u4e0d\u8db3");
            }
            if (StringUtils.isBlank(placeOrderDTO.getUserAddressId())) {
                return AjaxResult.error("\u8bf7\u9009\u62e9\u6536\u8d27\u5730\u5740");
            }
            placeOrderDTO.setPaymentWay("4");
            placeOrderDTO.setIsPay(true);
        } else {
            placeOrderDTO.setPaymentWay("2");
        }
        OrderInfo orderInfo = this.orderInfoService.orderSub(placeOrderDTO);
        if (orderInfo == null) {
            return AjaxResult.error(MyReturnCode.ERR_70003.getCode(), MyReturnCode.ERR_70003.getMsg());
        }
        if (isIntegralOrder) {
            this.integralFlowService.addPoints(Long.parseLong(memberId), -needPoints.intValue(), 1, "\u5151\u6362\u5546\u54c1\u8ba2\u5355\u53f7:" + orderInfo.getOrderNo());
        }
        return AjaxResult.success(orderInfo);
    }

    @DeleteMapping(value={"/{id}"})
    public AjaxResult removeById(@PathVariable String id) {
        OrderInfo orderInfo = (OrderInfo)this.orderInfoService.getById((Serializable)((Object)id));
        if (orderInfo == null) {
            return AjaxResult.error(MyReturnCode.ERR_70005.getCode(), MyReturnCode.ERR_70005.getMsg());
        }
        if (!OrderInfoEnum.STATUS_5.getValue().equals(orderInfo.getStatus()) || "1".equals(orderInfo.getIsPay())) {
            return AjaxResult.error(MyReturnCode.ERR_70001.getCode(), MyReturnCode.ERR_70001.getMsg());
        }
        return AjaxResult.success(this.orderInfoService.removeById((Serializable)((Object)id)));
    }

    @PutMapping(value={"/cancel/{id}"})
    public AjaxResult orderCancel(@PathVariable String id) {
        OrderInfo orderInfo = (OrderInfo)this.orderInfoService.getById((Serializable)((Object)id));
        if (orderInfo == null) {
            return AjaxResult.error(MyReturnCode.ERR_70005.getCode(), MyReturnCode.ERR_70005.getMsg());
        }
        if (!"0".equals(orderInfo.getIsPay())) {
            return AjaxResult.error(MyReturnCode.ERR_70001.getCode(), MyReturnCode.ERR_70001.getMsg());
        }
        this.orderInfoService.orderCancel(orderInfo);
        return AjaxResult.success();
    }

    @PutMapping(value={"/receive/{id}"})
    public AjaxResult orderReceive(@PathVariable String id) {
        OrderInfo orderInfo = (OrderInfo)this.orderInfoService.getById((Serializable)((Object)id));
        if (orderInfo == null) {
            return AjaxResult.error(MyReturnCode.ERR_70005.getCode(), MyReturnCode.ERR_70005.getMsg());
        }
        if (!OrderInfoEnum.STATUS_2.getValue().equals(orderInfo.getStatus())) {
            return AjaxResult.error(MyReturnCode.ERR_70001.getCode(), MyReturnCode.ERR_70001.getMsg());
        }
        this.orderInfoService.orderReceive(orderInfo);
        return AjaxResult.success();
    }

    @PostMapping(value={"/unifiedOrder"})
    public AjaxResult unifiedOrder(HttpServletRequest request, @RequestBody OrderInfo orderInfo) throws WxPayException {
        String memberId = MemberUtils.getMemberId();
        if (memberId == null || memberId.isEmpty()) {
            return AjaxResult.error(MyReturnCode.ERR_70001.getCode(), "\u8bf7\u5148\u767b\u5f55");
        }
        WxUser wxUser = new WxUser();
        wxUser.setId(memberId);
        wxUser.setSessionKey("");
        wxUser.setOpenId(memberId);
        orderInfo = (OrderInfo)this.orderInfoService.getById((Serializable)((Object)orderInfo.getId()));
        if (orderInfo == null) {
            return AjaxResult.error(MyReturnCode.ERR_70005.getCode(), MyReturnCode.ERR_70005.getMsg());
        }
        if (!"0".equals(orderInfo.getIsPay())) {
            return AjaxResult.error(MyReturnCode.ERR_70004.getCode(), MyReturnCode.ERR_70004.getMsg());
        }
        if (orderInfo.getPaymentPrice().compareTo(BigDecimal.ZERO) == 0) {
            orderInfo.setPaymentTime(LocalDateTime.now());
            this.orderInfoService.notifyOrder(orderInfo);
            return AjaxResult.success();
        }
        if ("4".equals(orderInfo.getPaymentWay())) {
            UmsMember member = (UmsMember)this.umsMemberService.getById(Long.valueOf(Long.parseLong(memberId)));
            if (member == null || member.getPoints() == null || member.getPoints() < orderInfo.getPaymentPrice().intValue()) {
                return AjaxResult.error("\u79ef\u5206\u4e0d\u8db3");
            }
            this.integralFlowService.addPoints(Long.parseLong(memberId), -orderInfo.getPaymentPrice().intValue(), 9, "\u79ef\u5206\u8d2d\u4e70\u5546\u54c1-" + orderInfo.getName());
            orderInfo.setPaymentTime(LocalDateTime.now());
            this.orderInfoService.notifyOrder(orderInfo);
            return AjaxResult.success();
        }
        String appId = WxMaUtil.getAppId(request);
        WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
        wxPayUnifiedOrderRequest.setAppid(appId);
        String body = orderInfo.getName();
        body = body.length() > 40 ? body.substring(0, 39) : body;
        wxPayUnifiedOrderRequest.setBody(body);
        wxPayUnifiedOrderRequest.setOutTradeNo(orderInfo.getOrderNo());
        wxPayUnifiedOrderRequest.setTotalFee(orderInfo.getPaymentPrice().multiply(new BigDecimal(100)).intValue());
        wxPayUnifiedOrderRequest.setTradeType("JSAPI");
        wxPayUnifiedOrderRequest.setNotifyUrl(this.mallConfigProperties.getNotifyHost() + "/weixin/api/ma/orderinfo/notify-order");
        wxPayUnifiedOrderRequest.setSpbillCreateIp("127.0.0.1");
        wxPayUnifiedOrderRequest.setOpenid(wxUser.getOpenId());
        WxPayService wxPayService = WxPayConfiguration.getPayService();
        return AjaxResult.success(JSONUtil.parse(wxPayService.createOrder(wxPayUnifiedOrderRequest)));
    }

    @PostMapping(value={"/notify-order"})
    public String notifyOrder(@RequestBody String xmlData) throws WxPayException {
        WxPayService wxPayService = WxPayConfiguration.getPayService();
        WxPayOrderNotifyResult notifyResult = wxPayService.parseOrderNotifyResult(xmlData);
        log.info("\u652f\u4ed8\u56de\u8c03, \u8ba2\u5355\u53f7: {}, \u91d1\u989d: {} \u5206", (Object)notifyResult.getOutTradeNo(), (Object)notifyResult.getTotalFee());
        OrderInfo orderInfo = (OrderInfo)this.orderInfoService.getOne((Wrapper)Wrappers.lambdaQuery().eq(OrderInfo::getOrderNo, notifyResult.getOutTradeNo()));
        if (orderInfo != null) {
            if (orderInfo.getPaymentPrice().multiply(new BigDecimal(100)).intValue() == notifyResult.getTotalFee().intValue()) {
                String timeEnd = notifyResult.getTimeEnd();
                LocalDateTime paymentTime = LocalDateTimeUtils.parse(timeEnd);
                orderInfo.setPaymentTime(paymentTime);
                orderInfo.setTransactionId(notifyResult.getTransactionId());
                this.orderInfoService.notifyOrder(orderInfo);
                return WxPayNotifyResponse.success("\u6210\u529f");
            }
            return WxPayNotifyResponse.fail("\u4ed8\u6b3e\u91d1\u989d\u4e0e\u8ba2\u5355\u91d1\u989d\u4e0d\u7b49");
        }
        return WxPayNotifyResponse.fail("\u65e0\u6b64\u8ba2\u5355");
    }

    @PostMapping(value={"/notify-logisticsr"})
    public String notifyLogisticsr(HttpServletRequest request, HttpServletResponse response) {
        String param = request.getParameter("param");
        String logisticsId = request.getParameter("logisticsId");
        String tenantId = request.getParameter("tenantId");
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("result", false);
        map.put("returnCode", "500");
        map.put("message", "\u4fdd\u5b58\u5931\u8d25");
        try {
            JSONObject jsonObject = JSONUtil.parseObj(param);
            map.put("result", true);
            map.put("returnCode", "200");
            map.put("message", "\u4fdd\u5b58\u6210\u529f");
            response.getWriter().print(JSONUtil.parseObj(map));
        }
        catch (Exception e) {
            map.put("message", "\u4fdd\u5b58\u5931\u8d25" + e.getMessage());
            try {
                response.getWriter().print(JSONUtil.parseObj(map));
            }
            catch (Exception e1) {
                log.error("\u7269\u6d41\u56de\u8c03\u5199\u54cd\u5e94\u5931\u8d25", e1);
            }
        }
        return null;
    }

    @GetMapping(value={"/countAll"})
    public AjaxResult count(OrderInfo orderInfo) {
        orderInfo.setUserId(MemberUtils.getMemberId());
        HashMap<String, Long> countAll = new HashMap<String, Long>();
        countAll.put(OrderInfoEnum.STATUS_0.getValue(), this.orderInfoService.count((Wrapper)((LambdaQueryWrapper)Wrappers.query(orderInfo).lambda().isNull(OrderInfo::getStatus)).eq(OrderInfo::getIsPay, "0")));
        countAll.put(OrderInfoEnum.STATUS_1.getValue(), this.orderInfoService.count((Wrapper)((LambdaQueryWrapper)Wrappers.query(orderInfo).lambda().eq(OrderInfo::getStatus, OrderInfoEnum.STATUS_1.getValue())).eq(OrderInfo::getIsPay, "1")));
        countAll.put(OrderInfoEnum.STATUS_2.getValue(), this.orderInfoService.count((Wrapper)((LambdaQueryWrapper)Wrappers.query(orderInfo).lambda().eq(OrderInfo::getStatus, OrderInfoEnum.STATUS_2.getValue())).eq(OrderInfo::getIsPay, "1")));
        countAll.put(OrderInfoEnum.STATUS_3.getValue(), this.orderInfoService.count((Wrapper)((LambdaQueryWrapper)Wrappers.query(orderInfo).lambda().eq(OrderInfo::getStatus, OrderInfoEnum.STATUS_3.getValue())).eq(OrderInfo::getIsPay, "1")));
        return AjaxResult.success(countAll);
    }

    @PostMapping(value={"/refunds"})
    public AjaxResult saveRefunds(@RequestBody OrderItem orderItem) {
        this.orderInfoService.saveRefunds(orderItem);
        return AjaxResult.success();
    }

    @PostMapping(value={"/notify-refunds"})
    public String notifyRefunds(@RequestBody String xmlData) {
        WxPayService wxPayService = WxPayConfiguration.getPayService();
        try {
            WxPayRefundNotifyResult notifyResult = wxPayService.parseRefundNotifyResult(xmlData);
            log.info("\u9000\u6b3e\u56de\u8c03, \u9000\u6b3e\u5355\u53f7: {}", (Object)(notifyResult.getReqInfo() != null ? notifyResult.getReqInfo().getOutRefundNo() : "unknown"));
            this.orderInfoService.notifyRefunds(notifyResult);
            return WxPayNotifyResponse.success("\u6210\u529f");
        }
        catch (Exception e) {
            log.error("\u9000\u6b3e\u56de\u8c03\u5904\u7406\u5f02\u5e38", e);
            return WxPayNotifyResponse.fail("\u5904\u7406\u5931\u8d25");
        }
    }

    @GetMapping(value={"/logistics/{id}"})
    public AjaxResult getLogistics(@PathVariable(value="id") String id) {
        OrderInfo orderInfo = (OrderInfo)this.orderInfoService.getById((Serializable)((Object)id));
        if (orderInfo == null) {
            return AjaxResult.error("\u8ba2\u5355\u4e0d\u5b58\u5728");
        }
        if (!orderInfo.getUserId().equals(MemberUtils.getMemberId())) {
            return AjaxResult.error("\u65e0\u6743\u64cd\u4f5c");
        }
        OrderLogistics logistics = (OrderLogistics)this.orderLogisticsService.getById((Serializable)((Object)orderInfo.getLogisticsId()));
        if (logistics == null) {
            return AjaxResult.success(new HashMap<String, Object>(){
                {
                    this.put("logistics", null);
                    this.put("track", null);
                }
            });
        }
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("logistics", logistics);
        result.put("track", this.kuaidi100QueryService.query(logistics));
        return AjaxResult.success(result);
    }

    @Generated
    public OrderInfoApi(OrderInfoService orderInfoService, OrderLogisticsService orderLogisticsService, Kuaidi100QueryService kuaidi100QueryService, TbIntegralFlowService integralFlowService, UmsMemberService umsMemberService, GoodsSpuService goodsSpuService, MallConfigProperties mallConfigProperties, WxMaUserMapper wxMaUserMapper) {
        this.orderInfoService = orderInfoService;
        this.orderLogisticsService = orderLogisticsService;
        this.kuaidi100QueryService = kuaidi100QueryService;
        this.integralFlowService = integralFlowService;
        this.umsMemberService = umsMemberService;
        this.goodsSpuService = goodsSpuService;
        this.mallConfigProperties = mallConfigProperties;
        this.wxMaUserMapper = wxMaUserMapper;
    }

    private static class UserIdInfo {
        List<String> userIdList = new ArrayList<String>();
        boolean hasPhone = false;

        private UserIdInfo() {
        }
    }
}

