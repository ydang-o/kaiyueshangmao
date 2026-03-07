package com.dingyangmall.framework.config;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.dingyangmall.common.config.DingyangmallConfig;
import com.dingyangmall.common.constant.Constants;
import com.dingyangmall.framework.interceptor.RepeatSubmitInterceptor;

/**
 * 通用配置
 * 
 * @author ruoyi
 */
@Configuration
public class ResourcesConfig implements WebMvcConfigurer
{
    @Autowired
    private RepeatSubmitInterceptor repeatSubmitInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        /** 管理端默认商品图：/profile/static/** 优先从 classpath:static/ 读取（如 jar 内 logo.png） */
        registry.addResourceHandler(Constants.RESOURCE_PREFIX + "/static/**")
                .addResourceLocations("classpath:static/");

        /** 本地文件上传路径：profile 为空或不合法时不注册，避免 /profile/xxx 请求 500 */
        String profile = DingyangmallConfig.getProfile();
        if (profile != null && !profile.trim().isEmpty()) {
            String path = profile.trim().replace("\\", "/");
            if (!path.endsWith("/")) {
                path = path + "/";
            }
            registry.addResourceHandler(Constants.RESOURCE_PREFIX + "/**")
                    .addResourceLocations("file:" + path);
            /** 管理端代理前缀：/dev-api/profile/** 与 /profile/** 指向同一本地目录，便于前端直接访问轮播图等本地文件 */
            registry.addResourceHandler("/dev-api/profile/**")
                    .addResourceLocations("file:" + path);
        }

        /** Knife4j 文档静态资源配置 */
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/")
                .setCacheControl(CacheControl.maxAge(5, TimeUnit.HOURS).cachePublic());
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .setCacheControl(CacheControl.maxAge(5, TimeUnit.HOURS).cachePublic());
    }

    /**
     * 自定义拦截规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(repeatSubmitInterceptor).addPathPatterns("/**");
    }
}

