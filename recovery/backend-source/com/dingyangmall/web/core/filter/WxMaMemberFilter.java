/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.dingyangmall.common.utils.StringUtils
 *  com.fasterxml.jackson.databind.JsonNode
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  jakarta.servlet.Filter
 *  jakarta.servlet.FilterChain
 *  jakarta.servlet.ReadListener
 *  jakarta.servlet.ServletException
 *  jakarta.servlet.ServletInputStream
 *  jakarta.servlet.ServletRequest
 *  jakarta.servlet.ServletResponse
 *  jakarta.servlet.http.HttpServletRequest
 *  jakarta.servlet.http.HttpServletRequestWrapper
 *  jakarta.servlet.http.HttpServletResponse
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.core.Ordered
 *  org.springframework.data.redis.core.RedisTemplate
 *  org.springframework.security.authentication.UsernamePasswordAuthenticationToken
 *  org.springframework.security.core.Authentication
 *  org.springframework.security.core.context.SecurityContextHolder
 *  org.springframework.stereotype.Component
 *  org.springframework.web.context.request.RequestAttributes
 *  org.springframework.web.context.request.RequestContextHolder
 *  org.springframework.web.context.request.ServletRequestAttributes
 */
package com.dingyangmall.web.core.filter;

import com.dingyangmall.common.utils.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class WxMaMemberFilter
implements Filter,
Ordered {
    private static final Logger log = LoggerFactory.getLogger(WxMaMemberFilter.class);
    private static final String REDIS_OPENID_SESSION_PREFIX = "wx:openid_session:";
    public static final String ATTR_MEMBER_ID = "memberId";
    private static final String HEADER_MEMBER_ID = "member-id";
    private final RedisTemplate<String, Object> redisTemplate;
    private final boolean sessionUseRedis;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static boolean isOpenidFormat(String s) {
        return StringUtils.isNotEmpty((String)s) && s.length() >= 26 && s.length() <= 32 && s.startsWith("o");
    }

    public WxMaMemberFilter(RedisTemplate<String, Object> redisTemplate, @Value(value="${wx.ma.session-use-redis:true}") boolean sessionUseRedis) {
        this.redisTemplate = redisTemplate;
        this.sessionUseRedis = sessionUseRedis;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Object toPass;
        String finalMemberId;
        block34: {
            HttpServletRequest req = (HttpServletRequest)request;
            String path = req.getRequestURI();
            String method = req.getMethod();
            boolean isWxMaPath = path.contains("/weixin/api/ma/");
            if (!isWxMaPath) {
                chain.doFilter(request, response);
                return;
            }
            if ("POST".equalsIgnoreCase(method) && path.endsWith("wxuser/login")) {
                chain.doFilter(request, response);
                return;
            }
            Object reqToPass = req;
            if ("POST".equalsIgnoreCase(method) && path.contains("wxuser") && !path.endsWith("wxuser/login")) {
                reqToPass = new CachedBodyRequestWrapper(req);
            }
            String memberId = null;
            String token = WxMaMemberFilter.getTokenFromRequest(req);
            if (StringUtils.isEmpty((String)token) && reqToPass instanceof CachedBodyRequestWrapper) {
                token = this.readTokenFromBody((CachedBodyRequestWrapper)((Object)reqToPass));
                if (log.isDebugEnabled() && StringUtils.isNotEmpty((String)token)) {
                    log.debug("[WxMa] path={} \u4ece body \u89e3\u6790 token", (Object)path);
                }
            }
            if (StringUtils.isNotEmpty((String)token)) {
                if (!this.sessionUseRedis) {
                    memberId = null;
                } else {
                    try {
                        String redisKey = "wx:token:" + token;
                        Object v = this.redisTemplate.opsForValue().get((Object)redisKey);
                        if (v instanceof Map) {
                            Map session = (Map)v;
                            memberId = (String)session.get(ATTR_MEMBER_ID);
                            if (StringUtils.isEmpty((String)memberId)) {
                                memberId = (String)session.get("openid");
                            }
                            if (StringUtils.isNotEmpty((String)memberId)) {
                                reqToPass.setAttribute(ATTR_MEMBER_ID, (Object)memberId);
                                req.setAttribute(ATTR_MEMBER_ID, (Object)memberId);
                                if (log.isInfoEnabled()) {
                                    log.info("[WxMa] path={} \u5df2\u6309 token \u6ce8\u5165 memberId", (Object)path);
                                }
                            }
                        }
                    }
                    catch (Exception e) {
                        log.warn("[WxMa] \u6309 token \u89e3\u6790\u5931\u8d25: {}", (Object)e.getMessage());
                    }
                }
            }
            finalMemberId = memberId;
            Object finalReq = reqToPass;
            toPass = StringUtils.isNotEmpty(finalMemberId) ? new HttpServletRequestWrapper((HttpServletRequest)finalReq){

                public Object getAttribute(String name) {
                    if (WxMaMemberFilter.ATTR_MEMBER_ID.equals(name)) {
                        return finalMemberId;
                    }
                    return super.getAttribute(name);
                }

                public String getHeader(String name) {
                    if (WxMaMemberFilter.HEADER_MEMBER_ID.equalsIgnoreCase(name)) {
                        return finalMemberId;
                    }
                    return super.getHeader(name);
                }

                public Enumeration<String> getHeaders(String name) {
                    if (WxMaMemberFilter.HEADER_MEMBER_ID.equalsIgnoreCase(name)) {
                        return Collections.enumeration(Collections.singletonList(finalMemberId));
                    }
                    return super.getHeaders(name);
                }
            } : reqToPass;
            try {
                RequestContextHolder.setRequestAttributes((RequestAttributes)new ServletRequestAttributes(toPass, (HttpServletResponse)response));
            }
            catch (Exception e) {
                log.trace("RequestContextHolder.setRequestAttributes \u5f02\u5e38: {}", (Object)e.getMessage());
            }
            if (StringUtils.isNotEmpty(finalMemberId)) {
                try {
                    SecurityContextHolder.getContext().setAuthentication((Authentication)new UsernamePasswordAuthenticationToken((Object)finalMemberId, null, Collections.emptyList()));
                }
                catch (Exception e) {
                    log.trace("SecurityContextHolder.setAuthentication \u5f02\u5e38: {}", (Object)e.getMessage());
                }
            }
            if (StringUtils.isNotEmpty(finalMemberId) && !this.sessionUseRedis) {
                try {
                    this.bindThirdSession(finalMemberId);
                }
                catch (Exception e) {
                    if (!log.isWarnEnabled()) break block34;
                    log.warn("[WxMa] ThirdSessionHolder \u6ce8\u5165\u5931\u8d25 path={} \u53ef\u80fd\u4ecd\u8fd4\u56de60001: {}", (Object)path, (Object)e.getMessage());
                }
            }
        }
        try {
            chain.doFilter((ServletRequest)toPass, response);
        }
        finally {
            if (StringUtils.isNotEmpty((String)finalMemberId)) {
                try {
                    SecurityContextHolder.clearContext();
                }
                catch (Exception exception) {}
                try {
                    this.clearThirdSession();
                }
                catch (Exception exception) {}
            }
        }
    }

    private void bindThirdSession(String openid) throws Exception {
        Object session = this.createThirdSession(openid);
        if (session == null) {
            throw new IllegalStateException("ThirdSession \u65e0\u6cd5\u521b\u5efa");
        }
        Class<?> holderClass = Class.forName("com.dingyangmall.weixin.utils.ThirdSessionHolder");
        for (Method m : holderClass.getMethods()) {
            String n = m.getName();
            if (!n.equals("set") && !n.equals("bind") && !n.equals("setThirdSession") || m.getParameterCount() != 1) continue;
            m.invoke(null, session);
            return;
        }
    }

    private Object createThirdSession(String openid) {
        String[] pkgs;
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
                        m.invoke(session, "");
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

    private static String getTokenFromRequest(HttpServletRequest req) {
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

    private String readTokenFromBody(CachedBodyRequestWrapper req) {
        byte[] body;
        try {
            body = req.getCachedBody();
        }
        catch (Exception e) {
            log.warn("[WxMa] \u8bfb\u53d6 body \u5931\u8d25: {}", (Object)e.getMessage());
            return null;
        }
        if (body == null || body.length == 0) {
            return null;
        }
        try {
            JsonNode v;
            JsonNode root = this.objectMapper.readTree(body);
            if (root != null && root.isObject() && (v = root.get("token")) != null && v.isTextual() && StringUtils.isNotEmpty((String)v.asText())) {
                return v.asText().trim();
            }
        }
        catch (Exception e) {
            log.debug("[WxMa] body \u975e JSON \u6216\u89e3\u6790\u5931\u8d25: {}", (Object)e.getMessage());
        }
        try {
            String raw = new String(body, StandardCharsets.UTF_8);
            Pattern p = Pattern.compile("\"token\"\\s*:\\s*\"([^\"]+)\"");
            Matcher m = p.matcher(raw);
            if (m.find() && StringUtils.isNotEmpty((String)m.group(1))) {
                return m.group(1).trim();
            }
        }
        catch (Exception e) {
            log.trace("[WxMa] body \u5b57\u7b26\u4e32\u515c\u5e95\u89e3\u6790\u5931\u8d25: {}", (Object)e.getMessage());
        }
        return null;
    }

    public int getOrder() {
        return Integer.MIN_VALUE;
    }

    private static class CachedBodyRequestWrapper
    extends HttpServletRequestWrapper {
        private byte[] cachedBody;

        public CachedBodyRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        public byte[] getCachedBody() throws IOException {
            if (this.cachedBody == null) {
                try (ServletInputStream in = super.getInputStream();){
                    this.cachedBody = in.readAllBytes();
                }
            }
            return this.cachedBody;
        }

        public ServletInputStream getInputStream() throws IOException {
            final byte[] body = this.getCachedBody();
            return new ServletInputStream(){
                private int i;

                public boolean isFinished() {
                    return this.i >= body.length;
                }

                public boolean isReady() {
                    return true;
                }

                public void setReadListener(ReadListener readListener) {
                }

                public int read() {
                    if (this.i >= body.length) {
                        return -1;
                    }
                    return body[this.i++] & 0xFF;
                }
            };
        }
    }
}

