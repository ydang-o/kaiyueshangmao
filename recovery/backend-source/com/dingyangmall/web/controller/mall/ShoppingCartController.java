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
 *  com.dingyangmall.mall.entity.ShoppingCart
 *  com.dingyangmall.mall.service.ShoppingCartService
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
import com.dingyangmall.mall.entity.ShoppingCart;
import com.dingyangmall.mall.service.ShoppingCartService;
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
@RequestMapping(value={"/shoppingcart"})
public class ShoppingCartController
extends BaseController {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(ShoppingCartController.class);
    private final ShoppingCartService shoppingCartService;

    @GetMapping(value={"/page"})
    @PreAuthorize(value="@ss.hasPermi('mall:shoppingcart:index')")
    public AjaxResult getShoppingCartPage(Page page, ShoppingCart shoppingCart) {
        return AjaxResult.success((Object)this.shoppingCartService.page((IPage)page, (Wrapper)Wrappers.query((Object)shoppingCart)));
    }

    @GetMapping(value={"/{id}"})
    @PreAuthorize(value="@ss.hasPermi('mall:shoppingcart:get')")
    public AjaxResult getById(@PathVariable(value="id") String id) {
        return AjaxResult.success((Object)this.shoppingCartService.getById((Serializable)((Object)id)));
    }

    @PostMapping
    @PreAuthorize(value="@ss.hasPermi('mall:shoppingcart:add')")
    public AjaxResult save(@RequestBody ShoppingCart shoppingCart) {
        return AjaxResult.success((Object)this.shoppingCartService.save((Object)shoppingCart));
    }

    @PutMapping
    @PreAuthorize(value="@ss.hasPermi('mall:shoppingcart:edit')")
    public AjaxResult updateById(@RequestBody ShoppingCart shoppingCart) {
        return AjaxResult.success((Object)this.shoppingCartService.updateById((Object)shoppingCart));
    }

    @DeleteMapping(value={"/{id}"})
    @PreAuthorize(value="@ss.hasPermi('mall:shoppingcart:del')")
    public AjaxResult removeById(@PathVariable String id) {
        return AjaxResult.success((Object)this.shoppingCartService.removeById((Serializable)((Object)id)));
    }

    @Generated
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }
}

