/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.entity;

import lombok.Generated;

public class WxOpenDataDTO {
    private String appId;
    private String userId;
    private String encryptedData;
    private String errMsg;
    private String iv;
    private String rawData;
    private String signature;
    private String sessionKey;

    @Generated
    public WxOpenDataDTO() {
    }

    @Generated
    public String getAppId() {
        return this.appId;
    }

    @Generated
    public String getUserId() {
        return this.userId;
    }

    @Generated
    public String getEncryptedData() {
        return this.encryptedData;
    }

    @Generated
    public String getErrMsg() {
        return this.errMsg;
    }

    @Generated
    public String getIv() {
        return this.iv;
    }

    @Generated
    public String getRawData() {
        return this.rawData;
    }

    @Generated
    public String getSignature() {
        return this.signature;
    }

    @Generated
    public String getSessionKey() {
        return this.sessionKey;
    }

    @Generated
    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Generated
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Generated
    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    @Generated
    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    @Generated
    public void setIv(String iv) {
        this.iv = iv;
    }

    @Generated
    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    @Generated
    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Generated
    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WxOpenDataDTO)) {
            return false;
        }
        WxOpenDataDTO other = (WxOpenDataDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$appId = this.getAppId();
        String other$appId = other.getAppId();
        if (this$appId == null ? other$appId != null : !this$appId.equals(other$appId)) {
            return false;
        }
        String this$userId = this.getUserId();
        String other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !this$userId.equals(other$userId)) {
            return false;
        }
        String this$encryptedData = this.getEncryptedData();
        String other$encryptedData = other.getEncryptedData();
        if (this$encryptedData == null ? other$encryptedData != null : !this$encryptedData.equals(other$encryptedData)) {
            return false;
        }
        String this$errMsg = this.getErrMsg();
        String other$errMsg = other.getErrMsg();
        if (this$errMsg == null ? other$errMsg != null : !this$errMsg.equals(other$errMsg)) {
            return false;
        }
        String this$iv = this.getIv();
        String other$iv = other.getIv();
        if (this$iv == null ? other$iv != null : !this$iv.equals(other$iv)) {
            return false;
        }
        String this$rawData = this.getRawData();
        String other$rawData = other.getRawData();
        if (this$rawData == null ? other$rawData != null : !this$rawData.equals(other$rawData)) {
            return false;
        }
        String this$signature = this.getSignature();
        String other$signature = other.getSignature();
        if (this$signature == null ? other$signature != null : !this$signature.equals(other$signature)) {
            return false;
        }
        String this$sessionKey = this.getSessionKey();
        String other$sessionKey = other.getSessionKey();
        return !(this$sessionKey == null ? other$sessionKey != null : !this$sessionKey.equals(other$sessionKey));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof WxOpenDataDTO;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $appId = this.getAppId();
        result = result * 59 + ($appId == null ? 43 : $appId.hashCode());
        String $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : $userId.hashCode());
        String $encryptedData = this.getEncryptedData();
        result = result * 59 + ($encryptedData == null ? 43 : $encryptedData.hashCode());
        String $errMsg = this.getErrMsg();
        result = result * 59 + ($errMsg == null ? 43 : $errMsg.hashCode());
        String $iv = this.getIv();
        result = result * 59 + ($iv == null ? 43 : $iv.hashCode());
        String $rawData = this.getRawData();
        result = result * 59 + ($rawData == null ? 43 : $rawData.hashCode());
        String $signature = this.getSignature();
        result = result * 59 + ($signature == null ? 43 : $signature.hashCode());
        String $sessionKey = this.getSessionKey();
        result = result * 59 + ($sessionKey == null ? 43 : $sessionKey.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "WxOpenDataDTO(appId=" + this.getAppId() + ", userId=" + this.getUserId() + ", encryptedData=" + this.getEncryptedData() + ", errMsg=" + this.getErrMsg() + ", iv=" + this.getIv() + ", rawData=" + this.getRawData() + ", signature=" + this.getSignature() + ", sessionKey=" + this.getSessionKey() + ")";
    }
}

