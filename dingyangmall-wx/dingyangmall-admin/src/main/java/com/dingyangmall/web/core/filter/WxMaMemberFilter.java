package com.dingyangmall.web.core.filter;

import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.web.core.WxMaConstants;
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
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

/**
 * 小程序 /weixin/api/ma/* 请求：根据 Header 的 X-Wx-Token（或 Authorization: Bearer）从 Redis wx:token:{token} 解析出当前用户并注入 memberId，
 * 若 Header 未带且为 POST，则从 Body JSON 的 token 兜底读取。
 * 由 WxMaSecurityConfig 注入并加入 Security 过滤链。
 */
@Component
public class WxMaMemberFilter implements Filter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(WxMaMemberFilter.class);

    private static final String REDIS_OPENID_SESSION_PREFIX = "wx:openid_session:";

    public static final String ATTR_MEMBER_ID = "memberId";
    private static final String HEADER_MEMBER_ID = "member-id";

    private static boolean isOpenidFormat(String s) {
        return StringUtils.isNotEmpty(s) && s.length() >= 26 && s.length() <= 32 && s.startsWith("o");
    }

    private final RedisTemplate<Object, Object> redisTemplate;
    private final boolean sessionUseRedis;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WxMaMemberFilter(RedisTemplate<Object, Object> redisTemplate,
                            @Value("${wx.ma.session-use-redis:true}") boolean sessionUseRedis) {
        this.redisTemplate = redisTemplate;
        this.sessionUseRedis = sessionUseRedis;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI();
        String method = req.getMethod();
        // 兼容 context-path：可能为 /weixin/api/ma/... 或 /xxx/weixin/api/ma/...
        boolean isWxMaPath = path.contains("/weixin/api/ma/");

        if (!isWxMaPath) {
            chain.doFilter(request, response);
            return;
        }
        if ("POST".equalsIgnoreCase(method) && path.endsWith("wxuser/login")) {
            chain.doFilter(request, response);
            return;
        }

        // 对 POST 且非登录的小程序接口先包装并缓存 body，避免被其它 Filter 先消费导致读不到 token
        HttpServletRequest reqToPass = req;
        if ("POST".equalsIgnoreCase(method) && path.contains("wxuser") && !path.endsWith("wxuser/login")) {
            reqToPass = new CachedBodyRequestWrapper(req);
        }

        String memberId = null;
        String token = getTokenFromRequest(req);
        if (StringUtils.isEmpty(token) && reqToPass instanceof CachedBodyRequestWrapper) {
            token = readTokenFromBody((CachedBodyRequestWrapper) reqToPass);
            if (log.isDebugEnabled() && StringUtils.isNotEmpty(token)) {
                log.debug("[WxMa] path={} 从 body 解析 token", path);
            }
        }
        if (StringUtils.isNotEmpty(token)) {
            if (!sessionUseRedis) {
                memberId = null;
            } else {
                try {
                    String redisKey = WxMaConstants.REDIS_TOKEN_PREFIX + token;
                    Object v = redisTemplate.opsForValue().get(redisKey);
                    if (v instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, String> session = (Map<String, String>) v;
                        memberId = session.get("memberId");
                        if (StringUtils.isEmpty(memberId)) memberId = session.get("openid");
                        if (StringUtils.isNotEmpty(memberId)) {
                            reqToPass.setAttribute(ATTR_MEMBER_ID, memberId);
                            req.setAttribute(ATTR_MEMBER_ID, memberId);
                            if (log.isInfoEnabled()) {
                                log.info("[WxMa] path={} 已按 token 注入 memberId", path);
                            }
                        }
                    }
                } catch (Exception e) {
                    log.warn("[WxMa] 按 token 解析失败: {}", e.getMessage());
                }
            }
        }

        final String finalMemberId = memberId;
        final HttpServletRequest finalReq = reqToPass;
        HttpServletRequest toPass = (StringUtils.isNotEmpty(finalMemberId))
            ? new HttpServletRequestWrapper(finalReq) {
                @Override
                public Object getAttribute(String name) {
                    if (ATTR_MEMBER_ID.equals(name)) {
                        return finalMemberId;
                    }
                    return super.getAttribute(name);
                }
                @Override
                public String getHeader(String name) {
                    if (HEADER_MEMBER_ID.equalsIgnoreCase(name)) {
                        return finalMemberId;
                    }
                    return super.getHeader(name);
                }
                @Override
                public Enumeration<String> getHeaders(String name) {
                    if (HEADER_MEMBER_ID.equalsIgnoreCase(name)) {
                        return Collections.enumeration(Collections.singletonList(finalMemberId));
                    }
                    return super.getHeaders(name);
                }
            }
            : reqToPass;

        // 让 ServletUtils.getRequest() / MemberUtils.getMemberId() 拿到带 memberId 的 request，避免下游返回 60001
        try {
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(toPass, (HttpServletResponse) response));
        } catch (Exception e) {
            log.trace("RequestContextHolder.setRequestAttributes 异常: {}", e.getMessage());
        }
        // 下游若按 SecurityContext 判断“已登录”则需设置 Authentication，否则仍会返回 60001
        if (StringUtils.isNotEmpty(finalMemberId)) {
            try {
                SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(finalMemberId, null, Collections.emptyList()));
            } catch (Exception e) {
                log.trace("SecurityContextHolder.setAuthentication 异常: {}", e.getMessage());
            }
        }
        // weixin 依赖 ThirdSessionHolder；sessionUseRedis 时由 EarlyFilter 已按 token 注入，此处仅在不使用 Redis 时补绑
        if (StringUtils.isNotEmpty(finalMemberId) && !sessionUseRedis) {
            try {
                bindThirdSession(finalMemberId);
            } catch (Exception e) {
                if (log.isWarnEnabled()) {
                    log.warn("[WxMa] ThirdSessionHolder 注入失败 path={} 可能仍返回60001: {}", path, e.getMessage());
                }
            }
        }
        try {
            chain.doFilter(toPass, response);
        } finally {
            if (StringUtils.isNotEmpty(finalMemberId)) {
                try {
                    SecurityContextHolder.clearContext();
                } catch (Exception ignored) {}
                try {
                    clearThirdSession();
                } catch (Exception ignored) {}
            }
        }
    }

    /**
     * 通过反射向 ThirdSessionHolder 注入会话，供 dingyangmall-weixin 下游拦截器校验，避免 60001 登录超时。
     * 当 session-use-redis=false 时，登录不写 Redis，需此处用 openid 构建最小会话注入。
     */
    private void bindThirdSession(String openid) throws Exception {
        Object session = createThirdSession(openid);
        if (session == null) throw new IllegalStateException("ThirdSession 无法创建");
        Class<?> holderClass = Class.forName("com.dingyangmall.weixin.utils.ThirdSessionHolder");
        for (Method m : holderClass.getMethods()) {
            String n = m.getName();
            if ((n.equals("set") || n.equals("bind") || n.equals("setThirdSession")) && m.getParameterCount() == 1) {
                m.invoke(null, session);
                return;
            }
        }
    }

    private Object createThirdSession(String openid) {
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
                                m.invoke(session, "");
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

    /** 从 POST body 中读取 token（标准字段 token）；req 须为 CachedBodyRequestWrapper。 */
    private String readTokenFromBody(CachedBodyRequestWrapper req) {
        byte[] body;
        try {
            body = req.getCachedBody();
        } catch (Exception e) {
            log.warn("[WxMa] 读取 body 失败: {}", e.getMessage());
            return null;
        }
        if (body == null || body.length == 0) return null;
        try {
            JsonNode root = objectMapper.readTree(body);
            if (root != null && root.isObject()) {
                JsonNode v = root.get(WxMaConstants.BODY_TOKEN_KEY);
                if (v != null && v.isTextual() && StringUtils.isNotEmpty(v.asText())) {
                    return v.asText().trim();
                }
            }
        } catch (Exception e) {
            log.debug("[WxMa] body 非 JSON 或解析失败: {}", e.getMessage());
        }
        try {
            String raw = new String(body, java.nio.charset.StandardCharsets.UTF_8);
            java.util.regex.Pattern p = java.util.regex.Pattern.compile("\"token\"\\s*:\\s*\"([^\"]+)\"");
            java.util.regex.Matcher m = p.matcher(raw);
            if (m.find() && StringUtils.isNotEmpty(m.group(1))) return m.group(1).trim();
        } catch (Exception e) {
            log.trace("[WxMa] body 字符串兜底解析失败: {}", e.getMessage());
        }
        return null;
    }

    /** 可重读 body 的 Request 包装：首次读取时缓存，后续 getInputStream() 返回缓存内容 */
    private static class CachedBodyRequestWrapper extends HttpServletRequestWrapper {
        private byte[] cachedBody;

        public CachedBodyRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        public byte[] getCachedBody() throws IOException {
            if (cachedBody == null) {
                try (InputStream in = super.getInputStream()) {
                    cachedBody = in.readAllBytes();
                }
            }
            return cachedBody;
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            byte[] body = getCachedBody();
            return new ServletInputStream() {
                private int i;

                @Override
                public boolean isFinished() {
                    return i >= body.length;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(ReadListener readListener) {}

                @Override
                public int read() {
                    if (i >= body.length) return -1;
                    return body[i++] & 0xff;
                }
            };
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
