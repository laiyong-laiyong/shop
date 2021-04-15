package com.sobey.module.bill.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author WCY
 * @createTime 2020/7/30 11:47
 * 个人账单
 */
@TableName("mall_personal_bill")
@ApiModel
public class PersonalBill extends SuperModel<PersonalBill> {

    @TableField("siteCode")
    @ApiModelProperty(notes = "站点")
    private String siteCode;

    @TableField("accountId")
    @ApiModelProperty(notes = "用户userCode")
    private String accountId;

    @TableField("account")
    @ApiModelProperty(notes = "用户名")
    private String account;

    @TableField("saleAccountId")
    @ApiModelProperty(notes = "销售userCode")
    private String saleAccountId;

    @TableField("totalAmount")
    @ApiModelProperty(notes = "消费总额")
    private BigDecimal totalAmount; //消费总额

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

    @TableField("refundTotalAmount")
    @ApiModelProperty(notes = "退款总额")
    private BigDecimal refundTotalAmount; //退款总额

    @TableField("balRefundTotalAmount")
    @ApiModelProperty(notes = "余额退款总额")
    private BigDecimal balRefundTotalAmount; //余额退款总额

    @TableField("wxRefundAmount")
    @ApiModelProperty(notes = "微信退款总额")
    private BigDecimal wxRefundAmount; //微信退款总额

    @TableField("zfbRefundAmount")
    @ApiModelProperty(notes = "支付宝退款总额")
    private BigDecimal zfbRefundAmount; //支付宝退款总额

    @TableField("limPriAmount")
    @ApiModelProperty(notes = "")
    private BigDecimal limPriAmount;//总限价金额

    @TableField("billDate")
    @ApiModelProperty(notes = "账单日期")
    private String billDate; //账单日期

    @TableField("createDate")
    @ApiModelProperty(notes = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;//创建日期

    @TableField(exist = false)
    private List<BillDetail> billDetails;

    @TableField(exist = false)
    @ApiModelProperty(notes = "主要用于账单后台管理页面月度统计功能的限价展示")
    private List<NewBillDetail> newBillDetails;

    public List<NewBillDetail> getNewBillDetails() {
        return newBillDetails;
    }

    public void setNewBillDetails(List<NewBillDetail> newBillDetails) {
        this.newBillDetails = newBillDetails;
    }

    public String getSaleAccountId() {
        return saleAccountId;
    }

    public void setSaleAccountId(String saleAccountId) {
        this.saleAccountId = saleAccountId;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public BigDecimal getLimPriAmount() {
        return limPriAmount;
    }

    public void setLimPriAmount(BigDecimal limPriAmount) {
        this.limPriAmount = limPriAmount;
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
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

    public BigDecimal getVoucherAmount() {
        return voucherAmount;
    }

    public void setVoucherAmount(BigDecimal voucherAmount) {
        this.voucherAmount = voucherAmount;
    }

    public BigDecimal getRefundTotalAmount() {
        return refundTotalAmount;
    }

    public void setRefundTotalAmount(BigDecimal refundTotalAmount) {
        this.refundTotalAmount = refundTotalAmount;
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

    public List<BillDetail> getBillDetails() {
        return billDetails;
    }

    public void setBillDetails(List<BillDetail> billDetails) {
        this.billDetails = billDetails;
    }
}
