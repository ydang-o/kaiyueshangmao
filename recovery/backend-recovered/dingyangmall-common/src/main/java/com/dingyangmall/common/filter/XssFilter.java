/*
 * Decompiled with CFR.
 */
package com.dingyangmall.common.filter;

import com.dingyangmall.common.enums.HttpMethod;
import com.dingyangmall.common.filter.XssHttpServletRequestWrapper;
import com.dingyangmall.common.utils.StringUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XssFilter
implements Filter {
    public List<String> excludes = new ArrayList<String>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String tempExcludes = filterConfig.getInitParameter("excludes");
        if (StringUtils.isNotEmpty(tempExcludes)) {
            String[] urls;
            for (String url : urls = tempExcludes.split(",")) {
                this.excludes.add(url);
            }
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;
        if (this.handleExcludeURL(req, resp)) {
            chain.doFilter(request, response);
            return;
        }
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest)request);
        chain.doFilter(xssRequest, response);
    }

    private boolean handleExcludeURL(HttpServletRequest request, HttpServletResponse response) {
        String url = request.getServletPath();
        String method = request.getMethod();
        if (method == null || HttpMethod.GET.matches(method) || HttpMethod.DELETE.matches(method)) {
            return true;
        }
        return StringUtils.matches(url, this.excludes);
    }

    @Override
    public void destroy() {
    }
}

