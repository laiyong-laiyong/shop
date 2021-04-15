package com.sobey.module.user.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.sobey.framework.mybatisPlus.SuperModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 */
@TableName("mall_init_data_user")
@ApiModel
public class User extends SuperModel<User> {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(example = "用户名/登录名")
	@TableField(value = "loginName")
	private String loginName;

	@ApiModelProperty(example = "用户密码")
	@TableField(value = "password")
	private String password;

	@ApiModelProperty(example = "电话号码")
	@TableField(value = "phone")
	private String phone;

	@ApiModelProperty(example = "昵称")
	@TableField(value = "nickName")
	private String nickName;

	@ApiModelProperty(example = "是否为根级父账号")
	@TableField(value = "rootUserFlag")
	private Integer rootUserFlag;

	@ApiModelProperty(example = "站点code")
	@TableField(value = "siteCode")
	private String siteCode;
	
	

	@ApiModelProperty(example = "是否成功执行(yes,no)")
	@TableField(value = "success")
	private String success;
	
	
	
	

	/**
	 * @return the success
	 */
	public String getSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(String success) {
		this.success = success;
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName
	 *            the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * @return the rootUserFlag
	 */
	public Integer getRootUserFlag() {
		return rootUserFlag;
	}

	/**
	 * @param rootUserFlag
	 *            the rootUserFlag to set
	 */
	public void setRootUserFlag(Integer rootUserFlag) {
		this.rootUserFlag = rootUserFlag;
	}

	/**
	 * @return the siteCode
	 */
	public String getSiteCode() {
		return siteCode;
	}

	/**
	 * @param siteCode
	 *            the siteCode to set
	 */
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

}
