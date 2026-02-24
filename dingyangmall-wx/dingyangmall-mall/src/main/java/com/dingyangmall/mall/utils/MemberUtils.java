package com.dingyangmall.mall.utils;

import com.dingyangmall.common.utils.ServletUtils;
import com.dingyangmall.common.utils.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

public class MemberUtils {
    
    /**
     * 获取当前登录会员ID
     * 优先从Header中获取 member-id
     * 否则返回默认测试ID "1"
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
