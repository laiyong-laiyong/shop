package com.sobey.module.wxpay.entity.onlypay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author WCY
 * @CreateTime 2020/4/27 9:45
 * 仅微信支付时，第三方调用支付系统需要的参数
 */
@ApiModel
public class OnlyPay {

    @ApiModelProperty(notes = "订单号,32个字符内,只能是数字、大小写字母_-|*",example = "adsb123",required = true)
    private String orderNo;//订单号

    @ApiModelProperty(notes = "总金额,单位:分",example = "100",required = true)
    private Integer totalFee;//总金额,单位:分

    @ApiModelProperty(notes = "商品id,32个字符内",example = "12314",required = true)
    private String productId;//

    @ApiModelProperty(notes = "商品描述",example = "索贝商城-saas服务",required = true)
    private String body;

    @ApiModelProperty(notes = "附加数据",example = "这是附加数据")
    private String attach;//附加数据

    @ApiModelProperty(notes = "回调地址",required = true)
    private String noticeUrl;

    @ApiModelProperty(notes = "JSAPI支付时的openid",required = true)
    private String openId;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getNoticeUrl() {
        return noticeUrl;
    }

    public void setNoticeUrl(String noticeUrl) {
        this.noticeUrl = noticeUrl;
    }
}
