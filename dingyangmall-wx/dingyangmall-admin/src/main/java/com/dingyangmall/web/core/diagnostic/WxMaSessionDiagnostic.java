package com.dingyangmall.web.core.diagnostic;

/**
 * 用于 60001 排查：在进入 Controller 前记录会话诊断信息，60001 改写时输出便于定位「下游哪一环未识别」。
 */
public final class WxMaSessionDiagnostic {

    private static final ThreadLocal<String> MESSAGE = new ThreadLocal<>();

    public static void set(String msg) {
        MESSAGE.set(msg);
    }

    public static String get() {
        return MESSAGE.get();
    }

    public static void clear() {
        MESSAGE.remove();
    }

    private WxMaSessionDiagnostic() {}
}
