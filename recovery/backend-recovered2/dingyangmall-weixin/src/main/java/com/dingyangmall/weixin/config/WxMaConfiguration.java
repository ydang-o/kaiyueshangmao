/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaKefuMessage;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.builder.ImageMessageBuilder;
import cn.binarywang.wx.miniapp.builder.TextMessageBuilder;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.binarywang.wx.miniapp.message.WxMaMessageHandler;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import com.dingyangmall.weixin.config.WxMaProperties;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Generated;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value={WxMaProperties.class})
public class WxMaConfiguration {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(WxMaConfiguration.class);
    private final WxMaProperties properties;
    private static final Map<String, WxMaMessageRouter> routers = Maps.newHashMap();
    private static Map<String, WxMaService> maServices;
    private final WxMaMessageHandler subscribeMsgHandler = (wxMessage, context, service, sessionManager) -> {
        service.getMsgService().sendSubscribeMsg(WxMaSubscribeMessage.builder().templateId("\u6b64\u5904\u66f4\u6362\u4e3a\u81ea\u5df1\u7684\u6a21\u677fid").data(Lists.newArrayList(new WxMaSubscribeMessage.MsgData("keyword1", "339208499"))).toUser(wxMessage.getFromUser()).build());
        return null;
    };
    private final WxMaMessageHandler logHandler = (wxMessage, context, service, sessionManager) -> {
        log.info("\u6536\u5230\u6d88\u606f\uff1a" + wxMessage.toString());
        service.getMsgService().sendKefuMsg(((TextMessageBuilder)WxMaKefuMessage.newTextBuilder().content("\u6536\u5230\u4fe1\u606f\u4e3a\uff1a" + wxMessage.toJson()).toUser(wxMessage.getFromUser())).build());
        return null;
    };
    private final WxMaMessageHandler textHandler = (wxMessage, context, service, sessionManager) -> {
        service.getMsgService().sendKefuMsg(((TextMessageBuilder)WxMaKefuMessage.newTextBuilder().content("\u56de\u590d\u6587\u672c\u6d88\u606f").toUser(wxMessage.getFromUser())).build());
        return null;
    };
    private final WxMaMessageHandler picHandler = (wxMessage, context, service, sessionManager) -> {
        try {
            WxMediaUploadResult uploadResult = service.getMediaService().uploadMedia("image", "png", ClassLoader.getSystemResourceAsStream("tmp.png"));
            service.getMsgService().sendKefuMsg(((ImageMessageBuilder)WxMaKefuMessage.newImageBuilder().mediaId(uploadResult.getMediaId()).toUser(wxMessage.getFromUser())).build());
        }
        catch (WxErrorException e) {
            e.printStackTrace();
        }
        return null;
    };
    private final WxMaMessageHandler qrcodeHandler = (wxMessage, context, service, sessionManager) -> {
        try {
            File file = service.getQrcodeService().createQrcode("123", 430);
            WxMediaUploadResult uploadResult = service.getMediaService().uploadMedia("image", file);
            service.getMsgService().sendKefuMsg(((ImageMessageBuilder)WxMaKefuMessage.newImageBuilder().mediaId(uploadResult.getMediaId()).toUser(wxMessage.getFromUser())).build());
        }
        catch (WxErrorException e) {
            e.printStackTrace();
        }
        return null;
    };

    @Autowired
    public WxMaConfiguration(WxMaProperties properties) {
        this.properties = properties;
    }

    public static WxMaService getMaService(String appId) {
        WxMaService wxService = maServices.get(appId);
        if (wxService == null) {
            throw new IllegalArgumentException(String.format("\u672a\u627e\u5230\u5bf9\u5e94appId=[%s]\u7684\u914d\u7f6e\uff0c\u8bf7\u6838\u5b9e\uff01", appId));
        }
        return wxService;
    }

    public static WxMaMessageRouter getRouter(String appId) {
        return routers.get(appId);
    }

    @PostConstruct
    public void init() {
        List<WxMaProperties.Config> configs = this.properties.getConfigs();
        if (configs == null) {
            throw new RuntimeException("\u5927\u54e5\uff0c\u62dc\u6258\u5148\u770b\u4e0b\u9879\u76ee\u9996\u9875\u7684\u8bf4\u660e\uff08readme\u6587\u4ef6\uff09\uff0c\u6dfb\u52a0\u4e0b\u76f8\u5173\u914d\u7f6e\uff0c\u6ce8\u610f\u522b\u914d\u9519\u4e86\uff01");
        }
        maServices = configs.stream().map(a -> {
            WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
            config.setAppid(a.getAppId());
            config.setSecret(a.getSecret());
            config.setToken(a.getToken());
            config.setAesKey(a.getAesKey());
            config.setMsgDataFormat(a.getMsgDataFormat());
            WxMaServiceImpl service = new WxMaServiceImpl();
            service.setWxMaConfig(config);
            routers.put(a.getAppId(), this.newRouter(service));
            return service;
        }).collect(Collectors.toMap(s -> s.getWxMaConfig().getAppid(), a -> a));
    }

    private WxMaMessageRouter newRouter(WxMaService service) {
        WxMaMessageRouter router = new WxMaMessageRouter(service);
        router.rule().handler(this.logHandler).next().rule().async(false).content("\u8ba2\u9605\u6d88\u606f").handler(this.subscribeMsgHandler).end().rule().async(false).content("\u6587\u672c").handler(this.textHandler).end().rule().async(false).content("\u56fe\u7247").handler(this.picHandler).end().rule().async(false).content("\u4e8c\u7ef4\u7801").handler(this.qrcodeHandler).end();
        return router;
    }
}

