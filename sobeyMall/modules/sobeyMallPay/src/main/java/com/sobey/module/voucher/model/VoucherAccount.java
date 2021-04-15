package com.sobey.module.voucher.model;

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
 * @author wcy
 * 代金券余额账户
 */
@TableName("mall_voucher_account")
@ApiModel
public class VoucherAccount extends SuperModel<VoucherAccount> {

    @TableField("account")
    @ApiModelProperty(notes = "用户名",example = "test")
    private String account;

    @TableField("accountId")
    @ApiModelProperty(notes = "用户编码",example = "123")
    private String accountId;

    @TableField("siteCode")
    @ApiModelProperty(notes = "站点",example = "123")
    private String siteCode;

    @TableField("vouAmount")
    @ApiModelProperty(notes = "代金券余额",example = "0.00")
    private BigDecimal vouAmount;

    @TableField("createTime")
    @ApiModelProperty(notes = "创建时间",example = "2020-04-01 00:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    private Date createTime;

    @TableField(value = "updateTime",fill = FieldFill.UPDATE)
    @ApiModelProperty(notes = "更新时间",example = "2020-04-02 00:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    private Date updateTime;

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
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
