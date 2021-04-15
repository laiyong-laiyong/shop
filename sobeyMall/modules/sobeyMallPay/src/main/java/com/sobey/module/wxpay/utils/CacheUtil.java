package com.sobey.module.wxpay.utils;

import com.alibaba.fastjson.JSONObject;
import com.sobey.framework.spring.SpringContextHolder;
import com.sobey.module.token.fegin.AuthService;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author WCY
 * @CreateTime 2020/4/27 11:15
 * 缓存工具类
 */
public class CacheUtil {

    private static final ConcurrentHashMap<String,String> cacheMap = new ConcurrentHashMap<>(64);

    // 定时器线程池，用于清除过期缓存
    private final static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private static final long expire = 7200000L;//两个小时

    /**
     * 将key-value放入缓存,并指定失效时间>0,若失效时间小于等于0则默认两个小时失效
     * @param key
     * @param value
     * @param expire 毫秒
     * @return
     */
    public static String put(String key,String value,long expire){
        String oldVal = cacheMap.put(key, value);
        if (expire <= 0){
            expire = CacheUtil.expire;
        }
        executor.schedule(()->{
            cacheMap.remove(key);
        },expire, TimeUnit.MILLISECONDS);
        return oldVal;
    }

    /**
     * 默认两个小时失效
     * @param key
     * @param value
     * @return
     */
    public static String put(String key,String value){
        return put(key, value,expire);
    }

    public static String remove(String key){
        return cacheMap.remove(key);
    }

    public static String get(String key){
        return cacheMap.get(key);
    }

    /**
     * 获取token
     * @return token
     */
    public static String getToken(){
        String token = CacheUtil.get("token");
        if (StringUtils.isBlank(token)) {
            AuthService authService = SpringContextHolder.getBean(AuthService.class);
            JSONObject jsonObject = authService.getToken();
            token = jsonObject.getString("access_token");
            //1h 过期
            CacheUtil.put("token", token.trim(), 1000*60*60L);
        }
        return token;
    }

}
