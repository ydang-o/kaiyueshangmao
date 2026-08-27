/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.weixin.entity.WxMsg;
import com.dingyangmall.weixin.entity.WxMsgVO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WxMsgMapper
extends BaseMapper<WxMsg> {
    public IPage<List<WxMsgVO>> listWxMsgMapGroup(Page var1, @Param(value="query") WxMsgVO var2);
}

