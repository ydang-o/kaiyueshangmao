/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dingyangmall.mall.entity.TbIntegralFlow;

public interface TbIntegralFlowService
extends IService<TbIntegralFlow> {
    public void addPoints(Long var1, Integer var2, Integer var3, String var4);
}

