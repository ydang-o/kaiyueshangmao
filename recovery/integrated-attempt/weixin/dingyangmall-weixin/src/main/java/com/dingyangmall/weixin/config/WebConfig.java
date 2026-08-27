/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.config;

import com.dingyangmall.weixin.interceptor.ThirdSessionInterceptor;
import lombok.Generated;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig
implements WebMvcConfigurer {
    private final RedisTemplate redisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ThirdSessionInterceptor(this.redisTemplate)).addPathPatterns("/weixin/api/**").excludePathPatterns("/weixin/api/ma/wxuser/login", "/weixin/api/ma/orderinfo/notify-order", "/weixin/api/ma/orderinfo/notify-logisticsr", "/weixin/api/ma/orderinfo/notify-refunds");
    }

    @Generated
    public WebConfig(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}

