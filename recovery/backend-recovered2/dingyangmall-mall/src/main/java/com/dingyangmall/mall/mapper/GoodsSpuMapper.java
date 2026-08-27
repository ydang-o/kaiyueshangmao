/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dingyangmall.mall.entity.GoodsSpu;
import org.apache.ibatis.annotations.Param;

public interface GoodsSpuMapper
extends BaseMapper<GoodsSpu> {
    public IPage<GoodsSpu> selectPage1(IPage<GoodsSpu> var1, @Param(value="query") GoodsSpu var2);

    public GoodsSpu selectById1(String var1);

    public GoodsSpu selectById2(String var1);

    public GoodsSpu selectById4(String var1);

    public GoodsSpu selectOneByShoppingCart(String var1);
}

