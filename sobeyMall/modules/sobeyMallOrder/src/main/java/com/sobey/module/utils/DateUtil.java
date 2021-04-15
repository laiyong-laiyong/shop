package com.sobey.module.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author WCY
 * @createTime 2020/5/15 14:53
 */
public class DateUtil {

    private DateUtil() {

    }

    public static final String FORMAT_1 = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_2 = "yyyy/MM/dd HH:mm:ss";
    public static final String FORMAT_3 = "yyyy/MM/dd";
    public static final String FORMAT_4 = "yyyy-MM-dd";
    public static final String FORMAT_5 = "yyyyMMddHHmmss";
    public static final String FORMAT_6 = "dd/MM/yyyy";
    public static final String FORMAT_7 = "yyyy年MM月dd日 HH:mm:ss";
    public static final String FORMAT_8 = "yyyy年MM月dd日";
    public static final String FORMAT_9 = "yyyy年MM月dd日 HH时mm分ss秒";

    private static final SimpleDateFormat format = new SimpleDateFormat();

    private static TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");

    static {
        //默认时区
        format.setTimeZone(timeZone);
    }

    /**
     * 指定TimeZone
     * @param timeZone
     */
    public static void setTimeZone(TimeZone timeZone){
        format.setTimeZone(timeZone);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        if (StringUtils.isBlank(pattern)){
            throw new RuntimeException("pattern should be not null");
        }
        if (null == date){
            throw new RuntimeException("date should be not blank");
        }
        format.applyPattern(pattern);
        return format.format(date);
    }

    /**
     * 将字符串日期转换为{@link Date},默认时区 GMT+8
     * @param dateText
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date parse(String dateText,String pattern) throws ParseException {
        if (StringUtils.isBlank(dateText)){
            throw new RuntimeException("dateText should be not blank");
        }
        if (StringUtils.isBlank(pattern)){
            throw new RuntimeException("pattern should be not blank");
        }
        format.applyPattern(pattern);
        return format.parse(dateText);
    }

}
