/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dingyangmall.mall.entity.ShoppingCart;
import org.apache.ibatis.annotations.Param;

public interface ShoppingCartMapper
extends BaseMapper<ShoppingCart> {
    public IPage<ShoppingCart> selectPage2(IPage<ShoppingCart> var1, @Param(value="query") ShoppingCart var2);
}

