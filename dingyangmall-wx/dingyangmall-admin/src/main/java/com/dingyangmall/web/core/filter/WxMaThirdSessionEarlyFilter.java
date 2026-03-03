package com.dingyangmall.web.core.filter;

import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.web.core.WxMaConstants;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 最早执行：从 Header X-Wx-Token（或 Authorization: Bearer）读取令牌，从 Redis wx:token:{token} 取会话注入 ThirdSessionHolder，
 * 确保 dingyangmall-weixin 模块校验时拿到完整会话。对「公开接口」未带 token 时注入匿名会话。
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WxMaThirdSessionEarlyFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(WxMaThirdSessionEarlyFilter.class);
    private static final String REDIS_OPENID_SESSION_PREFIX = "wx:openid_session:";
    /** 匿名 openid，用于 weixin 模块校验（满足 isOpenidFormat） */
    private static final String ANONYMOUS_OPENID = "oAnonymous00000000000000000";

    /** 公开接口（商品首页/分类/公告等，不需 token）：无 token 或 token 无效时注入匿名以通过 weixin 校验 */
    private static final String[] ANONYMOUS_PATHS = {
        "/weixin/api/ma/goodsspu/page",
        "/weixin/api/ma/goodscategory",
        "/weixin/api/ma/notice/list"
    };

    private final RedisTemplate<Object, Object> redisTemplate;
    private final boolean sessionUseRedis;

    public WxMaThirdSessionEarlyFilter(RedisTemplate<Object, Object> redisTemplate,
                                      @Value("${wx.ma.session-use-redis:true}") boolean sessionUseRedis) {
        this.redisTemplate = redisTemplate;
        this.sessionUseRedis = sessionUseRedis;
    }

    private static boolean isOpenidFormat(String s) {
        return StringUtils.isNotEmpty(s) && s.length() >= 26 && s.length() <= 32 && s.startsWith("o");
    }

    private static boolean isAnonymousPath(String path) {
        if (path == null) return false;
        for (String p : ANONYMOUS_PATHS) {
            if (path.contains(p)) return true;
        }
        return false;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI();
        if (!path.contains("/weixin/api/ma/") || (path.endsWith("wxuser/login") && "POST".equalsIgnoreCase(req.getMethod()))) {
            chain.doFilter(request, response);
            return;
        }
        String token = getTokenFromRequest(req);
        boolean bound = false;
        if (StringUtils.isNotEmpty(token) && sessionUseRedis) {
            Map<String, String> redisSession = getSessionByToken(token);
            if (redisSession != null) {
                String openid = redisSession.get("openid");
                if (StringUtils.isEmpty(openid)) openid = redisSession.get("memberId");
                if (StringUtils.isNotEmpty(openid) && isOpenidFormat(openid)) {
                    try {
                        bound = bindThirdSession(openid, redisSession);
                        if (bound) {
                            String sessionKeyVal = getSessionKeyValue(openid, redisSession);
                            if (StringUtils.isNotEmpty(sessionKeyVal)) {
                                req.setAttribute("sessionKey", sessionKeyVal);
                            }
                            if (log.isDebugEnabled()) {
                                log.debug("[WxMa] ThirdSessionEarlyFilter 已按 token 注入 ThirdSessionHolder path={}", path);
                            }
                        }
                    } catch (Exception e) {
                        if (log.isTraceEnabled()) {
                            log.trace("[WxMa] ThirdSessionEarlyFilter 注入失败: {}", e.getMessage());
                        }
                    }
                }
            } else if (!isAnonymousPath(path) && log.isDebugEnabled()) {
                log.debug("[WxMa] path={} token 无效或已过期", path);
            }
        }
        if (!bound && isAnonymousPath(path)) {
            // 公开接口：未带 token 时注入匿名，通过 weixin 校验
            try {
                bound = bindThirdSession(ANONYMOUS_OPENID, null);
                if (bound) {
                    req.setAttribute("sessionKey", "anonymous");
                    if (log.isDebugEnabled()) {
                        log.debug("[WxMa] ThirdSessionEarlyFilter 已注入匿名会话 path={}", path);
                    }
                }
            } catch (Exception e) {
                if (log.isTraceEnabled()) {
                    log.trace("[WxMa] ThirdSessionEarlyFilter 匿名会话注入失败: {}", e.getMessage());
                }
            }
        } else if (log.isDebugEnabled() && !bound) {
            log.debug("[WxMa] ThirdSessionEarlyFilter path={} 未带有效 {}，跳过注入", path, WxMaConstants.HEADER_TOKEN);
        }
        try {
            chain.doFilter(request, response);
        } finally {
            if (bound) {
                try {
                    clearThirdSession();
                } catch (Exception ignored) {}
            }
        }
    }

    /** 从请求头解析令牌：X-Wx-Token 或 Authorization: Bearer */
    private static String getTokenFromRequest(HttpServletRequest req) {
        String v = req.getHeader(WxMaConstants.HEADER_TOKEN);
        if (StringUtils.isNotEmpty(v)) return v.trim();
        String auth = req.getHeader("Authorization");
        if (StringUtils.isNotEmpty(auth) && auth.startsWith(WxMaConstants.AUTHORIZATION_BEARER)) {
            v = auth.substring(WxMaConstants.AUTHORIZATION_BEARER.length()).trim();
            if (StringUtils.isNotEmpty(v)) return v;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> getSessionByToken(String token) {
        if (StringUtils.isEmpty(token)) return null;
        String key = WxMaConstants.REDIS_TOKEN_PREFIX + token;
        try {
            Object v = redisTemplate.opsForValue().get(key);
            return v instanceof Map ? (Map<String, String>) v : null;
        } catch (Exception e) {
            if (log.isTraceEnabled()) log.trace("[WxMa] Redis 读 token 异常: {}", e.getMessage());
            return null;
        }
    }

    /** 从 token 会话 Map 取微信 session_key，写入 request 供 weixin 模块校验 */
    private String getSessionKeyValue(String openid, Map<String, String> redisSession) {
        if (ANONYMOUS_OPENID.equals(openid)) return "anonymous";
        if (redisSession != null) {
            String sk = redisSession.get("sessionKey");
            if (StringUtils.isEmpty(sk)) sk = redisSession.get("session_key");
            if (StringUtils.isNotEmpty(sk)) return sk;
        }
        return "";
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> getSessionFromRedis(String openid) {
        String key = REDIS_OPENID_SESSION_PREFIX + openid;
        try {
            Object v = redisTemplate.opsForValue().get(key);
            if (v == null) {
                if (log.isDebugEnabled()) log.debug("[WxMa] Redis 未命中 key={}", key);
                return null;
            }
            if (v instanceof Map) {
                return (Map<String, String>) v;
            }
            if (log.isDebugEnabled()) log.debug("[WxMa] Redis 值类型非 Map: {} key={}", v.getClass().getName(), key);
            return null;
        } catch (Exception e) {
            if (log.isWarnEnabled()) log.warn("[WxMa] Redis 读取会话异常 key={} {}", key, e.getMessage());
            return null;
        }
    }

    private boolean bindThirdSession(String openid, Map<String, String> redisSession) throws Exception {
        Object session = createThirdSession(openid, redisSession);
        if (session == null) return false;
        Class<?> holderClass = Class.forName("com.dingyangmall.weixin.utils.ThirdSessionHolder");
        for (Method m : holderClass.getMethods()) {
            String n = m.getName();
            if ((n.equals("set") || n.equals("bind") || n.equals("setThirdSession")) && m.getParameterCount() == 1) {
                m.invoke(null, session);
                return true;
            }
        }
        return false;
    }

    private Object createThirdSession(String openid, Map<String, String> redisSession) {
        String sessionKey = null;
        if (redisSession != null) {
            sessionKey = redisSession.get("sessionKey");
            if (StringUtils.isEmpty(sessionKey)) sessionKey = redisSession.get("session_key");
        }
        if (sessionKey == null) {
            sessionKey = ANONYMOUS_OPENID.equals(openid) ? "anonymous" : "";
        }

        String[] pkgs = {"com.dingyangmall.weixin.entity", "com.dingyangmall.weixin.domain"};
        for (String pkg : pkgs) {
            try {
                Class<?> sessionClass = Class.forName(pkg + ".ThirdSession");
                Object session = sessionClass.getDeclaredConstructor().newInstance();
                for (Method m : sessionClass.getMethods()) {
                    String name = m.getName();
                    if (name.startsWith("set") && m.getParameterCount() == 1 && m.getParameterTypes()[0] == String.class) {
                        try {
                            if (name.equals("setWxUserId") || name.equals("setOpenId") || name.equals("setOpenid")) {
                                m.invoke(session, openid);
                            } else if (name.equals("setSessionKey")) {
                                m.invoke(session, sessionKey);
                            }
                        } catch (Exception ignored) {}
                    }
                }
                return session;
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                continue;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private void clearThirdSession() throws Exception {
        Class<?> holderClass = Class.forName("com.dingyangmall.weixin.utils.ThirdSessionHolder");
        for (Method m : holderClass.getMethods()) {
            String n = m.getName();
            if ((n.equals("remove") || n.equals("clear")) && m.getParameterCount() == 0) {
                m.invoke(null);
                return;
            }
        }
    }
}
