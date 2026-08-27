/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.utils;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.dingyangmall.weixin.entity.ThirdSession;
import lombok.Generated;

public final class ThirdSessionHolder {
    private static final ThreadLocal<ThirdSession> THREAD_LOCAL_THIRD_SESSION = new TransmittableThreadLocal<ThirdSession>();

    public static void setThirdSession(ThirdSession thirdSession) {
        THREAD_LOCAL_THIRD_SESSION.set(thirdSession);
    }

    public static ThirdSession getThirdSession() {
        return THREAD_LOCAL_THIRD_SESSION.get();
    }

    public static void clear() {
        THREAD_LOCAL_THIRD_SESSION.remove();
    }

    public static String getWxUserId() {
        return THREAD_LOCAL_THIRD_SESSION.get().getWxUserId();
    }

    @Generated
    private ThirdSessionHolder() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

