package com.sobey.module.pay;

import org.apache.commons.lang3.StringUtils;

/**
 * @Description 支付状态
 * @Author WuChenYang
 * @Since 2020/2/4 16:03
 */
public enum PayStatus {

    ToBePaid("0","待支付"),//待支付 - 0
    Payments("1","支付中"),//支付中 - 1
    Paid("2","已支付"),//已支付 - 2
    Failure("3","支付失败"),//失败 - 3
    Cancelled("4","已取消"),//已取消 - 4
    Expired("5","已失效");//5 已失效

    /**
     * 获取状态说明
     * @param code
     * @return
     */
    public static String getDesc(String code){
        if (StringUtils.isBlank(code)){
            return null;
        }
        for (PayStatus payStatus : PayStatus.values()) {
            if (payStatus.getCode().equals(code)){
                return payStatus.getDesc();
            }
        }
        return null;
    }

    private String code;
    private String desc;

    PayStatus(String code, String desc) {
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