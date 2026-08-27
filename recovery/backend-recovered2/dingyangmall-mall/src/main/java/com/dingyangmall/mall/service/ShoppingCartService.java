/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dingyangmall.mall.entity.ShoppingCart;

public interface ShoppingCartService
extends IService<ShoppingCart> {
    public IPage<ShoppingCart> page2(IPage<ShoppingCart> var1, ShoppingCart var2);
}

