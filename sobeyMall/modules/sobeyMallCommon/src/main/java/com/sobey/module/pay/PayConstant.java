package com.sobey.module.pay;

/**
 * @Description TODO...
 * @Author WuChenYang
 * @CreateTime 2020/2/24 15:48
 */
public class PayConstant {

    /**
     * 交易类型
     */
    public enum TransType{

        CONSUMPTION("0","消费"),
        RECHARGE("1","充值");

        private String code;
        private String desc;

        TransType(String code, String desc) {
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

    /**
     * 交易方式
     */
    public enum PayMethod{

        Balance("0","余额"),
        WXPay("1","微信"),
        ZFBPay("2","支付宝"),
        Voucher("3","代金券");


        public static String getDesc(String code){
            for (PayMethod payMethod : PayMethod.values()) {
                if (payMethod.getCode().equals(code)){
                    return payMethod.getDesc();
                }
            }
            return "";
        }

        private String code;
        private String desc;

        PayMethod(String code, String desc) {
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

}
