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
 * @CreateTime 2020/4/6 19:55
 * 次级消息类型
 */
@TableName("mall_msg_sub_type")
@ApiModel
public class MallMsgSubType extends SuperModel<MallMsgSubType> {

    @TableField("code")
    @ApiModelProperty(notes = "子消息类型编码",example = "0")
    private String code;

    @TableField("desc")
    @ApiModelProperty(notes = "子消息类型说明",example = "服务开通提醒")
    private String desc;

    @TableField("basicUuid")
    @ApiModelProperty(notes = "基础消息类型uuid",example = "1")
    private String basicUuid;//基础消息类型的id

    @TableField("createTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    @ApiModelProperty(notes = "创建时间",example = "2020-04-01 12:00:00")
    private Date createTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBasicUuid() {
        return basicUuid;
    }

    public void setBasicUuid(String basicUuid) {
        this.basicUuid = basicUuid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
