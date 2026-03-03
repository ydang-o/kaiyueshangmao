package com.dingyangmall.mall.utils;

import com.dingyangmall.common.utils.ServletUtils;
import com.dingyangmall.common.utils.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

public class MemberUtils {
    
    /** 小程序端通过请求头 X-Wx-Session 解析后写入的会员 ID（openid），与 WxMaMemberFilter.ATTR_MEMBER_ID 一致 */
    public static final String ATTR_MEMBER_ID = "memberId";

    /**
     * 获取当前登录会员ID
     * 优先级：SecurityUtils(JWT) > request 属性 memberId(小程序 X-Wx-Session 解析) > Header member-id
     */
    public static String getMemberId() {
        try {
            Long userId = com.dingyangmall.common.utils.SecurityUtils.getUserId();
            if (userId != null) {
                return String.valueOf(userId);
            }
        } catch (Exception e) {
            // ignore
        }

        try {
            HttpServletRequest request = ServletUtils.getRequest();
            Object attr = request.getAttribute(ATTR_MEMBER_ID);
            if (attr != null && StringUtils.isNotEmpty(attr.toString())) {
                return attr.toString();
            }
            String memberId = request.getHeader("member-id");
            if (StringUtils.isNotEmpty(memberId)) {
                return memberId;
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }
}
