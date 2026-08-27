/*
 * Decompiled with CFR.
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
import org.springframework.security.config.annotation.AbstractSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class WxMaSecurityConfig {
    @Bean
    @Order(value=-2147483648)
    public SecurityFilterChain publicApiSecurityFilterChain(HttpSecurity http) throws Exception {
        return (SecurityFilterChain)http.securityMatcher(new AntPathRequestMatcher("/api/public/**")).authorizeHttpRequests(auth -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)auth.anyRequest()).permitAll()).csrf(AbstractHttpConfigurer::disable).build();
    }

    @Bean
    @Order(value=-2147483647)
    public SecurityFilterChain apiMaSecurityFilterChain(HttpSecurity http, WxMaTokenToMemberFilter tokenToMemberFilter, JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter) throws Exception {
        return (SecurityFilterChain)((AbstractSecurityBuilder)((Object)((HttpSecurity)http.securityMatcher(new AntPathRequestMatcher("/api/ma/**")).authorizeHttpRequests(auth -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)auth.anyRequest()).permitAll()).csrf(AbstractHttpConfigurer::disable).addFilterBefore((Filter)tokenToMemberFilter, AuthorizationFilter.class)).addFilterBefore((Filter)jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class))).build();
    }

    @Bean
    @Order(value=-2147483646)
    public SecurityFilterChain wxMaSecurityFilterChain(HttpSecurity http, WxMaMemberFilter wxMaMemberFilter) throws Exception {
        return (SecurityFilterChain)((AbstractSecurityBuilder)((Object)http.securityMatcher(new AntPathRequestMatcher("/weixin/api/ma/**")).authorizeHttpRequests(auth -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)auth.anyRequest()).permitAll()).csrf(AbstractHttpConfigurer::disable).addFilterBefore((Filter)wxMaMemberFilter, AuthorizationFilter.class))).build();
    }

    @Bean
    public FilterRegistrationBean<WxMaThirdSessionEarlyFilter> wxMaThirdSessionEarlyFilterRegistration(WxMaThirdSessionEarlyFilter filter) {
        FilterRegistrationBean<WxMaThirdSessionEarlyFilter> reg = new FilterRegistrationBean<WxMaThirdSessionEarlyFilter>(filter, new ServletRegistrationBean[0]);
        reg.setOrder(Integer.MIN_VALUE);
        reg.addUrlPatterns("/weixin/api/ma/*", "/weixin/api/ma/*/*");
        return reg;
    }

    @Bean
    public WxMaErrorRewriteFilter wxMaErrorRewriteFilter() {
        return new WxMaErrorRewriteFilter();
    }

    @Bean
    public FilterRegistrationBean<WxMaErrorRewriteFilter> wxMaErrorRewriteFilterRegistration(WxMaErrorRewriteFilter wxMaErrorRewriteFilter) {
        FilterRegistrationBean<WxMaErrorRewriteFilter> reg = new FilterRegistrationBean<WxMaErrorRewriteFilter>(wxMaErrorRewriteFilter, new ServletRegistrationBean[0]);
        reg.setOrder(Integer.MAX_VALUE);
        reg.addUrlPatterns("/weixin/api/ma/*", "/weixin/api/ma/*/*");
        return reg;
    }
}

