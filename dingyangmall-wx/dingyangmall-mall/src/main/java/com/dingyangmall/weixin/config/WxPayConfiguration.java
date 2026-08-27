package com.dingyangmall.weixin.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;

/**
 * Compatibility facade retained after the original optional weixin module was removed.
 * Payment credentials are read from environment variables when payment APIs are used.
 */
public final class WxPayConfiguration {
    private static final WxPayService PAY_SERVICE = createPayService();

    private WxPayConfiguration() {}

    public static WxPayService getPayService() {
        return PAY_SERVICE;
    }

    private static WxPayService createPayService() {
        WxPayConfig config = new WxPayConfig();
        config.setAppId(value("WX_APP_ID", ""));
        config.setMchId(value("WX_MCH_ID", ""));
        config.setMchKey(value("WX_MCH_KEY", ""));
        config.setKeyPath(value("WX_PAY_KEY_PATH", ""));
        config.setNotifyUrl(value("WX_NOTIFY_URL", ""));

        WxPayServiceImpl service = new WxPayServiceImpl();
        service.addConfig("default", config);
        service.switchover("default");
        return service;
    }

    private static String value(String name, String fallback) {
        String value = System.getenv(name);
        return value == null ? fallback : value;
    }
}
