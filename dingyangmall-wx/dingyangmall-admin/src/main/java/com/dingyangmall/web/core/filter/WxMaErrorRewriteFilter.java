package com.dingyangmall.web.core.filter;

import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.web.core.WxMaConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Rewrites 60001/60002 login error responses for /weixin/api/ma/* with detailed message (token/path hint).
 * Registered via WxMaSecurityConfig FilterRegistrationBean.
 */
public class WxMaErrorRewriteFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(WxMaErrorRewriteFilter.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String WX_MA_PATH = "/weixin/api/ma/";
    private static final int CODE_60001 = 60001;
    private static final int CODE_60002 = 60002;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String path = req.getRequestURI();
        if (!path.contains(WX_MA_PATH) || path.endsWith("wxuser/login")) {
            chain.doFilter(request, response);
            return;
        }

        BufferedResponseWrapper wrapper = new BufferedResponseWrapper(resp);
        chain.doFilter(request, wrapper);
        byte[] content = wrapper.getContent();
        if (content == null || content.length == 0) {
            return;
        }
        String body = new String(content, StandardCharsets.UTF_8);
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = objectMapper.readValue(body, Map.class);
            Object codeObj = map.get("code");
            int code = codeObj instanceof Number ? ((Number) codeObj).intValue() : -1;
            if (code == CODE_60001 || code == CODE_60002) {
                String originalMsg = map.get("msg") != null ? String.valueOf(map.get("msg")) : "";
                // 统一走 token 模式：若下游返回含 session 的文案则替换为 token 相关提示，避免对用户展示 session 字样
                String msgForUser = toTokenModeMessage(code, originalMsg);
                String detailMsg = buildDetailMsg(code, msgForUser, path);
                map.put("msg", detailMsg);
                byte[] newBody = objectMapper.writeValueAsBytes(map);
                if (!resp.isCommitted()) {
                    resp.setContentType("application/json;charset=UTF-8");
                    resp.setContentLength(newBody.length);
                    resp.getOutputStream().write(newBody);
                }
                if (log.isDebugEnabled()) {
                    log.debug("[WxMa] 60001/60002 response path={} code={} token present={}", path, code, StringUtils.isNotEmpty(req.getHeader(WxMaConstants.HEADER_TOKEN)));
                }
                return;
            }
        } catch (Exception e) {
            log.trace("Response not JSON or rewrite failed: {}", e.getMessage());
        }
        if (!resp.isCommitted()) {
            resp.setContentLength(content.length);
            resp.getOutputStream().write(content);
        }
    }

    /** 将下游可能返回的 session 相关文案统一改为 token 模式，不对前端暴露 session 字样 */
    private static String toTokenModeMessage(int code, String originalMsg) {
        if (StringUtils.isEmpty(originalMsg)) {
            return code == CODE_60002 ? "请携带登录令牌" : "登录已过期，请重新登录";
        }
        String lower = originalMsg.toLowerCase();
        if (lower.contains("session") || originalMsg.contains("会话")) {
            return code == CODE_60002 ? "请携带登录令牌（Header X-Wx-Token 或 body.token）" : "登录已过期，请重新登录";
        }
        return originalMsg;
    }

    private static String buildDetailMsg(int code, String originalMsg, String path) {
        String base = StringUtils.isNotEmpty(originalMsg) ? originalMsg : (code == CODE_60002 ? "请携带登录令牌" : "登录已过期，请重新登录");
        StringBuilder sb = new StringBuilder();
        sb.append(base);
        sb.append(" | path: ").append(path);
        if (code == CODE_60001) {
            sb.append(" | Check: 1) Header ").append(WxMaConstants.HEADER_TOKEN).append(" or body.").append(WxMaConstants.BODY_TOKEN_KEY).append("; 2) Token expired; 3) Re-login");
        } else if (code == CODE_60002) {
            sb.append(" | Check: Header ").append(WxMaConstants.HEADER_TOKEN).append(" or body.").append(WxMaConstants.BODY_TOKEN_KEY);
        }
        return sb.toString();
    }

    private static class BufferedResponseWrapper extends HttpServletResponseWrapper {
        private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        private PrintWriter writer;
        private ServletOutputStreamAdapter streamAdapter;

        BufferedResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            if (writer == null) {
                writer = new PrintWriter(new OutputStreamWriter(buffer, StandardCharsets.UTF_8));
            }
            return writer;
        }

        @Override
        public jakarta.servlet.ServletOutputStream getOutputStream() throws IOException {
            if (streamAdapter == null) {
                streamAdapter = new ServletOutputStreamAdapter(buffer);
            }
            return streamAdapter;
        }

        byte[] getContent() throws IOException {
            if (writer != null) {
                writer.flush();
            }
            return buffer.toByteArray();
        }
    }

    private static class ServletOutputStreamAdapter extends jakarta.servlet.ServletOutputStream {
        private final ByteArrayOutputStream out;

        ServletOutputStreamAdapter(ByteArrayOutputStream out) {
            this.out = out;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(jakarta.servlet.WriteListener listener) {}

        @Override
        public void write(int b) {
            out.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) {
            out.write(b, off, len);
        }
    }
}
