package com.sobey.module.salesman.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

/**
 * C销售表
 * 
 */
@TableName("mall_saleman")
public class Salesman extends SuperModel<Salesman> {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@NotBlank(message="usercode不能为空")
	@ApiModelProperty(example = "销售code")
	@TableField(value = "userCode")
	private String userCode;
	
	
	@NotBlank(message="name不能为空")
	@ApiModelProperty(example = "销售名字")
	@TableField(value = "name")
	private String name;
	
	@NotBlank(message="loginName不能为空")
	@ApiModelProperty(example = "销售loginName")
	@TableField(value = "loginName")
	private String loginName;

	@ApiModelProperty(example = "描述")
	@TableField(value = "description")
	private String description;
	
	
	@NotBlank(message="销售人员的siteCode不能为空")
	@TableField(value = "siteCode")
	@ApiModelProperty(example="销售人员的siteCode")
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

	@TableField(exist = false)
	@ApiModelProperty(notes = "月度销售额")
	private BigDecimal saleAmount;

	@TableField(exist = false)
	@ApiModelProperty(notes = "月度销售利润")
	private BigDecimal saleProfit;

	@TableField(exist = false)
	@ApiModelProperty(notes = "月份，查询每月销售情况时保存")
	private String billDate;

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public BigDecimal getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(BigDecimal saleAmount) {
		this.saleAmount = saleAmount;
	}

	public BigDecimal getSaleProfit() {
		return saleProfit;
	}

	public void setSaleProfit(BigDecimal saleProfit) {
		this.saleProfit = saleProfit;
	}

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
	 * @param userCode
	 *            the userCode to set
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
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
	 * @return the loginName
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * @param loginName the loginName to set
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
