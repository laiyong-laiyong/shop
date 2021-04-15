package com.sobey.module.fegin.order.entity.request;

/**
 * @Description 商品开通续费的参数
 * @Author WuChenYang
 * @CreateTime 2020/3/17 10:51
 */
public class ProductParam {

    private boolean debug;
    private String appId;
    private String userCode;
    private String openType;
    private String productCode;
    private String versionCode;
    private Charge[] charg;

    public String getAppId() {
        return appId;
    }

    public ProductParam setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
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
