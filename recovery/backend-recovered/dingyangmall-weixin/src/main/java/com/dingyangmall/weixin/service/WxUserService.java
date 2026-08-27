/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dingyangmall.weixin.entity.WxOpenDataDTO;
import com.dingyangmall.weixin.entity.WxUser;
import me.chanjar.weixin.common.error.WxErrorException;

public interface WxUserService
extends IService<WxUser> {
    public void synchroWxUser() throws WxErrorException;

    public boolean updateRemark(WxUser var1) throws WxErrorException;

    public void tagging(String var1, Long var2, String[] var3) throws WxErrorException;

    public WxUser getByOpenId(String var1);

    public WxUser loginMa(String var1, String var2) throws WxErrorException;

    public WxUser saveOrUptateWxUser(WxOpenDataDTO var1);
}

