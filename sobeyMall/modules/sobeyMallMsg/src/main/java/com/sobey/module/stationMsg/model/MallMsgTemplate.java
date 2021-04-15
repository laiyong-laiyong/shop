package com.sobey.module.stationMsg.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;

import java.util.Date;

/**
 * @Author WCY
 * @CreateTime 2020/4/3 11:56
 * 消息模板类
 */
@TableName("mall_msg_template")
public class MallMsgTemplate extends SuperModel<MallMsgTemplate> {

    @TableField("name")
    private String name;//模板名称

    @TableField("parentTypeCode")
    private String parentTypeCode;//父级类型编码

    @TableField("parentTypeDesc")
    private String parentTypeDesc;//父级类型编码说明

    @TableField("msgTypeCode")
    private String msgTypeCode;//消息类型编码

    @TableField("msgTypeDesc")
    private String msgTypeDesc;//消息类型编码说明

    @TableField("msgTemplate")
    private String msgTemplate;//模板内容

    @TableField("createTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    private Date createTime;

    @TableField("updateTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    private Date updateTime;

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
