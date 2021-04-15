package com.sobey.module.topic.model.request.openDetail;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Description 用量
 * @Author WuChenYang
 * @CreateTime 2020/3/16 13:57
 */
@ApiModel
public class Usage implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(notes = "计费编码",example = "2d499d322a07454db47ebb1b09d0f1fe")
    private String id; //计费编码

    @ApiModelProperty(notes = "用量",example = "100")
    private Double value; //使用量

    public String getId() {
        return id;
    }

    public Usage setId(String id) {
        this.id = id;
        return this;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
