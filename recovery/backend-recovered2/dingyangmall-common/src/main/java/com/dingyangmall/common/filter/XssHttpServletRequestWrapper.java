/*
 * Decompiled with CFR.
 */
package com.dingyangmall.common.filter;

import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.common.utils.html.EscapeUtil;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

public class XssHttpServletRequestWrapper
extends HttpServletRequestWrapper {
    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            int length = values.length;
            String[] escapesValues = new String[length];
            for (int i = 0; i < length; ++i) {
                escapesValues[i] = EscapeUtil.clean(values[i]).trim();
            }
            return escapesValues;
        }
        return super.getParameterValues(name);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (!this.isJsonRequest()) {
            return super.getInputStream();
        }
        String json = IOUtils.toString((InputStream)super.getInputStream(), "utf-8");
        if (StringUtils.isEmpty(json)) {
            return super.getInputStream();
        }
        json = EscapeUtil.clean(json).trim();
        final byte[] jsonBytes = json.getBytes("utf-8");
        final ByteArrayInputStream bis = new ByteArrayInputStream(jsonBytes);
        return new ServletInputStream(){

            @Override
            public boolean isFinished() {
                return true;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public int available() throws IOException {
                return jsonBytes.length;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public int read() throws IOException {
                return bis.read();
            }
        };
    }

    public boolean isJsonRequest() {
        String header = super.getHeader("Content-Type");
        return StringUtils.startsWithIgnoreCase(header, "application/json");
    }
}

