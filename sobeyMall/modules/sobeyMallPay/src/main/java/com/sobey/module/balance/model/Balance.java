package com.sobey.module.balance.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description 保存余额的实体类
 * @Author WuChenYang
 * @CreateTime 2020/3/6 10:56
 */
@TableName("mall_balance")
@ApiModel(value = "balanceInfo")
public class Balance extends SuperModel<Balance> {

    @TableField("account")
    @ApiModelProperty(notes = "用户名",example = "测试")
    private String account;

    @TableField("accountId")
    @ApiModelProperty(notes = "用户userCode,余额充值必填",example = "123")
    private String accountId;

    @TableField("siteCode")
    @ApiModelProperty(notes = "站点",example = "123")
    private String siteCode;

    @TableField("balance")
    @ApiModelProperty(notes = "余额,单位:元,充值时必填必填",example = "0.00")
    private BigDecimal balance;//余额

    @TableField(exist = false)
    @ApiModelProperty(notes = "代金券余额,账户管理中使用")
    private BigDecimal voucherAmount;

    @TableField("credits")
    @ApiModelProperty(notes = "信用额度,单位:元",example = "0.00")
    private BigDecimal credits;//信用额度

    @TableField("sign")
    private String sign;

    @TableField(value = "createDate",fill = FieldFill.INSERT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    @ApiModelProperty(notes = "创建时间",example = "2020-03-13 10:00:00")
    private Date createDate;

    @TableField(value = "updateDate",fill = FieldFill.UPDATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    @ApiModelProperty(notes = "更新时间",example = "2020-03-13 11:00:00")
    private Date updateDate;

    public BigDecimal getVoucherAmount() {
        return voucherAmount;
    }

    public void setVoucherAmount(BigDecimal voucherAmount) {
        this.voucherAmount = voucherAmount;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
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
