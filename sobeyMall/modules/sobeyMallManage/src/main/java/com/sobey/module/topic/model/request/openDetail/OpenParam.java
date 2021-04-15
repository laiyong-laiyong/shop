package com.sobey.module.topic.model.request.openDetail;

/**
 * @author WCY
 * @createTime 2020/5/14 18:26
 * 商品开通参数
 */
public class OpenParam {

    private String appId;
    private String userCode;
    private String openType;
    private String productCode;
    private String versionCode;
    private String expirationDate;//到期时间
    private Charge[] charg;

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getAppId() {
        return appId;
    }

    public OpenParam setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public Charge[] getCharg() {
        return charg;
    }

    public void setCharg(Charge[] charg) {
        this.charg = charg;
    }

}
