/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.dingyangmall.com
 * 注意：
 * 本软件为www.dingyangmall.com开发研制，项目使用请保留此说明
 */
package com.dingyangmall.web.api;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.mall.config.CommonConstants;
import com.dingyangmall.mall.config.MallConfigProperties;
import com.dingyangmall.mall.constant.MallConstants;
import com.dingyangmall.mall.dto.PlaceOrderDTO;
import com.dingyangmall.mall.entity.OrderInfo;
import com.dingyangmall.mall.entity.OrderItem;
import com.dingyangmall.mall.entity.OrderLogistics;
import com.dingyangmall.mall.enums.OrderInfoEnum;
import com.dingyangmall.mall.service.Kuaidi100QueryService;
import com.dingyangmall.mall.service.OrderInfoService;
import com.dingyangmall.weixin.config.WxPayConfiguration;
import com.dingyangmall.weixin.constant.MyReturnCode;
import com.dingyangmall.weixin.entity.WxUser;
import com.dingyangmall.weixin.utils.LocalDateTimeUtils;
import com.dingyangmall.weixin.utils.ThirdSessionHolder;
import com.dingyangmall.weixin.utils.WxMaUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 商城订单
 *
 * @author JL
 * @date 2019-09-10 15:21:22
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/weixin/api/ma/orderinfo")
public class OrderInfoApi {

    private final OrderInfoService orderInfoService;
    private final com.dingyangmall.mall.service.OrderLogisticsService orderLogisticsService;
    private final Kuaidi100QueryService kuaidi100QueryService;
	private final MallConfigProperties mallConfigProperties;

	/**
	* 分页查询
	* @param page 分页对象
	* @param orderInfo 商城订单
	* @return
	*/
    @GetMapping("/page")
    public AjaxResult getOrderInfoPage(Page page, OrderInfo orderInfo) {
		orderInfo.setUserId(ThirdSessionHolder.getWxUserId());
        return AjaxResult.success(orderInfoService.page2(page,orderInfo));
    }

    /**
    * 通过id查询商城订单
    * @param id
    * @return R
    */
    @GetMapping("/{id}")
    public AjaxResult getById(@PathVariable("id") String id){
		return AjaxResult.success(orderInfoService.getById2(id));
    }

    /**
    * 新增商城订单
    * @param placeOrderDTO 商城订单
    * @return R
    */
    @PostMapping
    public AjaxResult save(@RequestBody PlaceOrderDTO placeOrderDTO){
		placeOrderDTO.setUserId(ThirdSessionHolder.getWxUserId());
		placeOrderDTO.setPaymentWay(MallConstants.PAYMENT_WAY_2);
		OrderInfo orderInfo = orderInfoService.orderSub(placeOrderDTO);
		if(orderInfo == null){
			return AjaxResult.error(MyReturnCode.ERR_70003.getCode(), MyReturnCode.ERR_70003.getMsg());
		}
		return AjaxResult.success(orderInfo);
    }

    /**
    * 通过id删除商城订单
    * @param id
    * @return R
    */
    @DeleteMapping("/{id}")
    public AjaxResult removeById(@PathVariable String id){
		OrderInfo orderInfo = orderInfoService.getById(id);
		if(orderInfo == null){
			return AjaxResult.error(MyReturnCode.ERR_70005.getCode(), MyReturnCode.ERR_70005.getMsg());
		}
		if(!OrderInfoEnum.STATUS_5.getValue().equals(orderInfo.getStatus()) || CommonConstants.YES.equals(orderInfo.getIsPay())){
			return AjaxResult.error(MyReturnCode.ERR_70001.getCode(), MyReturnCode.ERR_70001.getMsg());
		}
		return AjaxResult.success(orderInfoService.removeById(id));
    }

	/**
	 * 取消商城订单
	 * @param id 商城订单
	 * @return R
	 */
	@PutMapping("/cancel/{id}")
	public AjaxResult orderCancel(@PathVariable String id){
		OrderInfo orderInfo = orderInfoService.getById(id);
		if(orderInfo == null){
			return AjaxResult.error(MyReturnCode.ERR_70005.getCode(), MyReturnCode.ERR_70005.getMsg());
		}
		if(!CommonConstants.NO.equals(orderInfo.getIsPay())){//只有未支付订单能取消
			return AjaxResult.error(MyReturnCode.ERR_70001.getCode(), MyReturnCode.ERR_70001.getMsg());
		}
		orderInfoService.orderCancel(orderInfo);
		return AjaxResult.success();
	}

