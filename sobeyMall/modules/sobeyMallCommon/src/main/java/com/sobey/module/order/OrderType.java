package com.sobey.module.order;

/**
 * @Description 订单类型
 * @Author WuChenYang
 * @CreateTime 2020/2/11 15:29
 */
public enum OrderType {

    New("0","新购"),
    Renew("1","续费"),
    Unsubscribe("2","退订");
    private String code;
    private String desc;


    public static String getDesc(String code){

        for (OrderType orderType : OrderType.values()) {
            if (orderType.getCode().equals(code)){
                return orderType.getDesc();
            }
        }

        return "";
    }

    /**
     * 判断是否是正确的订单类型
     * @return
     */
    public static boolean isContained(String code){

        for (OrderType orderType : OrderType.values()) {
            if (orderType.getCode().equals(code)){
                return true;
            }
        }
        return false;

    }

    OrderType(String code, String desc) {
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
