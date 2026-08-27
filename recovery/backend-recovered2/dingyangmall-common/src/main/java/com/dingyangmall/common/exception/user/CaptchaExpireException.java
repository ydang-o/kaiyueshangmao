/*
 * Decompiled with CFR.
 */
package com.dingyangmall.common.exception.user;

import com.dingyangmall.common.exception.user.UserException;

public class CaptchaExpireException
extends UserException {
    private static final long serialVersionUID = 1L;

    public CaptchaExpireException() {
        super("user.jcaptcha.expire", (Object[])null);
    }
}

