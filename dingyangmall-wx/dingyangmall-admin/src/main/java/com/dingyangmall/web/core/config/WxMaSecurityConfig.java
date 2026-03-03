package com.dingyangmall.web.core.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.dingyangmall.web.core.filter.WxMaErrorRewriteFilter;
import com.dingyangmall.web.core.filter.WxMaMemberFilter;
import com.dingyangmall.web.core.filter.WxMaThirdSessionEarlyFilter;
import com.dingyangmall.web.core.filter.WxMaTokenToMemberFilter;

/**
 * 小程序接口 /weixin/api/ma/** 放行，不校验 JWT，由 WxMaMemberFilter 根据请求头 X-Wx-Token 注入会员信息。
 * 公开接口 /api/public/** 完全放行，无需认证。
 */
@Configuration
public class WxMaSecurityConfig {

    /** 公开接口（商品分类树等），无需 token，最高优先级避免被默认链 401 */
    @Bean
    @Order(Integer.MIN_VALUE)
    public SecurityFilterChain publicApiSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
            .securityMatcher(new AntPathRequestMatcher("/api/public/**"))
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .csrf(AbstractHttpConfigurer::disable)
            .build();
    }

    /** /api/ma/**：仅按 token 注入 memberId，不经过 weixin 依赖，避免 ClassLoader 导致 60002 */
    @Bean
    @Order(Integer.MIN_VALUE + 1)
    public SecurityFilterChain apiMaSecurityFilterChain(HttpSecurity http, WxMaTokenToMemberFilter tokenToMemberFilter) throws Exception {
        return http
            .securityMatcher(new AntPathRequestMatcher("/api/ma/**"))
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .csrf(AbstractHttpConfigurer::disable)
            .addFilterBefore(tokenToMemberFilter, AuthorizationFilter.class)
            .build();
    }

    @Bean
    @Order(Integer.MIN_VALUE + 2)
    public SecurityFilterChain wxMaSecurityFilterChain(HttpSecurity http, WxMaMemberFilter wxMaMemberFilter) throws Exception {
        return http
            .securityMatcher(new AntPathRequestMatcher("/weixin/api/ma/**"))
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .csrf(AbstractHttpConfigurer::disable)
            // 在 Security 链内、Authorization 前执行，确保 Controller 拿到的 request/SecurityContext 带有 memberId（避免被主链里 JWT 等覆盖）
            .addFilterBefore(wxMaMemberFilter, AuthorizationFilter.class)
            .build();
    }

    /** 最早执行：从 Header X-Wx-Token 取令牌，从 Redis wx:token:{token} 取会话注入 ThirdSessionHolder */
    @Bean
    public FilterRegistrationBean<WxMaThirdSessionEarlyFilter> wxMaThirdSessionEarlyFilterRegistration(
            WxMaThirdSessionEarlyFilter filter) {
        FilterRegistrationBean<WxMaThirdSessionEarlyFilter> reg = new FilterRegistrationBean<>(filter);
        reg.setOrder(Ordered.HIGHEST_PRECEDENCE);
        reg.addUrlPatterns("/weixin/api/ma/*", "/weixin/api/ma/*/*");
        return reg;
    }

    @Bean
    public WxMaErrorRewriteFilter wxMaErrorRewriteFilter() {
        return new WxMaErrorRewriteFilter();
    }

    /** 对 /weixin/api/ma/* 的 60001/60002 响应改写为带完整说明的报错信息 */
    @Bean
    public FilterRegistrationBean<WxMaErrorRewriteFilter> wxMaErrorRewriteFilterRegistration(WxMaErrorRewriteFilter wxMaErrorRewriteFilter) {
        FilterRegistrationBean<WxMaErrorRewriteFilter> reg = new FilterRegistrationBean<>(wxMaErrorRewriteFilter);
        reg.setOrder(Ordered.LOWEST_PRECEDENCE);
        reg.addUrlPatterns("/weixin/api/ma/*", "/weixin/api/ma/*/*");
        return reg;
    }
}
