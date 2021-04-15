package com.sobey.module.fegin.balance.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description 保存余额的实体类
 * @Author WuChenYang
 * @CreateTime 2020/3/6 10:56
 */
@ApiModel(value = "balanceInfo")
public class Balance{

    private String uuid;

    @ApiModelProperty(notes = "用户名",example = "测试")
    private String account;

    @ApiModelProperty(notes = "用户userCode",example = "123")
    private String accountId;

    @ApiModelProperty(notes = "余额",example = "0.00")
    private BigDecimal balance;//余额

    @ApiModelProperty(notes = "信用额度,单位:元",example = "0.00")
    private BigDecimal credits;//信用额度

    private String sign;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    @ApiModelProperty(notes = "创建时间",example = "2020-03-13 10:00:00")
    private Date createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    @ApiModelProperty(notes = "更新时间",example = "2020-03-13 11:00:00")
    private Date updateDate;

    public String getUuid() {
        return uuid;
    }

    public Balance setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public BigDecimal getCredits() {
        return credits;
    }

    public void setCredits(BigDecimal credits) {
        this.credits = credits;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
