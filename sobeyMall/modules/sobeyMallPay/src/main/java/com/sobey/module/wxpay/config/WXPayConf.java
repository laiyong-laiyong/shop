package com.sobey.module.wxpay.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description TODO...
 * @Author WuChenYang
 * @Since 2020/2/5 15:08
 */
@ConfigurationProperties(prefix = "wx-pay")
@Configuration
public class WXPayConf {

    private String mchId;//商户号
    private String appid;
    private String key;
    private String noticeUrl;
    private String onlyPayNoticeUrl;

    public String getOnlyPayNoticeUrl() {
        return onlyPayNoticeUrl;
    }

    public void setOnlyPayNoticeUrl(String onlyPayNoticeUrl) {
        this.onlyPayNoticeUrl = onlyPayNoticeUrl;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNoticeUrl() {
        return noticeUrl;
    }

    public void setNoticeUrl(String noticeUrl) {
        this.noticeUrl = noticeUrl;
    }
}
