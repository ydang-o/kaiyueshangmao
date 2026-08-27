/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.config;

import lombok.Generated;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="tencent.sms")
public class TencentSmsProperties {
    private boolean enabled = false;
    private String secretId = "";
    private String secretKey = "";
    private String smsSdkAppId = "";
    private String signName = "";
    private String templateId = "";
    private String region = "ap-guangzhou";

    @Generated
    public TencentSmsProperties() {
    }

    @Generated
    public boolean isEnabled() {
        return this.enabled;
    }

    @Generated
    public String getSecretId() {
        return this.secretId;
    }

    @Generated
    public String getSecretKey() {
        return this.secretKey;
    }

    @Generated
    public String getSmsSdkAppId() {
        return this.smsSdkAppId;
    }

    @Generated
    public String getSignName() {
        return this.signName;
    }

    @Generated
    public String getTemplateId() {
        return this.templateId;
    }

    @Generated
    public String getRegion() {
        return this.region;
    }

    @Generated
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Generated
    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    @Generated
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Generated
    public void setSmsSdkAppId(String smsSdkAppId) {
        this.smsSdkAppId = smsSdkAppId;
    }

    @Generated
    public void setSignName(String signName) {
        this.signName = signName;
    }

    @Generated
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    @Generated
    public void setRegion(String region) {
        this.region = region;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TencentSmsProperties)) {
            return false;
        }
        TencentSmsProperties other = (TencentSmsProperties)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isEnabled() != other.isEnabled()) {
            return false;
        }
        String this$secretId = this.getSecretId();
        String other$secretId = other.getSecretId();
        if (this$secretId == null ? other$secretId != null : !this$secretId.equals(other$secretId)) {
            return false;
        }
        String this$secretKey = this.getSecretKey();
        String other$secretKey = other.getSecretKey();
        if (this$secretKey == null ? other$secretKey != null : !this$secretKey.equals(other$secretKey)) {
            return false;
        }
        String this$smsSdkAppId = this.getSmsSdkAppId();
        String other$smsSdkAppId = other.getSmsSdkAppId();
        if (this$smsSdkAppId == null ? other$smsSdkAppId != null : !this$smsSdkAppId.equals(other$smsSdkAppId)) {
            return false;
        }
        String this$signName = this.getSignName();
        String other$signName = other.getSignName();
        if (this$signName == null ? other$signName != null : !this$signName.equals(other$signName)) {
            return false;
        }
        String this$templateId = this.getTemplateId();
        String other$templateId = other.getTemplateId();
        if (this$templateId == null ? other$templateId != null : !this$templateId.equals(other$templateId)) {
            return false;
        }
        String this$region = this.getRegion();
        String other$region = other.getRegion();
        return !(this$region == null ? other$region != null : !this$region.equals(other$region));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof TencentSmsProperties;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isEnabled() ? 79 : 97);
        String $secretId = this.getSecretId();
        result = result * 59 + ($secretId == null ? 43 : $secretId.hashCode());
        String $secretKey = this.getSecretKey();
        result = result * 59 + ($secretKey == null ? 43 : $secretKey.hashCode());
        String $smsSdkAppId = this.getSmsSdkAppId();
        result = result * 59 + ($smsSdkAppId == null ? 43 : $smsSdkAppId.hashCode());
        String $signName = this.getSignName();
        result = result * 59 + ($signName == null ? 43 : $signName.hashCode());
        String $templateId = this.getTemplateId();
        result = result * 59 + ($templateId == null ? 43 : $templateId.hashCode());
        String $region = this.getRegion();
        result = result * 59 + ($region == null ? 43 : $region.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "TencentSmsProperties(enabled=" + this.isEnabled() + ", secretId=" + this.getSecretId() + ", secretKey=" + this.getSecretKey() + ", smsSdkAppId=" + this.getSmsSdkAppId() + ", signName=" + this.getSignName() + ", templateId=" + this.getTemplateId() + ", region=" + this.getRegion() + ")";
    }
}

