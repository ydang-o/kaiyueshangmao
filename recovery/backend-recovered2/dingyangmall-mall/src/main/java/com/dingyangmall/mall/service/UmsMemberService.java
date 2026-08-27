/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dingyangmall.mall.entity.UmsMember;

public interface UmsMemberService
extends IService<UmsMember> {
    public UmsMember getByMemberCode(String var1);

    public UmsMember getByPhone(String var1);

    public UmsMember getOrCreateByPhone(String var1, String var2, String var3);

    public UmsMember getOrCreateByOpenid(String var1, String var2, String var3);
}

