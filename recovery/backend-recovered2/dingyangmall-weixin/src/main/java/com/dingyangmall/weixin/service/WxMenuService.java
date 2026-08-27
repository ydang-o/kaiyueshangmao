/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dingyangmall.weixin.entity.WxMenu;
import me.chanjar.weixin.common.error.WxErrorException;

public interface WxMenuService
extends IService<WxMenu> {
    public String getWxMenuButton();

    public void saveAndRelease(String var1) throws WxErrorException;
}

