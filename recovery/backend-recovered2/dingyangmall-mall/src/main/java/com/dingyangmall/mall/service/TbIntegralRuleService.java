/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dingyangmall.mall.entity.TbIntegralRule;

public interface TbIntegralRuleService
extends IService<TbIntegralRule> {
    public void distributeRegisterPoints(Long var1);

    public void distributeInvitePoints(Long var1, Long var2);

    public boolean distributeSignInPoints(Long var1);
}

