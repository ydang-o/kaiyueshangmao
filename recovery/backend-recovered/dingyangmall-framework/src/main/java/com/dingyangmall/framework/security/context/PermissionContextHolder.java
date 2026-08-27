/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.security.context;

import com.dingyangmall.common.core.text.Convert;
import org.springframework.web.context.request.RequestContextHolder;

public class PermissionContextHolder {
    private static final String PERMISSION_CONTEXT_ATTRIBUTES = "PERMISSION_CONTEXT";

    public static void setContext(String permission) {
        RequestContextHolder.currentRequestAttributes().setAttribute(PERMISSION_CONTEXT_ATTRIBUTES, permission, 0);
    }

    public static String getContext() {
        return Convert.toStr(RequestContextHolder.currentRequestAttributes().getAttribute(PERMISSION_CONTEXT_ATTRIBUTES, 0));
    }
}

