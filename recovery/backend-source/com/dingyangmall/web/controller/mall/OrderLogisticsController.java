/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.core.toolkit.Wrappers
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.dingyangmall.common.core.controller.BaseController
 *  com.dingyangmall.common.core.domain.AjaxResult
 *  com.dingyangmall.mall.entity.OrderLogistics
 *  com.dingyangmall.mall.enums.OrderLogisticsEnum
 *  com.dingyangmall.mall.service.OrderLogisticsService
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.security.access.prepost.PreAuthorize
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.dingyangmall.web.controller.mall;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
        return AjaxResult.success((Object)this.orderLogisticsService.page((IPage)page, (Wrapper)Wrappers.query((Object)orderLogistics)));
    }

    @GetMapping(value={"/{id}"})
    @PreAuthorize(value="@ato.hasAuthority('mall:orderlogistics:get')")
    public AjaxResult getById(@PathVariable(value="id") String id) {
        return AjaxResult.success((Object)this.orderLogisticsService.getById((Serializable)((Object)id)));
    }

    @PostMapping
    @PreAuthorize(value="@ato.hasAuthority('mall:orderlogistics:add')")
    public AjaxResult save(@RequestBody OrderLogistics orderLogistics) {
        return AjaxResult.success((Object)this.orderLogisticsService.save((Object)orderLogistics));
    }

    @PutMapping
    @PreAuthorize(value="@ato.hasAuthority('mall:orderlogistics:edit')")
    public AjaxResult updateById(@RequestBody OrderLogistics orderLogistics) {
        return AjaxResult.success((Object)this.orderLogisticsService.updateById((Object)orderLogistics));
    }

    @DeleteMapping(value={"/{id}"})
    @PreAuthorize(value="@ato.hasAuthority('mall:orderlogistics:del')")
    public AjaxResult removeById(@PathVariable String id) {
        return AjaxResult.success((Object)this.orderLogisticsService.removeById((Serializable)((Object)id)));
    }

    @GetMapping(value={"/dict/{type}"})
    public AjaxResult getDictByType(@PathVariable String type) {
        return AjaxResult.success((Object)OrderLogisticsEnum.queryAll((String)type));
    }

    @Generated
    public OrderLogisticsController(OrderLogisticsService orderLogisticsService) {
        this.orderLogisticsService = orderLogisticsService;
    }
}

