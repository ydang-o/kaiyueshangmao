/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.entity;

import lombok.Generated;

public class WxUserTagsDict {
    private String name;
    private Long value;

    @Generated
    public WxUserTagsDict() {
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public Long getValue() {
        return this.value;
    }

    @Generated
    public void setName(String name) {
        this.name = name;
    }

    @Generated
    public void setValue(Long value) {
        this.value = value;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WxUserTagsDict)) {
            return false;
        }
        WxUserTagsDict other = (WxUserTagsDict)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$value = this.getValue();
        Long other$value = other.getValue();
        if (this$value == null ? other$value != null : !((Object)this$value).equals(other$value)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        return !(this$name == null ? other$name != null : !this$name.equals(other$name));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof WxUserTagsDict;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $value = this.getValue();
        result = result * 59 + ($value == null ? 43 : ((Object)$value).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "WxUserTagsDict(name=" + this.getName() + ", value=" + this.getValue() + ")";
    }
}

