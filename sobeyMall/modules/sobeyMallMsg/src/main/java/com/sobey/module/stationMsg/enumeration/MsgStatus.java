package com.sobey.module.stationMsg.enumeration;

/**
 * @Author WCY
 * @CreateTime 2020/4/3 17:53
 * 消息状态
 */
public enum MsgStatus {
    Unread("0","未读"),
    HaveRead("1","已读");
    private String code;
    private String desc;

    MsgStatus(String code, String desc) {
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
