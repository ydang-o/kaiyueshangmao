/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderInfoEnum implements IEnum<String>
{
    STATUS_0("0", "\u5f85\u4ed8\u6b3e"),
    STATUS_1("1", "\u5f85\u53d1\u8d27"),
    STATUS_2("2", "\u5f85\u6536\u8d27"),
    STATUS_3("3", "\u5df2\u5b8c\u6210"),
    STATUS_5("5", "\u5df2\u53d6\u6d88");

    public static String STATUS_PREFIX;
    private String value;
    private String desc;

    private OrderInfoEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @JsonValue
    public String getDesc() {
        return this.desc;
    }

    static {
        STATUS_PREFIX = "STATUS";
    }
}

