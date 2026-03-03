package com.dingyangmall.web.core.filter;

import com.dingyangmall.common.utils.StringUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 最早执行：仅从 Header 读取 third-session/openid 并注入 ThirdSessionHolder，
 * 确保在 dingyangmall-weixin 模块的校验逻辑之前完成注入，避免 60001 登录超时。
 * 与 WxMaMemberFilter 配合：本 Filter 只做 ThreadLocal 注入，不依赖 Spring Security 链顺序。
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WxMaThirdSessionEarlyFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(WxMaThirdSessionEarlyFilter.class);

    private static boolean isOpenidFormat(String s) {
        return StringUtils.isNotEmpty(s) && s.length() >= 26 && s.length() <= 32 && s.startsWith("o");
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
        String openid = req.getHeader("openid");
        if (StringUtils.isEmpty(openid)) openid = req.getHeader("third-session");
        if (StringUtils.isEmpty(openid)) openid = req.getHeader("Third-Session");
        if (openid != null) openid = openid.trim();
        boolean bound = false;
        if (StringUtils.isNotEmpty(openid) && isOpenidFormat(openid)) {
            try {
                bound = bindThirdSession(openid);
                if (log.isDebugEnabled() && bound) {
                    log.debug("[WxMa] ThirdSessionEarlyFilter 已注入 ThirdSessionHolder path={}", path);
                }
            } catch (Exception e) {
                if (log.isTraceEnabled()) {
                    log.trace("[WxMa] ThirdSessionEarlyFilter 注入失败: {}", e.getMessage());
                }
            }
        } else if (log.isDebugEnabled()) {
            log.debug("[WxMa] ThirdSessionEarlyFilter path={} 无有效 third-session/openid，跳过注入", path);
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

    private boolean bindThirdSession(String openid) throws Exception {
        Object session = createThirdSession(openid);
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
}
