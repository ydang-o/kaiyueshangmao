/*
 * Decompiled with CFR.
 */
package com.dingyangmall.common.exception.user;

import com.dingyangmall.common.exception.user.UserException;

public class BlackListException
extends UserException {
    private static final long serialVersionUID = 1L;

    public BlackListException() {
        super("login.blocked", (Object[])null);
    }
}

