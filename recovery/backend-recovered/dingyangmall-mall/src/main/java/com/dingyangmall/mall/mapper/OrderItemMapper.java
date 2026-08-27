/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dingyangmall.mall.entity.OrderItem;
import java.util.List;

public interface OrderItemMapper
extends BaseMapper<OrderItem> {
    public List<OrderItem> selectList2(OrderItem var1);
}

