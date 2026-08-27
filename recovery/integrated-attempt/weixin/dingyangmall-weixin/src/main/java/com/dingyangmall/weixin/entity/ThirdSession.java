/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.entity;

import java.io.Serializable;
import lombok.Generated;

public class ThirdSession
implements Serializable {
    private String wxUserId;
    private String appId;
    private String sessionKey;
    private String openId;

    @Generated
    public ThirdSession() {
    }

    @Generated
    public String getWxUserId() {
        return this.wxUserId;
    }

    @Generated
    public String getAppId() {
        return this.appId;
    }

    @Generated
    public String getSessionKey() {
        return this.sessionKey;
    }

    @Generated
    public String getOpenId() {
        return this.openId;
    }

    @Generated
    public void setWxUserId(String wxUserId) {
        this.wxUserId = wxUserId;
    }

    @Generated
    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Generated
    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    @Generated
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ThirdSession)) {
            return false;
        }
        ThirdSession other = (ThirdSession)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$wxUserId = this.getWxUserId();
        String other$wxUserId = other.getWxUserId();
        if (this$wxUserId == null ? other$wxUserId != null : !this$wxUserId.equals(other$wxUserId)) {
            return false;
        }
        String this$appId = this.getAppId();
        String other$appId = other.getAppId();
        if (this$appId == null ? other$appId != null : !this$appId.equals(other$appId)) {
            return false;
        }
        String this$sessionKey = this.getSessionKey();
        String other$sessionKey = other.getSessionKey();
        if (this$sessionKey == null ? other$sessionKey != null : !this$sessionKey.equals(other$sessionKey)) {
            return false;
        }
        String this$openId = this.getOpenId();
        String other$openId = other.getOpenId();
        return !(this$openId == null ? other$openId != null : !this$openId.equals(other$openId));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ThirdSession;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $wxUserId = this.getWxUserId();
        result = result * 59 + ($wxUserId == null ? 43 : $wxUserId.hashCode());
        String $appId = this.getAppId();
        result = result * 59 + ($appId == null ? 43 : $appId.hashCode());
        String $sessionKey = this.getSessionKey();
        result = result * 59 + ($sessionKey == null ? 43 : $sessionKey.hashCode());
        String $openId = this.getOpenId();
        result = result * 59 + ($openId == null ? 43 : $openId.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ThirdSession(wxUserId=" + this.getWxUserId() + ", appId=" + this.getAppId() + ", sessionKey=" + this.getSessionKey() + ", openId=" + this.getOpenId() + ")";
    }
}

