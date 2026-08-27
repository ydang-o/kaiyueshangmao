/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.web.exception;

import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.core.text.Convert;
import com.dingyangmall.common.exception.DemoModeException;
import com.dingyangmall.common.exception.ServiceException;
import com.dingyangmall.common.exception.user.CaptchaException;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.common.utils.html.EscapeUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value={AccessDeniedException.class})
    public AjaxResult handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("\u8bf7\u6c42\u5730\u5740'{}',\u6743\u9650\u6821\u9a8c\u5931\u8d25'{}'", (Object)requestURI, (Object)e.getMessage());
        return AjaxResult.error(403, "\u6ca1\u6709\u6743\u9650\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458\u6388\u6743");
    }

    @ExceptionHandler(value={HttpRequestMethodNotSupportedException.class})
    public AjaxResult handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("\u8bf7\u6c42\u5730\u5740'{}',\u4e0d\u652f\u6301'{}'\u8bf7\u6c42", (Object)requestURI, (Object)e.getMethod());
        return AjaxResult.error(e.getMessage());
    }

    @ExceptionHandler(value={ServiceException.class})
    public AjaxResult handleServiceException(ServiceException e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        Integer code = e.getCode();
        return StringUtils.isNotNull(code) ? AjaxResult.error(code, e.getMessage()) : AjaxResult.error(e.getMessage());
    }

    @ExceptionHandler(value={MissingPathVariableException.class})
    public AjaxResult handleMissingPathVariableException(MissingPathVariableException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("\u8bf7\u6c42\u8def\u5f84\u4e2d\u7f3a\u5c11\u5fc5\u9700\u7684\u8def\u5f84\u53d8\u91cf'{}',\u53d1\u751f\u7cfb\u7edf\u5f02\u5e38.", (Object)requestURI, (Object)e);
        return AjaxResult.error(String.format("\u8bf7\u6c42\u8def\u5f84\u4e2d\u7f3a\u5c11\u5fc5\u9700\u7684\u8def\u5f84\u53d8\u91cf[%s]", e.getVariableName()));
    }

    @ExceptionHandler(value={MethodArgumentTypeMismatchException.class})
    public AjaxResult handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String value = Convert.toStr(e.getValue());
        if (StringUtils.isNotEmpty(value)) {
            value = EscapeUtil.clean(value);
        }
        log.error("\u8bf7\u6c42\u53c2\u6570\u7c7b\u578b\u4e0d\u5339\u914d'{}',\u53d1\u751f\u7cfb\u7edf\u5f02\u5e38.", (Object)requestURI, (Object)e);
        return AjaxResult.error(String.format("\u8bf7\u6c42\u53c2\u6570\u7c7b\u578b\u4e0d\u5339\u914d\uff0c\u53c2\u6570[%s]\u8981\u6c42\u7c7b\u578b\u4e3a\uff1a'%s'\uff0c\u4f46\u8f93\u5165\u503c\u4e3a\uff1a'%s'", e.getName(), e.getRequiredType().getName(), value));
    }

    @ExceptionHandler(value={CaptchaException.class})
    public AjaxResult handleCaptchaException(CaptchaException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.warn("\u8bf7\u6c42\u5730\u5740'{}',\u9a8c\u8bc1\u7801\u6821\u9a8c\u5931\u8d25", (Object)requestURI);
        return AjaxResult.error("\u9a8c\u8bc1\u7801\u9519\u8bef");
    }

    @ExceptionHandler(value={RuntimeException.class})
    public AjaxResult handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("\u8bf7\u6c42\u5730\u5740'{}',\u53d1\u751f\u672a\u77e5\u5f02\u5e38.", (Object)requestURI, (Object)e);
        return AjaxResult.error(500, "\u7cfb\u7edf\u7e41\u5fd9\uff0c\u8bf7\u7a0d\u540e\u518d\u8bd5");
    }

    @ExceptionHandler(value={Exception.class})
    public AjaxResult handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("\u8bf7\u6c42\u5730\u5740'{}',\u53d1\u751f\u7cfb\u7edf\u5f02\u5e38.", (Object)requestURI, (Object)e);
        return AjaxResult.error(500, "\u7cfb\u7edf\u7e41\u5fd9\uff0c\u8bf7\u7a0d\u540e\u518d\u8bd5");
    }

    @ExceptionHandler(value={BindException.class})
    public AjaxResult handleBindException(BindException e) {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return AjaxResult.error(message);
    }

    @ExceptionHandler(value={MethodArgumentNotValidException.class})
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return AjaxResult.error(message);
    }

    @ExceptionHandler(value={DemoModeException.class})
    public AjaxResult handleDemoModeException(DemoModeException e) {
        return AjaxResult.error("\u6f14\u793a\u6a21\u5f0f\uff0c\u4e0d\u5141\u8bb8\u64cd\u4f5c");
    }
}

