/**
 * 
 */
package com.sobey.framework.jwt.model;

/**
 * @author lgc
 * @date 2020年10月14日 下午6:49:26
 */
public class UserInfo {
	private String site;
	private String user_id; 
	private String login_name; 
	private String user_name; 
	private String user_code; 
	private String userType;
	/**
	 * @return the site
	 */
	public String getSite() {
		return site;
	}
	/**
	 * @param site the site to set
	 */
	public void setSite(String site) {
		this.site = site;
	}
	
	
	
	/**
	 * @return the userType
	 */
	public String getUserType() {
		return userType;
	}
	/**
	 * @param userType the userType to set
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}
	/**
	 * @return the user_id
	 */
	public String getUser_id() {
		return user_id;
	}
	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	/**
	 * @return the login_name
	 */
	public String getLogin_name() {
		return login_name;
	}
	/**
	 * @param login_name the login_name to set
	 */
	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}
	/**
	 * @return the user_name
	 */
	public String getUser_name() {
		return user_name;
	}
	/**
	 * @param user_name the user_name to set
	 */
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	/**
	 * @return the user_code
	 */
	public String getUser_code() {
		return user_code;
	}
	/**
	 * @param user_code the user_code to set
	 */
	public void setUser_code(String user_code) {
		this.user_code = user_code;
	}
	
	

}
