/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.dingyangmall.com
 * 注意：
 * 本软件为www.dingyangmall.com开发研制，项目使用请保留此说明
 */
package com.dingyangmall.web.controller.mall;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.mall.config.CommonConstants;
import com.dingyangmall.mall.entity.OrderInfo;
import com.dingyangmall.mall.entity.OrderItem;
import com.dingyangmall.mall.entity.OrderLogistics;
import com.dingyangmall.mall.service.OrderInfoService;
import com.dingyangmall.mall.service.OrderLogisticsService;
import com.dingyangmall.mall.service.UmsMemberService;
import com.dingyangmall.mall.constant.MallReturnCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 商城订单
 *
 * @author JL
 * @date 2019-09-10 15:21:22
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/orderinfo")
public class OrderInfoController extends BaseController {

    private final OrderInfoService orderInfoService;
	private final OrderLogisticsService orderLogisticsService;
	private final UmsMemberService umsMemberService;

    /**
    * 分页查询
    * @param page 分页对象
    * @param orderInfo 商城订单
    * @return
    */
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermi('mall:orderinfo:index')")
    public AjaxResult getOrderInfoPage(Page page, OrderInfo orderInfo) {
        return AjaxResult.success(orderInfoService.page1(page, Wrappers.query(orderInfo)));
    }

	/**
	 * 查询数量
	 * @param orderInfo
	 * @return
	 */
	@GetMapping("/count")
	public AjaxResult getCount(OrderInfo orderInfo) {
		return AjaxResult.success(orderInfoService.count(Wrappers.query(orderInfo)));
	}

    /**
    * 通过id查询商城订单
    * @param id
    * @return R
    */
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('mall:orderinfo:get')")
    public AjaxResult getById(@PathVariable("id") String id){
		OrderInfo orderInfo = orderInfoService.getById(id);
		OrderLogistics orderLogistics = orderLogisticsService.getById(orderInfo.getLogisticsId());
		orderInfo.setOrderLogistics(orderLogistics);
		orderInfo.setUserInfo(umsMemberService.getById(orderInfo.getUserId()));
        return AjaxResult.success(orderInfo);
    }

    /**
    * 新增商城订单
    * @param orderInfo 商城订单
    * @return R
    */
    @PostMapping
    @PreAuthorize("@ss.hasPermi('mall:orderinfo:add')")
    public AjaxResult save(@RequestBody OrderInfo orderInfo){
        return AjaxResult.success(orderInfoService.save(orderInfo));
    }

    /**
    * 修改商城订单
    * @param orderInfo 商城订单
    * @return R
    */
    @PutMapping
    @PreAuthorize("@ss.hasPermi('mall:orderinfo:edit')")
    public AjaxResult updateById(@RequestBody OrderInfo orderInfo){
        return AjaxResult.success(orderInfoService.updateById(orderInfo));
    }

    /**
    * 通过id删除商城订单
    * @param id
    * @return R
    */
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('mall:orderinfo:del')")
    public AjaxResult removeById(@PathVariable String id){
        return AjaxResult.success(orderInfoService.removeById(id));
    }

	/**
	 * 取消商城订单
	 * @param id 商城订单
	 * @return R
	 */
	@PutMapping("/cancel/{id}")
	@PreAuthorize("@ss.hasPermi('mall:orderinfo:edit')")
	public AjaxResult orderCancel(@PathVariable String id){
		OrderInfo orderInfo = orderInfoService.getById(id);
		if(orderInfo == null){
			return AjaxResult.error(MallReturnCode.ERR_70005.getCode(), MallReturnCode.ERR_70005.getMsg());
		}
		if(!CommonConstants.NO.equals(orderInfo.getIsPay())){//只有未支付订单能取消
			return AjaxResult.error(MallReturnCode.ERR_70001.getCode(), MallReturnCode.ERR_70001.getMsg());
		}
		orderInfoService.orderCancel(orderInfo);
		return AjaxResult.success();
	}

	/**
	 * 操作退款
	 * @param orderItem
	 * @return R
	 */
	@PutMapping("/doOrderRefunds")
	@PreAuthorize("@ss.hasPermi('mall:orderinfo:edit')")
	public AjaxResult doOrderRefunds(@RequestBody OrderItem orderItem) {
		orderInfoService.doOrderRefunds(orderItem);
		return AjaxResult.success();
	}

	/**
	 * 订单发货：填写快递公司、快递单号，订单状态改为待收货
	 * @param id 订单ID
	 * @param body logistics=快递公司编码, logisticsNo=快递单号（可选：userName,telNum,address 收货信息）
	 */
	@PutMapping("/{id}/ship")
	@PreAuthorize("@ss.hasPermi('mall:orderinfo:edit')")
	public AjaxResult ship(@PathVariable String id, @RequestBody java.util.Map<String, String> body) {
		OrderInfo order = orderInfoService.getById(id);
		if (order == null) {
			return AjaxResult.error("订单不存在");
		}
		if (!com.dingyangmall.mall.enums.OrderInfoEnum.STATUS_1.getValue().equals(order.getStatus())) {
			return AjaxResult.error("只有待发货订单可执行发货");
		}
		String logisticsCode = body != null ? body.get("logistics") : null;
		String logisticsNo = body != null ? body.get("logisticsNo") : null;
		if (logisticsCode == null || logisticsCode.isEmpty() || logisticsNo == null || logisticsNo.isEmpty()) {
			return AjaxResult.error("请填写快递公司和快递单号");
		}
		com.dingyangmall.mall.entity.OrderLogistics logistics = new com.dingyangmall.mall.entity.OrderLogistics();
		logistics.setLogistics(logisticsCode);
		logistics.setLogisticsNo(logisticsNo);
		if (body != null) {
			if (body.get("userName") != null) logistics.setUserName(body.get("userName"));
			if (body.get("telNum") != null) logistics.setTelNum(body.get("telNum"));
			if (body.get("address") != null) logistics.setAddress(body.get("address"));
		}
		orderLogisticsService.save(logistics);
		order.setLogisticsId(logistics.getId());
		order.setStatus(com.dingyangmall.mall.enums.OrderInfoEnum.STATUS_2.getValue());
		order.setDeliveryTime(java.time.LocalDateTime.now());
		orderInfoService.updateById(order);
		return AjaxResult.success();
	}
}

