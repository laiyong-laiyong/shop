package com.sobey.module.productservice.entity.notice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author WCY
 * @createTime 2020/5/14 19:03
 */
@ApiModel
public class NoticeOpen {

    @ApiModelProperty(notes = "app编号",required = true)
    private String appId;

    @ApiModelProperty(notes = "是否成功开通",required = true,example = "true")
    private Boolean isOpen;

    @ApiModelProperty(notes = "html字符串")
    private String message;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
