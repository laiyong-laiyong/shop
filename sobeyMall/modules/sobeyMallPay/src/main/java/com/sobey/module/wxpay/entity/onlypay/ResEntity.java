package com.sobey.module.wxpay.entity.onlypay;

/**
 * @Author WCY
 * @CreateTime 2020/4/27 13:44
 */
public class ResEntity {

    private String resultCode;
    private String errorMsg;
    private String codeUrl;
    private String prePayId;

    public String getPrePayId() {
        return prePayId;
    }

    public void setPrePayId(String prePayId) {
        this.prePayId = prePayId;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }
}
