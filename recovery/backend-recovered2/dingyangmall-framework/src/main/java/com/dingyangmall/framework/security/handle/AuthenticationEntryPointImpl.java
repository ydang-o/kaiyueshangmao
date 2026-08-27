/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.security.handle;

import com.alibaba.fastjson2.JSON;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.utils.ServletUtils;
import com.dingyangmall.common.utils.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEntryPointImpl
implements AuthenticationEntryPoint,
Serializable {
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        int code = 401;
        String msg = StringUtils.format("\u8bf7\u6c42\u8bbf\u95ee\uff1a{}\uff0c\u8ba4\u8bc1\u5931\u8d25\uff0c\u65e0\u6cd5\u8bbf\u95ee\u7cfb\u7edf\u8d44\u6e90", request.getRequestURI());
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(code, msg)));
    }
}

