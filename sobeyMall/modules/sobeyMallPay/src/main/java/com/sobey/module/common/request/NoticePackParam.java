package com.sobey.module.common.request;

import java.util.List;

/**
 * @author WCY
 * @createTime 2020/8/13 10:52
 * 通知套餐包购买时的参数类
 */
public class NoticePackParam {

    private String userCode;
    private String siteCode;
    private String packUuid;
    private String packName;
    private String expireDate;
    private List<NoticePackResource> resources;

    public String getPackUuid() {
        return packUuid;
    }

    public void setPackUuid(String packUuid) {
        this.packUuid = packUuid;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public List<NoticePackResource> getResources() {
        return resources;
    }

    public void setResources(List<NoticePackResource> resources) {
        this.resources = resources;
    }
}
