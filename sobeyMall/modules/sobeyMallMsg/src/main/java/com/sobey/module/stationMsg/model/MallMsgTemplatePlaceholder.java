package com.sobey.module.stationMsg.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;

import java.util.Date;

/**
 * @Author WCY
 * @CreateTime 2020/4/6 19:45
 * 模板占位符实体类
 */
@TableName("mall_msg_template_placeholder")
public class MallMsgTemplatePlaceholder extends SuperModel<MallMsgTemplatePlaceholder> {

    @TableField("value")
    private String value;

    @TableField("desc")
    private String desc;

    @TableField("createTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    private Date createTime;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
