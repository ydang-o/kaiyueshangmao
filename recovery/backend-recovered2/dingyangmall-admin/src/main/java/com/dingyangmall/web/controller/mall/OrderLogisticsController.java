/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.controller.mall;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.mall.entity.OrderLogistics;
import com.dingyangmall.mall.enums.OrderLogisticsEnum;
import com.dingyangmall.mall.service.OrderLogisticsService;
import java.io.Serializable;
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
@RequestMapping(value={"/orderlogistics"})
public class OrderLogisticsController
extends BaseController {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(OrderLogisticsController.class);
    private final OrderLogisticsService orderLogisticsService;

    @GetMapping(value={"/page"})
    @PreAuthorize(value="@ato.hasAuthority('mall:orderlogistics:index')")
    public AjaxResult getOrderLogisticsPage(Page page, OrderLogistics orderLogistics) {
        return AjaxResult.success(this.orderLogisticsService.page(page, Wrappers.query(orderLogistics)));
    }

    @GetMapping(value={"/{id}"})
    @PreAuthorize(value="@ato.hasAuthority('mall:orderlogistics:get')")
    public AjaxResult getById(@PathVariable(value="id") String id) {
        return AjaxResult.success(this.orderLogisticsService.getById((Serializable)((Object)id)));
    }

    @PostMapping
    @PreAuthorize(value="@ato.hasAuthority('mall:orderlogistics:add')")
    public AjaxResult save(@RequestBody OrderLogistics orderLogistics) {
        return AjaxResult.success(this.orderLogisticsService.save(orderLogistics));
    }

    @PutMapping
    @PreAuthorize(value="@ato.hasAuthority('mall:orderlogistics:edit')")
    public AjaxResult updateById(@RequestBody OrderLogistics orderLogistics) {
        return AjaxResult.success(this.orderLogisticsService.updateById(orderLogistics));
    }

    @DeleteMapping(value={"/{id}"})
    @PreAuthorize(value="@ato.hasAuthority('mall:orderlogistics:del')")
    public AjaxResult removeById(@PathVariable String id) {
        return AjaxResult.success(this.orderLogisticsService.removeById((Serializable)((Object)id)));
    }

    @GetMapping(value={"/dict/{type}"})
    public AjaxResult getDictByType(@PathVariable String type) {
        return AjaxResult.success(OrderLogisticsEnum.queryAll(type));
    }

    @Generated
    public OrderLogisticsController(OrderLogisticsService orderLogisticsService) {
        this.orderLogisticsService = orderLogisticsService;
    }
}

