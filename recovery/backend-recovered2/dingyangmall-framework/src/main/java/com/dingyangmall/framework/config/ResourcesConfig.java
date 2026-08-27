/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.config;

import com.dingyangmall.common.config.DingyangmallConfig;
import com.dingyangmall.framework.interceptor.RepeatSubmitInterceptor;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourcesConfig
implements WebMvcConfigurer {
    @Autowired
    private RepeatSubmitInterceptor repeatSubmitInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/profile/static/**").addResourceLocations("classpath:static/");
        String profile = DingyangmallConfig.getProfile();
        if (profile != null && !profile.trim().isEmpty()) {
            Object path = profile.trim().replace("\\", "/");
            if (!((String)path).endsWith("/")) {
                path = (String)path + "/";
            }
            registry.addResourceHandler("/profile/**").addResourceLocations("file:" + (String)path);
            registry.addResourceHandler("/dev-api/profile/**").addResourceLocations("file:" + (String)path);
        }
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/").setCacheControl(CacheControl.maxAge(5L, TimeUnit.HOURS).cachePublic());
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/").setCacheControl(CacheControl.maxAge(5L, TimeUnit.HOURS).cachePublic());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.repeatSubmitInterceptor).addPathPatterns("/**");
    }
}

