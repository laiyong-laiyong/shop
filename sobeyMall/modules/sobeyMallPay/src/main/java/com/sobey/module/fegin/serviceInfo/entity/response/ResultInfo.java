package com.sobey.module.fegin.serviceInfo.entity.response;

/**
 * @Description TODO...
 * @Author WuChenYang
 * @CreateTime 2020/3/16 18:23
 */
public class ResultInfo {

    private String code;//FAIL or SUCCESS
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

}
