package com.sobey.module.fegin.serviceInfo.entity.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 */
public class ServiceInfo {


	private String uuid;

	@ApiModelProperty(notes = "服务编号")
	private String serviceNo;

	@ApiModelProperty(notes = "App编号")
	private String appId;

	@ApiModelProperty(notes = "服务状态 0-关闭 1-正常 2-开通中 3-开通失败",example = "0")
	private String serviceStatus;

	@ApiModelProperty(notes = "商品ID",required = true,example = "123")
	private String productId;

	@ApiModelProperty(notes = "关联商品Id(该商品若属于某个商品则传递该值)",example = "456")
	private String relatedProductId;

	@ApiModelProperty(notes = "关联商品名称",example = "关联")
	private String relatedProductName;

	@ApiModelProperty(notes = "版本编码",example = "1")
	private String versionId;

	@ApiModelProperty(notes = "存储商品规格信息,Json字符串",
			example = "{'productName':'商品名称','versionName':'版本名称','customName':'规格名称','optionName':'流量名称'}")
	private String productSpecs;

	@ApiModelProperty(notes = "存储前端自定义的信息,json字符串")
	private String specifications;

	@ApiModelProperty(notes = "html字符串")
	private String message;

	@ApiModelProperty(notes = "站点",required = true,example = "123")
	private String siteCode;

	@ApiModelProperty(notes = "用户ID",required = true,example = "123")
	private String accountId;//用户id

	@ApiModelProperty(notes = "用户名",required = true,example = "张三")
	private String account;

	@ApiModelProperty(notes = "开通类型 1-包周期(按版本) 2-按量",example = "1",required = true)
	private String openType;

	@ApiModelProperty(notes = "计费编码,JSON字符串,计费类型为按量时必填",example = "[{\"id\":\"100\",\"typeCode\":\"1\",\"type\":\"按时长\"},{\"id\":\"101\",\"typeCode\":\"2\",\"type\":\"按流量\"}]")
	private String chargeCodes;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(notes = "创建时间 yyyy-MM-dd HH:mm:ss")
	private Date createDate;//创建时间 yyyy-MM-dd HH:mm:ss

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(notes = "到期时间 yyyy-MM-dd HH:mm:ss")
	private Date expireDate;//到期时间 yyyy-MM-dd HH:mm:ss

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(notes = "更新时间 yyyy-MM-dd HH:mm:ss")
	private Date updateDate;

