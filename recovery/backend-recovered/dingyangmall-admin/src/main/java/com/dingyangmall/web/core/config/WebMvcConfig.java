/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.core.config;

import com.dingyangmall.web.core.interceptor.WxMaTokenInterceptor;
import com.dingyangmall.web.core.resolver.SqlFilterArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig
implements WebMvcConfigurer {
    private final WxMaTokenInterceptor wxMaTokenInterceptor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SqlFilterArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.wxMaTokenInterceptor).addPathPatterns("/weixin/api/ma/**").order(Integer.MIN_VALUE);
    }

    public WebMvcConfig(WxMaTokenInterceptor wxMaTokenInterceptor) {
        this.wxMaTokenInterceptor = wxMaTokenInterceptor;
    }
}

