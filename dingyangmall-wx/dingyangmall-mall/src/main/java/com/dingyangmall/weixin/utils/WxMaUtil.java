package com.dingyangmall.weixin.utils;

import jakarta.servlet.http.HttpServletRequest;

public final class WxMaUtil {
    private WxMaUtil() {}

    public static String getAppId(HttpServletRequest request) {
        if (request == null) return null;
        String appId = request.getHeader("app-id");
        if (appId == null || appId.isBlank()) appId = request.getParameter("app-id");
        return appId;
    }
}
