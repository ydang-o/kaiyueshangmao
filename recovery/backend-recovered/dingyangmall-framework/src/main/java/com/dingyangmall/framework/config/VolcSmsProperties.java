/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.config;

import lombok.Generated;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="volcengine.sms")
public class VolcSmsProperties {
    private boolean enabled = false;
    private String accessKey = "";
    private String secretKey = "";
    private String smsAccount = "";
    private String signName = "";
    private String templateId = "";
    private String region = "cn-north-1";
    private String serviceName = "volcSMS";
    private String version = "2020-01-01";
    private String host = "sms.volcengineapi.com";

    @Generated
    public VolcSmsProperties() {
    }

    @Generated
    public boolean isEnabled() {
        return this.enabled;
    }

    @Generated
    public String getAccessKey() {
        return this.accessKey;
    }

    @Generated
    public String getSecretKey() {
        return this.secretKey;
    }

    @Generated
    public String getSmsAccount() {
        return this.smsAccount;
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
    public String getServiceName() {
        return this.serviceName;
    }

    @Generated
    public String getVersion() {
        return this.version;
    }

    @Generated
    public String getHost() {
        return this.host;
    }

    @Generated
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Generated
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    @Generated
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Generated
    public void setSmsAccount(String smsAccount) {
        this.smsAccount = smsAccount;
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
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Generated
    public void setVersion(String version) {
        this.version = version;
    }

    @Generated
    public void setHost(String host) {
        this.host = host;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof VolcSmsProperties)) {
            return false;
        }
        VolcSmsProperties other = (VolcSmsProperties)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isEnabled() != other.isEnabled()) {
            return false;
        }
        String this$accessKey = this.getAccessKey();
        String other$accessKey = other.getAccessKey();
        if (this$accessKey == null ? other$accessKey != null : !this$accessKey.equals(other$accessKey)) {
            return false;
        }
        String this$secretKey = this.getSecretKey();
        String other$secretKey = other.getSecretKey();
        if (this$secretKey == null ? other$secretKey != null : !this$secretKey.equals(other$secretKey)) {
            return false;
        }
        String this$smsAccount = this.getSmsAccount();
        String other$smsAccount = other.getSmsAccount();
        if (this$smsAccount == null ? other$smsAccount != null : !this$smsAccount.equals(other$smsAccount)) {
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
        if (this$region == null ? other$region != null : !this$region.equals(other$region)) {
            return false;
        }
        String this$serviceName = this.getServiceName();
        String other$serviceName = other.getServiceName();
        if (this$serviceName == null ? other$serviceName != null : !this$serviceName.equals(other$serviceName)) {
            return false;
        }
        String this$version = this.getVersion();
        String other$version = other.getVersion();
        if (this$version == null ? other$version != null : !this$version.equals(other$version)) {
            return false;
        }
        String this$host = this.getHost();
        String other$host = other.getHost();
        return !(this$host == null ? other$host != null : !this$host.equals(other$host));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof VolcSmsProperties;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isEnabled() ? 79 : 97);
        String $accessKey = this.getAccessKey();
        result = result * 59 + ($accessKey == null ? 43 : $accessKey.hashCode());
        String $secretKey = this.getSecretKey();
        result = result * 59 + ($secretKey == null ? 43 : $secretKey.hashCode());
        String $smsAccount = this.getSmsAccount();
        result = result * 59 + ($smsAccount == null ? 43 : $smsAccount.hashCode());
        String $signName = this.getSignName();
        result = result * 59 + ($signName == null ? 43 : $signName.hashCode());
        String $templateId = this.getTemplateId();
        result = result * 59 + ($templateId == null ? 43 : $templateId.hashCode());
        String $region = this.getRegion();
        result = result * 59 + ($region == null ? 43 : $region.hashCode());
        String $serviceName = this.getServiceName();
        result = result * 59 + ($serviceName == null ? 43 : $serviceName.hashCode());
        String $version = this.getVersion();
        result = result * 59 + ($version == null ? 43 : $version.hashCode());
        String $host = this.getHost();
        result = result * 59 + ($host == null ? 43 : $host.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "VolcSmsProperties(enabled=" + this.isEnabled() + ", accessKey=" + this.getAccessKey() + ", secretKey=" + this.getSecretKey() + ", smsAccount=" + this.getSmsAccount() + ", signName=" + this.getSignName() + ", templateId=" + this.getTemplateId() + ", region=" + this.getRegion() + ", serviceName=" + this.getServiceName() + ", version=" + this.getVersion() + ", host=" + this.getHost() + ")";
    }
}

