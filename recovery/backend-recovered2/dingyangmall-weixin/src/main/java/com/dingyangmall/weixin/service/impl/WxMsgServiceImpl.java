/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyangmall.weixin.entity.WxMsg;
import com.dingyangmall.weixin.entity.WxMsgVO;
import com.dingyangmall.weixin.mapper.WxMsgMapper;
import com.dingyangmall.weixin.service.WxMsgService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WxMsgServiceImpl
extends ServiceImpl<WxMsgMapper, WxMsg>
implements WxMsgService {
    @Override
    public IPage<List<WxMsgVO>> listWxMsgMapGroup(Page page, WxMsgVO wxMsgVO) {
        return ((WxMsgMapper)this.baseMapper).listWxMsgMapGroup(page, wxMsgVO);
    }
}

