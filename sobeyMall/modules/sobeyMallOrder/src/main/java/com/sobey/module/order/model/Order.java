package com.sobey.module.order.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.module.productservice.model.MetricUsage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 */
@ApiModel(description = "订单")
public class Order {

	//@Id 如果字段名不叫 id 而是其他名称则需要此有注释才可对应mongodb中的主键_id
	@ApiModelProperty(notes = "主键",example = "1")
	private String id;//自定义主键

	@ApiModelProperty(notes = "订单号",example = "1")
	private String orderNo;

	@ApiModelProperty(notes = "订单类型 0-新购 1-续费",example = "0",required = true)
	private String orderType;

	@ApiModelProperty(notes = "服务编码,订单为续费时,该参数必填",example = "123")
	private String serviceNo;

	@ApiModelProperty(notes = "支付状态 0-待支付 1-支付中 2-已支付 3-支付失败 4-已取消",example = "0")
	private String paymentStatus;//支付状态

	@ApiModelProperty(notes = "商品编码",example = "123",required = true)
	private String productId;

	@ApiModelProperty(notes = "关联商品Id(该商品若属于某个商品则传递该值)",example = "456")
	private String relatedProductId;

	@ApiModelProperty(notes = "关联商品名称",example = "关联")
	private String relatedProductName;

	@ApiModelProperty(notes = "版本编码",example = "1")
	private String versionId;

	@ApiModelProperty(notes = "存储前端自定义信息,Json字符串")//example = "{'productName':'商品名称','versionName':'版本名称','customName':'规格名称','optionName':'流量名称'}"
	private String productSpecs;

	@ApiModelProperty(notes = "存储前端自定义信息,Json字符串")
	private String specifications;

	@ApiModelProperty(notes = "开通类型 1-包周期(按版本) 2-按量",example = "1")
	private String openType;

	@ApiModelProperty(notes = "计费编码,JSON字符串,开通类型为按量时必填",example = "[{\"id\":\"100\",\"typeCode\":\"3\",\"type\":\"test\"},{\"id\":\"101\",\"typeCode\":\"2\",\"type\":\"test\"}]")
	private String chargeCodes;

	@ApiModelProperty(notes = "购买时长,单位:天,如果是退订则为负数",example = "30")
	private Integer duration;//购买时长,单位:天

	@ApiModelProperty(notes = "用户ID",example = "123",required = true)
	private String accountId;//用户id

	@ApiModelProperty(notes = "站点",example = "123",required = true)
	private String siteCode;

	@ApiModelProperty(notes = "用户名",example = "张三",required = true)
	private String account;

	@ApiModelProperty(dataType = "BigDecimal",example = "100.01",notes = "订单金额,单位:元,保留两位小数",required = true)
	@Field(targetType = FieldType.DECIMAL128)
	private BigDecimal orderAmount;//订单金额,保留两位小数 单位 元

	@ApiModelProperty(required = true)
	@Field(targetType = FieldType.DECIMAL128)
	private BigDecimal limPri;//限价

	@ApiModelProperty(dataType = "BigDecimal",example = "0.8",notes = "折扣,保留一位小数")
	@Field(targetType = FieldType.DECIMAL128)
	private BigDecimal discount;//折扣

	@ApiModelProperty(dataType = "BigDecimal",example = "80.00",notes = "折扣后价格")
	@Field(targetType = FieldType.DECIMAL128)
	private BigDecimal discountPrice;//折扣后价格

	@ApiModelProperty(dataType = "BigDecimal",example = "100.01",notes = "付款金额,单位:元,保留两位小数")
	@Field(targetType = FieldType.DECIMAL128)
	private BigDecimal payAmount;//付款金额,保留两位小数 单位 元

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	@ApiModelProperty(notes = "创建时间 yyyy-MM-dd HH:mm:ss",example = "2020-01-01 00:00:00")
	private Date createDate;//创建时间 yyyy-MM-dd HH:mm:ss

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	@ApiModelProperty(notes = "支付时间 yyyy-MM-dd HH:mm:ss",example = "2020-01-01 00:02:00")
	private Date payDate;//支付时间 yyyy-MM-dd HH:mm:ss

	@ApiModelProperty(notes = "交易类型 0-消费 1-充值",required = true)
	private String transType;//0-消费 1-充值

