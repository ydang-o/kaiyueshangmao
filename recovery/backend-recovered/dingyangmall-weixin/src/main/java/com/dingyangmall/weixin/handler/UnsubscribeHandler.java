/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.handler;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dingyangmall.weixin.entity.WxUser;
import com.dingyangmall.weixin.handler.AbstractHandler;
import com.dingyangmall.weixin.handler.MsgHandler;
import com.dingyangmall.weixin.mapper.WxUserMapper;
import com.dingyangmall.weixin.service.WxMsgService;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Generated;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UnsubscribeHandler
extends AbstractHandler {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(UnsubscribeHandler.class);
    private final WxMsgService wxMsgService;
    private final WxUserMapper wxUserMapper;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) {
        String openId = wxMessage.getFromUser();
        log.info("\u53d6\u6d88\u5173\u6ce8\u7528\u6237 OPENID: " + openId);
        WxUser wxUser = (WxUser)this.wxUserMapper.selectOne((Wrapper)Wrappers.lambdaQuery().eq(WxUser::getOpenId, openId));
        if (wxUser != null) {
            wxUser.setSubscribe("0");
            wxUser.setCancelSubscribeTime(LocalDateTime.now());
            this.wxUserMapper.updateById(wxUser);
            MsgHandler.getWxMpXmlOutMessage(wxMessage, null, wxUser, this.wxMsgService);
        }
        return null;
    }

    @Generated
    public UnsubscribeHandler(WxMsgService wxMsgService, WxUserMapper wxUserMapper) {
        this.wxMsgService = wxMsgService;
        this.wxUserMapper = wxUserMapper;
    }
}

