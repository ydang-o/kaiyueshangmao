/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dingyangmall.mall.entity.TbLotteryConfig;
import java.util.List;

public interface TbLotteryConfigService
extends IService<TbLotteryConfig> {
    public TbLotteryConfig getActiveConfig();

    public List<TbLotteryConfig> getActiveList();

    public boolean saveConfig(TbLotteryConfig var1);

    public boolean removeConfigById(Long var1);
}

