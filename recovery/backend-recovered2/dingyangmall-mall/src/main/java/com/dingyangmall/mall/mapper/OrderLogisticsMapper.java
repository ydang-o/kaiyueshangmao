/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dingyangmall.mall.entity.OrderLogistics;
import java.io.Serializable;

public interface OrderLogisticsMapper
extends BaseMapper<OrderLogistics> {
    public OrderLogistics selectById2(Serializable var1);
}

