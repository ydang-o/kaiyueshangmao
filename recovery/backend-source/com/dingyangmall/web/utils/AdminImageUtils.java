/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.dingyangmall.mall.entity.GoodsSpu
 *  org.springframework.util.StringUtils
 */
package com.dingyangmall.web.utils;

import com.dingyangmall.mall.entity.GoodsSpu;
import org.springframework.util.StringUtils;

public final class AdminImageUtils {
    private static final String PROFILE_PREFIX = "/profile/";
    private static final String DEFAULT_LOGO = "/profile/static/logo.png";
    private static final String ADMIN_API_PREFIX = "/dev-api";

    public static boolean isLocalPath(String url) {
        if (!StringUtils.hasText((String)url)) {
            return false;
        }
        String trimUrl = url.trim();
        if (trimUrl.startsWith(PROFILE_PREFIX) || trimUrl.startsWith("/dev-api/profile/")) {
            return true;
        }
        return trimUrl.contains(PROFILE_PREFIX);
    }

    public static String toLocalOrDefault(String url) {
        if (!StringUtils.hasText((String)url)) {
            return AdminImageUtils.toAdminDisplayUrl(DEFAULT_LOGO);
        }
        String path = AdminImageUtils.extractProfilePath(url);
        if (path != null) {
            return AdminImageUtils.toAdminDisplayUrl(path);
        }
        return AdminImageUtils.toAdminDisplayUrl(DEFAULT_LOGO);
    }

    public static String toAdminDisplayUrl(String url) {
        if (!StringUtils.hasText((String)url)) {
            return "/dev-api/profile/static/logo.png";
        }
        String u = url.trim();
        if (u.startsWith(ADMIN_API_PREFIX)) {
            return u;
        }
        if (u.startsWith(PROFILE_PREFIX)) {
            return ADMIN_API_PREFIX + u;
        }
        return "/dev-api/profile/static/logo.png";
    }

    private static String extractProfilePath(String url) {
        if (!StringUtils.hasText((String)url)) {
            return null;
        }
        String u = url.trim();
        if (u.startsWith(PROFILE_PREFIX)) {
            return u;
        }
        if (u.startsWith("/dev-api/profile/")) {
            return u.substring(ADMIN_API_PREFIX.length());
        }
        int idx = u.indexOf(PROFILE_PREFIX);
        if (idx >= 0) {
            return u.substring(idx);
        }
        return null;
    }

    public static String[] toLocalOrDefaultPicUrls(String[] picUrls) {
        if (picUrls == null || picUrls.length == 0) {
            return new String[]{AdminImageUtils.toAdminDisplayUrl(DEFAULT_LOGO)};
        }
        String[] out = new String[picUrls.length];
        for (int i = 0; i < picUrls.length; ++i) {
            out[i] = AdminImageUtils.toLocalOrDefault(picUrls[i]);
        }
        return out;
    }

    public static void normalizeGoodsSpuPicUrls(GoodsSpu spu) {
        if (spu == null) {
            return;
        }
        spu.setPicUrls(AdminImageUtils.toLocalOrDefaultPicUrls(spu.getPicUrls()));
    }
}

