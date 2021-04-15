package com.sobey.module.common.enumeration;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * @author WCY
 * @createTime 2020/5/20 10:59
 * 有效时长单位
 */
public enum EffectiveDurationUnitType {
    Year("1","年"),
    Month("2","月"),
    Day("3","日"),
    Hour("4","小时");

    /**
     * 根据有效时长与时长单位和生效时间获取失效时间
     * @param code
     * @param duration
     * @param effectiveDate
     * @return
     */
    public static Date getExpireDate(String code,int duration,Date effectiveDate){

        for (EffectiveDurationUnitType type : EffectiveDurationUnitType.values()) {
            if (type.getCode().equals(code)){
                switch (type){
                    case Year:
                        return DateUtils.addYears(effectiveDate,duration);
                    case Month:
                        return DateUtils.addMonths(effectiveDate,duration);
                    case Day:
                        return DateUtils.addDays(effectiveDate,duration);
                    case Hour:
                        return DateUtils.addHours(effectiveDate,duration);
                }
            }
        }
        return effectiveDate;
    }

    private String code;
    private String desc;

    EffectiveDurationUnitType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
