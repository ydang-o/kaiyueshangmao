/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.constant;

public enum MallReturnCode {
    ERR_70001("70001", "\u64cd\u4f5c\u4e0d\u5141\u8bb8"),
    ERR_70003("70003", "\u7528\u6237\u4e0d\u5b58\u5728"),
    ERR_70005("70005", "\u8ba2\u5355\u4e0d\u5b58\u5728"),
    ERR_70007("70007", "\u5bc6\u7801\u9519\u8bef");

    private final String code;
    private final String msg;

    private MallReturnCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}

