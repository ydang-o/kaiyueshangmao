/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.listener;

import cn.hutool.core.util.StrUtil;
import com.dingyangmall.mall.config.RedisConfigProperties;
import com.dingyangmall.mall.entity.OrderInfo;
import com.dingyangmall.mall.enums.OrderInfoEnum;
import com.dingyangmall.mall.service.OrderInfoService;
import java.io.Serializable;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyExpirationListener
implements MessageListener {
    private RedisTemplate<String, Object> redisTemplate;
    private RedisConfigProperties redisConfigProperties;
    private OrderInfoService orderInfoService;

    public RedisKeyExpirationListener(RedisTemplate<String, Object> redisTemplate, RedisConfigProperties redisConfigProperties, OrderInfoService orderInfoService) {
        this.redisTemplate = redisTemplate;
        this.redisConfigProperties = redisConfigProperties;
        this.orderInfoService = orderInfoService;
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        RedisSerializer<?> serializer = this.redisTemplate.getValueSerializer();
        String channel = String.valueOf(serializer.deserialize(message.getChannel()));
        String body = String.valueOf(serializer.deserialize(message.getBody()));
        if (StrUtil.format((CharSequence)"__keyevent@{}__:expired", this.redisConfigProperties.getDatabase()).equals(channel)) {
            String orderId;
            String[] str;
            String wxOrderId;
            OrderInfo orderInfo;
            if (body.contains("mall:order:is_pay_0:") && (orderInfo = (OrderInfo)this.orderInfoService.getById((Serializable)((Object)(wxOrderId = (str = (body = body.replace("mall:order:is_pay_0:", "")).split(":"))[1])))) != null && "0".equals(orderInfo.getIsPay())) {
                this.orderInfoService.orderCancel(orderInfo);
            }
            if (body.contains("mall:order:status_2:") && (orderInfo = (OrderInfo)this.orderInfoService.getById((Serializable)((Object)(orderId = (str = (body = body.replace("mall:order:status_2:", "")).split(":"))[1])))) != null && OrderInfoEnum.STATUS_2.getValue().equals(orderInfo.getStatus())) {
                this.orderInfoService.orderReceive(orderInfo);
            }
        }
    }
}

