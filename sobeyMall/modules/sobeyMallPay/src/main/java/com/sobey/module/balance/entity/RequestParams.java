package com.sobey.module.balance.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description 余额支付请求参数
 * @Author WuChenYang
 * @CreateTime 2020/3/10 18:46
 */
@ApiModel(description = "余额支付条件")
public class RequestParams {

    @ApiModelProperty(value = "用户code",dataType = "string",example = "123",required = true)
    private String accountId;

    @ApiModelProperty(value = "订单号",dataType = "string",example = "12345",required = true)
    private String orderNo;

    @ApiModelProperty(value = "商品名称",dataType = "string",example = "测试",required = true)
    private String productName;

    @ApiModelProperty(value = "总金额",dataType = "double",example = "11.11")
    private Double totalFee;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }
}
