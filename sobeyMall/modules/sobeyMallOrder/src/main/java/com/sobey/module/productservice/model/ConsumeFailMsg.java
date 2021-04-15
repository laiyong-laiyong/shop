package com.sobey.module.productservice.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @Description 保存消费信息时处理失败的计费信息
 * @Author WuChenYang
 * @CreateTime 2020/3/19 16:25
 */
@TableName("consume_fail_msg")
@ApiModel
public class ConsumeFailMsg extends SuperModel<ConsumeFailMsg> {

    @TableField("appId")
    @ApiModelProperty(notes = "app编号")
    private String appId;

    @TableField("userCode")
    @ApiModelProperty(notes = "用户编码")
    private String userCode;//用户编码

    @TableField("usage")
    @ApiModelProperty(notes = "用量,JSON字符串")
    private String usage;//用量,JSON字符串

    @TableField("failReason")
    @ApiModelProperty(notes = "失败原因")
    private String failReason;//失败原因

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    @TableField("createDate")
    @ApiModelProperty(notes = "创建时间")
    private Date createDate;

    @TableField("manualProcessStatus")
    @ApiModelProperty(notes = "人工处理状态 0-未处理 1-已处理")
    private String manualProcessStatus;//人工处理状态 0-未处理 1-已处理

    @TableField("handler")
    @ApiModelProperty(notes = "处理人")
    private String handler;

    @TableField("handleDate")
    @ApiModelProperty(notes = "处理时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    private Date handleDate;//处理时间

    @ApiModelProperty(notes = "计费id,查询时使用")
    @TableField(exist = false)
    private String chargeId;

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public Date getHandleDate() {
        return handleDate;
    }

    public void setHandleDate(Date handleDate) {
        this.handleDate = handleDate;
    }

    public String getManualProcessStatus() {
        return manualProcessStatus;
    }

    public void setManualProcessStatus(String manualProcessStatus) {
        this.manualProcessStatus = manualProcessStatus;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
