/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.dto;

import lombok.Generated;

public class AppRegisterBody {
    private String phone;
    private String password;
    private String code;
    private String inviteCode;

    @Generated
    public AppRegisterBody() {
    }

    @Generated
    public String getPhone() {
        return this.phone;
    }

    @Generated
    public String getPassword() {
        return this.password;
    }

    @Generated
    public String getCode() {
        return this.code;
    }

    @Generated
    public String getInviteCode() {
        return this.inviteCode;
    }

    @Generated
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Generated
    public void setPassword(String password) {
        this.password = password;
    }

    @Generated
    public void setCode(String code) {
        this.code = code;
    }

    @Generated
    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AppRegisterBody)) {
            return false;
        }
        AppRegisterBody other = (AppRegisterBody)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$phone = this.getPhone();
        String other$phone = other.getPhone();
        if (this$phone == null ? other$phone != null : !this$phone.equals(other$phone)) {
            return false;
        }
        String this$password = this.getPassword();
        String other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) {
            return false;
        }
        String this$code = this.getCode();
        String other$code = other.getCode();
        if (this$code == null ? other$code != null : !this$code.equals(other$code)) {
            return false;
        }
        String this$inviteCode = this.getInviteCode();
        String other$inviteCode = other.getInviteCode();
        return !(this$inviteCode == null ? other$inviteCode != null : !this$inviteCode.equals(other$inviteCode));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof AppRegisterBody;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $phone = this.getPhone();
        result = result * 59 + ($phone == null ? 43 : $phone.hashCode());
        String $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        String $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        String $inviteCode = this.getInviteCode();
        result = result * 59 + ($inviteCode == null ? 43 : $inviteCode.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "AppRegisterBody(phone=" + this.getPhone() + ", password=" + this.getPassword() + ", code=" + this.getCode() + ", inviteCode=" + this.getInviteCode() + ")";
    }
}

