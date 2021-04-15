package com.sobey.module.salescustomer.model;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;
import com.sobey.module.salescustomer.model.request.Customer;
import com.sobey.module.salesman.model.Salesman;

import io.swagger.annotations.ApiModelProperty;

/**
 * C销售和客户的关联表
 * 
 */
@TableName("mall_saleman_customer")
public class Salescustomer extends SuperModel<Salescustomer> {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@NotBlank(message="saleUserCode不能为空")
	@ApiModelProperty(example = "销售saleUserCode")
	@TableField(value = "saleUserCode")
	private String saleUserCode;
	
	@NotBlank(message="createUserCode不能为空")
	@ApiModelProperty(example = "创建者UserCode")
	@TableField(value = "createUserCode")
	private String createUserCode;
	
	
	@ApiModelProperty(example = "客户userCode")
	@TableField(value = "customerUserCode")
	private String customerUserCode;
	
	@ApiModelProperty(example = "客户loginName")
	@TableField(value = "customerLoginName")
	private String customerLoginName;

	@ApiModelProperty(example = "描述")
	@TableField(value = "description")
	private String description;
	
	
	@TableField(value = "siteCode")
	@ApiModelProperty(example="客户的siteCode")
	private String siteCode;

	@ApiModelProperty(example = "创建时间,不用填")
	@TableField(value = "createDate", fill = FieldFill.INSERT)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date createDate;

	@ApiModelProperty(example = "修改时间,不用填")
	@TableField(value = "updateDate", fill = FieldFill.UPDATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date updateDate;

	/**
	 * 以下字段在数据库中不存在，供页面查询用
	 * 
	 */
	@ApiModelProperty(example = "开始时间,查询用,格式为：yyyy-MM-dd HH:mm:ss", hidden = true)
	@TableField(exist = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date startDate;

	@ApiModelProperty(example = "结束时间,查询用,格式为：yyyy-MM-dd HH:mm:ss", hidden = true)
	@TableField(exist = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date endDate;
	
	
	@ApiModelProperty(example = "只新增用，示例：[{\"userCode\":\"\",\"loginName\":\"\"}]")
	@NotEmpty(message="customers不能为空")
	@Valid
	@TableField(exist = false)
	private List<Customer> customers;
	
	
//	@ApiModelProperty(example = "查询时作为返回值用")
	@TableField(exist = false)
	private Salesman saleman;
	
	
	

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
	 * @param startDate
	 *            the startDate to set
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
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the saleUserCode
	 */
	public String getSaleUserCode() {
		return saleUserCode;
	}

	/**
	 * @param saleUserCode the saleUserCode to set
	 */
	public void setSaleUserCode(String saleUserCode) {
		this.saleUserCode = saleUserCode;
	}

	

	/**
	 * @return the createUserCode
	 */
	public String getCreateUserCode() {
		return createUserCode;
	}

	/**
	 * @param createUserCode the createUserCode to set
	 */
	public void setCreateUserCode(String createUserCode) {
		this.createUserCode = createUserCode;
	}

	/**
	 * @return the customerUserCode
	 */
	public String getCustomerUserCode() {
		return customerUserCode;
	}

	/**
	 * @param customerUserCode the customerUserCode to set
	 */
	public void setCustomerUserCode(String customerUserCode) {
		this.customerUserCode = customerUserCode;
	}

	/**
	 * @return the customerLoginName
	 */
	public String getCustomerLoginName() {
		return customerLoginName;
	}

	/**
	 * @param customerLoginName the customerLoginName to set
	 */
	public void setCustomerLoginName(String customerLoginName) {
		this.customerLoginName = customerLoginName;
	}

	/**
	 * @return the customers
	 */
	public List<Customer> getCustomers() {
		return customers;
	}

	/**
	 * @param customers the customers to set
	 */
	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}

	/**
	 * @return the saleman
	 */
	public Salesman getSaleman() {
		return saleman;
	}

	/**
	 * @param saleman the saleman to set
	 */
	public void setSaleman(Salesman saleman) {
		this.saleman = saleman;
	}

	/**
	 * @return the siteCode
	 */
	public String getSiteCode() {
		return siteCode;
	}

	/**
	 * @param siteCode the siteCode to set
	 */
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	

	

	
	
}
