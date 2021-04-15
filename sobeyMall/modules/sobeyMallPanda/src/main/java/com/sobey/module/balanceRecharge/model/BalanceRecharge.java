package com.sobey.module.balanceRecharge.model;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@TableName("mall_balance_recharge")
public class BalanceRecharge{

    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty(example="id")
    private Long id;

    @TableField(value = "shellId")
    @ApiModelProperty(example="申请id")
    private Integer shellId;//申请id

    @TableField(value = "account")
    @ApiModelProperty(example="客户名称")
    private String account;//客户

    @TableField(value = "projectId")
    @ApiModelProperty(example="项目号")
    private String projectId;//项目号

    @TableField(value = "projectName")
    @ApiModelProperty(example="项目名称")
    private String projectName;//项目名称

    @TableField(value = "agreementId")
    @ApiModelProperty(example="合同号")
    private String agreementId;//合同号

    @TableField(value = "way")
    @ApiModelProperty(example="考核方式")
    private String way;//考核方式

    @TableField(value = "cloudService")
    @ApiModelProperty(example="云服务")
    private String cloudService;//云服务

    @TableField(value = "lingYunAc")
    @ApiModelProperty(example="凌云账号")
    private String lingYunAc;//凌云账号

    @TableField(value = "lingYunId")
    @ApiModelProperty(example="凌云ID")
    private String lingYunId;//凌云ID

    @TableField(value = "seller")
    @ApiModelProperty(example="销售人员")
    private String seller;//销售人员

    @TableField(value = "preSale")
    @ApiModelProperty(example="售前人员")
    private String preSale;//售前人员

    @TableField(value = "amount")
    @ApiModelProperty(example="金额（两位小数）")
    private BigDecimal amount;//金额

    @TableField(value = "recharge")
    @ApiModelProperty(example="0充值/1额度")
    private Integer recharge;//0充值/1额度

    @TableField(value = "checkFlag")
    @ApiModelProperty(example="待确认/1已完成/2失败/3手动处理/4退回")
    private Integer checkFlag;//0待确认/1已完成/2失败(再次处理仅翻转为3状态，不调用接口)/3手动处理/4退回

    @TableField(value = "checkMs")
    @ApiModelProperty(example="失败原因")
    private String checkMs;//失败原因

    @TableField(value = "checkName")
    @ApiModelProperty(example="操作账号")
    private String checkName;//操作账号

    @TableField(value = "backMs")
    @ApiModelProperty(example="退回原因")
    private String backMs;//退回原因

    @TableField(value = "createDate", fill = FieldFill.INSERT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    @ApiModelProperty(example="申请时间")
    private Date createDate;//申请时间

    //传参使用

    @TableField(exist = false)
    private List<BalanceRechargeContract> service;

    @TableField(exist = false)
    private BalanceRecharge balanceRecharge;

    @TableField(exist = false)
    private List<Integer> recharges;

    @TableField(exist = false)
    private List<Integer> checkFlags;

    public List<BalanceRechargeContract> getService() {
        return service;
    }

    public void setService(List<BalanceRechargeContract> service) {
        this.service = service;
    }

    public void setBalanceRecharge(BalanceRecharge balanceRecharge) {
        this.balanceRecharge = balanceRecharge;
    }

    public BalanceRecharge getBalanceRecharge() {
        return balanceRecharge;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setLingYunAc(String lingYunAc) {
        this.lingYunAc = lingYunAc;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public void setCloudService(String cloudService) {
        this.cloudService = cloudService;
    }


    public void setCheckFlag(Integer checkFlag) {
        this.checkFlag = checkFlag;
    }

    public void setCheckMs(String checkMs) {
        this.checkMs = checkMs;
    }

    public void setLingYunId(String lingYunId) {
        this.lingYunId = lingYunId;
    }

    public void setRecharge(Integer recharge) {
        this.recharge = recharge;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getLingYunAc() {
        return lingYunAc;
    }

    public String getAccount() {
        return account;
    }


    public BigDecimal getAmount() {
        return amount;
    }


    public Integer getCheckFlag() {
        return checkFlag;
    }

    public Integer getRecharge() {
        return recharge;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgreementId() {
        return agreementId;
    }

    public String getCheckMs() {
        return checkMs;
    }

    public String getCheckName() {
        return checkName;
    }

    public String getCloudService() {
        return cloudService;
    }

    public Date getCreateDate() {
        return createDate;
    }


    public String getLingYunId() {
        return lingYunId;
    }

    public String getWay() {
        return way;
    }

    public void setBackMs(String backMs) {
        this.backMs = backMs;
    }

    public String getBackMs() {
        return backMs;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getSeller() {
        return seller;
    }

    public void setPreSale(String preSale) {
        this.preSale = preSale;
    }

    public String getPreSale() {
        return preSale;
    }

    public List<Integer> getCheckFlags() {
        return checkFlags;
    }

    public List<Integer> getRecharges() {
        return recharges;
    }

    public void setCheckFlags(List<Integer> checkFlags) {
        this.checkFlags = checkFlags;
    }

    public void setRecharges(List<Integer> recharges) {
        this.recharges = recharges;
    }

    public Integer getShellId() {
        return shellId;
    }

    public void setShellId(Integer shellId) {
        this.shellId = shellId;
    }

}

