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
 * @CreateTime 2020/4/6 19:52
 * 基础消息类型
 */
@TableName("mall_msg_basic_type")
@ApiModel
public class MallMsgBasicType extends SuperModel<MallMsgBasicType> {

    @TableField("code")
    @ApiModelProperty(notes = "基础消息类型编码",example = "0")
    private String code;

    @TableField("desc")
    @ApiModelProperty(notes = "消息类型说明",example = "系统类型消息")
    private String desc;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
