/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.config;

import com.dingyangmall.framework.config.FastJson2JsonRedisSerializer;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig
extends CachingConfigurerSupport {
    @Value(value="${spring.redis.host:127.0.0.1}")
    private String redisHost;
    @Value(value="${spring.redis.port:6379}")
    private int redisPort;
    @Value(value="${spring.redis.password:}")
    private String redisPassword;
    @Value(value="${spring.redis.database:0}")
    private int redisDatabase;
    @Value(value="${spring.redis.timeout:10s}")
    private Duration redisTimeout;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(this.redisHost);
        config.setPort(this.redisPort);
        config.setDatabase(this.redisDatabase);
        if (this.redisPassword != null && !this.redisPassword.isEmpty()) {
            config.setPassword(this.redisPassword);
        }
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder().commandTimeout(this.redisTimeout).build();
        return new LettuceConnectionFactory(config, clientConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(connectionFactory);
        FastJson2JsonRedisSerializer<Object> serializer = new FastJson2JsonRedisSerializer<Object>(Object.class);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public DefaultRedisScript<Long> limitScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<Long>();
        redisScript.setScriptText(this.limitScriptText());
        redisScript.setResultType(Long.class);
        return redisScript;
    }

    private String limitScriptText() {
        return "local key = KEYS[1]\nlocal count = tonumber(ARGV[1])\nlocal time = tonumber(ARGV[2])\nlocal current = redis.call('get', key);\nif current and tonumber(current) > count then\n    return tonumber(current);\nend\ncurrent = redis.call('incr', key)\nif tonumber(current) == 1 then\n    redis.call('expire', key, time)\nend\nreturn tonumber(current);";
    }
}

