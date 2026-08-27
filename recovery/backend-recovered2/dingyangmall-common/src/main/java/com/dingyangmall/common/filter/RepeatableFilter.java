/*
 * Decompiled with CFR.
 */
package com.dingyangmall.common.filter;

import com.dingyangmall.common.filter.RepeatedlyRequestWrapper;
import com.dingyangmall.common.utils.StringUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RepeatableFilter
implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        RepeatedlyRequestWrapper requestWrapper = null;
        if (request instanceof HttpServletRequest && StringUtils.startsWithIgnoreCase(request.getContentType(), "application/json")) {
            requestWrapper = new RepeatedlyRequestWrapper((HttpServletRequest)request, response);
        }
        if (null == requestWrapper) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(requestWrapper, response);
        }
    }

    @Override
    public void destroy() {
    }
}

