package com.sobey.util.common.regex;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * @Description 正则工具类
 * @Author WuChenYang
 * @CreateTime 2020/2/19 17:55
 */
public class PatternUtil {

    private static final String[] special = new String[]{"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|"};

    /**
     * 左模糊
     *
     * @param keyword
     * @return
     */
    public static Pattern left(String keyword) {

        String value = escapeExprSpecialWord(keyword);
        return Pattern.compile("^.*" + value + "$");

    }

    /**
     * 右模糊
     *
     * @param keyword
     * @return
     */
    public static Pattern right(String keyword) {

        String value = escapeExprSpecialWord(keyword);
        return Pattern.compile("^" + value + ".*$");

    }

    /**
     * 模糊匹配
     *
     * @param keyword
     * @return
     */
    public static Pattern obscure(String keyword) {

        String value = escapeExprSpecialWord(keyword);
        return Pattern.compile("^.*" + value + ".*$");

    }

    /**
     * 需要模糊查询的字符串中如果包含特殊字符需要转义
     *
     * @param keyword
     * @return
     */
    public static String escapeExprSpecialWord(String keyword) {

        if (StringUtils.isBlank(keyword)) {
            return keyword;
        }

        for (String key : special) {
            if (keyword.contains(key)) {
                keyword = keyword.replace(key, "\\" + key);
            }
        }
        return keyword;
    }

    /**
     * 校验金额整数位长度是否<=11，小数位长度是否<=2
     *
     * @param val
     * @return
     */
    public static boolean regNumber(String val) {

        if (StringUtils.isBlank(val)) {
            return false;
        }
        //如果包含小数就用小数位匹配
        if (val.contains(".")){
            return Pattern.compile("^[0-9]{1,11}\\.[0-9]{1,2}$").matcher(val).find();
        }
        return Pattern.compile("^[0-9]{1,11}$").matcher(val).find();

    }

}
