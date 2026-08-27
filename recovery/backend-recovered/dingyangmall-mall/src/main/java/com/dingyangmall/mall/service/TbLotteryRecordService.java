/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dingyangmall.mall.entity.TbLotteryRecord;

public interface TbLotteryRecordService
extends IService<TbLotteryRecord> {
    public TbLotteryRecord draw(Long var1);
}

