package com.sobey.module.fegin.msg.request.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @Author WCY
 * @CreateTime 2020/4/16 15:39
 */
public class MsgTemplate {

    private String uuid;

    private String name;//模板名称

    private String parentTypeCode;//父级类型编码

    private String parentTypeDesc;//父级类型编码说明

    private String msgTypeCode;//消息类型编码

    private String msgTypeDesc;//消息类型编码说明

    private String msgTemplate;//模板内容

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    private Date createTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    private Date updateTime;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentTypeCode() {
        return parentTypeCode;
    }

    public void setParentTypeCode(String parentTypeCode) {
        this.parentTypeCode = parentTypeCode;
    }

    public String getParentTypeDesc() {
        return parentTypeDesc;
    }

    public void setParentTypeDesc(String parentTypeDesc) {
        this.parentTypeDesc = parentTypeDesc;
    }

    public String getMsgTypeCode() {
        return msgTypeCode;
    }

    public void setMsgTypeCode(String msgTypeCode) {
        this.msgTypeCode = msgTypeCode;
    }

    public String getMsgTypeDesc() {
        return msgTypeDesc;
    }

    public void setMsgTypeDesc(String msgTypeDesc) {
        this.msgTypeDesc = msgTypeDesc;
    }

    public String getMsgTemplate() {
        return msgTemplate;
    }

    public void setMsgTemplate(String msgTemplate) {
        this.msgTemplate = msgTemplate;
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
