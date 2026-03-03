package com.dingyangmall.web.core.filter;

import com.dingyangmall.common.utils.StringUtils;
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
import org.springframework.core.Ordered;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 对 /weixin/api/ma/* 的 60001/60002 等登录态错误响应做改写，不再只返回「登录超时，请重新登录」，
 * 而是返回带完整说明的报错信息，便于前端和排查。由 WxMaSecurityConfig 通过 FilterRegistrationBean 注册。
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
                String detailMsg = buildDetailMsg(code, originalMsg, path);
                map.put("msg", detailMsg);
                byte[] newBody = objectMapper.writeValueAsBytes(map);
                if (!resp.isCommitted()) {
                    resp.setContentType("application/json;charset=UTF-8");
                    resp.setContentLength(newBody.length);
                    resp.getOutputStream().write(newBody);
                }
                if (log.isDebugEnabled()) {
                    log.debug("[WxMa] 已改写 {} 响应: code={}, msg={}", path, code, detailMsg);
                }
                return;
            }
        } catch (Exception e) {
            log.trace("响应非 JSON 或改写失败: {}", e.getMessage());
        }
        if (!resp.isCommitted()) {
            resp.setContentLength(content.length);
            resp.getOutputStream().write(content);
        }
    }

    private static String buildDetailMsg(int code, String originalMsg, String path) {
        String base = StringUtils.isNotEmpty(originalMsg) ? originalMsg : (code == CODE_60002 ? "session不能为空" : "登录超时");
        StringBuilder sb = new StringBuilder();
        sb.append(base);
        sb.append(" | 接口: ").append(path);
        if (code == CODE_60001) {
            sb.append(" | 可能原因: 1) 请求未带 Header third-session/openid 或 body._thirdSession；2) Redis 中无该会话或已过期；3) 下游校验未识别到 memberId");
        } else if (code == CODE_60002) {
            sb.append(" | 可能原因: 请求未携带 third-session 或 body._thirdSession");
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
