/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.builder;

import com.dingyangmall.weixin.builder.AbstractBuilder;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;

public class TextBuilder
extends AbstractBuilder {
    @Override
    public WxMpXmlOutMessage build(String content, WxMpXmlMessage wxMessage, WxMpService service) {
        WxMpXmlOutTextMessage m = ((me.chanjar.weixin.mp.builder.outxml.TextBuilder)((me.chanjar.weixin.mp.builder.outxml.TextBuilder)WxMpXmlOutMessage.TEXT().content(content).fromUser(wxMessage.getToUser())).toUser(wxMessage.getFromUser())).build();
        return m;
    }
}

