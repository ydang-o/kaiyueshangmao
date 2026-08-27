/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.api;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.mall.entity.ShoppingCart;
import com.dingyangmall.mall.service.ShoppingCartService;
import com.dingyangmall.mall.utils.MemberUtils;
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
        return AjaxResult.success(this.shoppingCartService.page2(page, shoppingCart));
    }

    @GetMapping(value={"/count"})
    public AjaxResult getShoppingCartCount(ShoppingCart shoppingCart) {
        shoppingCart.setUserId(MemberUtils.getMemberId());
        return AjaxResult.success(this.shoppingCartService.count(Wrappers.query(shoppingCart)));
    }

    @PostMapping
    public AjaxResult save(@RequestBody ShoppingCart shoppingCart) {
        shoppingCart.setUserId(MemberUtils.getMemberId());
        return AjaxResult.success(this.shoppingCartService.save(shoppingCart));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody ShoppingCart shoppingCart) {
        shoppingCart.setUserId(MemberUtils.getMemberId());
        return AjaxResult.success(this.shoppingCartService.updateById(shoppingCart));
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
            return AjaxResult.error("ids\u4e0d\u80fd\u4e3a\u7a7a");
        }
        return AjaxResult.success(this.shoppingCartService.removeByIds(ids));
    }

    @Generated
    public ShoppingCartApi(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }
}

