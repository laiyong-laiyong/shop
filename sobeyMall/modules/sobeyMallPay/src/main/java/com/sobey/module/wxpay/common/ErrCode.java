package com.sobey.module.wxpay.common;

import org.apache.commons.lang3.StringUtils;

/**
 * @Description 返回结果中的错误码列表
 * @Author WuChenYang
 * @Since 2020/1/14 11:41
 */
public enum ErrCode {

    /**
     * 统一下单相关错误
     */
    INVALID_REQUEST("INVALID_REQUEST","参数错误"),
    NOAUTH("NOAUTH","商户无此接口权限"),
    NOTENOUGH("NOTENOUGH","余额不足"),
    ORDERPAID("ORDERPAID","商户订单已支付"),
    ORDERCLOSED("ORDERCLOSED","订单已关闭"),
    SYSTEMERROR("SYSTEMERROR","系统错误"),
    APPID_NOT_EXIST("APPID_NOT_EXIST","APPID不存在"),
    MCHID_NOT_EXIST("MCHID_NOT_EXIST","MCHID不存在"),
    APPID_MCHID_NOT_MATCH("APPID_MCHID_NOT_MATCH","appid和mch_id不匹配"),
    LACK_PARAMS("LACK_PARAMS","缺少参数"),
    OUT_TRADE_NO_USED("OUT_TRADE_NO_USED","商户订单号重复"),
    SIGNERROR("SIGNERROR","签名错误"),
    XML_FORMAT_ERROR("XML_FORMAT_ERROR","XML格式错误"),
    REQUIRE_POST_METHOD("REQUIRE_POST_METHOD","请使用post方法"),
    POST_DATA_EMPTY("POST_DATA_EMPTY","post数据为空"),
    NOT_UTF8("NOT_UTF8","编码格式错误"),

    /**
     * 订单查询相关错误
     */
    ORDER_NOT_EXIST("ORDERNOTEXIST","交易订单号不存在"),
    SYSTEM_ERROR("SYSTEMERROR","系统错误");


    /**
     * 通过code获取错误码描述
     * @param code
     * @return
     */
    public static String getDesc(String code){
        if (StringUtils.isEmpty(code)){
            return "";
        }
        for (ErrCode errCode : ErrCode.values()) {
            if (errCode.getCode().equals(code)){
                return errCode.getDesc();
            }
        }
        return "Unknown Error";
    }


    private String code;
    private String desc;


    ErrCode(String code, String desc) {
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
