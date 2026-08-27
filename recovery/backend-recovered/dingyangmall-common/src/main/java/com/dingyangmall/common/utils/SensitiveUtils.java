/*
 * Decompiled with CFR.
 */
package com.dingyangmall.common.utils;

import cn.hutool.core.util.StrUtil;

public class SensitiveUtils {
    public static final String DEFAULT_PAD_STR = "*";

    public static String process(String data) {
        return SensitiveUtils.process(data, 2, 1, DEFAULT_PAD_STR);
    }

    public static String process(String data, Integer leftLen, Integer rightLen) {
        return SensitiveUtils.process(data, leftLen, rightLen, DEFAULT_PAD_STR);
    }

    public static String process(String originStr, int prefixNoMaskLen, int suffixNoMaskLen, String maskStr) {
        if (originStr == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int n = originStr.length();
        for (int i = 0; i < n; ++i) {
            if (i < prefixNoMaskLen) {
                sb.append(originStr.charAt(i));
                continue;
            }
            if (i > n - suffixNoMaskLen - 1) {
                sb.append(originStr.charAt(i));
                continue;
            }
            sb.append(maskStr);
        }
        return sb.toString();
    }

    public static String chineseName(String fullName) {
        if (fullName == null) {
            return null;
        }
        return SensitiveUtils.process(fullName, 0, 1, DEFAULT_PAD_STR);
    }

    public static String mobilePhone(String num) {
        return SensitiveUtils.process(num, 0, 4, DEFAULT_PAD_STR);
    }

    public static String address(String address) {
        return SensitiveUtils.process(address, 6, 0, DEFAULT_PAD_STR);
    }

    public static String email(String email) {
        if (email == null) {
            return null;
        }
        int index = StrUtil.indexOf(email, '@');
        if (index <= 1) {
            return email;
        }
        String preEmail = SensitiveUtils.process(email.substring(0, index), 1, 0, DEFAULT_PAD_STR);
        return preEmail + email.substring(index);
    }

    public static String password(String password) {
        if (password == null) {
            return null;
        }
        return "******";
    }

    public static String key(String key) {
        if (key == null) {
            return null;
        }
        int viewLength = 6;
        StringBuilder tmpKey = new StringBuilder(SensitiveUtils.process(key, 0, 3, DEFAULT_PAD_STR));
        if (tmpKey.length() > viewLength) {
            return tmpKey.substring(tmpKey.length() - viewLength);
        }
        if (tmpKey.length() < viewLength) {
            int buffLength = viewLength - tmpKey.length();
            for (int i = 0; i < buffLength; ++i) {
                tmpKey.insert(0, DEFAULT_PAD_STR);
            }
            return tmpKey.toString();
        }
        return tmpKey.toString();
    }

    public static void main(String[] args) {
        String s = SensitiveUtils.mobilePhone("18653653621");
        System.out.println(s);
    }
}

