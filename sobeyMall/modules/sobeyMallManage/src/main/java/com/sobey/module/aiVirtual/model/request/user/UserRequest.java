/**
 * 
 */
package com.sobey.module.aiVirtual.model.request.user;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author lgc
 * @date 2021年4月1日 下午3:10:09
 */
public class UserRequest {

	private String token;
	@NotBlank(message="oranzition_id不能为空")
	private String oranzition_id;
	@NotBlank(message="oranzition_name不能为空")
	private String oranzition_name;
	
	@NotNull(message="user不能为空")
	@Valid
	private UserDetail user;

	/**
	 * @return the oranzition_id
	 */
	public String getOranzition_id() {
		return oranzition_id;
	}

	/**
	 * @param oranzition_id
	 *            the oranzition_id to set
	 */
	public void setOranzition_id(String oranzition_id) {
		this.oranzition_id = oranzition_id;
	}

	/**
	 * @return the oranzition_name
	 */
	public String getOranzition_name() {
		return oranzition_name;
	}

	/**
	 * @param oranzition_name
	 *            the oranzition_name to set
	 */
	public void setOranzition_name(String oranzition_name) {
		this.oranzition_name = oranzition_name;
	}

	/**
	 * @return the user
	 */
	public UserDetail getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(UserDetail user) {
		this.user = user;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}
	
	

}
