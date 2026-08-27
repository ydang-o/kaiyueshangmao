/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.dingyangmall.common.utils.StringUtils
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  jakarta.servlet.Filter
 *  jakarta.servlet.FilterChain
 *  jakarta.servlet.ServletException
 *  jakarta.servlet.ServletOutputStream
 *  jakarta.servlet.ServletRequest
 *  jakarta.servlet.ServletResponse
 *  jakarta.servlet.WriteListener
 *  jakarta.servlet.http.HttpServletRequest
 *  jakarta.servlet.http.HttpServletResponse
 *  jakarta.servlet.http.HttpServletResponseWrapper
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.dingyangmall.web.core.filter;

import com.dingyangmall.common.utils.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WxMaErrorRewriteFilter
implements Filter {
    private static final Logger log = LoggerFactory.getLogger(WxMaErrorRewriteFilter.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String WX_MA_PATH = "/weixin/api/ma/";
    private static final int CODE_60001 = 60001;
    private static final int CODE_60002 = 60002;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;
        String path = req.getRequestURI();
        if (!path.contains(WX_MA_PATH) || path.endsWith("wxuser/login")) {
            chain.doFilter(request, response);
            return;
        }
        BufferedResponseWrapper wrapper = new BufferedResponseWrapper(resp);
        chain.doFilter(request, (ServletResponse)wrapper);
        byte[] content = wrapper.getContent();
        if (content == null || content.length == 0) {
            return;
        }
        String body = new String(content, StandardCharsets.UTF_8);
        try {
            int code;
            Map map = (Map)objectMapper.readValue(body, Map.class);
            Object codeObj = map.get("code");
            int n = code = codeObj instanceof Number ? ((Number)codeObj).intValue() : -1;
            if (code == 60001 || code == 60002) {
                String originalMsg = map.get("msg") != null ? String.valueOf(map.get("msg")) : "";
                String msgForUser = WxMaErrorRewriteFilter.toTokenModeMessage(code, originalMsg);
                String detailMsg = WxMaErrorRewriteFilter.buildDetailMsg(code, msgForUser, path);
                map.put("msg", detailMsg);
                byte[] newBody = objectMapper.writeValueAsBytes((Object)map);
                if (!resp.isCommitted()) {
                    resp.setContentType("application/json;charset=UTF-8");
                    resp.setContentLength(newBody.length);
                    resp.getOutputStream().write(newBody);
                }
                if (log.isDebugEnabled()) {
                    log.debug("[WxMa] 60001/60002 response path={} code={} token present={}", new Object[]{path, code, StringUtils.isNotEmpty((String)req.getHeader("X-Wx-Token"))});
                }
                return;
            }
        }
        catch (Exception e) {
            log.trace("Response not JSON or rewrite failed: {}", (Object)e.getMessage());
        }
        if (!resp.isCommitted()) {
            resp.setContentLength(content.length);
            resp.getOutputStream().write(content);
        }
    }

    private static String toTokenModeMessage(int code, String originalMsg) {
        if (StringUtils.isEmpty((String)originalMsg)) {
            return code == 60002 ? "\u8bf7\u643a\u5e26\u767b\u5f55\u4ee4\u724c" : "\u767b\u5f55\u5df2\u8fc7\u671f\uff0c\u8bf7\u91cd\u65b0\u767b\u5f55";
        }
        String lower = originalMsg.toLowerCase();
        if (lower.contains("session") || originalMsg.contains("\u4f1a\u8bdd")) {
            return code == 60002 ? "\u8bf7\u643a\u5e26\u767b\u5f55\u4ee4\u724c\uff08Header X-Wx-Token \u6216 body.token\uff09" : "\u767b\u5f55\u5df2\u8fc7\u671f\uff0c\u8bf7\u91cd\u65b0\u767b\u5f55";
        }
        return originalMsg;
    }

    private static String buildDetailMsg(int code, String originalMsg, String path) {
        String base = StringUtils.isNotEmpty((String)originalMsg) ? originalMsg : (code == 60002 ? "\u8bf7\u643a\u5e26\u767b\u5f55\u4ee4\u724c" : "\u767b\u5f55\u5df2\u8fc7\u671f\uff0c\u8bf7\u91cd\u65b0\u767b\u5f55");
        StringBuilder sb = new StringBuilder();
        sb.append(base);
        sb.append(" | path: ").append(path);
        if (code == 60001) {
            sb.append(" | Check: 1) Header ").append("X-Wx-Token").append(" or body.").append("token").append("; 2) Token expired; 3) Re-login");
        } else if (code == 60002) {
            sb.append(" | Check: Header ").append("X-Wx-Token").append(" or body.").append("token");
        }
        return sb.toString();
    }

    private static class BufferedResponseWrapper
    extends HttpServletResponseWrapper {
        private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        private PrintWriter writer;
        private ServletOutputStreamAdapter streamAdapter;

        BufferedResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        public PrintWriter getWriter() throws IOException {
            if (this.writer == null) {
                this.writer = new PrintWriter(new OutputStreamWriter((OutputStream)this.buffer, StandardCharsets.UTF_8));
            }
            return this.writer;
        }

        public ServletOutputStream getOutputStream() throws IOException {
            if (this.streamAdapter == null) {
                this.streamAdapter = new ServletOutputStreamAdapter(this.buffer);
            }
            return this.streamAdapter;
        }

        byte[] getContent() throws IOException {
            if (this.writer != null) {
                this.writer.flush();
            }
            return this.buffer.toByteArray();
        }
    }

    private static class ServletOutputStreamAdapter
    extends ServletOutputStream {
        private final ByteArrayOutputStream out;

        ServletOutputStreamAdapter(ByteArrayOutputStream out) {
            this.out = out;
        }

        public boolean isReady() {
            return true;
        }

        public void setWriteListener(WriteListener listener) {
        }

        public void write(int b) {
            this.out.write(b);
        }

        public void write(byte[] b, int off, int len) {
            this.out.write(b, off, len);
        }
    }
}

