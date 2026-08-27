/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.config;

import com.dingyangmall.weixin.config.WxMaProperties;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WxPayConfiguration {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(WxPayConfiguration.class);
    private static WxMaProperties wxMaProperties;

    @Autowired
    public WxPayConfiguration(WxMaProperties wxMaProperties) {
        WxPayConfiguration.wxMaProperties = wxMaProperties;
    }

    public static WxPayService getPayService() {
        WxPayServiceImpl wxPayService = null;
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(wxMaProperties.getConfigs().get(0).getAppId());
        payConfig.setMchId(wxMaProperties.getConfigs().get(0).getMchId());
        payConfig.setMchKey(wxMaProperties.getConfigs().get(0).getMchKey());
        payConfig.setKeyPath(wxMaProperties.getConfigs().get(0).getKeyPath());
        payConfig.setUseSandboxEnv(false);
        wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }
}

