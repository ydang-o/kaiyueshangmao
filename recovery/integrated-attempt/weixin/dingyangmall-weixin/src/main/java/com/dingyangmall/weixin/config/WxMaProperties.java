/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.config;

import java.util.List;
import lombok.Generated;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="wx.ma")
public class WxMaProperties {
    private List<Config> configs;

    @Generated
    public WxMaProperties() {
    }

    @Generated
    public List<Config> getConfigs() {
        return this.configs;
    }

    @Generated
    public void setConfigs(List<Config> configs) {
        this.configs = configs;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WxMaProperties)) {
            return false;
        }
        WxMaProperties other = (WxMaProperties)o;
        if (!other.canEqual(this)) {
            return false;
        }
        List<Config> this$configs = this.getConfigs();
        List<Config> other$configs = other.getConfigs();
        return !(this$configs == null ? other$configs != null : !((Object)this$configs).equals(other$configs));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof WxMaProperties;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        List<Config> $configs = this.getConfigs();
        result = result * 59 + ($configs == null ? 43 : ((Object)$configs).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "WxMaProperties(configs=" + String.valueOf(this.getConfigs()) + ")";
    }

    public static class Config {
        private String appId;
        private String secret;
        private String token;
        private String aesKey;
        private String msgDataFormat;
        private String mchId;
        private String mchKey;
        private String keyPath;

        @Generated
        public Config() {
        }

        @Generated
        public String getAppId() {
            return this.appId;
        }

        @Generated
        public String getSecret() {
            return this.secret;
        }

        @Generated
        public String getToken() {
            return this.token;
        }

        @Generated
        public String getAesKey() {
            return this.aesKey;
        }

        @Generated
        public String getMsgDataFormat() {
            return this.msgDataFormat;
        }

        @Generated
        public String getMchId() {
            return this.mchId;
        }

        @Generated
        public String getMchKey() {
            return this.mchKey;
        }

        @Generated
        public String getKeyPath() {
            return this.keyPath;
        }

        @Generated
        public void setAppId(String appId) {
            this.appId = appId;
        }

        @Generated
        public void setSecret(String secret) {
            this.secret = secret;
        }

        @Generated
        public void setToken(String token) {
            this.token = token;
        }

        @Generated
        public void setAesKey(String aesKey) {
            this.aesKey = aesKey;
        }

        @Generated
        public void setMsgDataFormat(String msgDataFormat) {
            this.msgDataFormat = msgDataFormat;
        }

        @Generated
        public void setMchId(String mchId) {
            this.mchId = mchId;
        }

        @Generated
        public void setMchKey(String mchKey) {
            this.mchKey = mchKey;
        }

        @Generated
        public void setKeyPath(String keyPath) {
            this.keyPath = keyPath;
        }

        @Generated
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Config)) {
                return false;
            }
            Config other = (Config)o;
            if (!other.canEqual(this)) {
                return false;
            }
            String this$appId = this.getAppId();
            String other$appId = other.getAppId();
            if (this$appId == null ? other$appId != null : !this$appId.equals(other$appId)) {
                return false;
            }
            String this$secret = this.getSecret();
            String other$secret = other.getSecret();
            if (this$secret == null ? other$secret != null : !this$secret.equals(other$secret)) {
                return false;
            }
            String this$token = this.getToken();
            String other$token = other.getToken();
            if (this$token == null ? other$token != null : !this$token.equals(other$token)) {
                return false;
            }
            String this$aesKey = this.getAesKey();
            String other$aesKey = other.getAesKey();
            if (this$aesKey == null ? other$aesKey != null : !this$aesKey.equals(other$aesKey)) {
                return false;
            }
            String this$msgDataFormat = this.getMsgDataFormat();
            String other$msgDataFormat = other.getMsgDataFormat();
            if (this$msgDataFormat == null ? other$msgDataFormat != null : !this$msgDataFormat.equals(other$msgDataFormat)) {
                return false;
            }
            String this$mchId = this.getMchId();
            String other$mchId = other.getMchId();
            if (this$mchId == null ? other$mchId != null : !this$mchId.equals(other$mchId)) {
                return false;
            }
            String this$mchKey = this.getMchKey();
            String other$mchKey = other.getMchKey();
            if (this$mchKey == null ? other$mchKey != null : !this$mchKey.equals(other$mchKey)) {
                return false;
            }
            String this$keyPath = this.getKeyPath();
            String other$keyPath = other.getKeyPath();
            return !(this$keyPath == null ? other$keyPath != null : !this$keyPath.equals(other$keyPath));
        }

        @Generated
        protected boolean canEqual(Object other) {
            return other instanceof Config;
        }

        @Generated
        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            String $appId = this.getAppId();
            result = result * 59 + ($appId == null ? 43 : $appId.hashCode());
            String $secret = this.getSecret();
            result = result * 59 + ($secret == null ? 43 : $secret.hashCode());
            String $token = this.getToken();
            result = result * 59 + ($token == null ? 43 : $token.hashCode());
            String $aesKey = this.getAesKey();
            result = result * 59 + ($aesKey == null ? 43 : $aesKey.hashCode());
            String $msgDataFormat = this.getMsgDataFormat();
            result = result * 59 + ($msgDataFormat == null ? 43 : $msgDataFormat.hashCode());
            String $mchId = this.getMchId();
            result = result * 59 + ($mchId == null ? 43 : $mchId.hashCode());
            String $mchKey = this.getMchKey();
            result = result * 59 + ($mchKey == null ? 43 : $mchKey.hashCode());
            String $keyPath = this.getKeyPath();
            result = result * 59 + ($keyPath == null ? 43 : $keyPath.hashCode());
            return result;
        }

        @Generated
        public String toString() {
            return "WxMaProperties.Config(appId=" + this.getAppId() + ", secret=" + this.getSecret() + ", token=" + this.getToken() + ", aesKey=" + this.getAesKey() + ", msgDataFormat=" + this.getMsgDataFormat() + ", mchId=" + this.getMchId() + ", mchKey=" + this.getMchKey() + ", keyPath=" + this.getKeyPath() + ")";
        }
    }
}

