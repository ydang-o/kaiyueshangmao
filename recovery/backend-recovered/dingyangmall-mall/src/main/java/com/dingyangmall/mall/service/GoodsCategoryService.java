/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dingyangmall.mall.entity.GoodsCategory;
import com.dingyangmall.mall.entity.GoodsCategoryTree;
import java.util.List;

public interface GoodsCategoryService
extends IService<GoodsCategory> {
    public List<GoodsCategoryTree> selectTree(GoodsCategory var1);
}

