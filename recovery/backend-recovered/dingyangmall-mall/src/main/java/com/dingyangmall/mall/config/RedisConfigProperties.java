/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.config;

import lombok.Generated;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="spring.redis")
public class RedisConfigProperties {
    private String host;
    private String port;
    private String password;
    private String database;

    @Generated
    public RedisConfigProperties() {
    }

    @Generated
    public String getHost() {
        return this.host;
    }

    @Generated
    public String getPort() {
        return this.port;
    }

    @Generated
    public String getPassword() {
        return this.password;
    }

    @Generated
    public String getDatabase() {
        return this.database;
    }

    @Generated
    public void setHost(String host) {
        this.host = host;
    }

    @Generated
    public void setPort(String port) {
        this.port = port;
    }

    @Generated
    public void setPassword(String password) {
        this.password = password;
    }

    @Generated
    public void setDatabase(String database) {
        this.database = database;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RedisConfigProperties)) {
            return false;
        }
        RedisConfigProperties other = (RedisConfigProperties)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$host = this.getHost();
        String other$host = other.getHost();
        if (this$host == null ? other$host != null : !this$host.equals(other$host)) {
            return false;
        }
        String this$port = this.getPort();
        String other$port = other.getPort();
        if (this$port == null ? other$port != null : !this$port.equals(other$port)) {
            return false;
        }
        String this$password = this.getPassword();
        String other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) {
            return false;
        }
        String this$database = this.getDatabase();
        String other$database = other.getDatabase();
        return !(this$database == null ? other$database != null : !this$database.equals(other$database));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof RedisConfigProperties;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $host = this.getHost();
        result = result * 59 + ($host == null ? 43 : $host.hashCode());
        String $port = this.getPort();
        result = result * 59 + ($port == null ? 43 : $port.hashCode());
        String $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        String $database = this.getDatabase();
        result = result * 59 + ($database == null ? 43 : $database.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "RedisConfigProperties(host=" + this.getHost() + ", port=" + this.getPort() + ", password=" + this.getPassword() + ", database=" + this.getDatabase() + ")";
    }
}

