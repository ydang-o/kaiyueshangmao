/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dingyangmall.mall.entity.OrderInfo;
import java.io.Serializable;
import org.apache.ibatis.annotations.Param;

public interface OrderInfoMapper
extends BaseMapper<OrderInfo> {
    public IPage<OrderInfo> selectPage1(IPage<OrderInfo> var1, @Param(value="query") OrderInfo var2);

    public IPage<OrderInfo> selectPage2(IPage<OrderInfo> var1, @Param(value="query") OrderInfo var2);

    public OrderInfo selectById2(Serializable var1);
}

