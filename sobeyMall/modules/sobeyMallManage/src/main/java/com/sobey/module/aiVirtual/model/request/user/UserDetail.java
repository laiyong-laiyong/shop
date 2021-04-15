/**
 * 
 */
package com.sobey.module.aiVirtual.model.request.user;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author lgc
 * @date 2021年4月1日 下午3:11:26
 */
public class UserDetail {
	
	@NotBlank(message = "uid不能为空")
	private String uid;
	@NotBlank(message = "username不能为空")
	private String username;
	@NotBlank(message = "account不能为空")
	private String account;
	
	@Digits(integer = 10,fraction = 0 ,message="usertype不能为空且必须是数字")
	private int usertype;
	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}
	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}
	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}
	/**
	 * @return the usertype
	 */
	public int getUsertype() {
		return usertype;
	}
	/**
	 * @param usertype the usertype to set
	 */
	public void setUsertype(int usertype) {
		this.usertype = usertype;
	}
	
	
	
	
	

}
