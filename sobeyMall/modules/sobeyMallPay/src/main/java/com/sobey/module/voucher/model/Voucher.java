package com.sobey.module.voucher.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 代金券
 */
@TableName("mall_voucher")
@ApiModel
public class Voucher extends SuperModel<Voucher>  {

    @TableField("vouCode")
    @ApiModelProperty(notes = "代金券兑换码",example = "abd")
    @ExcelProperty(value = "兑换码",index = 0)
    private String vouCode;//代金券编码

    @TableField("voucherType")
    @ApiModelProperty(notes = "代金券类别 0-平台类 1-销售推广类",example = "0")
    @ExcelProperty(value = "代金券类别(0-平台类 1-销售推广类)",index = 1)
    private String voucherType;

    @TableField("account")
    @ApiModelProperty(notes = "充值账户",example = "test")
    @ExcelProperty(value = "充值账户",index = 2)
    private String account;//用户名

    @TableField("accountId")
    @ApiModelProperty(notes = "充值账户编码",example = "abd")
    @ExcelProperty(value = "充值账户编码",index = 3)
    private String accountId;//充值账户

    @TableField("issuedAccount")
    @ApiModelProperty(notes = "下发用户",example = "admin")
    @ExcelProperty(value = "下发账户",index = 4)
    private String issuedAccount;


    @TableField("issuedAccountId")
    @ApiModelProperty(notes = "下发用户id",example = "123456")
    @ExcelProperty(value = "下发账户编码",index = 5)
    private String issuedAccountId;

    @TableField("issuedPhone")
    @ApiModelProperty(notes = "收券人手机号",example = "138xxx")
    @ExcelProperty(value = "收券人手机号",index = 6)
    private String issuedPhone;

    @TableField("amount")
    @ApiModelProperty(notes = "金额",example = "100.00")
    @ExcelProperty(value = "代金券金额",index = 7)
    private BigDecimal amount;//金额

    @TableField("receiveStatus")
    @ApiModelProperty(notes = "领取状态 0-未领取 1-已领取 2-已过期",example = "0")
    @ExcelProperty(value = "领取状态(0-未领取 1-已领取 2-已过期)",index = 8)
    private String receiveStatus;//领取状态 0-未领取 1-已领取

    @TableField("client")
    @ApiModelProperty(notes = "客户")
    @ExcelProperty(value = "客户",index = 9)
    private String client;

    @TableField("seller")
    @ApiModelProperty(notes = "销售")
    @ExcelProperty(value = "销售",index = 10)
    private String seller;

    @TableField("remark")
    @ApiModelProperty(notes = "备注")
    @ExcelProperty(value = "备注",index = 11)
    private String remark;

    @TableField("createName")
    @ApiModelProperty(notes = "创建人",example = "system")
    @ExcelProperty(value = "创建人",index = 12)
    private String createName;

    @TableField("createTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    @ApiModelProperty(notes = "创建时间",example = "2020-04-01 00:00:00")
    @ExcelProperty(value = "创建时间",index = 13)
    private Date createTime;//代金券创建时间

    @TableField("expireTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    @ApiModelProperty(notes = "到期时间",example = "2020-04-01 00:00:00")
    @ExcelProperty(value = "到期时间",index = 14)
    private Date expireTime;

    @TableField("updateTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    @ApiModelProperty(notes = "领取时间",example = "2020-04-02 00:00:00")
    @ExcelProperty(value = "领取时间",index = 15)
    private Date updateTime;//代金券领取时间


    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public String getIssuedPhone() {
        return issuedPhone;
    }

    public void setIssuedPhone(String issuedPhone) {
        this.issuedPhone = issuedPhone;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getIssuedAccount() {
        return issuedAccount;
    }

    public void setIssuedAccount(String issuedAccount) {
        this.issuedAccount = issuedAccount;
    }

    public String getIssuedAccountId() {
        return issuedAccountId;
    }

    public void setIssuedAccountId(String issuedAccountId) {
        this.issuedAccountId = issuedAccountId;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getVouCode() {
        return vouCode;
    }

    public void setVouCode(String vouCode) {
        this.vouCode = vouCode;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReceiveStatus() {
        return receiveStatus;
    }

    public void setReceiveStatus(String receiveStatus) {
        this.receiveStatus = receiveStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
