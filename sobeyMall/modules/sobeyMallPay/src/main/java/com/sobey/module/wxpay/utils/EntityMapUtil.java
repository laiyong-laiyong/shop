package com.sobey.module.wxpay.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 实体类与Map相关工具类
 * @Author WuChenYang
 * @Since 2020/1/14 14:54
 */
public class EntityMapUtil {

    private static final Logger log = LoggerFactory.getLogger(EntityMapUtil.class);

    /**
     * 实体类转map，map的key为实体类中对应的字段名
     * @param obj
     * @return
     */
    public static Map<String,String> entityToMap(Object obj){

        if (null == obj){
            return null;
        }
        Map<String,String> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object o = field.get(obj);
                if (null == o || StringUtils.isBlank(o.toString())){
                    continue;
                }
                map.put(field.getName(),o.toString());
            } catch (IllegalAccessException e) {
                log.info(clazz.getTypeName()+"--"+e.getMessage());
            }
        }
        return map;
    }

    /**
     * map转为实体类对象(仅限于字段为String或Integer两种类型的对象)
     * @param map
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T>T mapToEntity(Map<String,String> map,Class<T> clazz) throws Exception{

        if (null == map || map.size() == 0 || clazz == null){
            return null;
        }
        T instance = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (entry.getKey().trim().equals(fieldName)){
                    Method getMethod =
                            clazz.getMethod("get"+fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1),null);
                    Class<?> returnType = getMethod.getReturnType();
                    if (Integer.class.getTypeName().equals(returnType.getTypeName())){
                        field.set(instance,Integer.valueOf(entry.getValue().trim()));
                    }else {
                        field.set(instance, entry.getValue().trim());
                    }
                    break;
                }
            }
        }
        return instance;
    }

}