	@ApiModelProperty(notes = "支付方式 0-余额 1-微信 2-支付宝",example = "1")
	private String payMethod;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	@ApiModelProperty(notes = "更新时间 yyyy-MM-dd HH:mm:ss",example = "2020-02-01 00:00:00")
	private Date updateDate;

	@ApiModelProperty(notes = "商品开通地址",required = true,example = "http(s)://....")
	private String openUrl;

	@ApiModelProperty(notes = "商品续费地址",example = "http(s)://....")
	private String renewUrl;

	@ApiModelProperty(notes = "商品关闭地址",required = true,example = "http(s)://....")
	private String closeUrl;

	@Transient//该字段不存数据库
	private List<MetricUsage> metricUsages;

	public String getRelatedProductId() {
		return relatedProductId;
	}

	public void setRelatedProductId(String relatedProductId) {
		this.relatedProductId = relatedProductId;
	}

	public String getRelatedProductName() {
		return relatedProductName;
	}

	public void setRelatedProductName(String relatedProductName) {
		this.relatedProductName = relatedProductName;
	}

	public List<MetricUsage> getMetricUsages() {
		return metricUsages;
	}

	public void setMetricUsages(List<MetricUsage> metricUsages) {
		this.metricUsages = metricUsages;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public BigDecimal getLimPri() {
		return limPri;
	}

	public Order setLimPri(BigDecimal limPri) {
		this.limPri = limPri;
		return this;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public Order setDiscount(BigDecimal discount) {
		this.discount = discount;
		return this;
	}

	public BigDecimal getDiscountPrice() {
		return discountPrice;
	}

	public Order setDiscountPrice(BigDecimal discountPrice) {
		this.discountPrice = discountPrice;
		return this;
	}

	public String getId() {
		return id;
	}

	public Order setId(String id) {
		this.id = id;
		return this;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public Order setOrderNo(String orderNo) {
		this.orderNo = orderNo;
		return this;
	}

	public String getAccount() {
		return account;
	}

	public Order setAccount(String account) {
		this.account = account;
		return this;
	}

	public String getOrderType() {
		return orderType;
	}

	public Order setOrderType(String orderType) {
		this.orderType = orderType;
		return this;
	}

	public String getServiceNo() {
		return serviceNo;
	}

	public Order setServiceNo(String serviceNo) {
		this.serviceNo = serviceNo;
		return this;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public Order setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
		return this;
	}

	public String getProductId() {
		return productId;
	}

	public Order setProductId(String productId) {
		this.productId = productId;
		return this;
	}

	public String getVersionId() {
		return versionId;
	}

	public Order setVersionId(String versionId) {
		this.versionId = versionId;
		return this;
	}

	public String getProductSpecs() {
		return productSpecs;
	}

	public Order setProductSpecs(String productSpecs) {
		this.productSpecs = productSpecs;
		return this;
	}

	public String getOpenType() {
		return openType;
	}

	public Order setOpenType(String openType) {
		this.openType = openType;
		return this;
	}

	public String getChargeCodes() {
		return chargeCodes;
	}

	public Order setChargeCodes(String chargeCodes) {
		this.chargeCodes = chargeCodes;
		return this;
	}

	public Integer getDuration() {
		return duration;
	}

	public Order setDuration(Integer duration) {
		this.duration = duration;
		return this;
	}

	public String getAccountId() {
		return accountId;
	}

	public Order setAccountId(String accountId) {
		this.accountId = accountId;
		return this;
	}

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public Order setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
		return this;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public Order setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
		return this;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public Order setCreateDate(Date createDate) {
		this.createDate = createDate;
		return this;
	}

	public Date getPayDate() {
		return payDate;
	}

	public Order setPayDate(Date payDate) {
		this.payDate = payDate;
		return this;
	}

	public String getTransType() {
		return transType;
	}

	public Order setTransType(String transType) {
		this.transType = transType;
		return this;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public Order setPayMethod(String payMethod) {
		this.payMethod = payMethod;
		return this;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public Order setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
		return this;
	}

	public String getOpenUrl() {
		return openUrl;
	}

	public Order setOpenUrl(String openUrl) {
		this.openUrl = openUrl;
		return this;
	}

	public String getRenewUrl() {
		return renewUrl;
	}

	public Order setRenewUrl(String renewUrl) {
		this.renewUrl = renewUrl;
		return this;
	}

	public String getCloseUrl() {
		return closeUrl;
	}

	public Order setCloseUrl(String closeUrl) {
		this.closeUrl = closeUrl;
		return this;
	}
}
