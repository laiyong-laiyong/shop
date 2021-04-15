package com.sobey.module.bill.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 后台管理账单页面中的月度统计功能，改版后的保存格式(要区分每种支付方式对应的限价)
 * @author WCY
 * @create 2020/10/9 10:33
 */
@TableName("mall_new_bill_detail")
@ApiModel
public class NewBillDetail extends SuperModel<NewBillDetail> {

    @TableField("siteCode")
    @ApiModelProperty(notes = "站点")
    private String siteCode;

    @TableField("accountId")
    @ApiModelProperty(notes = "用户id")
    private String accountId; //用户id

    @TableField("account")
    @ApiModelProperty(notes = "用户名")
    private String account;//用户名

    @TableField("productId")
    @ApiModelProperty(notes = "商品id")
    private String productId; //商品id

    @TableField("productName")
    @ApiModelProperty(notes = "商品名称")
    private String productName; //商品名称

    @TableField("billingMethod")
    @ApiModelProperty(notes = "计费方式 1-包周期 2-按量")
    private String billingMethod; //计费方式

    @TableField("billType")
    @ApiModelProperty(notes = "账单类型 0-消费")
    private String billType; //账单类型

    @TableField("payMethod")
    @ApiModelProperty(notes = "支付方式")
    private String payMethod;

    @TableField("totalOrderAmount")
    @ApiModelProperty(notes = "订单总金额")
    private BigDecimal totalOrderAmount; //订单总金额

    @TableField("totalPayAmount")
    @ApiModelProperty(notes = "消费总金额")
    private BigDecimal totalPayAmount; //消费总金额

    @TableField("totalLmiPri")
    @ApiModelProperty
    private BigDecimal totalLmiPri; //限价总金额

    @TableField("totalRefundAmount")
    @ApiModelProperty(notes = "退款总金额")
    private BigDecimal totalRefundAmount;//退款总金额

    @TableField("billDate")
    @ApiModelProperty(notes = "账单日期")
    private String billDate; //账单日期

    @TableField(value = "createDate",fill = FieldFill.INSERT)
    @ApiModelProperty(notes = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBillingMethod() {
        return billingMethod;
    }

    public void setBillingMethod(String billingMethod) {
        this.billingMethod = billingMethod;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public BigDecimal getTotalOrderAmount() {
        return totalOrderAmount;
    }

    public void setTotalOrderAmount(BigDecimal totalOrderAmount) {
        this.totalOrderAmount = totalOrderAmount;
    }

    public BigDecimal getTotalPayAmount() {
        return totalPayAmount;
    }

    public void setTotalPayAmount(BigDecimal totalPayAmount) {
        this.totalPayAmount = totalPayAmount;
    }

    public BigDecimal getTotalLmiPri() {
        return totalLmiPri;
    }

    public void setTotalLmiPri(BigDecimal totalLmiPri) {
        this.totalLmiPri = totalLmiPri;
    }

    public BigDecimal getTotalRefundAmount() {
        return totalRefundAmount;
    }

    public void setTotalRefundAmount(BigDecimal totalRefundAmount) {
        this.totalRefundAmount = totalRefundAmount;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
