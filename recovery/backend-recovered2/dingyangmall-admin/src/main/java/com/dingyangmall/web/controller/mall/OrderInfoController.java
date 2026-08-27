/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.controller.mall;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.mall.constant.MallReturnCode;
import com.dingyangmall.mall.entity.OrderInfo;
import com.dingyangmall.mall.entity.OrderItem;
import com.dingyangmall.mall.entity.OrderLogistics;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.enums.OrderInfoEnum;
import com.dingyangmall.mall.service.OrderInfoService;
import com.dingyangmall.mall.service.OrderLogisticsService;
import com.dingyangmall.mall.service.UmsMemberService;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/orderinfo"})
public class OrderInfoController
extends BaseController {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(OrderInfoController.class);
    private final OrderInfoService orderInfoService;
    private final OrderLogisticsService orderLogisticsService;
    private final UmsMemberService umsMemberService;

    @GetMapping(value={"/page"})
    @PreAuthorize(value="@ss.hasPermi('mall:orderinfo:index')")
    public AjaxResult getOrderInfoPage(Page page, OrderInfo orderInfo) {
        return AjaxResult.success(this.orderInfoService.page1(page, Wrappers.query(orderInfo)));
    }

    @GetMapping(value={"/count"})
    public AjaxResult getCount(OrderInfo orderInfo) {
        return AjaxResult.success(this.orderInfoService.count(Wrappers.query(orderInfo)));
    }

    @GetMapping(value={"/{id}"})
    @PreAuthorize(value="@ss.hasPermi('mall:orderinfo:get')")
    public AjaxResult getById(@PathVariable(value="id") String id) {
        OrderInfo orderInfo = (OrderInfo)this.orderInfoService.getById((Serializable)((Object)id));
        OrderLogistics orderLogistics = (OrderLogistics)this.orderLogisticsService.getById((Serializable)((Object)orderInfo.getLogisticsId()));
        orderInfo.setOrderLogistics(orderLogistics);
        orderInfo.setUserInfo((UmsMember)this.umsMemberService.getById((Serializable)((Object)orderInfo.getUserId())));
        return AjaxResult.success(orderInfo);
    }

    @PostMapping
    @PreAuthorize(value="@ss.hasPermi('mall:orderinfo:add')")
    public AjaxResult save(@RequestBody OrderInfo orderInfo) {
        return AjaxResult.success(this.orderInfoService.save(orderInfo));
    }

    @PutMapping
    @PreAuthorize(value="@ss.hasPermi('mall:orderinfo:edit')")
    public AjaxResult updateById(@RequestBody OrderInfo orderInfo) {
        return AjaxResult.success(this.orderInfoService.updateById(orderInfo));
    }

    @DeleteMapping(value={"/{id}"})
    @PreAuthorize(value="@ss.hasPermi('mall:orderinfo:del')")
    public AjaxResult removeById(@PathVariable String id) {
        return AjaxResult.success(this.orderInfoService.removeById((Serializable)((Object)id)));
    }

    @PutMapping(value={"/cancel/{id}"})
    @PreAuthorize(value="@ss.hasPermi('mall:orderinfo:edit')")
    public AjaxResult orderCancel(@PathVariable String id) {
        OrderInfo orderInfo = (OrderInfo)this.orderInfoService.getById((Serializable)((Object)id));
        if (orderInfo == null) {
            return AjaxResult.error(MallReturnCode.ERR_70005.getCode(), (Object)MallReturnCode.ERR_70005.getMsg());
        }
        if (!"0".equals(orderInfo.getIsPay())) {
            return AjaxResult.error(MallReturnCode.ERR_70001.getCode(), (Object)MallReturnCode.ERR_70001.getMsg());
        }
        this.orderInfoService.orderCancel(orderInfo);
        return AjaxResult.success();
    }

    @PutMapping(value={"/doOrderRefunds"})
    @PreAuthorize(value="@ss.hasPermi('mall:orderinfo:edit')")
    public AjaxResult doOrderRefunds(@RequestBody OrderItem orderItem) {
        this.orderInfoService.doOrderRefunds(orderItem);
        return AjaxResult.success();
    }

    @PutMapping(value={"/{id}/ship"})
    @PreAuthorize(value="@ss.hasPermi('mall:orderinfo:edit')")
    public AjaxResult ship(@PathVariable String id, @RequestBody Map<String, String> body) {
        String logisticsNo;
        OrderInfo order = (OrderInfo)this.orderInfoService.getById((Serializable)((Object)id));
        if (order == null) {
            return AjaxResult.error("\u8ba2\u5355\u4e0d\u5b58\u5728");
        }
        if (!OrderInfoEnum.STATUS_1.getValue().equals(order.getStatus())) {
            return AjaxResult.error("\u53ea\u6709\u5f85\u53d1\u8d27\u8ba2\u5355\u53ef\u6267\u884c\u53d1\u8d27");
        }
        String logisticsCode = body != null ? body.get("logistics") : null;
        String string = logisticsNo = body != null ? body.get("logisticsNo") : null;
        if (logisticsCode == null || logisticsCode.isEmpty() || logisticsNo == null || logisticsNo.isEmpty()) {
            return AjaxResult.error("\u8bf7\u586b\u5199\u5feb\u9012\u516c\u53f8\u548c\u5feb\u9012\u5355\u53f7");
        }
        OrderLogistics logistics = order.getLogisticsId() != null && !order.getLogisticsId().isEmpty() ? (OrderLogistics)this.orderLogisticsService.getById((Serializable)((Object)order.getLogisticsId())) : new OrderLogistics();
        logistics.setLogistics(logisticsCode);
        logistics.setLogisticsNo(logisticsNo);
        if (body != null) {
            if (body.get("userName") != null) {
                logistics.setUserName(body.get("userName"));
            }
            if (body.get("telNum") != null) {
                logistics.setTelNum(body.get("telNum"));
            }
            if (body.get("address") != null) {
                logistics.setAddress(body.get("address"));
            }
        }
        if (logistics.getId() != null && !logistics.getId().isEmpty()) {
            this.orderLogisticsService.updateById(logistics);
        } else {
            this.orderLogisticsService.save(logistics);
            order.setLogisticsId(logistics.getId());
        }
        order.setStatus(OrderInfoEnum.STATUS_2.getValue());
        order.setDeliveryTime(LocalDateTime.now());
        this.orderInfoService.updateById(order);
        return AjaxResult.success();
    }

    @Generated
    public OrderInfoController(OrderInfoService orderInfoService, OrderLogisticsService orderLogisticsService, UmsMemberService umsMemberService) {
        this.orderInfoService = orderInfoService;
        this.orderLogisticsService = orderLogisticsService;
        this.umsMemberService = umsMemberService;
    }
}

