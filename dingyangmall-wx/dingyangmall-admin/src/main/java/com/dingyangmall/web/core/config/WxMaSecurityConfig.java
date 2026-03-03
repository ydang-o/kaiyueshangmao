package com.dingyangmall.web.core.config;

import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.data.redis.core.RedisTemplate;

import com.dingyangmall.web.core.filter.WxMaErrorRewriteFilter;
import com.dingyangmall.web.core.filter.WxMaMemberFilter;
import com.dingyangmall.web.core.filter.WxMaThirdSessionEarlyFilter;

/**
 * 小程序接口 /weixin/api/ma/** 放行，不校验 JWT，由 WxMaMemberFilter 根据 openid（兼容 third-session 字段名）注入会员信息，
 * 避免返回 60001「登录超时」。使用最低 Order 确保本链优先于 framework 默认链。
 */
@Configuration
public class WxMaSecurityConfig {

    @Bean
    @Order(Integer.MIN_VALUE)
    public SecurityFilterChain wxMaSecurityFilterChain(HttpSecurity http, WxMaMemberFilter wxMaMemberFilter) throws Exception {
        return http
            .securityMatcher(new AntPathRequestMatcher("/weixin/api/ma/**"))
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .csrf(AbstractHttpConfigurer::disable)
            // 在 Security 链内、Authorization 前执行，确保 Controller 拿到的 request/SecurityContext 带有 memberId（避免被主链里 JWT 等覆盖）
            .addFilterBefore(wxMaMemberFilter, AuthorizationFilter.class)
            .build();
    }

    @Bean
    public WxMaMemberFilter wxMaMemberFilter(RedisTemplate<Object, Object> redisTemplate,
                                            @Value("${wx.ma.session-use-redis:true}") boolean sessionUseRedis) {
        return new WxMaMemberFilter(redisTemplate, sessionUseRedis);
    }

    /** 最早执行：在 weixin 模块校验前从 Header 注入 ThirdSessionHolder，避免 60001 */
    @Bean
    public FilterRegistrationBean<WxMaThirdSessionEarlyFilter> wxMaThirdSessionEarlyFilterRegistration(
            WxMaThirdSessionEarlyFilter filter) {
        FilterRegistrationBean<WxMaThirdSessionEarlyFilter> reg = new FilterRegistrationBean<>(filter);
        reg.setOrder(Ordered.HIGHEST_PRECEDENCE);
        reg.addUrlPatterns("/weixin/api/ma/*");
        return reg;
    }

    @Bean
    public WxMaErrorRewriteFilter wxMaErrorRewriteFilter() {
        return new WxMaErrorRewriteFilter();
    }

    /** WxMaMemberFilter 已加入 wxMaSecurityFilterChain（在 AuthorizationFilter 前），不再在主链重复注册，避免与 Security 链顺序冲突导致下游拿不到 memberId */

    /** 对 /weixin/api/ma/* 的 60001/60002 响应改写为带完整说明的报错信息，不再只报「登录超时」 */
    @Bean
    public FilterRegistrationBean<WxMaErrorRewriteFilter> wxMaErrorRewriteFilterRegistration(WxMaErrorRewriteFilter wxMaErrorRewriteFilter) {
        FilterRegistrationBean<WxMaErrorRewriteFilter> reg = new FilterRegistrationBean<>(wxMaErrorRewriteFilter);
        reg.setOrder(Ordered.LOWEST_PRECEDENCE);
        reg.addUrlPatterns("/weixin/api/ma/*");
        return reg;
    }
}
