package com.sobey.module.balance.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author WCY
 * @createTime 2020/7/13 14:59
 * 余额充值记录
 */
@TableName("mall_recharge_balance_records")
public class RechargeBalanceRecords extends SuperModel<RechargeBalanceRecords> {

    @TableField("account")
    private String account;//充值账户

    @TableField("accountId")
    private String accountId;//充值账户userCode

    @TableField("operationName")
    private String operationName;//操作人

    @TableField("amount")
    private BigDecimal amount;//充值金额 //保留两位小数

    @TableField("client")
    private String client; //客户

    @TableField("seller")
    private String seller; //销售

    @TableField("contractNo")
    private String contractNo;//合同号

    @TableField("remark")
    private String remark; //备注

    @TableField("rechargeTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date rechargeTime; //充值时间

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
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

    public Date getRechargeTime() {
        return rechargeTime;
    }

    public void setRechargeTime(Date rechargeTime) {
        this.rechargeTime = rechargeTime;
    }
}
