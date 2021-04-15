package com.sobey.module.balance.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * @author WCY
 * @createTime 2020/7/13 15:14
 * 充值余额的接口参数
 */
@ApiModel
public class RechargeBalanceParam {

    @ApiModelProperty(notes = "充值账户名,必填",example = "张三",required = true)
    private String account;//充值账户

    @ApiModelProperty(notes = "充值账户userCode,必填",example = "124567890",required = true)
    private String accountId;//充值账户userCode

    @ApiModelProperty(notes = "操作人",required = true)
    private String operationName;

    @ApiModelProperty(notes = "充值金额,保留两位小数,必填",example = "100.50",required = true)
    private BigDecimal amount;//充值金额 //保留两位小数

    @ApiModelProperty(notes = "签名,使用公钥加密金额后得到的值(RSA算法,X509标准)",required = true)
    private String sign;

    @ApiModelProperty(notes = "客户")
    private String client; //客户

    @ApiModelProperty(notes = "销售")
    private String seller; //销售

    @ApiModelProperty(notes = "合同号")
    private String contractNo;//合同号

    @ApiModelProperty(notes = "备注")
    private String remark; //备注

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
