package com.sobey.module.stationMsg.enumeration;

/**
 * @author WCY
 * @createTime 2020/7/9 15:34
 */
public enum IsOnDuty {

    OFF("0","休息"),
    ON("1","值班");


    public static boolean contains(String code){
        for (IsOnDuty value : IsOnDuty.values()) {
            if (value.getCode().equals(code)){
                return true;
            }
        }
        return false;
    }

    private String code;
    private String desc;

    IsOnDuty(String code, String desc) {
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
