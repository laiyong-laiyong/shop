package com.sobey.module.stationMsg.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 推广
 * @author WCY
 * @create 2020/10/14 10:15
 */
@TableName("mall_promote")
@ApiModel
public class Promote extends SuperModel<Promote> {

    @TableField("code")
    @ApiModelProperty(notes = "编码 新增时必填")
    private String code;

    @TableField("name")
    @ApiModelProperty(notes = "名称 新增时必填")
    private String name;

    @TableField(value = "createDate",fill = FieldFill.INSERT)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(notes = "创建时间 yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
