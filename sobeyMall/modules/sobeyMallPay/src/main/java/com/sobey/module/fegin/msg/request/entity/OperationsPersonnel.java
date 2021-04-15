package com.sobey.module.fegin.msg.request.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author WCY
 * @createTime 2020/7/9 14:28
 * 运维人员值班表
 */
@ApiModel
public class OperationsPersonnel {

    private String uuid;

    @ApiModelProperty(notes = "用户userCode",required = true)
    private String accountId;

    @ApiModelProperty(notes = "用户名",required = true)
    private String account;

    @ApiModelProperty(notes = "电话号码",required = true)
    private String phoneNum;

    @ApiModelProperty(notes = "电子邮箱")
    private String email;

    @ApiModelProperty(notes = "是否值班 0-休息 1-值班")
    private String isOnDuty;//是否值班

    @ApiModelProperty(notes = "最后修改人")
    private String lastModifyAccount;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(notes = "创建时间")
    private Date createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(notes = "修改时间")
    private Date updateDate;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLastModifyAccount() {
        return lastModifyAccount;
    }

    public void setLastModifyAccount(String lastModifyAccount) {
        this.lastModifyAccount = lastModifyAccount;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsOnDuty() {
        return isOnDuty;
    }

    public void setIsOnDuty(String isOnDuty) {
        this.isOnDuty = isOnDuty;
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
