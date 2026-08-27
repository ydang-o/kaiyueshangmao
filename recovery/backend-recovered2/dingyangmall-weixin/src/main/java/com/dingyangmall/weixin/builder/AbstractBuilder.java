/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.builder;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBuilder {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public abstract WxMpXmlOutMessage build(String var1, WxMpXmlMessage var2, WxMpService var3);
}

