/*
 * Decompiled with CFR.
 */
package com.dingyangmall.common.core.redis;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisCache {
    @Autowired
    public RedisTemplate redisTemplate;

    public <T> void setCacheObject(String key, T value) {
        this.redisTemplate.opsForValue().set(key, value);
    }

    public <T> void setCacheObject(String key, T value, Integer timeout, TimeUnit timeUnit) {
        this.redisTemplate.opsForValue().set(key, value, timeout.intValue(), timeUnit);
    }

    public boolean expire(String key, long timeout) {
        return this.expire(key, timeout, TimeUnit.SECONDS);
    }

    public boolean expire(String key, long timeout, TimeUnit unit) {
        return this.redisTemplate.expire(key, timeout, unit);
    }

    public long getExpire(String key) {
        return this.redisTemplate.getExpire(key);
    }

    public Boolean hasKey(String key) {
        return this.redisTemplate.hasKey(key);
    }

    public <T> T getCacheObject(String key) {
        ValueOperations operation = this.redisTemplate.opsForValue();
        return (T)operation.get(key);
    }

    public boolean deleteObject(String key) {
        return this.redisTemplate.delete(key);
    }

    public boolean deleteObject(Collection collection) {
        return this.redisTemplate.delete(collection) > 0L;
    }

    public <T> long setCacheList(String key, List<T> dataList) {
        Long count = this.redisTemplate.opsForList().rightPushAll(key, (Collection<T>)dataList);
        return count == null ? 0L : count;
    }

    public <T> List<T> getCacheList(String key) {
        return this.redisTemplate.opsForList().range(key, 0L, -1L);
    }

    public <T> BoundSetOperations<String, T> setCacheSet(String key, Set<T> dataSet) {
        BoundSetOperations<String, Object> setOperation = this.redisTemplate.boundSetOps(key);
        Iterator<T> it = dataSet.iterator();
        while (it.hasNext()) {
            setOperation.add(it.next());
        }
        return setOperation;
    }

    public <T> Set<T> getCacheSet(String key) {
        return this.redisTemplate.opsForSet().members(key);
    }

    public <T> void setCacheMap(String key, Map<String, T> dataMap) {
        if (dataMap != null) {
            this.redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    public <T> Map<String, T> getCacheMap(String key) {
        return this.redisTemplate.opsForHash().entries(key);
    }

    public <T> void setCacheMapValue(String key, String hKey, T value) {
        this.redisTemplate.opsForHash().put(key, hKey, value);
    }

    public <T> T getCacheMapValue(String key, String hKey) {
        HashOperations opsForHash = this.redisTemplate.opsForHash();
        return (T)opsForHash.get(key, hKey);
    }

    public <T> List<T> getMultiCacheMapValue(String key, Collection<Object> hKeys) {
        return this.redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    public boolean deleteCacheMapValue(String key, String hKey) {
        return this.redisTemplate.opsForHash().delete(key, hKey) > 0L;
    }

    public Collection<String> keys(String pattern) {
        return this.redisTemplate.keys(pattern);
    }
}

