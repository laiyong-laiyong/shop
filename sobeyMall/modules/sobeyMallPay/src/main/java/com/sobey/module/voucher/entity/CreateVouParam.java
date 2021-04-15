package com.sobey.module.voucher.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * @author WCY
 * @createTime 2020/7/13 11:13
 * 创建代金券的参数
 */
@ApiModel
public class CreateVouParam {

    @ApiModelProperty(notes = "代金券类别 0-平台类 1-销售推广类",example = "0",required = true)
    private String voucherType;

    @ApiModelProperty(notes = "代金券金额,保留两位小数,必填",example = "100",required = true)
    private BigDecimal amount;

    @ApiModelProperty(notes = "下发账户userCode,必填",required = true)
    private String issuedAccountId;

    @ApiModelProperty(notes = "下发账户名,必填",required = true)
    private String issuedAccount;

    @ApiModelProperty(notes = "收券人手机号,必填",required = true)
    private String issuedPhone;

    @ApiModelProperty(notes = "客户")
    private String client;

    @ApiModelProperty(notes = "销售")
    private String seller;

    @ApiModelProperty(notes = "备注")
    private String remark;

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getIssuedAccountId() {
        return issuedAccountId;
    }

    public void setIssuedAccountId(String issuedAccountId) {
        this.issuedAccountId = issuedAccountId;
    }

    public String getIssuedAccount() {
        return issuedAccount;
    }

    public void setIssuedAccount(String issuedAccount) {
        this.issuedAccount = issuedAccount;
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
}
