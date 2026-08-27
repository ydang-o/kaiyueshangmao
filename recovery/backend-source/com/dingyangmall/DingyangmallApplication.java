/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.dingyangmall.framework.config.VolcSmsProperties
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.boot.SpringApplication
 *  org.springframework.boot.autoconfigure.SpringBootApplication
 *  org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
 *  org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
 *  org.springframework.boot.context.properties.EnableConfigurationProperties
 *  org.springframework.scheduling.annotation.EnableScheduling
 */
package com.dingyangmall;

import com.dingyangmall.framework.config.VolcSmsProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableConfigurationProperties(value={VolcSmsProperties.class})
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, RedisAutoConfiguration.class})
public class DingyangmallApplication {
    private static final Logger log = LoggerFactory.getLogger(DingyangmallApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DingyangmallApplication.class, (String[])args);
        log.info("\u5982\u56cd\u4f18\u9009\u542f\u52a8\u6210\u529f");
    }
}

