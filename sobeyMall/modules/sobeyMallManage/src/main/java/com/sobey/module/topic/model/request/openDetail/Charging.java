package com.sobey.module.topic.model.request.openDetail;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Description 计费接口的入参
 * @Author WuChenYang
 * @CreateTime 2020/3/16 10:21
 */
@ApiModel
public class Charging implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(notes = "App编号", required = true)
    private String appId;

    @ApiModelProperty(notes = "请求Id", required = true)
    private String requestId;

    @ApiModelProperty(notes = "用户编码")
    private String userCode;

    @ApiModelProperty(notes = "用量,按需计费必填", example = "[{\"id\": \"10001\",\"value\": 50}]",required = true)
    private Usage[] usage;//用量

    public String getAppId() {
        return appId;
    }

    public Charging setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Usage[] getUsage() {
        return usage;
    }

    public void setUsage(Usage[] usage) {
        this.usage = usage;
    }
}
