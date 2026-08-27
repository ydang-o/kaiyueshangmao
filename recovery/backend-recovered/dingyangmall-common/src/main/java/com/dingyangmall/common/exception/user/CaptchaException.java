/*
 * Decompiled with CFR.
 */
package com.dingyangmall.common.exception.user;

import com.dingyangmall.common.exception.user.UserException;

public class CaptchaException
extends UserException {
    private static final long serialVersionUID = 1L;

    public CaptchaException() {
        super("user.jcaptcha.error", (Object[])null);
    }
}

