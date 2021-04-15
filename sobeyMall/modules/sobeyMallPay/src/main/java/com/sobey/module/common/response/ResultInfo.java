package com.sobey.module.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description 支付通知信息
 * @Author WuChenYang
 * @CreateTime 2020/3/9 15:48
 */
@ApiModel
public class ResultInfo {

    @ApiModelProperty(notes = "返回码,SUCCESS-成功 FAIL-失败",example = "FAIL")
    private String rt_code;//成功SUCCESS 失败FAIL

    @ApiModelProperty(notes = "返回信息",example = "参数为空")
    private String rt_msg;

    public String getRt_code() {
        return rt_code;
    }

    public void setRt_code(String rt_code) {
        this.rt_code = rt_code;
    }

    public String getRt_msg() {
        return rt_msg;
    }

    public void setRt_msg(String rt_msg) {
        this.rt_msg = rt_msg;
    }
}
