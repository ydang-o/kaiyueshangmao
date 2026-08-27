/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.core.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(value={WxMaService.class})
@EnableConfigurationProperties(value={WxMaConfigProperties.class})
public class WxMaAutoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(WxMaAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean(value={WxMaService.class})
    @ConditionalOnProperty(prefix="wx.ma", name={"configs[0].appId", "configs[0].secret"})
    public WxMaService wxMaService(WxMaConfigProperties properties) {
        List<WxMaConfigProperties.Config> configs = properties.getConfigs();
        if (configs == null || configs.isEmpty()) {
            throw new IllegalStateException("wx.ma.configs \u4e3a\u7a7a\uff0c\u65e0\u6cd5\u521d\u59cb\u5316 WxMaService");
        }
        WxMaConfigProperties.Config first = configs.get(0);
        if (first == null || first.getAppId() == null || first.getAppId().isBlank() || first.getSecret() == null || first.getSecret().isBlank()) {
            throw new IllegalStateException("wx.ma.configs[0].appId/secret \u672a\u914d\u7f6e\uff0c\u65e0\u6cd5\u521d\u59cb\u5316 WxMaService");
        }
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(first.getAppId());
        config.setSecret(first.getSecret());
        WxMaServiceImpl service = new WxMaServiceImpl();
        service.setWxMaConfig(config);
        log.info("WxMaService initialized, appId={}", (Object)first.getAppId());
        return service;
    }

    @ConfigurationProperties(prefix="wx.ma")
    public static class WxMaConfigProperties {
        private List<Config> configs;

        public List<Config> getConfigs() {
            return this.configs;
        }

        public void setConfigs(List<Config> configs) {
            this.configs = configs;
        }

        public static class Config {
            private String appId;
            private String secret;

            public String getAppId() {
                return this.appId;
            }

            public void setAppId(String appId) {
                this.appId = appId;
            }

            public String getSecret() {
                return this.secret;
            }

            public void setSecret(String secret) {
                this.secret = secret;
            }
        }
    }
}

