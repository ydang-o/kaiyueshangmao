/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.builder;

import com.dingyangmall.weixin.builder.AbstractBuilder;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutImageMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

public class ImageBuilder
extends AbstractBuilder {
    @Override
    public WxMpXmlOutMessage build(String content, WxMpXmlMessage wxMessage, WxMpService service) {
        WxMpXmlOutImageMessage m = ((me.chanjar.weixin.mp.builder.outxml.ImageBuilder)((me.chanjar.weixin.mp.builder.outxml.ImageBuilder)WxMpXmlOutMessage.IMAGE().mediaId(content).fromUser(wxMessage.getToUser())).toUser(wxMessage.getFromUser())).build();
        return m;
    }
}

