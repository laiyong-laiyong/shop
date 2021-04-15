package com.sobey.module.common.enumeration;

/**
 * @Description 开通类型类型
 * @Author WuChenYang
 * @CreateTime 2020/3/13 17:07
 */
public enum OpenType {

    Cycle("1","包周期(按版本)"),
    Demand("2","按量");
    private String code;
    private String desc;

    /**
     * 判断是否是正确的开通类型
     * @param code
     * @return
     */
    public static boolean isContained(String code){
        for (OpenType value : OpenType.values()) {
            if (value.getCode().equals(code)){
                return true;
            }
        }
        return false;
    }

    OpenType(String code, String desc) {
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
