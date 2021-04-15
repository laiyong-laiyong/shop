package com.sobey.module.productservice.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 */
@TableName("service_info")
public class ServiceInfo extends SuperModel<ServiceInfo> {


	@TableField("serviceNo")
	@ApiModelProperty(notes = "服务编号")
	private String serviceNo;

	@TableField("appId")
	@ApiModelProperty(notes = "App编号")
	private String appId;

	@TableField("serviceStatus")
	@ApiModelProperty(notes = "服务状态 0-关闭 1-正常 2-开通中 3-开通失败",example = "0")
	private String serviceStatus;

	@ApiModelProperty(notes = "商品编码",required = true,example = "123")
	@TableField("productId")
	private String productId;

	@ApiModelProperty(notes = "关联商品Id(该商品若属于某个商品则传递该值)",example = "456")
	@TableField("relatedProductId")
	private String relatedProductId;

	@ApiModelProperty(notes = "关联商品名称",example = "关联")
	@TableField("relatedProductName")
	private String relatedProductName;

	@ApiModelProperty(notes = "版本编码",example = "1")
	@TableField("versionId")
	private String versionId;

	@ApiModelProperty(notes = "存储前端自定义信息,Json字符串")
	@TableField("productSpecs")
	private String productSpecs;

	@ApiModelProperty(notes = "存储前端自定义信息,Json字符串")
	@TableField("specifications")
	private String specifications;

	@ApiModelProperty(notes = "html字符串,存储商品开通成功后登陆的信息")
	@TableField("message")
	private String message;

	@ApiModelProperty(notes = "站点",required = true,example = "123")
	@TableField("siteCode")
	private String siteCode;

	@ApiModelProperty(notes = "用户ID",required = true,example = "123")
	@TableField("accountId")
	private String accountId;//用户id

	@ApiModelProperty(notes = "用户名",required = true,example = "张三")
	@TableField("account")
	private String account;

	@ApiModelProperty(notes = "开通类型 1-包周期(按版本) 2-按量",example = "1",required = true)
	@TableField("openType")
	private String openType;

	@ApiModelProperty(notes = "计费编码,JSON字符串,计费类型为按量时必填",example = "[{\"id\":\"100\",\"typeCode\":\"1\",\"type\":\"按时长\"},{\"id\":\"101\",\"typeCode\":\"2\",\"type\":\"按流量\"}]")
	@TableField("chargeCodes")
	private String chargeCodes;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(notes = "创建时间 yyyy-MM-dd HH:mm:ss")
	@TableField("createDate")
	private Date createDate;//创建时间 yyyy-MM-dd HH:mm:ss

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(notes = "到期时间 yyyy-MM-dd HH:mm:ss")
	@TableField("expireDate")
	private Date expireDate;//到期时间 yyyy-MM-dd HH:mm:ss

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(notes = "更新时间 yyyy-MM-dd HH:mm:ss")
	@TableField(value = "updateDate",fill = FieldFill.UPDATE)
	private Date updateDate;

	@ApiModelProperty(notes = "销毁时间,服务是关闭状态时有值 yyyy-MM-dd HH:mm:ss")
	@TableField("destroyDate")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date destroyDate;

	@TableField("openUrl")
	@ApiModelProperty(notes = "商品开通地址",required = true,example = "http(s)://....")
	private String openUrl;

	@TableField("renewUrl")
	@ApiModelProperty(notes = "商品续费地址",example = "http(s)://....")
	private String renewUrl;

	@TableField("closeUrl")
	@ApiModelProperty(notes = "商品关闭地址",required = true,example = "http(s)://....")
	private String closeUrl;

	@TableField("failReason")
	@ApiModelProperty(notes = "失败原因,记录商品开通，关闭失败的原因")
	private String failReason;

	@TableField(exist = false)
	@ApiModelProperty(notes = "内部使用,调用接口的一方无需传递")
	private Date endDate;

	@TableField(exist = false)
	@ApiModelProperty(notes = "过期时间倒计时,单位:天 eg:30天内",example = "30")
	private Integer num;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(notes = "开通开始时间查询条件 yyyy-MM-dd HH:mm:ss")
	@TableField(exist = false)
	private Date openStart;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(notes = "开通结束时间查询条件 yyyy-MM-dd HH:mm:ss")
	@TableField(exist = false)
	private Date openEnd;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(notes = "过期开始时间查询条件 yyyy-MM-dd HH:mm:ss")
	@TableField(exist = false)
	private Date expireStart;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(notes = "过期结束时间查询条件 yyyy-MM-dd HH:mm:ss")
	@TableField(exist = false)
	private Date expireEnd;
	
	
	@TableField(exist = false)
	@ApiModelProperty(example="是否启用关联商品，1：启用，2：不启用")
	private String enableRelation;
	
	
	

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

	public Date getDestroyDate() {
		return destroyDate;
	}

	public void setDestroyDate(Date destroyDate) {
		this.destroyDate = destroyDate;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public Date getOpenStart() {
		return openStart;
	}

	public ServiceInfo setOpenStart(Date openStart) {
		this.openStart = openStart;
		return this;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getMessage() {
		return message;
	}

	public ServiceInfo setMessage(String message) {
		this.message = message;
		return this;
	}

	public Date getOpenEnd() {
		return openEnd;
	}

	public ServiceInfo setOpenEnd(Date openEnd) {
		this.openEnd = openEnd;
		return this;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
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

	public String getAppId() {
		return appId;
	}

	public ServiceInfo setAppId(String appId) {
		this.appId = appId;
		return this;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getServiceNo() {
		return serviceNo;
	}

	public void setServiceNo(String serviceNo) {
		this.serviceNo = serviceNo;
	}

	public String getServiceStatus() {
		return serviceStatus;
	}

	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getProductSpecs() {
		return productSpecs;
	}

	public void setProductSpecs(String productSpecs) {
		this.productSpecs = productSpecs;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getOpenType() {
		return openType;
	}

	public void setOpenType(String openType) {
		this.openType = openType;
	}

	public String getChargeCodes() {
		return chargeCodes;
	}

	public void setChargeCodes(String chargeCodes) {
		this.chargeCodes = chargeCodes;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getOpenUrl() {
		return openUrl;
	}

	public void setOpenUrl(String openUrl) {
		this.openUrl = openUrl;
	}

	public String getRenewUrl() {
		return renewUrl;
	}

	public void setRenewUrl(String renewUrl) {
		this.renewUrl = renewUrl;
	}

	public String getCloseUrl() {
		return closeUrl;
	}

	public void setCloseUrl(String closeUrl) {
		this.closeUrl = closeUrl;
	}

	/**
	 * @return the enableRelation
	 */
	public String getEnableRelation() {
		return enableRelation;
	}

	/**
	 * @param enableRelation the enableRelation to set
	 */
	public void setEnableRelation(String enableRelation) {
		this.enableRelation = enableRelation;
	}
	
	
}
