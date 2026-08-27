/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.entity;

import com.dingyangmall.weixin.entity.WxMsg;
import lombok.Generated;

public class WxMsgVO
extends WxMsg {
    private static final long serialVersionUID = 1L;
    private Integer countMsg;
    private String notInRepType;

    @Generated
    public WxMsgVO() {
    }

    @Generated
    public Integer getCountMsg() {
        return this.countMsg;
    }

    @Generated
    public String getNotInRepType() {
        return this.notInRepType;
    }

    @Generated
    public void setCountMsg(Integer countMsg) {
        this.countMsg = countMsg;
    }

    @Generated
    public void setNotInRepType(String notInRepType) {
        this.notInRepType = notInRepType;
    }

    @Override
    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WxMsgVO)) {
            return false;
        }
        WxMsgVO other = (WxMsgVO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$countMsg = this.getCountMsg();
        Integer other$countMsg = other.getCountMsg();
        if (this$countMsg == null ? other$countMsg != null : !((Object)this$countMsg).equals(other$countMsg)) {
            return false;
        }
        String this$notInRepType = this.getNotInRepType();
        String other$notInRepType = other.getNotInRepType();
        return !(this$notInRepType == null ? other$notInRepType != null : !this$notInRepType.equals(other$notInRepType));
    }

    @Override
    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof WxMsgVO;
    }

    @Override
    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $countMsg = this.getCountMsg();
        result = result * 59 + ($countMsg == null ? 43 : ((Object)$countMsg).hashCode());
        String $notInRepType = this.getNotInRepType();
        result = result * 59 + ($notInRepType == null ? 43 : $notInRepType.hashCode());
        return result;
    }

    @Override
    @Generated
    public String toString() {
        return "WxMsgVO(countMsg=" + this.getCountMsg() + ", notInRepType=" + this.getNotInRepType() + ")";
    }
}

