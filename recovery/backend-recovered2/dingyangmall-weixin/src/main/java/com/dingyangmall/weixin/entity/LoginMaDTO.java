/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.entity;

import lombok.Generated;

public class LoginMaDTO {
    private String jsCode;

    @Generated
    public LoginMaDTO() {
    }

    @Generated
    public String getJsCode() {
        return this.jsCode;
    }

    @Generated
    public void setJsCode(String jsCode) {
        this.jsCode = jsCode;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LoginMaDTO)) {
            return false;
        }
        LoginMaDTO other = (LoginMaDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$jsCode = this.getJsCode();
        String other$jsCode = other.getJsCode();
        return !(this$jsCode == null ? other$jsCode != null : !this$jsCode.equals(other$jsCode));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof LoginMaDTO;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $jsCode = this.getJsCode();
        result = result * 59 + ($jsCode == null ? 43 : $jsCode.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "LoginMaDTO(jsCode=" + this.getJsCode() + ")";
    }
}

