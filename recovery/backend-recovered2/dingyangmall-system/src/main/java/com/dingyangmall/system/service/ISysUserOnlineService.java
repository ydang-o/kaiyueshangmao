/*
 * Decompiled with CFR.
 */
package com.dingyangmall.system.service;

import com.dingyangmall.common.core.domain.model.LoginUser;
import com.dingyangmall.system.domain.SysUserOnline;

public interface ISysUserOnlineService {
    public SysUserOnline selectOnlineByIpaddr(String var1, LoginUser var2);

    public SysUserOnline selectOnlineByUserName(String var1, LoginUser var2);

    public SysUserOnline selectOnlineByInfo(String var1, String var2, LoginUser var3);

    public SysUserOnline loginUserToUserOnline(LoginUser var1);
}

