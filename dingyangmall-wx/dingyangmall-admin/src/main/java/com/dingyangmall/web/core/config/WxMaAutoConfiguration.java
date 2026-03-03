package com.dingyangmall.web.core.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 当未由 dingyangmall-weixin 模块提供 WxMaService 时，从 wx.ma.configs 创建 Bean，
 * 以便 POST /weixin/api/ma/wxuser/login 可用。
 */
@Configuration
@ConditionalOnClass(WxMaService.class)
@EnableConfigurationProperties(WxMaAutoConfiguration.WxMaConfigProperties.class)
public class WxMaAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(WxMaAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean(WxMaService.class)
    @ConditionalOnProperty(prefix = "wx.ma", name = {"configs[0].appId", "configs[0].secret"})
    public WxMaService wxMaService(WxMaConfigProperties properties) {
        List<WxMaConfigProperties.Config> configs = properties.getConfigs();
        if (configs == null || configs.isEmpty()) {
            throw new IllegalStateException("wx.ma.configs 为空，无法初始化 WxMaService");
        }
        WxMaConfigProperties.Config first = configs.get(0);
        if (first == null || first.getAppId() == null || first.getAppId().isBlank() || first.getSecret() == null || first.getSecret().isBlank()) {
            throw new IllegalStateException("wx.ma.configs[0].appId/secret 未配置，无法初始化 WxMaService");
        }
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(first.getAppId());
        config.setSecret(first.getSecret());
        WxMaServiceImpl service = new WxMaServiceImpl();
        service.setWxMaConfig(config);
        log.info("WxMaService 已初始化，appId={}", first.getAppId());
        return service;
    }

    @org.springframework.boot.context.properties.ConfigurationProperties(prefix = "wx.ma")
    public static class WxMaConfigProperties {
        private List<Config> configs;

        public List<Config> getConfigs() {
            return configs;
        }

        public void setConfigs(List<Config> configs) {
            this.configs = configs;
        }

        public static class Config {
            private String appId;
            private String secret;

            public String getAppId() {
                return appId;
            }

            public void setAppId(String appId) {
                this.appId = appId;
            }

            public String getSecret() {
                return secret;
            }

            public void setSecret(String secret) {
                this.secret = secret;
            }
        }
    }
}
