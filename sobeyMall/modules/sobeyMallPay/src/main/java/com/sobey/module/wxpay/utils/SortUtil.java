package com.sobey.module.wxpay.utils;

import java.util.*;

/**
 * @Description 排序工具
 * @Author WuChenYang
 * @Since 2019/10/29 17:08
 */
public class SortUtil {

    /**
     * 将参数按照字母排序
     * @param paramMap
     * @return
     */
    public static List<Map.Entry<String, String>> sortParam(Map<String,String> paramMap){
        if (null == paramMap || paramMap.size() == 0){
            return null;
        }
        Set<Map.Entry<String, String>> entries = paramMap.entrySet();
        List<Map.Entry<String, String>> list = new LinkedList<>(entries);
        list.sort((o1,o2)->{
            String key1 = o1.getKey();
            String key2 = o2.getKey();
            int result = key1.charAt(0) - key2.charAt(0);
            //如果等于0继续比较后续字符
            if (result == 0){
                char[] chars1 = key1.toCharArray();
                char[] chars2 = key2.toCharArray();
                if (chars1.length >= chars2.length){
                    for (int i = 1; i < chars2.length; i++) {
                        result = chars1[i] - chars2[i];
                        if (result == 0){
                            //如果较长字符串的前一部分与较短字符串全部相同则将较短字符串放在前边
                            if (i == chars2.length-1){
                                result = 1;
                            }
                        }else {
                            break;
                        }
                    }
                }
                if (chars1.length < chars2.length){
                    for (int i = 1; i < chars1.length; i++) {
                        result = chars1[i] - chars2[i];
                        if (result == 0){
                            if (i == chars1.length-1){
                                result = -1;
                            }
                        }else {
                            break;
                        }
                    }
                }
            }
            return Integer.compare(result,0);
        });
        return list;
    }

}
