package com.sobey.module.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author WCY
 * @createTime 2020/7/9 11:46
 */
@Component
public class RedisLock {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * @param key
     * @param value
     * @param timeout 超时时间 毫秒
     * @return
     */
    public boolean lock(String key, String value, long timeout) {
        Boolean absent = redisTemplate.boundValueOps(key).setIfAbsent(value, timeout, TimeUnit.MILLISECONDS);
        return null == absent ? false : absent;
    }

    public boolean unlock(String key) {
        Boolean delete = redisTemplate.delete(key);
        return delete == null ? false : delete;
    }

}
