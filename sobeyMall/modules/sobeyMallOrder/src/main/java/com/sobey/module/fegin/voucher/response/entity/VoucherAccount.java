package com.sobey.module.fegin.voucher.response.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wcy
 * 代金券余额账户
 */
@ApiModel
public class VoucherAccount {

    private String uuid;

    @ApiModelProperty(notes = "用户名",example = "test")
    private String account;

    @ApiModelProperty(notes = "用户编码",example = "123")
    private String accountId;

    @ApiModelProperty(notes = "代金券余额",example = "0.00")
    private BigDecimal vouAmount;

    @ApiModelProperty(notes = "创建时间",example = "2020-04-01 00:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    private Date createTime;

    @ApiModelProperty(notes = "更新时间",example = "2020-04-02 00:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    private Date updateTime;

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

    public BigDecimal getVouAmount() {
        return vouAmount;
    }

    public void setVouAmount(BigDecimal vouAmount) {
        this.vouAmount = vouAmount;
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
