/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyangmall.mall.entity.OrderItem;
import com.dingyangmall.mall.mapper.OrderItemMapper;
import com.dingyangmall.mall.service.OrderItemService;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl
extends ServiceImpl<OrderItemMapper, OrderItem>
implements OrderItemService {
}

