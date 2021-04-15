package com.sobey.module.stationMsg.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @Author WCY
 * @CreateTime 2020/4/7 18:23
 * 广播类型消息实体
 */
@TableName("mall_msg_broadcast")
@ApiModel
public class MallMsgBroadcast extends SuperModel<MallMsgBroadcast> {

    @TableField("basicMsgType")
    @ApiModelProperty(notes = "基础消息类型编码",example = "1")
    private String basicMsgType;//基础消息类型

    @TableField("subMsgType")
    @ApiModelProperty(notes = "子消息类型编码",example = "-")
    private String subMsgType;//子消息类型

    @TableField("msgContent")
    @ApiModelProperty(notes = "消息内容",example = "测试")
    private String msgContent;//消息内容

    @TableField("sendStatus")
    @ApiModelProperty(notes = "发送状态 0-未发送 1-已发送",example = "0")
    private String sendStatus;//发送状态 0-未发送 1-已发送

    @TableField("createTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    @ApiModelProperty(notes = "创建时间",example = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @TableField("sentTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    @ApiModelProperty(notes = "发送时间",example = "yyyy-MM-dd HH:mm:ss")
    private Date sentTime;//发送时间

    @TableField("expireTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    @ApiModelProperty(notes = "消息到期时间",example = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;

    @TableField("updateTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    @ApiModelProperty(notes = "更新时间",example = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

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

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getSentTime() {
        return sentTime;
    }

    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }
}
