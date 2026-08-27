/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.core.toolkit.Wrappers
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.dingyangmall.common.core.domain.AjaxResult
 *  com.dingyangmall.mall.entity.ShoppingCart
 *  com.dingyangmall.mall.service.ShoppingCartService
 *  com.dingyangmall.mall.utils.MemberUtils
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.dingyangmall.web.api;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.mall.entity.ShoppingCart;
import com.dingyangmall.mall.service.ShoppingCartService;
import com.dingyangmall.mall.utils.MemberUtils;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/weixin/api/ma/shoppingcart", "/api/ma/shoppingcart"})
public class ShoppingCartApi {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(ShoppingCartApi.class);
    private final ShoppingCartService shoppingCartService;

    @GetMapping(value={"/page"})
    public AjaxResult getShoppingCartPage(Page page, ShoppingCart shoppingCart) {
        shoppingCart.setUserId(MemberUtils.getMemberId());
        return AjaxResult.success((Object)this.shoppingCartService.page2((IPage)page, shoppingCart));
    }

    @GetMapping(value={"/count"})
    public AjaxResult getShoppingCartCount(ShoppingCart shoppingCart) {
        shoppingCart.setUserId(MemberUtils.getMemberId());
        return AjaxResult.success((Object)this.shoppingCartService.count((Wrapper)Wrappers.query((Object)shoppingCart)));
    }

    @PostMapping
    public AjaxResult save(@RequestBody ShoppingCart shoppingCart) {
        shoppingCart.setUserId(MemberUtils.getMemberId());
        return AjaxResult.success((Object)this.shoppingCartService.save((Object)shoppingCart));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody ShoppingCart shoppingCart) {
        shoppingCart.setUserId(MemberUtils.getMemberId());
        return AjaxResult.success((Object)this.shoppingCartService.updateById((Object)shoppingCart));
    }

    @PostMapping(value={"/del"})
    public AjaxResult del(@RequestBody Object body) {
        List ids = null;
        if (body instanceof Map) {
            Map map = (Map)body;
            Object idsObj = map.get("ids");
            if (idsObj instanceof List) {
                ids = (List)idsObj;
            }
        } else if (body instanceof List) {
            ids = (List)body;
        }
        if (ids == null || ids.isEmpty()) {
            return AjaxResult.error((String)"ids\u4e0d\u80fd\u4e3a\u7a7a");
        }
        return AjaxResult.success((Object)this.shoppingCartService.removeByIds((Collection)ids));
    }

    @Generated
    public ShoppingCartApi(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }
}

