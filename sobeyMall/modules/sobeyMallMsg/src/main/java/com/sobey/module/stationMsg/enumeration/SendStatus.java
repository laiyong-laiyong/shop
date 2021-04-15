package com.sobey.module.stationMsg.enumeration;

/**
 * @Author WCY
 * @CreateTime 2020/4/7 18:28
 * 发送状态
 */
public enum SendStatus {

    NotSent("0","未发送"),
    Sent("1","已发送");
    private String code;
    private String desc;

    SendStatus(String code, String desc) {
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
