/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.dto;

import java.io.Serializable;
import lombok.Generated;

public class IntegralPacketDTO
implements Serializable {
    private static final long serialVersionUID = 1L;
    private String phone;
    private Integer amount;
    private String code;

    @Generated
    public IntegralPacketDTO() {
    }

    @Generated
    public String getPhone() {
        return this.phone;
    }

    @Generated
    public Integer getAmount() {
        return this.amount;
    }

    @Generated
    public String getCode() {
        return this.code;
    }

    @Generated
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Generated
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Generated
    public void setCode(String code) {
        this.code = code;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof IntegralPacketDTO)) {
            return false;
        }
        IntegralPacketDTO other = (IntegralPacketDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$amount = this.getAmount();
        Integer other$amount = other.getAmount();
        if (this$amount == null ? other$amount != null : !((Object)this$amount).equals(other$amount)) {
            return false;
        }
        String this$phone = this.getPhone();
        String other$phone = other.getPhone();
        if (this$phone == null ? other$phone != null : !this$phone.equals(other$phone)) {
            return false;
        }
        String this$code = this.getCode();
        String other$code = other.getCode();
        return !(this$code == null ? other$code != null : !this$code.equals(other$code));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof IntegralPacketDTO;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $amount = this.getAmount();
        result = result * 59 + ($amount == null ? 43 : ((Object)$amount).hashCode());
        String $phone = this.getPhone();
        result = result * 59 + ($phone == null ? 43 : $phone.hashCode());
        String $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "IntegralPacketDTO(phone=" + this.getPhone() + ", amount=" + this.getAmount() + ", code=" + this.getCode() + ")";
    }
}

