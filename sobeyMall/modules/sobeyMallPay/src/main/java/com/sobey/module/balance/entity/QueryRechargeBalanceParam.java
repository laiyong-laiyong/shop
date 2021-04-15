package com.sobey.module.balance.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author WCY
 * @createTime 2020/7/13 16:40
 * 充值记录查询接口参数
 */
@ApiModel
public class QueryRechargeBalanceParam {

    @ApiModelProperty(notes = "主键",example = "123")
    private String uuid;

    @ApiModelProperty(notes = "充值账户名",example = "张三")
    private String account;//充值账户

    @ApiModelProperty(notes = "充值账户userCode",example = "124567890")
    private String accountId;//充值账户userCode

    @ApiModelProperty(notes = "客户")
    private String client; //客户

    @ApiModelProperty(notes = "销售")
    private String seller; //销售

    @ApiModelProperty(notes = "合同号")
    private String contractNo;//合同号

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
}
