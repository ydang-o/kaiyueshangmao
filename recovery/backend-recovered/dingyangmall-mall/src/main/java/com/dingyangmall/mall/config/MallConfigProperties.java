/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.config;

import lombok.Generated;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="mall")
public class MallConfigProperties {
    private String notifyHost = "notify-host";
    private String logisticsKey = "logistics-key";
    private String kuaidi100Key = "";
    private String kuaidi100Customer = "";

    @Generated
    public MallConfigProperties() {
    }

    @Generated
    public String getNotifyHost() {
        return this.notifyHost;
    }

    @Generated
    public String getLogisticsKey() {
        return this.logisticsKey;
    }

    @Generated
    public String getKuaidi100Key() {
        return this.kuaidi100Key;
    }

    @Generated
    public String getKuaidi100Customer() {
        return this.kuaidi100Customer;
    }

    @Generated
    public void setNotifyHost(String notifyHost) {
        this.notifyHost = notifyHost;
    }

    @Generated
    public void setLogisticsKey(String logisticsKey) {
        this.logisticsKey = logisticsKey;
    }

    @Generated
    public void setKuaidi100Key(String kuaidi100Key) {
        this.kuaidi100Key = kuaidi100Key;
    }

    @Generated
    public void setKuaidi100Customer(String kuaidi100Customer) {
        this.kuaidi100Customer = kuaidi100Customer;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MallConfigProperties)) {
            return false;
        }
        MallConfigProperties other = (MallConfigProperties)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$notifyHost = this.getNotifyHost();
        String other$notifyHost = other.getNotifyHost();
        if (this$notifyHost == null ? other$notifyHost != null : !this$notifyHost.equals(other$notifyHost)) {
            return false;
        }
        String this$logisticsKey = this.getLogisticsKey();
        String other$logisticsKey = other.getLogisticsKey();
        if (this$logisticsKey == null ? other$logisticsKey != null : !this$logisticsKey.equals(other$logisticsKey)) {
            return false;
        }
        String this$kuaidi100Key = this.getKuaidi100Key();
        String other$kuaidi100Key = other.getKuaidi100Key();
        if (this$kuaidi100Key == null ? other$kuaidi100Key != null : !this$kuaidi100Key.equals(other$kuaidi100Key)) {
            return false;
        }
        String this$kuaidi100Customer = this.getKuaidi100Customer();
        String other$kuaidi100Customer = other.getKuaidi100Customer();
        return !(this$kuaidi100Customer == null ? other$kuaidi100Customer != null : !this$kuaidi100Customer.equals(other$kuaidi100Customer));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof MallConfigProperties;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $notifyHost = this.getNotifyHost();
        result = result * 59 + ($notifyHost == null ? 43 : $notifyHost.hashCode());
        String $logisticsKey = this.getLogisticsKey();
        result = result * 59 + ($logisticsKey == null ? 43 : $logisticsKey.hashCode());
        String $kuaidi100Key = this.getKuaidi100Key();
        result = result * 59 + ($kuaidi100Key == null ? 43 : $kuaidi100Key.hashCode());
        String $kuaidi100Customer = this.getKuaidi100Customer();
        result = result * 59 + ($kuaidi100Customer == null ? 43 : $kuaidi100Customer.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "MallConfigProperties(notifyHost=" + this.getNotifyHost() + ", logisticsKey=" + this.getLogisticsKey() + ", kuaidi100Key=" + this.getKuaidi100Key() + ", kuaidi100Customer=" + this.getKuaidi100Customer() + ")";
    }
}

