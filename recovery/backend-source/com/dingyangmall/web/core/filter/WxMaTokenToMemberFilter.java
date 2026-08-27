/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.dingyangmall.common.core.domain.model.LoginUser
 *  com.dingyangmall.common.utils.StringUtils
 *  com.dingyangmall.framework.web.service.TokenService
 *  jakarta.servlet.FilterChain
 *  jakarta.servlet.ServletException
 *  jakarta.servlet.ServletRequest
 *  jakarta.servlet.ServletResponse
 *  jakarta.servlet.http.HttpServletRequest
 *  jakarta.servlet.http.HttpServletResponse
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.core.Ordered
 *  org.springframework.data.redis.core.RedisTemplate
 *  org.springframework.stereotype.Component
 *  org.springframework.web.filter.OncePerRequestFilter
 */
package com.dingyangmall.web.core.filter;

import com.dingyangmall.common.core.domain.model.LoginUser;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.framework.web.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class WxMaTokenToMemberFilter
extends OncePerRequestFilter
implements Ordered {
    private static final Logger log = LoggerFactory.getLogger(WxMaTokenToMemberFilter.class);
    public static final String ATTR_MEMBER_ID = "memberId";
    private final RedisTemplate<String, Object> redisTemplate;
    private final TokenService tokenService;

    public WxMaTokenToMemberFilter(RedisTemplate<String, Object> redisTemplate, TokenService tokenService) {
        this.redisTemplate = redisTemplate;
        this.tokenService = tokenService;
    }

    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path == null || !path.contains("/api/ma/");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        block8: {
            String path = request.getRequestURI();
            String token = WxMaTokenToMemberFilter.getToken(request);
            log.debug("[WxMaTokenToMemberFilter] path={}, token={}", (Object)path, StringUtils.isNotEmpty((String)token) ? token.substring(0, Math.min(12, token.length())) + "..." : "null");
            if (StringUtils.isNotEmpty((String)token)) {
                try {
                    String key = "wx:token:" + token;
                    Object v = this.redisTemplate.opsForValue().get((Object)key);
                    if (v instanceof Map) {
                        Map session = (Map)v;
                        String memberId = (String)session.get(ATTR_MEMBER_ID);
                        if (StringUtils.isEmpty((String)memberId)) {
                            memberId = (String)session.get("openid");
                        }
                        if (StringUtils.isNotEmpty((String)memberId)) {
                            request.setAttribute(ATTR_MEMBER_ID, (Object)memberId);
                            log.debug("[WxMaTokenToMemberFilter] \u4eceRedis\u83b7\u53d6memberId={}", (Object)memberId);
                        }
                        break block8;
                    }
                    if (this.tokenService == null) {
                        WxMaTokenToMemberFilter.writeUnauthorized(response, "\u767b\u5f55\u5df2\u8fc7\u671f\uff0c\u8bf7\u91cd\u65b0\u767b\u5f55");
                        return;
                    }
                    LoginUser loginUser = this.tokenService.getLoginUser(request);
                    if (loginUser != null && loginUser.getUser() != null) {
                        String memberId = String.valueOf(loginUser.getUser().getUserId());
                        request.setAttribute(ATTR_MEMBER_ID, (Object)memberId);
                        log.debug("[WxMaTokenToMemberFilter] \u4eceJWT\u83b7\u53d6memberId={}", (Object)memberId);
                        break block8;
                    }
                    log.warn("[WxMaTokenToMemberFilter] JWT\u89e3\u6790\u5931\u8d25\u6216\u7528\u6237\u4e3a\u7a7a, path={}", (Object)path);
                    WxMaTokenToMemberFilter.writeUnauthorized(response, "\u767b\u5f55\u5df2\u8fc7\u671f\uff0c\u8bf7\u91cd\u65b0\u767b\u5f55");
                    return;
                }
                catch (Exception e) {
                    log.error("[WxMaTokenToMemberFilter] \u89e3\u6790token\u5f02\u5e38, path={}", (Object)path, (Object)e);
                    WxMaTokenToMemberFilter.writeUnauthorized(response, "\u767b\u5f55\u5df2\u8fc7\u671f\uff0c\u8bf7\u91cd\u65b0\u767b\u5f55");
                    return;
                }
            }
            log.warn("[WxMaTokenToMemberFilter] \u8bf7\u6c42\u672a\u643a\u5e26token, path={}", (Object)path);
        }
        chain.doFilter((ServletRequest)request, (ServletResponse)response);
    }

    private static void writeUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write("{\"code\":401,\"msg\":\"" + message + "\"}");
        writer.flush();
    }

    private static String getToken(HttpServletRequest req) {
        String v = req.getHeader("X-Wx-Token");
        if (StringUtils.isNotEmpty((String)v)) {
            return v.trim();
        }
        String auth = req.getHeader("Authorization");
        if (StringUtils.isNotEmpty((String)auth) && auth.startsWith("Bearer ") && StringUtils.isNotEmpty((String)(v = auth.substring("Bearer ".length()).trim()))) {
            return v;
        }
        return null;
    }

    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}

