/*
 * Decompiled with CFR.
 */
package com.dingyangmall.common.utils;

public class LogUtils {
    public static String getBlock(Object msg) {
        if (msg == null) {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }
}

