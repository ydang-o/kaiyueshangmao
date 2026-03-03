package com.dingyangmall.web.core.interceptor;

import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.web.core.WxMaConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * 小程序端请求使用令牌（token）认证，本拦截器负责解析与校验：
 * 1. 从请求头 X-Wx-Token 或 Authorization: Bearer 解析 token
 * 2. 公开接口（商品列表、分类、公告等）无 token 放行，由 Filter 注入匿名会话
 * 3. 需登录接口：无 token 或 token 在 Redis 中不存在/已过期时返回 60001，并写出 JSON 响应
 */
@Component
public class WxMaTokenInterceptor implements HandlerInterceptor, Ordered {

    private static final Logger log = LoggerFactory.getLogger(WxMaTokenInterceptor.class);
    private static final int CODE_LOGIN_TIMEOUT = 60001;
    private static final String REDIS_TOKEN_PREFIX = WxMaConstants.REDIS_TOKEN_PREFIX;

    private final RedisTemplate<Object, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    /** 不要求登录的公开路径（仅做前缀匹配） */
    private static final String[] ANONYMOUS_PATH_PREFIXES = {
        "/weixin/api/ma/goodsspu/page",
        "/weixin/api/ma/goodscategory/tree",
        "/weixin/api/ma/notice/list"
    };

    public WxMaTokenInterceptor(RedisTemplate<Object, Object> redisTemplate,
                               ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    private static boolean isAnonymousPath(String path) {
        if (path == null) return false;
        for (String p : ANONYMOUS_PATH_PREFIXES) {
            if (path.contains(p)) return true;
        }
        return false;
    }

    /** 从请求头解析 token：X-Wx-Token 或 Authorization: Bearer */
    private static String parseTokenFromRequest(HttpServletRequest request) {
        String v = request.getHeader(WxMaConstants.HEADER_TOKEN);
        if (StringUtils.isNotEmpty(v)) return v.trim();
        String auth = request.getHeader("Authorization");
        if (StringUtils.isNotEmpty(auth) && auth.startsWith(WxMaConstants.AUTHORIZATION_BEARER)) {
            v = auth.substring(WxMaConstants.AUTHORIZATION_BEARER.length()).trim();
            if (StringUtils.isNotEmpty(v)) return v;
        }
        return null;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        if (path == null || !path.contains("/weixin/api/ma/")) {
            return true;
        }
        if (path.endsWith("wxuser/login") && "POST".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        // 公开接口：不校验 token，由 Filter 注入匿名会话
        if (isAnonymousPath(path)) {
            return true;
        }
        // 需登录接口：先看 Filter 是否已注入 memberId（说明 token 有效）
        Object memberId = request.getAttribute("memberId");
        if (memberId != null && StringUtils.isNotEmpty(memberId.toString())) {
            return true;
        }
        // 未注入则解析 token 并校验
        String token = parseTokenFromRequest(request);
        if (StringUtils.isEmpty(token)) {
            if (log.isDebugEnabled()) {
                log.debug("[WxMa] 拦截器：path={} 未带 token，返回 60001", path);
            }
            writeLoginTimeout(response);
            return false;
        }
        String key = REDIS_TOKEN_PREFIX + token;
        try {
            Object v = redisTemplate.opsForValue().get(key);
            if (v == null || !(v instanceof Map)) {
                if (log.isDebugEnabled()) {
                    log.debug("[WxMa] 拦截器：path={} token 无效或已过期，返回 60001", path);
                }
                writeLoginTimeout(response);
                return false;
            }
        } catch (Exception e) {
            log.warn("[WxMa] 拦截器校验 token 异常: {}", e.getMessage());
            writeLoginTimeout(response);
            return false;
        }
        return true;
    }

    private void writeLoginTimeout(HttpServletResponse response) {
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            Map<String, Object> body = new HashMap<>();
            body.put("code", CODE_LOGIN_TIMEOUT);
            body.put("msg", "登录超时，请重新登录");
            body.put("data", null);
            response.getWriter().write(objectMapper.writeValueAsString(body));
            response.getWriter().flush();
        } catch (Exception e) {
            log.warn("[WxMa] 写入 60001 响应失败: {}", e.getMessage());
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
