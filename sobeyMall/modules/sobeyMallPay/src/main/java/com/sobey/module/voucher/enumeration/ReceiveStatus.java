package com.sobey.module.voucher.enumeration;

/**
 * @author wcy
 * 代金券领取状态
 */
public enum ReceiveStatus {
    No("0","未领取"),
    Yes("1","已领取"),
    Expire("2","已过期");
    private String code;
    private String desc;

    ReceiveStatus(String code, String desc) {
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
