package com.dingyangmall.web.core.filter;

import com.dingyangmall.common.utils.StringUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * 小程序 /weixin/api/ma/* 请求：根据 Header third-session 从 Redis 解析出 openid，
 * 写入 request 属性 {@value #ATTR_MEMBER_ID}，供 {@link com.dingyangmall.mall.utils.MemberUtils#getMemberId()} 使用，
 * 解决购物车等接口因无 JWT 导致 60001「登录超时」的问题。
 */
@Component
public class WxMaMemberFilter implements Filter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(WxMaMemberFilter.class);

    /** 与 WxUserApi 中 Redis key 前缀一致 */
    private static final String REDIS_KEY_PREFIX = "wx:third_session:";

    /** request 属性名：小程序端通过 third-session 解析出的会员 ID（openid），MemberUtils 会读取此属性 */
    public static final String ATTR_MEMBER_ID = "memberId";

    private final RedisTemplate<Object, Object> redisTemplate;

    public WxMaMemberFilter(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI();
        String method = req.getMethod();

        if (!path.startsWith("/weixin/api/ma/")) {
            chain.doFilter(request, response);
            return;
        }
        // 登录接口不需要从 third-session 取会员
        if ("POST".equalsIgnoreCase(method) && path.endsWith("wxuser/login")) {
            chain.doFilter(request, response);
            return;
        }

        String thirdSession = req.getHeader("third-session");
        if (StringUtils.isEmpty(thirdSession)) {
            thirdSession = req.getHeader("Third-Session");
        }
        if (StringUtils.isNotEmpty(thirdSession)) {
            try {
                Object v = redisTemplate.opsForValue().get(REDIS_KEY_PREFIX + thirdSession);
                if (v instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, String> session = (Map<String, String>) v;
                    String memberId = session.get("wxUserId");
                    if (StringUtils.isEmpty(memberId)) {
                        memberId = session.get("openid");
                    }
                    if (StringUtils.isNotEmpty(memberId)) {
                        req.setAttribute(ATTR_MEMBER_ID, memberId);
                    }
                }
            } catch (Exception e) {
                log.debug("解析 third-session 失败: {}", e.getMessage());
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }
}
