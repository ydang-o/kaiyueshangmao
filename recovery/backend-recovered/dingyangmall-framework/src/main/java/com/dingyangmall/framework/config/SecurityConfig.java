/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.config;

import com.dingyangmall.framework.config.properties.PermitAllUrlProperties;
import com.dingyangmall.framework.security.filter.JwtAuthenticationTokenFilter;
import com.dingyangmall.framework.security.handle.AuthenticationEntryPointImpl;
import com.dingyangmall.framework.security.handle.LogoutSuccessHandlerImpl;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.AbstractSecurityBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableMethodSecurity(prePostEnabled=true, securedEnabled=true)
@Configuration
public class SecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationEntryPointImpl unauthorizedHandler;
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;
    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;
    @Autowired
    private PermitAllUrlProperties permitAllUrl;

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(this.bCryptPasswordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return (SecurityFilterChain)((AbstractSecurityBuilder)((Object)((HttpSecurity)((HttpSecurity)httpSecurity.csrf(csrf -> csrf.disable()).headers(headersCustomizer -> headersCustomizer.cacheControl(cache -> cache.disable()).frameOptions(options -> options.sameOrigin())).exceptionHandling(exception -> exception.authenticationEntryPoint(this.unauthorizedHandler)).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(requests -> {
            ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)requests.requestMatchers("/api/public/**")).permitAll();
            ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)requests.requestMatchers("/api/ma/**")).permitAll();
            ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)requests.requestMatchers("/weixin/api/ma/**")).permitAll();
            this.permitAllUrl.getUrls().forEach(url -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)requests.requestMatchers((String)url)).permitAll());
            ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)requests.requestMatchers("/login", "/register", "/captchaImage", "/weixin/portal/**", "/weixin/api/**", "/dingyangmall-wiki/**", "/app/member/login", "/app/member/login-by-sms", "/app/member/register-by-sms", "/app/member/register", "/app/member/send-sms-code")).permitAll().requestMatchers(HttpMethod.GET, "/", "/*.html", "/**.html", "/**.css", "/**.js", "/profile/**", "/dev-api/profile/**")).permitAll().requestMatchers("/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**", "/druid/**", "/doc.html", "/webjars/**")).permitAll().anyRequest()).authenticated();
        }).logout(logout -> logout.logoutUrl("/logout").logoutSuccessHandler(this.logoutSuccessHandler)).addFilterBefore((Filter)this.authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)).addFilterBefore((Filter)this.corsFilter(), JwtAuthenticationTokenFilter.class)).addFilterBefore((Filter)this.corsFilter(), LogoutFilter.class))).build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setMaxAge(1800L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

