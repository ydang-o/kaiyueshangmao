/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.utils;

import jakarta.servlet.http.HttpServletRequest;

public class WxMaUtil {
    public static String getAppId(HttpServletRequest request) {
        String appId = request.getHeader("app-id");
        return appId;
    }
}

