/**
 * 
 */
package com.sobey.framework.jwt.model;

import java.util.List;

/**
 * @author lgc
 * @date 2020年2月28日 下午1:59:15
 */
public class Token {

	private String user_name;
	private List<String> scope;
	private Long exp;
	private List<String> authorities;
	private String jti;
	private String client_id;
	private UserInfo user_info;

	
	/**
	 * @return the user_info
	 */
	public UserInfo getUser_info() {
		return user_info;
	}

	/**
	 * @param user_info the user_info to set
	 */
	public void setUser_info(UserInfo user_info) {
		this.user_info = user_info;
	}

	/**
	 * @return the user_name
	 */
	public String getUser_name() {
		return user_name;
	}

	/**
	 * @param user_name
	 *            the user_name to set
	 */
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	/**
	 * @return the scope
	 */
	public List<String> getScope() {
		return scope;
	}

	/**
	 * @param scope
	 *            the scope to set
	 */
	public void setScope(List<String> scope) {
		this.scope = scope;
	}

	/**
	 * @return the exp
	 */
	public Long getExp() {
		return exp;
	}

	/**
	 * @param exp
	 *            the exp to set
	 */
	public void setExp(Long exp) {
		this.exp = exp;
	}

	/**
	 * @return the authorities
	 */
	public List<String> getAuthorities() {
		return authorities;
	}

	/**
	 * @param authorities
	 *            the authorities to set
	 */
	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}

	/**
	 * @return the jti
	 */
	public String getJti() {
		return jti;
	}

	/**
	 * @param jti
	 *            the jti to set
	 */
	public void setJti(String jti) {
		this.jti = jti;
	}

	/**
	 * @return the client_id
	 */
	public String getClient_id() {
		return client_id;
	}

	/**
	 * @param client_id
	 *            the client_id to set
	 */
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

}
