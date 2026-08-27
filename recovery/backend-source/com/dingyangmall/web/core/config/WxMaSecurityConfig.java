/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.dingyangmall.framework.security.filter.JwtAuthenticationTokenFilter
 *  jakarta.servlet.Filter
 *  org.springframework.boot.web.servlet.FilterRegistrationBean
 *  org.springframework.boot.web.servlet.ServletRegistrationBean
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 *  org.springframework.core.annotation.Order
 *  org.springframework.security.config.annotation.web.builders.HttpSecurity
 *  org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
 *  org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer$AuthorizedUrl
 *  org.springframework.security.web.SecurityFilterChain
 *  org.springframework.security.web.access.intercept.AuthorizationFilter
 *  org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
 *  org.springframework.security.web.util.matcher.AntPathRequestMatcher
 *  org.springframework.security.web.util.matcher.RequestMatcher
 */
package com.dingyangmall.web.core.config;

import com.dingyangmall.framework.security.filter.JwtAuthenticationTokenFilter;
import com.dingyangmall.web.core.filter.WxMaErrorRewriteFilter;
import com.dingyangmall.web.core.filter.WxMaMemberFilter;
import com.dingyangmall.web.core.filter.WxMaThirdSessionEarlyFilter;
import com.dingyangmall.web.core.filter.WxMaTokenToMemberFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
public class WxMaSecurityConfig {
    @Bean
    @Order(value=-2147483648)
    public SecurityFilterChain publicApiSecurityFilterChain(HttpSecurity http) throws Exception {
        return (SecurityFilterChain)http.securityMatcher((RequestMatcher)new AntPathRequestMatcher("/api/public/**")).authorizeHttpRequests(auth -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)auth.anyRequest()).permitAll()).csrf(AbstractHttpConfigurer::disable).build();
    }

    @Bean
    @Order(value=-2147483647)
    public SecurityFilterChain apiMaSecurityFilterChain(HttpSecurity http, WxMaTokenToMemberFilter tokenToMemberFilter, JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter) throws Exception {
        return (SecurityFilterChain)http.securityMatcher((RequestMatcher)new AntPathRequestMatcher("/api/ma/**")).authorizeHttpRequests(auth -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)auth.anyRequest()).permitAll()).csrf(AbstractHttpConfigurer::disable).addFilterBefore((Filter)tokenToMemberFilter, AuthorizationFilter.class).addFilterBefore((Filter)jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class).build();
    }

    @Bean
    @Order(value=-2147483646)
    public SecurityFilterChain wxMaSecurityFilterChain(HttpSecurity http, WxMaMemberFilter wxMaMemberFilter) throws Exception {
        return (SecurityFilterChain)http.securityMatcher((RequestMatcher)new AntPathRequestMatcher("/weixin/api/ma/**")).authorizeHttpRequests(auth -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)auth.anyRequest()).permitAll()).csrf(AbstractHttpConfigurer::disable).addFilterBefore((Filter)wxMaMemberFilter, AuthorizationFilter.class).build();
    }

    @Bean
    public FilterRegistrationBean<WxMaThirdSessionEarlyFilter> wxMaThirdSessionEarlyFilterRegistration(WxMaThirdSessionEarlyFilter filter) {
        FilterRegistrationBean reg = new FilterRegistrationBean((Filter)filter, new ServletRegistrationBean[0]);
        reg.setOrder(Integer.MIN_VALUE);
        reg.addUrlPatterns(new String[]{"/weixin/api/ma/*", "/weixin/api/ma/*/*"});
        return reg;
    }

    @Bean
    public WxMaErrorRewriteFilter wxMaErrorRewriteFilter() {
        return new WxMaErrorRewriteFilter();
    }

    @Bean
    public FilterRegistrationBean<WxMaErrorRewriteFilter> wxMaErrorRewriteFilterRegistration(WxMaErrorRewriteFilter wxMaErrorRewriteFilter) {
        FilterRegistrationBean reg = new FilterRegistrationBean((Filter)wxMaErrorRewriteFilter, new ServletRegistrationBean[0]);
        reg.setOrder(Integer.MAX_VALUE);
        reg.addUrlPatterns(new String[]{"/weixin/api/ma/*", "/weixin/api/ma/*/*"});
        return reg;
    }
}

