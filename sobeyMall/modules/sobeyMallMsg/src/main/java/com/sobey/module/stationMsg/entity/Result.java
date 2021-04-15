package com.sobey.module.stationMsg.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author WCY
 * @CreateTime 2020/4/8 11:33
 */
@ApiModel
public class Result {

    @ApiModelProperty(notes = "业务逻辑处理结果编码 FAIL or SUCCESS",example = "SUCCESS")
    private String code;//FAIL or SUCCESS

    @ApiModelProperty(notes = "返回消息",example = "成功")
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static Result withFail(String msg){
        Result result = new Result();
        result.setCode("FAIL");
        result.setMsg(msg);
        return result;
    }

    public static Result withSuccess(String msg){
        Result result = new Result();
        result.setCode("SUCCESS");
        result.setMsg(msg);
        return result;
    }

}
