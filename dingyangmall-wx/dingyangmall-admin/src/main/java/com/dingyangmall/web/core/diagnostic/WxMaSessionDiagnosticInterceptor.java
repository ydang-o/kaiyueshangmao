package com.dingyangmall.web.core.diagnostic;

import com.dingyangmall.common.utils.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

/**
 * 诊断用：在进入 Controller 前记录 ThirdSessionHolder、request.memberId、SecurityContext 状态，
 * 便于 60001 时确认「下游哪一环未识别」。
 */
@Component
public class WxMaSessionDiagnosticInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(WxMaSessionDiagnosticInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI();
        if (!path.contains("/weixin/api/ma/")) {
            return true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("path=").append(path);

        String holderUserId = null;
        String holderSessionKey = null;
        try {
            Class<?> holderClass = Class.forName("com.dingyangmall.weixin.utils.ThirdSessionHolder");
            Method getUserId = holderClass.getMethod("getWxUserId");
            holderUserId = (String) getUserId.invoke(null);
            sb.append(" | ThirdSessionHolder.getWxUserId()=").append(holderUserId != null ? holderUserId : "null");
            try {
                Method getSession = holderClass.getMethod("getThirdSession");
                Object session = getSession.invoke(null);
                if (session != null) {
                    Method sk = session.getClass().getMethod("getSessionKey");
                    holderSessionKey = (String) sk.invoke(session);
                    sb.append(" | getThirdSession().getSessionKey()=").append(holderSessionKey != null ? "(有)" : "null");
                } else {
                    sb.append(" | getThirdSession()=null");
                }
            } catch (Exception e) {
                sb.append(" | getThirdSession() 异常: ").append(e.getMessage());
            }
        } catch (Exception e) {
            sb.append(" | ThirdSessionHolder 反射异常: ").append(e.getMessage());
        }

        Object attrMemberId = request.getAttribute("memberId");
        String memberId = attrMemberId != null ? attrMemberId.toString() : null;
        sb.append(" | request.memberId=").append(StringUtils.isEmpty(memberId) ? "null" : memberId);

        var auth = SecurityContextHolder.getContext().getAuthentication();
        sb.append(" | SecurityContext.principal=").append(auth != null && auth.getPrincipal() != null ? auth.getPrincipal() : "null");

        // 60001 抛出处附近：打印 weixin 校验可能用到的 sessionKey / third-session，便于对照
        String sessionKeyFromHolder = holderSessionKey;
        if (sessionKeyFromHolder == null) {
            try {
                Class<?> hc = Class.forName("com.dingyangmall.weixin.utils.ThirdSessionHolder");
                Method m = hc.getMethod("getSessionKey");
                sessionKeyFromHolder = (String) m.invoke(null);
            } catch (Exception ignored) { }
        }
        log.info("校验时 sessionKey from Holder: {}", sessionKeyFromHolder);
        log.info("校验时 sessionKey from Request: {}", request.getAttribute("sessionKey"));
        log.info("校验时 thirdSession from Header: {}", request.getHeader("third-session"));

        String msg = sb.toString();
        WxMaSessionDiagnostic.set(msg);
        log.info("[WxMa][诊断] 进入 Controller 前 {}", msg);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                               Object handler, Exception ex) {
        // 由 WxMaErrorRewriteFilter 在改写 60001 后统一 clear，此处不清理以便写 60001 时能读到诊断
    }
}
