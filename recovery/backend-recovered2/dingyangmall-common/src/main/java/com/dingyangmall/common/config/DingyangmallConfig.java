/*
 * Decompiled with CFR.
 */
package com.dingyangmall.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="dingyangmall")
public class DingyangmallConfig {
    private String name;
    private String version;
    private String copyrightYear;
    private static String profile;
    private static boolean addressEnabled;
    private static String captchaType;
    private static boolean demoEnabled;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCopyrightYear() {
        return this.copyrightYear;
    }

    public void setCopyrightYear(String copyrightYear) {
        this.copyrightYear = copyrightYear;
    }

    public static String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        DingyangmallConfig.profile = profile;
    }

    public static boolean isAddressEnabled() {
        return addressEnabled;
    }

    public void setAddressEnabled(boolean addressEnabled) {
        DingyangmallConfig.addressEnabled = addressEnabled;
    }

    public static String getCaptchaType() {
        return captchaType;
    }

    public void setCaptchaType(String captchaType) {
        DingyangmallConfig.captchaType = captchaType;
    }

    public static String getImportPath() {
        return DingyangmallConfig.getProfile() + "/import";
    }

    public static String getAvatarPath() {
        return DingyangmallConfig.getProfile() + "/avatar";
    }

    public static String getDownloadPath() {
        return DingyangmallConfig.getProfile() + "/download/";
    }

    public static String getUploadPath() {
        return DingyangmallConfig.getProfile() + "/upload";
    }

    public static boolean isDemoEnabled() {
        return demoEnabled;
    }

    public void setDemoEnabled(boolean demoEnabled) {
        DingyangmallConfig.demoEnabled = demoEnabled;
    }
}

