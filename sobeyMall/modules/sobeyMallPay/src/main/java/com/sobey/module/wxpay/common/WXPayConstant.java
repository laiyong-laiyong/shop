package com.sobey.module.wxpay.common;

/**
 * @Description 微信支付相关的一些常量
 * @Author WuChenYang
 * @Since 2020/1/14 10:31
 */
public class WXPayConstant {

    //统一下单url
    public static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    //查询订单url
    public static final String ORDER_QUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
    //退款接口
    public static final String ORDER_REFUND_URL="https://api.mch.weixin.qq.com/secapi/pay/refund";
    //MD5算法
    public static final String MD5 = "MD5";
    //HAMC-SHA256算法
    public static final String HMAC_SHA256 = "HMAC-SHA256";
    //货币类型，境内只支持人民币CNY
    public static final String CNY = "CNY";

    //交易类型
    public enum TradeType{
        JSAPI,//JSAPI支付
        NATIVE,//Native支付
        APP, //APP支付
        MWEB //H5支付
    }


}