	/**
	 * 商城订单确认收货
	 * @param id 商城订单
	 * @return R
	 */
	@PutMapping("/receive/{id}")
	public AjaxResult orderReceive(@PathVariable String id){
		OrderInfo orderInfo = orderInfoService.getById(id);
		if(orderInfo == null){
			return AjaxResult.error(MyReturnCode.ERR_70005.getCode(), MyReturnCode.ERR_70005.getMsg());
		}
		if(!OrderInfoEnum.STATUS_2.getValue().equals(orderInfo.getStatus())){//只有待收货订单能确认收货
			return AjaxResult.error(MyReturnCode.ERR_70001.getCode(), MyReturnCode.ERR_70001.getMsg());
		}
		orderInfoService.orderReceive(orderInfo);
		return AjaxResult.success();
	}

	/**
	 * 调用统一下单接口，并组装生成支付所需参数对象.
	 *
	 * @param orderInfo 统一下单请求参数
	 * @return 返回 {@link com.github.binarywang.wxpay.bean.order}包下的类对象
	 */
	@PostMapping("/unifiedOrder")
	public AjaxResult unifiedOrder(HttpServletRequest request, @RequestBody OrderInfo orderInfo) throws WxPayException {
		//检验用户session登录
		WxUser wxUser = new WxUser();
		wxUser.setId(ThirdSessionHolder.getThirdSession().getWxUserId());
		wxUser.setSessionKey(ThirdSessionHolder.getThirdSession().getSessionKey());
		wxUser.setOpenId(ThirdSessionHolder.getThirdSession().getOpenId());
		orderInfo = orderInfoService.getById(orderInfo.getId());
		if(orderInfo == null){
			return AjaxResult.error(MyReturnCode.ERR_70005.getCode(), MyReturnCode.ERR_70005.getMsg());
		}
		if(!CommonConstants.NO.equals(orderInfo.getIsPay())){//只有未支付的详单能发起支付
			return AjaxResult.error(MyReturnCode.ERR_70004.getCode(), MyReturnCode.ERR_70004.getMsg());
		}
		if(orderInfo.getPaymentPrice().compareTo(BigDecimal.ZERO)==0){//0元购买不调支付
			orderInfo.setPaymentTime(LocalDateTime.now());
			orderInfoService.notifyOrder(orderInfo);
			return AjaxResult.success();
		}
		String appId = WxMaUtil.getAppId(request);
		WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
		wxPayUnifiedOrderRequest.setAppid(appId);
		String body = orderInfo.getName();
		body = body.length() > 40 ? body.substring(0,39) : body;
		wxPayUnifiedOrderRequest.setBody(body);
		wxPayUnifiedOrderRequest.setOutTradeNo(orderInfo.getOrderNo());
		wxPayUnifiedOrderRequest.setTotalFee(orderInfo.getPaymentPrice().multiply(new BigDecimal(100)).intValue());
		wxPayUnifiedOrderRequest.setTradeType("JSAPI");
		wxPayUnifiedOrderRequest.setNotifyUrl(mallConfigProperties.getNotifyHost()+"/weixin/api/ma/orderinfo/notify-order");
		wxPayUnifiedOrderRequest.setSpbillCreateIp("127.0.0.1");
		wxPayUnifiedOrderRequest.setOpenid(wxUser.getOpenId());
		WxPayService wxPayService = WxPayConfiguration.getPayService();
		return AjaxResult.success(JSONUtil.parse(wxPayService.createOrder(wxPayUnifiedOrderRequest)));
	}

	/**
	 * 支付回调
	 * @param xmlData
	 * @return
	 * @throws WxPayException
	 */
	@PostMapping("/notify-order")
	public String notifyOrder(@RequestBody String xmlData) throws WxPayException {
		WxPayService wxPayService = WxPayConfiguration.getPayService();
		WxPayOrderNotifyResult notifyResult = wxPayService.parseOrderNotifyResult(xmlData);
		log.info("支付回调, 订单号: {}, 金额: {} 分", notifyResult.getOutTradeNo(), notifyResult.getTotalFee());
		OrderInfo orderInfo = orderInfoService.getOne(Wrappers.<OrderInfo>lambdaQuery()
				.eq(OrderInfo::getOrderNo,notifyResult.getOutTradeNo()));
		if(orderInfo != null){
			if(orderInfo.getPaymentPrice().multiply(new BigDecimal(100)).intValue() == notifyResult.getTotalFee()){
				String timeEnd = notifyResult.getTimeEnd();
				LocalDateTime paymentTime = LocalDateTimeUtils.parse(timeEnd);
				orderInfo.setPaymentTime(paymentTime);
				orderInfo.setTransactionId(notifyResult.getTransactionId());
				orderInfoService.notifyOrder(orderInfo);
				return WxPayNotifyResponse.success("成功");
			}else{
				return WxPayNotifyResponse.fail("付款金额与订单金额不等");
			}
		}else{
			return WxPayNotifyResponse.fail("无此订单");
		}
	}

