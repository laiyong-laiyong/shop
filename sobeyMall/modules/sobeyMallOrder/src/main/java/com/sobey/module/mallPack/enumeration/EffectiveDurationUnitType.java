package com.sobey.module.mallPack.enumeration;

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
