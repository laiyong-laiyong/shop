package com.sobey.module.stationMsg.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Author WCY
 * @CreateTime 2020/4/13 14:50
 * 调用发送消息接口时传的参数
 */
@ApiModel
public class MsgEntity {

    @ApiModelProperty(notes = "用户名(按需填写)",example = "测试")
    private String username;

    @ApiModelProperty(notes = "商品名称",example = "测试商品")
    private String productName;

    @ApiModelProperty(notes = "服务到期时间(按需填写) 格式:yyyy/MM/dd HH:mm:ss",example = "2020/04/04 12:00:00")
    private String expireTime;

    @ApiModelProperty(notes = "剩余时长(按需填写) 单位:天",example = "12")
    private String remainingTime;

    @ApiModelProperty(notes = "账户余额(按需填写)",example = "11.11")
    private String balance;

    @ApiModelProperty(notes = "交易金额(按需填写)",example = "100.09")
    private String transactionAmount;

    @ApiModelProperty(notes = "工单号(按需填写)",example = "123")
    private String workOrderNumber;

    @ApiModelProperty(notes = "代金券兑换码",example = "123")
    private String voucherCode;

    @ApiModelProperty(notes = "信用额度",example = "20.00")
    private String credits;

    @ApiModelProperty(notes = "接收人userCodes",example = "['123','456']")
    private List<String> userCodes;

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public List<String> getUserCodes() {
        return userCodes;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public void setUserCodes(List<String> userCodes) {
        this.userCodes = userCodes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(String remainingTime) {
        this.remainingTime = remainingTime;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getWorkOrderNumber() {
        return workOrderNumber;
    }

    public void setWorkOrderNumber(String workOrderNumber) {
        this.workOrderNumber = workOrderNumber;
    }
}
