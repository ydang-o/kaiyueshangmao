/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.core.filter;

import com.dingyangmall.common.utils.StringUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Order(value=-2147483648)
public class WxMaThirdSessionEarlyFilter
implements Filter {
    private static final Logger log = LoggerFactory.getLogger(WxMaThirdSessionEarlyFilter.class);
    private static final String REDIS_OPENID_SESSION_PREFIX = "wx:openid_session:";
    private static final String ANONYMOUS_OPENID = "oAnonymous00000000000000000";
    private static final String[] ANONYMOUS_PATHS = new String[]{"/weixin/api/ma/goodsspu/page", "/weixin/api/ma/goodscategory", "/weixin/api/ma/notice/list"};
    private final RedisTemplate<String, Object> redisTemplate;
    private final boolean sessionUseRedis;

    public WxMaThirdSessionEarlyFilter(RedisTemplate<String, Object> redisTemplate, @Value(value="${wx.ma.session-use-redis:true}") boolean sessionUseRedis) {
        this.redisTemplate = redisTemplate;
        this.sessionUseRedis = sessionUseRedis;
    }

    private static boolean isOpenidFormat(String s) {
        return StringUtils.isNotEmpty(s) && s.length() >= 26 && s.length() <= 32 && s.startsWith("o");
    }

    private static boolean isAnonymousPath(String path) {
        if (path == null) {
            return false;
        }
        for (String p : ANONYMOUS_PATHS) {
            if (!path.contains(p)) continue;
            return true;
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        boolean bound;
        block28: {
            HttpServletRequest req = (HttpServletRequest)request;
            String path = req.getRequestURI();
            if (!path.contains("/weixin/api/ma/") || path.endsWith("wxuser/login") && "POST".equalsIgnoreCase(req.getMethod())) {
                chain.doFilter(request, response);
                return;
            }
            String token = WxMaThirdSessionEarlyFilter.getTokenFromRequest(req);
            bound = false;
            if (StringUtils.isNotEmpty(token) && this.sessionUseRedis) {
                Map<String, String> redisSession = this.getSessionByToken(token);
                if (redisSession != null) {
                    String openid = redisSession.get("openid");
                    if (StringUtils.isEmpty(openid)) {
                        openid = redisSession.get("memberId");
                    }
                    if (StringUtils.isNotEmpty(openid) && WxMaThirdSessionEarlyFilter.isOpenidFormat(openid)) {
                        try {
                            bound = this.bindThirdSession(openid, redisSession);
                            if (bound) {
                                String sessionKeyVal = this.getSessionKeyValue(openid, redisSession);
                                if (StringUtils.isNotEmpty(sessionKeyVal)) {
                                    req.setAttribute("sessionKey", sessionKeyVal);
                                }
                                if (log.isDebugEnabled()) {
                                    log.debug("[WxMa] ThirdSessionEarlyFilter \u5df2\u6309 token \u6ce8\u5165 ThirdSessionHolder path={}", (Object)path);
                                }
                            }
                        }
                        catch (Exception e) {
                            if (log.isTraceEnabled()) {
                                log.trace("[WxMa] ThirdSessionEarlyFilter \u6ce8\u5165\u5931\u8d25: {}", (Object)e.getMessage());
                            }
                        }
                    }
                } else if (!WxMaThirdSessionEarlyFilter.isAnonymousPath(path) && log.isDebugEnabled()) {
                    log.debug("[WxMa] path={} token \u65e0\u6548\u6216\u5df2\u8fc7\u671f", (Object)path);
                }
            }
            if (!bound && WxMaThirdSessionEarlyFilter.isAnonymousPath(path)) {
                try {
                    bound = this.bindThirdSession(ANONYMOUS_OPENID, null);
                    if (!bound) break block28;
                    req.setAttribute("sessionKey", "anonymous");
                    if (log.isDebugEnabled()) {
                        log.debug("[WxMa] ThirdSessionEarlyFilter \u5df2\u6ce8\u5165\u533f\u540d\u4f1a\u8bdd path={}", (Object)path);
                    }
                    break block28;
                }
                catch (Exception e) {
                    if (log.isTraceEnabled()) {
                        log.trace("[WxMa] ThirdSessionEarlyFilter \u533f\u540d\u4f1a\u8bdd\u6ce8\u5165\u5931\u8d25: {}", (Object)e.getMessage());
                    }
                    break block28;
                }
            }
            if (log.isDebugEnabled() && !bound) {
                log.debug("[WxMa] ThirdSessionEarlyFilter path={} \u672a\u5e26\u6709\u6548 {}\uff0c\u8df3\u8fc7\u6ce8\u5165", (Object)path, (Object)"X-Wx-Token");
            }
        }
        try {
            chain.doFilter(request, response);
        }
        finally {
            if (bound) {
                try {
                    this.clearThirdSession();
                }
                catch (Exception exception) {}
            }
        }
    }

    private static String getTokenFromRequest(HttpServletRequest req) {
        String v = req.getHeader("X-Wx-Token");
        if (StringUtils.isNotEmpty(v)) {
            return v.trim();
        }
        String auth = req.getHeader("Authorization");
        if (StringUtils.isNotEmpty(auth) && auth.startsWith("Bearer ") && StringUtils.isNotEmpty(v = auth.substring("Bearer ".length()).trim())) {
            return v;
        }
        return null;
    }

    private Map<String, String> getSessionByToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        String key = "wx:token:" + token;
        try {
            Object v = this.redisTemplate.opsForValue().get(key);
            return v instanceof Map ? (Map)v : null;
        }
        catch (Exception e) {
            if (log.isTraceEnabled()) {
                log.trace("[WxMa] Redis \u8bfb token \u5f02\u5e38: {}", (Object)e.getMessage());
            }
            return null;
        }
    }

    private String getSessionKeyValue(String openid, Map<String, String> redisSession) {
        if (ANONYMOUS_OPENID.equals(openid)) {
            return "anonymous";
        }
        if (redisSession != null) {
            String sk = redisSession.get("sessionKey");
            if (StringUtils.isEmpty(sk)) {
                sk = redisSession.get("session_key");
            }
            if (StringUtils.isNotEmpty(sk)) {
                return sk;
            }
        }
        return "";
    }

    private Map<String, String> getSessionFromRedis(String openid) {
        String key = REDIS_OPENID_SESSION_PREFIX + openid;
        try {
            Object v = this.redisTemplate.opsForValue().get(key);
            if (v == null) {
                if (log.isDebugEnabled()) {
                    log.debug("[WxMa] Redis \u672a\u547d\u4e2d key={}", (Object)key);
                }
                return null;
            }
            if (v instanceof Map) {
                return (Map)v;
            }
            if (log.isDebugEnabled()) {
                log.debug("[WxMa] Redis \u503c\u7c7b\u578b\u975e Map: {} key={}", (Object)v.getClass().getName(), (Object)key);
            }
            return null;
        }
        catch (Exception e) {
            if (log.isWarnEnabled()) {
                log.warn("[WxMa] Redis \u8bfb\u53d6\u4f1a\u8bdd\u5f02\u5e38 key={} {}", (Object)key, (Object)e.getMessage());
            }
            return null;
        }
    }

    private boolean bindThirdSession(String openid, Map<String, String> redisSession) throws Exception {
        Object session = this.createThirdSession(openid, redisSession);
        if (session == null) {
            return false;
        }
        Class<?> holderClass = Class.forName("com.dingyangmall.weixin.utils.ThirdSessionHolder");
        for (Method m : holderClass.getMethods()) {
            String n = m.getName();
            if (!n.equals("set") && !n.equals("bind") && !n.equals("setThirdSession") || m.getParameterCount() != 1) continue;
            m.invoke(null, session);
            return true;
        }
        return false;
    }

    private Object createThirdSession(String openid, Map<String, String> redisSession) {
        String[] pkgs;
        String sessionKey = null;
        if (redisSession != null && StringUtils.isEmpty(sessionKey = redisSession.get("sessionKey"))) {
            sessionKey = redisSession.get("session_key");
        }
        if (sessionKey == null) {
            sessionKey = ANONYMOUS_OPENID.equals(openid) ? "anonymous" : "";
        }
        for (String pkg : pkgs = new String[]{"com.dingyangmall.weixin.entity", "com.dingyangmall.weixin.domain"}) {
            try {
                Class<?> sessionClass = Class.forName(pkg + ".ThirdSession");
                Object session = sessionClass.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                for (Method m : sessionClass.getMethods()) {
                    String name = m.getName();
                    if (!name.startsWith("set") || m.getParameterCount() != 1 || m.getParameterTypes()[0] != String.class) continue;
                    try {
                        if (name.equals("setWxUserId") || name.equals("setOpenId") || name.equals("setOpenid")) {
                            m.invoke(session, openid);
                            continue;
                        }
                        if (!name.equals("setSessionKey")) continue;
                        m.invoke(session, sessionKey);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
                return session;
            }
            catch (ClassNotFoundException | NoSuchMethodException e) {
            }
            catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private void clearThirdSession() throws Exception {
        Class<?> holderClass = Class.forName("com.dingyangmall.weixin.utils.ThirdSessionHolder");
        for (Method m : holderClass.getMethods()) {
            String n = m.getName();
            if (!n.equals("remove") && !n.equals("clear") || m.getParameterCount() != 0) continue;
            m.invoke(null, new Object[0]);
            return;
        }
    }
}

