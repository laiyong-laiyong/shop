package com.sobey.util.common.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Rukiy on 2017-11-30
 */
public class JsonKit {

    private static final SerializeConfig config;

    static {
        config = new SerializeConfig();
//        config.put(java.util.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
//        config.put(java.sql.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
    }

    private static final SerializerFeature[] features = {SerializerFeature.WriteMapNullValue, // 输出空置字段
            SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
            SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
            SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
            SerializerFeature.WriteNullStringAsEmpty, // 字符类型字段如果为null，输出为""，而不是null
            SerializerFeature.WriteDateUseDateFormat,
            SerializerFeature.DisableCircularReferenceDetect,    //关闭循环引用的时候出现 $ref
    };


    /**
     * Bean对象转JSON
     *
     * @param object
     * @return
     */
    public static String beanToJson(Object object) {
        return beanToJson(object, null);
    }

    /**
     * Bean对象转JSON
     *
     * @param object
     * @param dataFormatString
     * @return
     */
    public static String beanToJson(Object object, String dataFormatString) {
        if (object != null) {
            if (StringUtils.isEmpty(dataFormatString)) {
                return JSON.toJSONString(object, features);
            }
            return JSON.toJSONStringWithDateFormat(object, dataFormatString, features);
        } else {
            return null;
        }
    }


    /**
     * String转JSON字符串
     *
     * @param key
     * @param value
     * @return
     */
    public static String strToJson(String key, Object value) {
        if (StringUtils.isEmpty(key) || value == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>(16);
        map.put(key, value);
        return beanToJson(map);
    }

    /**
     * 将json字符串转换成对象
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T jsonToBean(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json) || clazz == null) {
            return null;
        }
        return (T) JSON.parseObject(json, clazz, JSON.DEFAULT_PARSER_FEATURE, new Feature[0]);
    }

    /**
     * json字符串转map
     *
     * @param json
     * @return
     */
    public static Map<String, Object> jsonToMap(String json) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        return jsonToBean(json, Map.class);
    }


    /**
     * json字符串转Array
     *
     * @param json
     * @return
     */
    public static <T> T[] jsonToArray(String json, Class<T> clazz) {
        List<T> list = jsonToList(json, clazz);
        if (list == null) return null;
        int size = list.size();
        int i = 0;
        T[] array = (T[]) Array.newInstance(clazz, size);
        for (Iterator<T> itr = list.iterator();
             itr.hasNext(); ) {
            array[i++] = itr.next();
        }
        return array;
    }

    /**
     * json字符串转List
     *
     * @param json
     * @return
     */
    public static <T> List<T> jsonToList(String json, Class<T> clazz) {
        if (json == null) {
            return null;
        }
        List<T> list;
        DefaultJSONParser parser = new DefaultJSONParser(json);
        JSONLexer lexer = parser.getLexer();
        if (lexer.token() == JSONToken.NULL) {
            lexer.nextToken();
            list = null;
        } else {
            list = new ArrayList<>();
            parser.parseArray(clazz, list);
        }
        parser.close();
        return list;
    }
    
    /**
     * C枚举转json
     * 
     * @param enumClass
     * @return
     */
    @SuppressWarnings("unchecked")
	public static String enumToJson(Class<? extends Enum> enumClass) {
        Method methodValues;
        Object invoke = null;
		try {
			methodValues = enumClass.getMethod("values");
			invoke = methodValues.invoke(null);
		} catch (NoSuchMethodException | SecurityException  | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
		}
		
        int length = java.lang.reflect.Array.getLength(invoke);
        List<Object> values = new ArrayList<Object>();
        for (int i=0; i<length; i++) {
            values.add(java.lang.reflect.Array.get(invoke, i));
        }

        SerializeConfig config = new SerializeConfig();
        config.configEnumAsJavaBean(enumClass);

        return JSON.toJSONString(values,config);
    }
}
