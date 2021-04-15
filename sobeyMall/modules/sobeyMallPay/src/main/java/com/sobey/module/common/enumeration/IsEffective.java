package com.sobey.module.common.enumeration;

/**
 * @author WCY
 * @createTime 2020/5/20 11:45
 * 是否有效
 */
public enum IsEffective {

    Expire("0","已过期"),
    Effective("1","有效"),
    UseUp("2","已用完");
    private String code;
    private String desc;

    IsEffective(String code, String desc) {
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
