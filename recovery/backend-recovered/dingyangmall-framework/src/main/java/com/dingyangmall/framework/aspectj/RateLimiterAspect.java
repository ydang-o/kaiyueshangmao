/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.aspectj;

import com.dingyangmall.common.annotation.RateLimiter;
import com.dingyangmall.common.enums.LimitType;
import com.dingyangmall.common.exception.ServiceException;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.common.utils.ip.IpUtils;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RateLimiterAspect {
    private static final Logger log = LoggerFactory.getLogger(RateLimiterAspect.class);
    private RedisTemplate<String, Object> redisTemplate;
    private RedisScript<Long> limitScript;

    @Autowired
    public void setRedisTemplate1(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setLimitScript(RedisScript<Long> limitScript) {
        this.limitScript = limitScript;
    }

    @Before(value="@annotation(rateLimiter)")
    public void doBefore(JoinPoint point, RateLimiter rateLimiter) throws Throwable {
        int time = rateLimiter.time();
        int count = rateLimiter.count();
        String combineKey = this.getCombineKey(rateLimiter, point);
        List<String> keys = Collections.singletonList(combineKey);
        try {
            Long number = this.redisTemplate.execute(this.limitScript, keys, count, time);
            if (StringUtils.isNull(number) || number.intValue() > count) {
                throw new ServiceException("\u8bbf\u95ee\u8fc7\u4e8e\u9891\u7e41\uff0c\u8bf7\u7a0d\u5019\u518d\u8bd5");
            }
            log.info("\u9650\u5236\u8bf7\u6c42'{}',\u5f53\u524d\u8bf7\u6c42'{}',\u7f13\u5b58key'{}'", count, number.intValue(), combineKey);
        }
        catch (ServiceException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException("\u670d\u52a1\u5668\u9650\u6d41\u5f02\u5e38\uff0c\u8bf7\u7a0d\u5019\u518d\u8bd5");
        }
    }

    public String getCombineKey(RateLimiter rateLimiter, JoinPoint point) {
        StringBuffer stringBuffer = new StringBuffer(rateLimiter.key());
        if (rateLimiter.limitType() == LimitType.IP) {
            stringBuffer.append(IpUtils.getIpAddr()).append("-");
        }
        MethodSignature signature = (MethodSignature)point.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = method.getDeclaringClass();
        stringBuffer.append(targetClass.getName()).append("-").append(method.getName());
        return stringBuffer.toString();
    }
}

