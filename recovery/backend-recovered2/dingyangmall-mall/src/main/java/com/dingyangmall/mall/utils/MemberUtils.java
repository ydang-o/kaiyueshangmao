/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.utils;

import com.dingyangmall.common.utils.SecurityUtils;
import com.dingyangmall.common.utils.ServletUtils;
import com.dingyangmall.common.utils.StringUtils;
import jakarta.servlet.http.HttpServletRequest;

public class MemberUtils {
    public static final String ATTR_MEMBER_ID = "memberId";

    public static String getMemberId() {
        try {
            Long userId = SecurityUtils.getUserId();
            if (userId != null) {
                return String.valueOf(userId);
            }
        }
        catch (Exception userId) {
            // empty catch block
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
        }
        catch (Exception exception) {
            // empty catch block
        }
        return null;
    }
}

