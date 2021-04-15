package com.sobey.module.productservice.entity.charge;

/**
 * @author WCY
 * @createTime 2020/5/14 18:27
 */
public class CloseParam {

    private String appId;
    private String userCode;
    private String openType;
    private Charge[] charg;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
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

    public Charge[] getCharg() {
        return charg;
    }

    public void setCharg(Charge[] charg) {
        this.charg = charg;
    }
}
