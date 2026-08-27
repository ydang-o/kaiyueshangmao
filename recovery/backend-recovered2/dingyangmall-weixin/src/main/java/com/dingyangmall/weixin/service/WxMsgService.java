/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dingyangmall.weixin.entity.WxMsg;
import com.dingyangmall.weixin.entity.WxMsgVO;
import java.util.List;

public interface WxMsgService
extends IService<WxMsg> {
    public IPage<List<WxMsgVO>> listWxMsgMapGroup(Page var1, WxMsgVO var2);
}

