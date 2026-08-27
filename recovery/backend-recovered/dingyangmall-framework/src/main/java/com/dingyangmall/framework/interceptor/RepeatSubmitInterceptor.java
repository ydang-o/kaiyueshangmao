/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.interceptor;

import com.alibaba.fastjson2.JSON;
import com.dingyangmall.common.annotation.RepeatSubmit;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.utils.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public abstract class RepeatSubmitInterceptor
implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            Method method = handlerMethod.getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            if (annotation != null && this.isRepeatSubmit(request, annotation)) {
                AjaxResult ajaxResult = AjaxResult.error(annotation.message());
                ServletUtils.renderString(response, JSON.toJSONString(ajaxResult));
                return false;
            }
            return true;
        }
        return true;
    }

    public abstract boolean isRepeatSubmit(HttpServletRequest var1, RepeatSubmit var2);
}

