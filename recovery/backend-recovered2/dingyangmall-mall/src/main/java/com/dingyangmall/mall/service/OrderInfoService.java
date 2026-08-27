/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dingyangmall.mall.dto.PlaceOrderDTO;
import com.dingyangmall.mall.entity.OrderInfo;
import com.dingyangmall.mall.entity.OrderItem;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import java.io.Serializable;

public interface OrderInfoService
extends IService<OrderInfo> {
    public IPage<OrderInfo> page1(IPage<OrderInfo> var1, Wrapper<OrderInfo> var2);

    public OrderInfo orderSub(PlaceOrderDTO var1);

    public IPage<OrderInfo> page2(IPage<OrderInfo> var1, OrderInfo var2);

    public OrderInfo getById2(Serializable var1);

    public void orderCancel(OrderInfo var1);

    public void orderReceive(OrderInfo var1);

    public void notifyOrder(OrderInfo var1);

    public void saveRefunds(OrderItem var1);

    public void doOrderRefunds(OrderItem var1);

    public void notifyRefunds(WxPayRefundNotifyResult var1);
}

