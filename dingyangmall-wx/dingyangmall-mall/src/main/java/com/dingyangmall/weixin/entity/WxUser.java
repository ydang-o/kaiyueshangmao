package com.dingyangmall.weixin.entity;

/** Minimal login/session model shared by the mobile order APIs. */
public class WxUser {
    private String id;
    private String sessionKey;
    private String openId;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSessionKey() { return sessionKey; }
    public void setSessionKey(String sessionKey) { this.sessionKey = sessionKey; }
    public String getOpenId() { return openId; }
    public void setOpenId(String openId) { this.openId = openId; }
}
