package com.sobey.module.stationMsg.enumeration;

/**
 * @Author WCY
 * @CreateTime 2020/4/14 14:14
 * 公告状态
 */
public enum ProclamationStatus {

    Unpublished("0","未发布"),
    Published("1","已发布"),
    Revoked("2","已撤销");
    private String code;
    private String desc;

    ProclamationStatus(String code, String desc) {
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