	/**
	 * 物流信息回调
	 * @param request
	 * @return
	 */
	@PostMapping("/notify-logisticsr")
	public String notifyLogisticsr(HttpServletRequest request, HttpServletResponse response) {
		String param = request.getParameter("param");
		String logisticsId = request.getParameter("logisticsId");
		String tenantId = request.getParameter("tenantId");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("result",false);
		map.put("returnCode","500");
		map.put("message","保存失败");
		try {
			JSONObject jsonObject = JSONUtil.parseObj(param);
//			orderInfoService.notifyLogisticsr(logisticsId, jsonObject);
			map.put("result",true);
			map.put("returnCode","200");
			map.put("message","保存成功");
			//这里必须返回，否则认为失败，过30分钟又会重复推送。
			response.getWriter().print(JSONUtil.parseObj(map));
		} catch (Exception e) {
			map.put("message","保存失败" + e.getMessage());
			//保存失败，服务端等30分钟会重复推送。
			try {
				response.getWriter().print(JSONUtil.parseObj(map));
			} catch (Exception e1) {
				log.error("物流回调写响应失败", e1);
			}
		}
		return null;
	}

	/**
	 * 统计各个状态订单计数
	 * @param orderInfo
	 * @return R
	 */
	@GetMapping("/countAll")
	public AjaxResult count(OrderInfo orderInfo){
		orderInfo.setUserId(ThirdSessionHolder.getWxUserId());
		Map<String, Long> countAll = new HashMap<>();
		countAll.put(OrderInfoEnum.STATUS_0.getValue(),orderInfoService.count(Wrappers.query(orderInfo).lambda()
				.isNull(OrderInfo::getStatus)
				.eq(OrderInfo::getIsPay,CommonConstants.NO)));

		countAll.put(OrderInfoEnum.STATUS_1.getValue(),orderInfoService.count(Wrappers.query(orderInfo).lambda()
				.eq(OrderInfo::getStatus,OrderInfoEnum.STATUS_1.getValue())
				.eq(OrderInfo::getIsPay,CommonConstants.YES)));

		countAll.put(OrderInfoEnum.STATUS_2.getValue(),orderInfoService.count(Wrappers.query(orderInfo).lambda()
				.eq(OrderInfo::getStatus,OrderInfoEnum.STATUS_2.getValue())
				.eq(OrderInfo::getIsPay,CommonConstants.YES)));

		countAll.put(OrderInfoEnum.STATUS_3.getValue(),orderInfoService.count(Wrappers.query(orderInfo).lambda()
				.eq(OrderInfo::getStatus,OrderInfoEnum.STATUS_3.getValue())
				.eq(OrderInfo::getIsPay,CommonConstants.YES)));
		return AjaxResult.success(countAll);
	}

	/**
	 * 发起退款申请
	 * @param orderItem
	 * @return R
	 */
	@PostMapping("/refunds")
	public AjaxResult saveRefunds(@RequestBody OrderItem orderItem) {
		orderInfoService.saveRefunds(orderItem);
		return AjaxResult.success();
	}

	/**
	 * 退款回调
	 * @param xmlData
	 * @return
	 * @throws WxPayException
	 */
	@PostMapping("/notify-refunds")
	public String notifyRefunds(@RequestBody String xmlData) {
		WxPayService wxPayService = WxPayConfiguration.getPayService();
		try {
			WxPayRefundNotifyResult notifyResult = wxPayService.parseRefundNotifyResult(xmlData);
			log.info("退款回调, 退款单号: {}", notifyResult.getReqInfo() != null ? notifyResult.getReqInfo().getOutRefundNo() : "unknown");
			orderInfoService.notifyRefunds(notifyResult);
			return WxPayNotifyResponse.success("成功");
		} catch (Exception e) {
			log.error("退款回调处理异常", e);
			return WxPayNotifyResponse.fail("处理失败");
		}
	}



    /**
     * 获取物流信息（含快递100实时轨迹，未配置时仅返回本地物流信息）
     * @param id 订单 ID
     * @return logistics 订单物流基本信息，track 实时轨迹（快递100）
     */
    @GetMapping("/logistics/{id}")
    public AjaxResult getLogistics(@PathVariable("id") String id){
        OrderInfo orderInfo = orderInfoService.getById(id);
        if(orderInfo == null){
            return AjaxResult.error("订单不存在");
        }
        if(!orderInfo.getUserId().equals(ThirdSessionHolder.getWxUserId())){
             return AjaxResult.error("无权操作");
        }
        OrderLogistics logistics = orderLogisticsService.getById(orderInfo.getLogisticsId());
        if (logistics == null) {
            return AjaxResult.success(new HashMap<String, Object>() {{
                put("logistics", null);
                put("track", null);
            }});
        }
        Map<String, Object> result = new HashMap<>();
        result.put("logistics", logistics);
        result.put("track", kuaidi100QueryService.query(logistics));
        return AjaxResult.success(result);
    }
}

