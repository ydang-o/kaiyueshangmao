package com.dingyangmall.web.core.filter;

import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.web.core.WxMaConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

/**
 * 仅根据 X-Wx-Token 从 Redis 解析 memberId 并写入 request 属性，不依赖 ThirdSessionHolder。
 * 用于 /api/ma/** 链，避免 weixin 依赖的拦截器（可能在不同 ClassLoader）导致 60002。
 */
@Component
public class WxMaTokenToMemberFilter extends OncePerRequestFilter implements Ordered {

    public static final String ATTR_MEMBER_ID = "memberId";

    private final RedisTemplate<Object, Object> redisTemplate;

    public WxMaTokenToMemberFilter(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path == null || !path.contains("/api/ma/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            try {
                String key = WxMaConstants.REDIS_TOKEN_PREFIX + token;
                Object v = redisTemplate.opsForValue().get(key);
                if (v instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, String> session = (Map<String, String>) v;
                    String memberId = session.get("memberId");
                    if (StringUtils.isEmpty(memberId)) memberId = session.get("openid");
                    if (StringUtils.isNotEmpty(memberId)) {
                        request.setAttribute(ATTR_MEMBER_ID, memberId);
                    }
                }
            } catch (Exception ignored) {
            }
        }
        chain.doFilter(request, response);
    }

    private static String getToken(HttpServletRequest req) {
        String v = req.getHeader(WxMaConstants.HEADER_TOKEN);
        if (StringUtils.isNotEmpty(v)) return v.trim();
        String auth = req.getHeader("Authorization");
        if (StringUtils.isNotEmpty(auth) && auth.startsWith(WxMaConstants.AUTHORIZATION_BEARER)) {
            v = auth.substring(WxMaConstants.AUTHORIZATION_BEARER.length()).trim();
            if (StringUtils.isNotEmpty(v)) return v;
        }
        return null;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
