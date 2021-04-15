package com.sobey.module.stationMsg.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author wcy
 * 消息类
 */
@TableName("mall_msg")
@ApiModel
public class MallMsg extends SuperModel<MallMsg> {

    @TableField("basicMsgType")
    @ApiModelProperty(notes = "基础消息类型",example = "0")
    private String basicMsgType;//基础消息类型

    @TableField("subMsgType")
    @ApiModelProperty(notes = "消息类型",example = "0")
    private String subMsgType;//子消息类型

    @TableField("msgContent")
    @ApiModelProperty(notes = "消息内容",example = "测试内容")
    private String msgContent;//消息内容

    @TableField("accountId")
    @ApiModelProperty(notes = "接收人userCode",example = "123456")
    private String accountId;//接收人

    @TableField("msgStatus")
    @ApiModelProperty(notes = "消息状态 1-已读 0-未读",example = "1")
    private String msgStatus;//消息状态 1-已读 0-未读

    @TableField("createTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    @ApiModelProperty(notes = "创建时间",example = "2020-04-01 12:00:00")
    private Date createTime; //创建时间

    public String getBasicMsgType() {
        return basicMsgType;
    }

    public void setBasicMsgType(String basicMsgType) {
        this.basicMsgType = basicMsgType;
    }

    public String getSubMsgType() {
        return subMsgType;
    }

    public void setSubMsgType(String subMsgType) {
        this.subMsgType = subMsgType;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(String msgStatus) {
        this.msgStatus = msgStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
