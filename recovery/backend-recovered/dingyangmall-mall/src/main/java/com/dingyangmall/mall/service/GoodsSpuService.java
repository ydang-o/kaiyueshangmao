/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dingyangmall.mall.entity.GoodsSpu;

public interface GoodsSpuService
extends IService<GoodsSpu> {
    public IPage<GoodsSpu> page1(IPage<GoodsSpu> var1, GoodsSpu var2);

    public boolean save1(GoodsSpu var1);

    public boolean updateById1(GoodsSpu var1);

    public GoodsSpu getById1(String var1);

    public GoodsSpu getById2(String var1);
}

