package com.sobey.module.balanceRecharge.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;

import java.util.Date;

@TableName("mall_balance_recharge_contract")
public class BalanceRechargeContract extends SuperModel<BalanceRechargeContract> {

    @TableId(value = "id",type = IdType.AUTO )
    private Long id;

    @TableField(value = "projectId")
    private Long projectId;

    @TableField(value = "serviceSize")
    private String serviceSize;

    @TableField(value = "service")
    private String service;

    @TableField(value = "contract")
    private String contract;//折扣方式

    @TableField(value = "contractDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+08")
    private Date contractDate;//折扣到期时间

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getContract() {
        return contract;
    }

    public Date getContractDate() {
        return contractDate;
    }

    public String getService() {
        return service;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getServiceSize() {
        return serviceSize;
    }

    public void setServiceSize(String serviceSize) {
        this.serviceSize = serviceSize;
    }
}

