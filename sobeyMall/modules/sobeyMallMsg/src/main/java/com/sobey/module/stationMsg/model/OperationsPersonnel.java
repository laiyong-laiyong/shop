package com.sobey.module.stationMsg.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author WCY
 * @createTime 2020/7/9 14:28
 * 运维人员值班表
 */
@TableName("mall_operations_personnel")
@ApiModel
public class OperationsPersonnel extends SuperModel<OperationsPersonnel> {

    @TableField("accountId")
    @ApiModelProperty(notes = "用户userCode",required = true)
    private String accountId;

    @TableField("account")
    @ApiModelProperty(notes = "用户名",required = true)
    private String account;

    @TableField("phoneNum")
    @ApiModelProperty(notes = "电话号码",required = true)
    private String phoneNum;

    @TableField("email")
    @ApiModelProperty(notes = "电子邮箱")
    private String email;

    @TableField("isOnDuty")
    @ApiModelProperty(notes = "是否值班 0-休息 1-值班")
    private String isOnDuty;//是否值班

    @TableField("lastModifyAccount")
    @ApiModelProperty(notes = "最后修改人")
    private String lastModifyAccount;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "createDate",fill = FieldFill.INSERT)
    @ApiModelProperty(notes = "创建时间")
    private Date createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "updateDate",fill = FieldFill.UPDATE)
    @ApiModelProperty(notes = "修改时间")
    private Date updateDate;

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