	@ApiModelProperty(notes = "销毁时间,服务是关闭状态时有值 yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date destroyDate;

	@ApiModelProperty(notes = "商品开通地址",required = true,example = "http(s)://....")
	private String openUrl;

	@ApiModelProperty(notes = "商品续费地址",required = true,example = "http(s)://....")
	private String renewUrl;

	@ApiModelProperty(notes = "商品关闭地址",required = true,example = "http(s)://....")
	private String closeUrl;

	@ApiModelProperty(notes = "失败原因,记录商品开通，关闭失败的原因")
	private String failReason;

	@ApiModelProperty(notes = "内部使用,调用接口的一方无需传递")
	private Date endDate;

	@ApiModelProperty(notes = "过期时间倒计时,单位:天 eg:30天内",example = "30")
	private Integer num;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	@ApiModelProperty(notes = "开通开始时间查询条件 yyyy-MM-dd HH:mm:ss")
	private Date openStart;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	@ApiModelProperty(notes = "开通结束时间查询条件 yyyy-MM-dd HH:mm:ss")
	private Date openEnd;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	@ApiModelProperty(notes = "过期开始时间查询条件 yyyy-MM-dd HH:mm:ss")
	private Date expireStart;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	@ApiModelProperty(notes = "过期结束时间查询条件 yyyy-MM-dd HH:mm:ss")
	private Date expireEnd;

	public String getRelatedProductId() {
		return relatedProductId;
	}

	public ServiceInfo setRelatedProductId(String relatedProductId) {
		this.relatedProductId = relatedProductId;
		return this;
	}

	public String getRelatedProductName() {
		return relatedProductName;
	}

	public void setRelatedProductName(String relatedProductName) {
		this.relatedProductName = relatedProductName;
	}

	public Date getDestroyDate() {
		return destroyDate;
	}

	public ServiceInfo setDestroyDate(Date destroyDate) {
		this.destroyDate = destroyDate;
		return this;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public ServiceInfo setSiteCode(String siteCode) {
		this.siteCode = siteCode;
		return this;
	}

	public String getSpecifications() {
		return specifications;
	}

	public ServiceInfo setSpecifications(String specifications) {
		this.specifications = specifications;
		return this;
	}

	public String getAccount() {
		return account;
	}

	public ServiceInfo setAccount(String account) {
		this.account = account;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUuid() {
		return uuid;
	}

	public ServiceInfo setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	public String getServiceNo() {
		return serviceNo;
	}

	public ServiceInfo setServiceNo(String serviceNo) {
		this.serviceNo = serviceNo;
		return this;
	}

	public String getAppId() {
		return appId;
	}

	public ServiceInfo setAppId(String appId) {
		this.appId = appId;
		return this;
	}

	public String getServiceStatus() {
		return serviceStatus;
	}

	public ServiceInfo setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
		return this;
	}

	public String getProductId() {
		return productId;
	}

	public ServiceInfo setProductId(String productId) {
		this.productId = productId;
		return this;
	}

	public String getVersionId() {
		return versionId;
	}

	public ServiceInfo setVersionId(String versionId) {
		this.versionId = versionId;
		return this;
	}

	public String getProductSpecs() {
		return productSpecs;
	}

	public ServiceInfo setProductSpecs(String productSpecs) {
		this.productSpecs = productSpecs;
		return this;
	}

	public String getAccountId() {
		return accountId;
	}

	public ServiceInfo setAccountId(String accountId) {
		this.accountId = accountId;
		return this;
	}

	public String getOpenType() {
		return openType;
	}

	public ServiceInfo setOpenType(String openType) {
		this.openType = openType;
		return this;
	}

	public String getChargeCodes() {
		return chargeCodes;
	}

	public ServiceInfo setChargeCodes(String chargeCodes) {
		this.chargeCodes = chargeCodes;
		return this;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public ServiceInfo setCreateDate(Date createDate) {
		this.createDate = createDate;
		return this;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public ServiceInfo setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
		return this;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public ServiceInfo setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
		return this;
	}

	public String getOpenUrl() {
		return openUrl;
	}

	public ServiceInfo setOpenUrl(String openUrl) {
		this.openUrl = openUrl;
		return this;
	}

	public String getRenewUrl() {
		return renewUrl;
	}

	public ServiceInfo setRenewUrl(String renewUrl) {
		this.renewUrl = renewUrl;
		return this;
	}

	public String getCloseUrl() {
		return closeUrl;
	}

	public ServiceInfo setCloseUrl(String closeUrl) {
		this.closeUrl = closeUrl;
		return this;
	}

	public Date getEndDate() {
		return endDate;
	}

	public ServiceInfo setEndDate(Date endDate) {
		this.endDate = endDate;
		return this;
	}

	public Integer getNum() {
		return num;
	}

	public ServiceInfo setNum(Integer num) {
		this.num = num;
		return this;
	}

	public Date getOpenStart() {
		return openStart;
	}

	public ServiceInfo setOpenStart(Date openStart) {
		this.openStart = openStart;
		return this;
	}

	public Date getOpenEnd() {
		return openEnd;
	}

	public ServiceInfo setOpenEnd(Date openEnd) {
		this.openEnd = openEnd;
		return this;
	}

	public Date getExpireStart() {
		return expireStart;
	}

	public ServiceInfo setExpireStart(Date expireStart) {
		this.expireStart = expireStart;
		return this;
	}

	public Date getExpireEnd() {
		return expireEnd;
	}

	public ServiceInfo setExpireEnd(Date expireEnd) {
		this.expireEnd = expireEnd;
		return this;
	}
}
