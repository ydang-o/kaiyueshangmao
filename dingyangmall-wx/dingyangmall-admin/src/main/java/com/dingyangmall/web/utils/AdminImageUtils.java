package com.dingyangmall.web.utils;

import com.dingyangmall.common.constant.Constants;
import com.dingyangmall.mall.entity.GoodsSpu;
import org.springframework.util.StringUtils;

/**
 * 管理端图片 URL 处理：统一为可加载地址；非本地路径用默认 logo。
 * 返回给前端的 URL 统一带 /dev-api 前缀，便于管理端代理正确转发，解决照片无法加载。
 */
public final class AdminImageUtils {

    private static final String PROFILE_PREFIX = Constants.RESOURCE_PREFIX + "/";
    /** 默认 logo 相对路径 */
    private static final String DEFAULT_LOGO = "/profile/static/logo.png";
    /** 管理端请求图片时需带此前缀，才能被代理转发到后端 */
    private static final String ADMIN_API_PREFIX = "/dev-api";

    /**
     * 判断是否为本地路径（/profile/ 或 /dev-api/profile/ 开头，或含 profile/upload/）
     */
    public static boolean isLocalPath(String url) {
        if (!StringUtils.hasText(url)) return false;
        String trimUrl = url.trim();
        if (trimUrl.startsWith(PROFILE_PREFIX) || trimUrl.startsWith(ADMIN_API_PREFIX + PROFILE_PREFIX)) return true;
        // 兼容带域名的本地路径，如 http://localhost:7500/profile/upload/...
        if (trimUrl.contains(PROFILE_PREFIX)) return true;
        return false;
    }

    /**
     * 单张图：本地路径保留（并统一为管理端可访问形式），否则返回默认 logo
     */
    public static String toLocalOrDefault(String url) {
        if (!StringUtils.hasText(url)) return toAdminDisplayUrl(DEFAULT_LOGO);
        String path = extractProfilePath(url);
        if (path != null) return toAdminDisplayUrl(path);
        return toAdminDisplayUrl(DEFAULT_LOGO);
    }

    /**
     * 将 URL 转为管理端可加载的地址：统一为 /dev-api/profile/...，前端 img 请求会走代理
     */
    public static String toAdminDisplayUrl(String url) {
        if (!StringUtils.hasText(url)) return ADMIN_API_PREFIX + DEFAULT_LOGO;
        String u = url.trim();
        if (u.startsWith(ADMIN_API_PREFIX)) return u;
        if (u.startsWith(PROFILE_PREFIX)) return ADMIN_API_PREFIX + u;
        return ADMIN_API_PREFIX + DEFAULT_LOGO;
    }

    /**
     * 从任意 URL 中提取 /profile/ 开头的相对路径，便于统一为本地路径
     */
    private static String extractProfilePath(String url) {
        if (!StringUtils.hasText(url)) return null;
        String u = url.trim();
        if (u.startsWith(PROFILE_PREFIX)) return u;
        if (u.startsWith(ADMIN_API_PREFIX + PROFILE_PREFIX)) return u.substring(ADMIN_API_PREFIX.length());
        int idx = u.indexOf(PROFILE_PREFIX);
        if (idx >= 0) return u.substring(idx);
        return null;
    }

    /**
     * 将商品 picUrls 转为管理端可加载的地址：本地路径统一为 /dev-api/profile/...，外链用默认 logo
     */
    public static String[] toLocalOrDefaultPicUrls(String[] picUrls) {
        if (picUrls == null || picUrls.length == 0) {
            return new String[] { toAdminDisplayUrl(DEFAULT_LOGO) };
        }
        String[] out = new String[picUrls.length];
        for (int i = 0; i < picUrls.length; i++) {
            out[i] = toLocalOrDefault(picUrls[i]);
        }
        return out;
    }

    /**
     * 原地修正商品主图：管理端返回可加载的图片地址，解决照片无法加载
     */
    public static void normalizeGoodsSpuPicUrls(GoodsSpu spu) {
        if (spu == null) return;
        spu.setPicUrls(toLocalOrDefaultPicUrls(spu.getPicUrls()));
    }
}
