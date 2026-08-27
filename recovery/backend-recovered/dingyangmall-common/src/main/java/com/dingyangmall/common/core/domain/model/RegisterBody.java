/*
 * Decompiled with CFR.
 */
package com.dingyangmall.common.core.domain.model;

import com.dingyangmall.common.core.domain.model.LoginBody;

public class RegisterBody
extends LoginBody {
    private String phonenumber;
    private String smsCode;
    private String inviteCode;

    public String getPhonenumber() {
        return this.phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getSmsCode() {
        return this.smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getInviteCode() {
        return this.inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}

