package com.sobey.module.wxpay.entity.onlypay;

import java.util.Map;

/**
 * @author WCY
 * @createTime 2020/5/11 18:51
 * 请求贝影接口的返回值
 */
public class BeingResp {

    private String error_code;
    private String error_msg;
    private Map<String,String> extend_message;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public Map<String, String> getExtend_message() {
        return extend_message;
    }

    public void setExtend_message(Map<String, String> extend_message) {
        this.extend_message = extend_message;
    }
}
