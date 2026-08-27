/*
 * Decompiled with CFR.
 */
package com.dingyangmall.common.exception.user;

import com.dingyangmall.common.exception.user.UserException;

public class UserNotExistsException
extends UserException {
    private static final long serialVersionUID = 1L;

    public UserNotExistsException() {
        super("user.not.exists", (Object[])null);
    }
}

