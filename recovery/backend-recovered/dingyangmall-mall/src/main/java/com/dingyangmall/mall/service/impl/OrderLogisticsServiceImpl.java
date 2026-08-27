/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyangmall.mall.entity.OrderLogistics;
import com.dingyangmall.mall.mapper.OrderLogisticsMapper;
import com.dingyangmall.mall.service.OrderLogisticsService;
import java.io.Serializable;
import org.springframework.stereotype.Service;

@Service
public class OrderLogisticsServiceImpl
extends ServiceImpl<OrderLogisticsMapper, OrderLogistics>
implements OrderLogisticsService {
    @Override
    public OrderLogistics getById(Serializable id) {
        return ((OrderLogisticsMapper)this.baseMapper).selectById2(id);
    }
}

