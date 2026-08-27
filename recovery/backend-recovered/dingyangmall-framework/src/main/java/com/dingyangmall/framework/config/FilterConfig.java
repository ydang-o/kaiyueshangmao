/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.config;

import com.dingyangmall.common.filter.RepeatableFilter;
import com.dingyangmall.common.filter.XssFilter;
import com.dingyangmall.common.utils.StringUtils;
import jakarta.servlet.DispatcherType;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Value(value="${xss.excludes}")
    private String excludes;
    @Value(value="${xss.urlPatterns}")
    private String urlPatterns;

    @Bean
    @ConditionalOnProperty(value={"xss.enabled"}, havingValue="true")
    public FilterRegistrationBean xssFilterRegistration() {
        FilterRegistrationBean<XssFilter> registration = new FilterRegistrationBean<XssFilter>();
        registration.setDispatcherTypes(DispatcherType.REQUEST, new DispatcherType[0]);
        registration.setFilter(new XssFilter());
        registration.addUrlPatterns(StringUtils.split(this.urlPatterns, ","));
        registration.setName("xssFilter");
        registration.setOrder(Integer.MIN_VALUE);
        HashMap<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("excludes", this.excludes);
        registration.setInitParameters(initParameters);
        return registration;
    }

    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean<RepeatableFilter> registration = new FilterRegistrationBean<RepeatableFilter>();
        registration.setFilter(new RepeatableFilter());
        registration.addUrlPatterns("/*");
        registration.setName("repeatableFilter");
        registration.setOrder(Integer.MAX_VALUE);
        return registration;
    }
}

