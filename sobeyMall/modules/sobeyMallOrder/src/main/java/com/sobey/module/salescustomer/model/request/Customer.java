package com.sobey.module.salescustomer.model.request;

import javax.validation.constraints.NotBlank;

import com.baomidou.mybatisplus.annotations.TableField;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 */
public class Customer {

	@NotBlank(message = "userCode不能为空")
	@ApiModelProperty(example = "客户userCode")
	@TableField(value = "userCode")
	private String userCode;

	@NotBlank(message = "loginName不能为空")
	@ApiModelProperty(example = "客户loginName")
	@TableField(value = "loginName")
	private String loginName;
	
	
	@NotBlank(message="客户的siteCode不能为空")
	@TableField(value = "siteCode")
	@ApiModelProperty(example="客户的siteCode")
	private String siteCode;

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
	 * @return the loginName
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * @param loginName
	 *            the loginName to set
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
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
