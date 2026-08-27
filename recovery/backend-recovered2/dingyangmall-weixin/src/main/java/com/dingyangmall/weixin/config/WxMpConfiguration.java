/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.config;

import com.dingyangmall.weixin.config.WxMpProperties;
import com.dingyangmall.weixin.handler.KfSessionHandler;
import com.dingyangmall.weixin.handler.LocationHandler;
import com.dingyangmall.weixin.handler.LogHandler;
import com.dingyangmall.weixin.handler.MenuHandler;
import com.dingyangmall.weixin.handler.MsgHandler;
import com.dingyangmall.weixin.handler.NullHandler;
import com.dingyangmall.weixin.handler.ScanHandler;
import com.dingyangmall.weixin.handler.StoreCheckNotifyHandler;
import com.dingyangmall.weixin.handler.SubscribeHandler;
import com.dingyangmall.weixin.handler.UnsubscribeHandler;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Generated;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value={WxMpProperties.class})
public class WxMpConfiguration {
    private final LogHandler logHandler;
    private final NullHandler nullHandler;
    private final KfSessionHandler kfSessionHandler;
    private final StoreCheckNotifyHandler storeCheckNotifyHandler;
    private final LocationHandler locationHandler;
    private final MenuHandler menuHandler;
    private final MsgHandler msgHandler;
    private final UnsubscribeHandler unsubscribeHandler;
    private final SubscribeHandler subscribeHandler;
    private final ScanHandler scanHandler;
    private final WxMpProperties properties;

    @Bean
    public WxMpService wxMpService() {
        List<WxMpProperties.MpConfig> configs = this.properties.getConfigs();
        if (configs == null) {
            throw new RuntimeException("\u5927\u54e5\uff0c\u62dc\u6258\u5148\u770b\u4e0b\u9879\u76ee\u9996\u9875\u7684\u8bf4\u660e\uff08readme\u6587\u4ef6\uff09\uff0c\u6dfb\u52a0\u4e0b\u76f8\u5173\u914d\u7f6e\uff0c\u6ce8\u610f\u522b\u914d\u9519\u4e86\uff01");
        }
        WxMpServiceImpl service = new WxMpServiceImpl();
        service.setMultiConfigStorages(configs.stream().map(a -> {
            WxMpDefaultConfigImpl configStorage = new WxMpDefaultConfigImpl();
            configStorage.setAppId(a.getAppId());
            configStorage.setSecret(a.getSecret());
            configStorage.setToken(a.getToken());
            configStorage.setAesKey(a.getAesKey());
            return configStorage;
        }).collect(Collectors.toMap(WxMpDefaultConfigImpl::getAppId, a -> a, (o, n) -> o)));
        return service;
    }

    @Bean
    public WxMpMessageRouter messageRouter(WxMpService wxMpService) {
        WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);
        newRouter.rule().handler(this.logHandler).next();
        newRouter.rule().async(false).msgType("event").event("kf_create_session").handler(this.kfSessionHandler).end();
        newRouter.rule().async(false).msgType("event").event("kf_close_session").handler(this.kfSessionHandler).end();
        newRouter.rule().async(false).msgType("event").event("kf_switch_session").handler(this.kfSessionHandler).end();
        newRouter.rule().async(false).msgType("event").event("poi_check_notify").handler(this.storeCheckNotifyHandler).end();
        newRouter.rule().async(false).msgType("event").event("CLICK").handler(this.menuHandler).end();
        newRouter.rule().async(false).msgType("event").event("VIEW").handler(this.nullHandler).end();
        newRouter.rule().async(false).msgType("event").event("subscribe").handler(this.subscribeHandler).end();
        newRouter.rule().async(false).msgType("event").event("unsubscribe").handler(this.unsubscribeHandler).end();
        newRouter.rule().async(false).msgType("event").event("LOCATION").handler(this.locationHandler).end();
        newRouter.rule().async(false).msgType("location").handler(this.locationHandler).end();
        newRouter.rule().async(false).msgType("event").event("SCAN").handler(this.scanHandler).end();
        newRouter.rule().async(false).handler(this.msgHandler).end();
        return newRouter;
    }

    @Generated
    public WxMpConfiguration(LogHandler logHandler, NullHandler nullHandler, KfSessionHandler kfSessionHandler, StoreCheckNotifyHandler storeCheckNotifyHandler, LocationHandler locationHandler, MenuHandler menuHandler, MsgHandler msgHandler, UnsubscribeHandler unsubscribeHandler, SubscribeHandler subscribeHandler, ScanHandler scanHandler, WxMpProperties properties) {
        this.logHandler = logHandler;
        this.nullHandler = nullHandler;
        this.kfSessionHandler = kfSessionHandler;
        this.storeCheckNotifyHandler = storeCheckNotifyHandler;
        this.locationHandler = locationHandler;
        this.menuHandler = menuHandler;
        this.msgHandler = msgHandler;
        this.unsubscribeHandler = unsubscribeHandler;
        this.subscribeHandler = subscribeHandler;
        this.scanHandler = scanHandler;
        this.properties = properties;
    }
}

