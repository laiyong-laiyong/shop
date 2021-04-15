package com.sobey.module.discount.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;
import com.sobey.module.discount.model.request.ProductDiscount;

import io.swagger.annotations.ApiModelProperty;

/**
 * 折扣表
 * 
 */
@TableName("mall_product_discount")
public class Discount extends SuperModel<Discount> {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@NotBlank(message="userCode不能为空")
	@ApiModelProperty(example="用户code")
	@TableField(value = "userCode")
	private String userCode;
	
	
	@NotBlank(message="userSiteCode不能为空")
	@ApiModelProperty(example="用户userSiteCode")
	@TableField(value = "userSiteCode")
	private String userSiteCode;
	
	
	@NotBlank(message="userLoginName不能为空")
	@ApiModelProperty(example="用户的userLoginName")
	@TableField(value = "userLoginName")
	private String userLoginName;

	
	@ApiModelProperty(example="商品编号")
	@TableField(value = "productId")
	private String productId;
	
	
	@ApiModelProperty(example="折扣状态")
	@TableField(value = "status")
	private String status;
	
	
	@ApiModelProperty(example="请求的uuid")
	@TableField(value = "requestUuid")
	private String requestUuid;
	
	
	
	@ApiModelProperty(example="销售")
	@TableField(value = "salesman")
	private String salesman;
	
	@ApiModelProperty(example="客户")
	@TableField(value = "customer")
	private String customer;
	
	@ApiModelProperty(example="描述")
	@TableField(value = "description")
	private String description;
	
	
	
	
	@ApiModelProperty(example="折扣,值是小数")
	@TableField(value = "discount")
	private BigDecimal discount;
	

	@ApiModelProperty(example="创建时间,不用填",hidden=true)
	@TableField(value = "createDate", fill = FieldFill.INSERT)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date createDate;

	@ApiModelProperty(example="修改时间,不用填",hidden=true)
	@TableField(value = "updateDate", fill = FieldFill.UPDATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date updateDate;
	
	
	@NotNull(message="expirationDate不能为空")
	@ApiModelProperty(example="过期时间,格式为：yyyy-MM-dd HH:mm:ss")
	@TableField(value = "expirationDate")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date expirationDate;
	
	
	/**
	 * 以下字段在数据库中不存在，供页面查询用
	 * 
	 */
	@ApiModelProperty(example="开始时间,查询用,格式为：yyyy-MM-dd HH:mm:ss")
	@TableField(exist = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date startDate;

	@ApiModelProperty(example="结束时间,查询用,格式为：yyyy-MM-dd HH:mm:ss")
	@TableField(exist = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date endDate;
	
	
	@NotEmpty(message="productDiscounts不能为空")
	@Valid
	@TableField(exist = false)
	private List<ProductDiscount> productDiscounts;

	


	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return the userCode
	 */
	public String getUserCode() {
		return userCode;
	}

	/**
	 * @param userCode the userCode to set
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * @return the discount
	 */
	public BigDecimal getDiscount() {
		return discount;
	}

	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the expirationDate
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @param expirationDate the expirationDate to set
	 */
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * @return the productDiscounts
	 */
	public List<ProductDiscount> getProductDiscounts() {
		return productDiscounts;
	}

	/**
	 * @param productDiscounts the productDiscounts to set
	 */
	public void setProductDiscounts(List<ProductDiscount> productDiscounts) {
		this.productDiscounts = productDiscounts;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the salesman
	 */
	public String getSalesman() {
		return salesman;
	}

	/**
	 * @param salesman the salesman to set
	 */
	public void setSalesman(String salesman) {
		this.salesman = salesman;
	}

	/**
	 * @return the customer
	 */
	public String getCustomer() {
		return customer;
	}

	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(String customer) {
		this.customer = customer;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the requestUuid
	 */
	public String getRequestUuid() {
		return requestUuid;
	}

	/**
	 * @param requestUuid the requestUuid to set
	 */
	public void setRequestUuid(String requestUuid) {
		this.requestUuid = requestUuid;
	}

	/**
	 * @return the userLoginName
	 */
	public String getUserLoginName() {
		return userLoginName;
	}

	/**
	 * @param userLoginName the userLoginName to set
	 */
	public void setUserLoginName(String userLoginName) {
		this.userLoginName = userLoginName;
	}

	/**
	 * @return the userSiteCode
	 */
	public String getUserSiteCode() {
		return userSiteCode;
	}

	/**
	 * @param userSiteCode the userSiteCode to set
	 */
	public void setUserSiteCode(String userSiteCode) {
		this.userSiteCode = userSiteCode;
	}

	

	
	
	

}
