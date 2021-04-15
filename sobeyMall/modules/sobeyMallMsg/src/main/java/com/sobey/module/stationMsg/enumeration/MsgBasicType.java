package com.sobey.module.stationMsg.enumeration;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author WCY
 * @CreateTime 2020/4/6 19:59
 * 基础消息类型
 */
public enum MsgBasicType {

    System("0","系统类型消息"),
    Announcement("1","广播类型消息(发给所有人)"),
    Proclamation("2","公告类型消息");
    private String code;
    private String desc;

    MsgBasicType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 判断code是否正确
     * @param code
     * @return
     */
    public static boolean isTrueCode(String code){
        if (StringUtils.isBlank(code)){
            return false;
        }
        for (MsgBasicType basicType : MsgBasicType.values()) {
            if (basicType.getCode().equals(code.trim())){
                return true;
            }
        }
        return false;
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
