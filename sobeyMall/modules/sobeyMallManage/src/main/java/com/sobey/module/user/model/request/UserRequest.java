/**
 * 
 */
package com.sobey.module.user.model.request;

/**
 * @author lgc
 * @date 2021年2月2日 下午4:57:31
 */
public class UserRequest {

	private Boolean root_user_flag;
	private String phone;
	private String password;
	private String site_code;
	private String login_name;
	private String nick_name;
	/**
	 * @return the root_user_flag
	 */
	public Boolean getRoot_user_flag() {
		return root_user_flag;
	}
	/**
	 * @param root_user_flag the root_user_flag to set
	 */
	public void setRoot_user_flag(Boolean root_user_flag) {
		this.root_user_flag = root_user_flag;
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
	 * @return the site_code
	 */
	public String getSite_code() {
		return site_code;
	}
	/**
	 * @param site_code the site_code to set
	 */
	public void setSite_code(String site_code) {
		this.site_code = site_code;
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
	 * @return the nick_name
	 */
	public String getNick_name() {
		return nick_name;
	}
	/**
	 * @param nick_name the nick_name to set
	 */
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}
	
	

}
