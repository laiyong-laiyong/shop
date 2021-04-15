package com.sobey.module.stationMsg.enumeration;

/**
 * @Author WCY
 * @CreateTime 2020/4/8 17:30
 * 占位符
 */
public class TemplatePlaceholder {

    public static final String username = "${username}";

    public static final String productName = "${productName}";

    public static final String expireTime = "${yyyy/MM/dd HH:mm:ss}";//服务到期时间

    public static final String remainingTime = "${remainingTime}";//服务剩余时长 天

    public static final String balance = "${balance}";//账户余额

    public static final String transactionAmount = "${transactionAmount}";//交易金额

    public static final String workOrderNumber = "${workOrderNumber}";//工单号

    public static final String voucherCode = "${voucherCode}";//代金券编码

    public static final String credits = "${credits}";//信用额度

}
