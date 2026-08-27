/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.config;

import cn.hutool.core.util.StrUtil;
import com.dingyangmall.mall.config.RedisConfigProperties;
import com.dingyangmall.mall.listener.RedisKeyExpirationListener;
import com.dingyangmall.mall.service.OrderInfoService;
import lombok.Generated;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class RedisListenerConfig {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisConfigProperties redisConfigProperties;
    private final OrderInfoService orderInfoService;

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener((MessageListener)new RedisKeyExpirationListener(this.redisTemplate, this.redisConfigProperties, this.orderInfoService), new PatternTopic(StrUtil.format((CharSequence)"__keyevent@{}__:expired", this.redisConfigProperties.getDatabase())));
        return container;
    }

    @Generated
    public RedisListenerConfig(RedisTemplate<String, Object> redisTemplate, RedisConfigProperties redisConfigProperties, OrderInfoService orderInfoService) {
        this.redisTemplate = redisTemplate;
        this.redisConfigProperties = redisConfigProperties;
        this.orderInfoService = orderInfoService;
    }
}

