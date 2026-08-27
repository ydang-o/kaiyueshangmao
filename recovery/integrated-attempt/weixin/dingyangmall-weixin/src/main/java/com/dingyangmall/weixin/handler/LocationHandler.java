/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.handler;

import com.dingyangmall.weixin.builder.TextBuilder;
import com.dingyangmall.weixin.handler.AbstractHandler;
import java.util.Map;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

@Component
public class LocationHandler
extends AbstractHandler {
    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) {
        if (wxMessage.getMsgType().equals("location")) {
            try {
                String content = "\u611f\u8c22\u53cd\u9988\uff0c\u60a8\u7684\u7684\u5730\u7406\u4f4d\u7f6e\u5df2\u6536\u5230\uff01";
                return new TextBuilder().build(content, wxMessage, null);
            }
            catch (Exception e) {
                this.logger.error("\u4f4d\u7f6e\u6d88\u606f\u63a5\u6536\u5904\u7406\u5931\u8d25", e);
                return null;
            }
        }
        this.logger.info("\u4e0a\u62a5\u5730\u7406\u4f4d\u7f6e\uff0c\u7eac\u5ea6 : {}\uff0c\u7ecf\u5ea6 : {}\uff0c\u7cbe\u5ea6 : {}", wxMessage.getLatitude(), wxMessage.getLongitude(), String.valueOf(wxMessage.getPrecision()));
        return null;
    }
}

