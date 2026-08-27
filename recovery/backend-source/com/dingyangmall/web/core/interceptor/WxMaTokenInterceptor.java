/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.dingyangmall.common.utils.StringUtils
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  jakarta.servlet.http.HttpServletRequest
 *  jakarta.servlet.http.HttpServletResponse
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.core.Ordered
 *  org.springframework.data.redis.core.RedisTemplate
 *  org.springframework.stereotype.Component
 *  org.springframework.web.servlet.HandlerInterceptor
 */
package com.dingyangmall.web.core.interceptor;

import com.dingyangmall.common.utils.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class WxMaTokenInterceptor
implements HandlerInterceptor,
Ordered {
    private static final Logger log = LoggerFactory.getLogger(WxMaTokenInterceptor.class);
    private static final int CODE_LOGIN_TIMEOUT = 60001;
    private static final String REDIS_TOKEN_PREFIX = "wx:token:";
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final String[] ANONYMOUS_PATH_PREFIXES = new String[]{"/weixin/api/ma/goodsspu/page", "/weixin/api/ma/goodscategory", "/weixin/api/ma/notice/list"};

    public WxMaTokenInterceptor(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    private static boolean isAnonymousPath(String path) {
        if (path == null) {
            return false;
        }
        for (String p : ANONYMOUS_PATH_PREFIXES) {
            if (!path.contains(p)) continue;
            return true;
        }
        return false;
    }

    private static String parseTokenFromRequest(HttpServletRequest request) {
        String v = request.getHeader("X-Wx-Token");
        if (StringUtils.isNotEmpty((String)v)) {
            return v.trim();
        }
        String auth = request.getHeader("Authorization");
        if (StringUtils.isNotEmpty((String)auth) && auth.startsWith("Bearer ") && StringUtils.isNotEmpty((String)(v = auth.substring("Bearer ".length()).trim()))) {
            return v;
        }
        return null;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        if (path == null || !path.contains("/weixin/api/ma/")) {
            return true;
        }
        if (path.endsWith("wxuser/login") && "POST".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        if (WxMaTokenInterceptor.isAnonymousPath(path)) {
            return true;
        }
        Object memberId = request.getAttribute("memberId");
        if (memberId != null && StringUtils.isNotEmpty((String)memberId.toString())) {
            return true;
        }
        String token = WxMaTokenInterceptor.parseTokenFromRequest(request);
        if (StringUtils.isEmpty((String)token)) {
            if (log.isDebugEnabled()) {
                log.debug("[WxMa] \u62e6\u622a\u5668\uff1apath={} \u672a\u5e26 token\uff0c\u8fd4\u56de 60001", (Object)path);
            }
            this.writeLoginTimeout(response);
            return false;
        }
        String key = REDIS_TOKEN_PREFIX + token;
        try {
            Object v = this.redisTemplate.opsForValue().get((Object)key);
            if (v == null || !(v instanceof Map)) {
                if (log.isDebugEnabled()) {
                    log.debug("[WxMa] \u62e6\u622a\u5668\uff1apath={} token \u65e0\u6548\u6216\u5df2\u8fc7\u671f\uff0c\u8fd4\u56de 60001", (Object)path);
                }
                this.writeLoginTimeout(response);
                return false;
            }
        }
        catch (Exception e) {
            log.warn("[WxMa] \u62e6\u622a\u5668\u6821\u9a8c token \u5f02\u5e38: {}", (Object)e.getMessage());
            this.writeLoginTimeout(response);
            return false;
        }
        return true;
    }

    private void writeLoginTimeout(HttpServletResponse response) {
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            HashMap<String, Object> body = new HashMap<String, Object>();
            body.put("code", 60001);
            body.put("msg", "\u767b\u5f55\u8d85\u65f6\uff0c\u8bf7\u91cd\u65b0\u767b\u5f55");
            body.put("data", null);
            response.getWriter().write(this.objectMapper.writeValueAsString(body));
            response.getWriter().flush();
        }
        catch (Exception e) {
            log.warn("[WxMa] \u5199\u5165 60001 \u54cd\u5e94\u5931\u8d25: {}", (Object)e.getMessage());
        }
    }

    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}

