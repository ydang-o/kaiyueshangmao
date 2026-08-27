/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.dto;

import lombok.Generated;

public class DynamicCodeDTO {
    private String encryptedCode;
    private Long timestamp;
    private Integer expireSeconds;
    private Long id;
    private String name;

    public DynamicCodeDTO() {
    }

    public DynamicCodeDTO(String encryptedCode, Long timestamp, Integer expireSeconds) {
        this.encryptedCode = encryptedCode;
        this.timestamp = timestamp;
        this.expireSeconds = expireSeconds;
    }

    @Generated
    public String getEncryptedCode() {
        return this.encryptedCode;
    }

    @Generated
    public Long getTimestamp() {
        return this.timestamp;
    }

    @Generated
    public Integer getExpireSeconds() {
        return this.expireSeconds;
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public void setEncryptedCode(String encryptedCode) {
        this.encryptedCode = encryptedCode;
    }

    @Generated
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Generated
    public void setExpireSeconds(Integer expireSeconds) {
        this.expireSeconds = expireSeconds;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setName(String name) {
        this.name = name;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DynamicCodeDTO)) {
            return false;
        }
        DynamicCodeDTO other = (DynamicCodeDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$timestamp = this.getTimestamp();
        Long other$timestamp = other.getTimestamp();
        if (this$timestamp == null ? other$timestamp != null : !((Object)this$timestamp).equals(other$timestamp)) {
            return false;
        }
        Integer this$expireSeconds = this.getExpireSeconds();
        Integer other$expireSeconds = other.getExpireSeconds();
        if (this$expireSeconds == null ? other$expireSeconds != null : !((Object)this$expireSeconds).equals(other$expireSeconds)) {
            return false;
        }
        Long this$id = this.getId();
        Long other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        String this$encryptedCode = this.getEncryptedCode();
        String other$encryptedCode = other.getEncryptedCode();
        if (this$encryptedCode == null ? other$encryptedCode != null : !this$encryptedCode.equals(other$encryptedCode)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        return !(this$name == null ? other$name != null : !this$name.equals(other$name));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof DynamicCodeDTO;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $timestamp = this.getTimestamp();
        result = result * 59 + ($timestamp == null ? 43 : ((Object)$timestamp).hashCode());
        Integer $expireSeconds = this.getExpireSeconds();
        result = result * 59 + ($expireSeconds == null ? 43 : ((Object)$expireSeconds).hashCode());
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        String $encryptedCode = this.getEncryptedCode();
        result = result * 59 + ($encryptedCode == null ? 43 : $encryptedCode.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "DynamicCodeDTO(encryptedCode=" + this.getEncryptedCode() + ", timestamp=" + this.getTimestamp() + ", expireSeconds=" + this.getExpireSeconds() + ", id=" + this.getId() + ", name=" + this.getName() + ")";
    }
}

