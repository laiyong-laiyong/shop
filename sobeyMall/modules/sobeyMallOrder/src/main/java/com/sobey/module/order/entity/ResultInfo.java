package com.sobey.module.order.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description 调用本模块接口的统一返回类
 * @Author WuChenYang
 * @CreateTime 2020/3/13 17:54
 */
@ApiModel
public class ResultInfo {

    @ApiModelProperty(notes = "业务结果 FAIL or SUCCESS")
    private String code;//FAIL or SUCCESS

    @ApiModelProperty(notes = "信息")
    private String msg;

    @ApiModelProperty(notes = "返回体")
    private Object body;

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

    public static ResultInfo withFail(String msg){
        ResultInfo rt = new ResultInfo();
        rt.setCode("FAIL");
        rt.setMsg(msg);
        return rt;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public static ResultInfo withSuccess(String msg){
        ResultInfo rt = new ResultInfo();
        rt.setCode("SUCCESS");
        rt.setMsg(msg);
        return rt;
    }
}
