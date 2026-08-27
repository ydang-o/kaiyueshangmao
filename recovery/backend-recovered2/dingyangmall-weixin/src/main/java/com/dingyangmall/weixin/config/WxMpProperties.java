/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.config;

import com.dingyangmall.weixin.utils.JsonUtils;
import java.util.List;
import lombok.Generated;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="wx.mp")
public class WxMpProperties {
    private List<MpConfig> configs;

    public String toString() {
        return JsonUtils.toJson(this);
    }

    @Generated
    public WxMpProperties() {
    }

    @Generated
    public List<MpConfig> getConfigs() {
        return this.configs;
    }

    @Generated
    public void setConfigs(List<MpConfig> configs) {
        this.configs = configs;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WxMpProperties)) {
            return false;
        }
        WxMpProperties other = (WxMpProperties)o;
        if (!other.canEqual(this)) {
            return false;
        }
        List<MpConfig> this$configs = this.getConfigs();
        List<MpConfig> other$configs = other.getConfigs();
        return !(this$configs == null ? other$configs != null : !((Object)this$configs).equals(other$configs));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof WxMpProperties;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        List<MpConfig> $configs = this.getConfigs();
        result = result * 59 + ($configs == null ? 43 : ((Object)$configs).hashCode());
        return result;
    }

    public static class MpConfig {
        private String appId;
        private String secret;
        private String token;
        private String aesKey;

        @Generated
        public MpConfig() {
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
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof MpConfig)) {
                return false;
            }
            MpConfig other = (MpConfig)o;
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
            return !(this$aesKey == null ? other$aesKey != null : !this$aesKey.equals(other$aesKey));
        }

        @Generated
        protected boolean canEqual(Object other) {
            return other instanceof MpConfig;
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
            return result;
        }

        @Generated
        public String toString() {
            return "WxMpProperties.MpConfig(appId=" + this.getAppId() + ", secret=" + this.getSecret() + ", token=" + this.getToken() + ", aesKey=" + this.getAesKey() + ")";
        }
    }
}

