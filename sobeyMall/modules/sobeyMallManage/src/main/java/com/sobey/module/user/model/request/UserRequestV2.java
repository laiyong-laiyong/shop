/**
 * 
 */
package com.sobey.module.user.model.request;

/**
 * @author lgc
 * @date 2021年2月2日 下午4:57:31
 */
public class UserRequestV2 {

	private Boolean rootUserFlag;
	private String phone;
	private String password;
	private String siteCode;
	private String loginName;
	private String nickName;
	/**
	 * @return the rootUserFlag
	 */
	public Boolean getRootUserFlag() {
		return rootUserFlag;
	}
	/**
	 * @param rootUserFlag the rootUserFlag to set
	 */
	public void setRootUserFlag(Boolean rootUserFlag) {
		this.rootUserFlag = rootUserFlag;
	}
	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
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
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}
	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	

}
