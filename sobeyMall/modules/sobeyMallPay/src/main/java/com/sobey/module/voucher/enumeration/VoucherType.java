package com.sobey.module.voucher.enumeration;

/**
 * @author WCY
 * @createTime 2020/7/13 17:56
 * 代金券类别
 */
public enum VoucherType {

    Platform("0","平台类"),
    SalesPromotion("1","销售推广类");


    VoucherType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private String code;
    private String desc;

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
