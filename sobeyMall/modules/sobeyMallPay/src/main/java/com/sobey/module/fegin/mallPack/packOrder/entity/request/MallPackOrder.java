package com.sobey.module.fegin.mallPack.packOrder.entity.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author WCY
 * @createTime 2020/5/20 9:33
 * 套餐包订单
 */
@ApiModel
public class MallPackOrder {

    @ApiModelProperty(notes = "主键")
    private String id;

    @ApiModelProperty(notes = "站点", required = true)
    private String siteCode;

    @ApiModelProperty(notes = "用户userCode", required = true)
    private String accountId;//购买用户id

    @ApiModelProperty(notes = "用户名", required = true)
    private String account;//购买用户

    @ApiModelProperty(notes = "套餐包id", required = true)
    private String packUuid;

    @ApiModelProperty(notes = "套餐包编码")
    private String packCode;

    @ApiModelProperty(notes = "套餐包名称", required = true)
    private String packName;

    @ApiModelProperty(notes = "商品id", required = true)
    private String productId;

    @ApiModelProperty(notes = "商品名称")
    private String productName;

    @ApiModelProperty(notes = "有效时长", example = "12", required = true)
    private Integer duration;

    @ApiModelProperty(notes = "有效时长单位", example = "2", required = true)
    private String unit;

    @ApiModelProperty(notes = "订单号")
    private String orderNo;

    @ApiModelProperty(notes = "订单类型 默认0-新购")
    private String orderType = "0";

    @ApiModelProperty(notes = "订单金额",required = true)
    private BigDecimal orderAmount;

    private BigDecimal limPri;//限价

    @ApiModelProperty(dataType = "BigDecimal",example = "0.8",notes = "折扣,保留一位小数")
    private BigDecimal discount;//折扣

    @ApiModelProperty(dataType = "BigDecimal",example = "80.00",notes = "折扣后价格")
    private BigDecimal discountPrice;//折扣后价格

    @ApiModelProperty(notes = "付款金额")
    private BigDecimal payAmount;

    @ApiModelProperty(notes = "付款方式")
    private String payMethod;

    @ApiModelProperty(notes = "支付状态")
    private String payStatus; //支付状态

    @ApiModelProperty(notes = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate; //yyyy-MM-dd HH:mm:ss

    @ApiModelProperty(notes = "支付时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payDate;//付款时间 yyyy-MM-dd HH:mm:ss

    @ApiModelProperty(notes = "资源信息")
    private List<MallPackResource> resources;

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public BigDecimal getLimPri() {
        return limPri;
    }

    public void setLimPri(BigDecimal limPri) {
        this.limPri = limPri;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPackUuid() {
        return packUuid;
    }

    public void setPackUuid(String packUuid) {
        this.packUuid = packUuid;
    }

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
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

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public List<MallPackResource> getResources() {
        return resources;
    }

    public void setResources(List<MallPackResource> resources) {
        this.resources = resources;
    }
}
