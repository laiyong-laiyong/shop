package com.sobey.module.stationMsg.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author WCY
 * @createTime 2020/4/28 10:37
 * 工单在线聊天消息体
 */
@ApiModel
public class WorkOrderMsg {

    @ApiModelProperty(notes = "消息类型 0-普通消息 1-文件类型消息",example = "0",required = true)
    private String typeCode; //0-普通消息,1-文件类型消息

    @ApiModelProperty(notes = "消息内容,如果是文件类型消息则该值为地址",example = "你好")
    private String content;

    @ApiModelProperty(notes = "消息发送者userCode",example = "123",required = true)
    private String sourceUserCode;

    @ApiModelProperty(notes = "消息接收者userCode",example = "456",required = true)
    private String targetUserCode;

    @ApiModelProperty(notes = "工单号",example = "123456",required = true)
    private String workOrderNum;

    public String getWorkOrderNum() {
        return workOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        this.workOrderNum = workOrderNum;
    }

    public String getSourceUserCode() {
        return sourceUserCode;
    }

    public void setSourceUserCode(String sourceUserCode) {
        this.sourceUserCode = sourceUserCode;
    }

    public String getTargetUserCode() {
        return targetUserCode;
    }

    public void setTargetUserCode(String targetUserCode) {
        this.targetUserCode = targetUserCode;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
