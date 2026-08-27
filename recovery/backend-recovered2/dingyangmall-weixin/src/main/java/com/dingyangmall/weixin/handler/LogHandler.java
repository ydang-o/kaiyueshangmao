/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.handler;

import cn.hutool.core.util.StrUtil;
import com.dingyangmall.common.utils.http.HttpUtils;
import com.dingyangmall.weixin.handler.AbstractHandler;
import com.dingyangmall.weixin.utils.JsonUtils;
import java.util.Map;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

@Component
public class LogHandler
extends AbstractHandler {
    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) {
        this.logger.info("\n\u63a5\u6536\u5230\u8bf7\u6c42\u6d88\u606f\uff0c\u5185\u5bb9\uff1a{}", (Object)JsonUtils.toJson(wxMessage));
        if (wxMessage.getMsgType().equals("event") && (wxMessage.getEvent().equals("subscribe") || wxMessage.getEvent().equals("SCAN")) && wxMessage.getEventKey().contains("jl-wiki")) {
            String openId = wxMessage.getFromUser();
            String sceneStr = StrUtil.split((CharSequence)wxMessage.getEventKey(), ":")[1];
            String string = HttpUtils.sendPost("http://127.0.0.1:8083/dingyangmall-open/user", StrUtil.format((CharSequence)"openId={}&sceneStr={}", openId, sceneStr));
        }
        return null;
    }
}

