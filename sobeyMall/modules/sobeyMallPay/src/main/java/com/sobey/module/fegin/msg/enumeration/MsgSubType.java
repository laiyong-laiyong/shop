package com.sobey.module.fegin.msg.enumeration;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author WCY
 * @CreateTime 2020/4/6 20:01
 * 次级消息类型
 */
@ApiModel
public enum MsgSubType {

    Welcome("0","-1","欢迎信息"),
    ServiceOpenNotice("0","0","商品开通通知"),
    ServiceRenewNotice("0","1","商品续费通知"),
    ServiceCloseNotice("0","2","商品关闭通知"),
    ArrearsNotice("0","3","欠费提醒"),
    RechargeNotice("0","4","余额充值提醒"),
    WorkOrderPending("0","5","工单待处理(运维)"),
    WorkOrderAcceptance("0","6","工单受理通知(用户)"),
    WorkOrderConfirmation("0","7","工单确认通知(用户)"),
    ServiceExpireRemain("0","11","服务到期提醒"),
    VoucherCreateSuccessNotice("0","12","代金券创建完成通知"),
    CreditsNotice("0","13","信用额度提醒"),
    ArrearsNoticeToAdmin("0","14","欠费提醒(给管理人员)"),

    SystemAnnouncement("2","8","系统公告"),
    ActivityNotice("2","9","活动通知"),
    News("2","10","新闻动态");

    @ApiModelProperty(notes = "子消息类型编码",example = "0")
    private String code;

    @ApiModelProperty(notes = "基础消息类型编码",example = "0")
    private String basicCode;

    @ApiModelProperty(notes = "子消息编码描述",example = "商品开通通知")
    private String desc;

    MsgSubType(String basicCode, String code, String desc) {
        this.basicCode = basicCode;
        this.code = code;
        this.desc = desc;
    }

    public String getBasicCode() {
        return basicCode;
    }

    public void setBasicCode(String basicCode) {
        this.basicCode = basicCode;
    }

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
}
