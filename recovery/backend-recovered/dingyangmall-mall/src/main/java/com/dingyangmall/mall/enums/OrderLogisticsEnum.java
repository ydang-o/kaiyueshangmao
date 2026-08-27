/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum OrderLogisticsEnum implements IEnum<String>
{
    STATUS_ER("ER", "\u9519\u8bef"),
    STATUS_0("0", "\u5728\u9014"),
    STATUS_1("1", "\u63fd\u6536\u4e2d"),
    STATUS_2("2", "\u7591\u96be"),
    STATUS_3("3", "\u5df2\u7b7e\u6536"),
    STATUS_4("4", "\u9000\u7b7e"),
    STATUS_5("5", "\u6d3e\u4ef6\u4e2d"),
    STATUS_6("6", "\u9000\u56de"),
    STATUS_7("7", "\u8f6c\u6295"),
    LOGISTICS_TIANTIAN("tiantian", "\u5929\u5929\u5feb\u9012"),
    LOGISTICS_HUITONGKUAIDI("huitongkuaidi", "\u767e\u4e16\u5feb\u9012"),
    LOGISTICS_YUNDA("yunda", "\u97f5\u8fbe\u5feb\u9012"),
    LOGISTICS_YUANTONG("yuantong", "\u5706\u901a\u901f\u9012"),
    LOGISTICS_DEBANGWULIU("debangwuliu", "\u5fb7\u90a6"),
    LOGISTICS_EMS("ems", "EMS"),
    LOGISTICS_SHUNFENG("shunfeng", "\u987a\u4e30\u901f\u8fd0"),
    LOGISTICS_ZHONGTONG("zhongtong", "\u4e2d\u901a\u5feb\u9012"),
    LOGISTICS_SHENTONG("shentong", "\u7533\u901a\u5feb\u9012");

    public static String LOGISTICS_PREFIX;
    public static String STATUS_PREFIX;
    private String value;
    private String desc;

    private OrderLogisticsEnum(String value, String desc) {
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

    public static List<Map<String, String>> queryAll(String prefix) {
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (OrderLogisticsEnum t : OrderLogisticsEnum.values()) {
            if (!t.name().contains(prefix)) continue;
            HashMap<String, String> mp = new HashMap<String, String>();
            mp.put("value", t.getValue());
            mp.put("label", t.getDesc());
            list.add(mp);
        }
        return list;
    }

    static {
        LOGISTICS_PREFIX = "LOGISTICS";
        STATUS_PREFIX = "STATUS";
    }
}

