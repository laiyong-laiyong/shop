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
 * @author WCY
 * @createTime 2020/7/28 14:56
 * 账单消费汇总，包括每个用户每个产品的消费总额
 */
@TableName("mall_bill_detail")
@ApiModel
public class BillDetail extends SuperModel<BillDetail> {

    @TableField("billUuid")
    @ApiModelProperty(notes = "")
    private String billUuid;//关联账单总表的主键

    @TableField("personalBillUuid")
    @ApiModelProperty(notes = "")
    private String personalBillUuid; //关联个人账单表的主键

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

    @TableField("totalOrderAmount")
    @ApiModelProperty(notes = "订单总金额")
    private BigDecimal totalOrderAmount; //订单总金额

    @TableField("totalPayAmount")
    @ApiModelProperty(notes = "消费总金额")
    private BigDecimal totalPayAmount; //消费总金额

    @TableField("balAmount")
    @ApiModelProperty(notes = "余额支付总额")
    private BigDecimal balAmount; //余额支付总额

    @TableField("wxAmount")
    @ApiModelProperty(notes = "微信支付总额")
    private BigDecimal wxAmount; //微信支付总额

    @TableField("zfbAmount")
    @ApiModelProperty(notes = "支付宝支付总额")
    private BigDecimal zfbAmount; //支付宝支付总额

    @TableField("voucherAmount")
    @ApiModelProperty(notes = "代金券支付总额")
    private BigDecimal voucherAmount; //代金券支付总额

    @TableField("totalLmiPri")
    @ApiModelProperty
    private BigDecimal totalLmiPri; //限价总金额

    @TableField("totalRefundAmount")
    @ApiModelProperty(notes = "退款总金额")
    private BigDecimal totalRefundAmount;//退款总金额

    @TableField("balRefundTotalAmount")
    @ApiModelProperty(notes = "余额退款总额")
    private BigDecimal balRefundTotalAmount; //余额退款总额

    @TableField("wxRefundAmount")
    @ApiModelProperty(notes = "微信退款总额")
    private BigDecimal wxRefundAmount; //微信退款总额

    @TableField("zfbRefundAmount")
    @ApiModelProperty(notes = "支付宝退款总额")
    private BigDecimal zfbRefundAmount; //支付宝退款总额

    @TableField(value = "createDate",fill = FieldFill.INSERT)
    @ApiModelProperty(notes = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    public BigDecimal getVoucherAmount() {
        return voucherAmount;
    }

    public void setVoucherAmount(BigDecimal voucherAmount) {
        this.voucherAmount = voucherAmount;
    }

    public BigDecimal getBalAmount() {
        return balAmount;
    }

    public void setBalAmount(BigDecimal balAmount) {
        this.balAmount = balAmount;
    }

    public BigDecimal getWxAmount() {
        return wxAmount;
    }

    public void setWxAmount(BigDecimal wxAmount) {
        this.wxAmount = wxAmount;
    }

    public BigDecimal getZfbAmount() {
        return zfbAmount;
    }

    public void setZfbAmount(BigDecimal zfbAmount) {
        this.zfbAmount = zfbAmount;
    }

    public BigDecimal getBalRefundTotalAmount() {
        return balRefundTotalAmount;
    }

    public void setBalRefundTotalAmount(BigDecimal balRefundTotalAmount) {
        this.balRefundTotalAmount = balRefundTotalAmount;
    }

    public BigDecimal getWxRefundAmount() {
        return wxRefundAmount;
    }

    public void setWxRefundAmount(BigDecimal wxRefundAmount) {
        this.wxRefundAmount = wxRefundAmount;
    }

    public BigDecimal getZfbRefundAmount() {
        return zfbRefundAmount;
    }

    public void setZfbRefundAmount(BigDecimal zfbRefundAmount) {
        this.zfbRefundAmount = zfbRefundAmount;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBillUuid() {
        return billUuid;
    }

    public void setBillUuid(String billUuid) {
        this.billUuid = billUuid;
    }

    public String getPersonalBillUuid() {
        return personalBillUuid;
    }

    public void setPersonalBillUuid(String personalBillUuid) {
        this.personalBillUuid = personalBillUuid;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
