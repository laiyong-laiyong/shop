/**
 * 
 */
package com.sobey.module.fegin.order.entity.response;

import java.util.Map;

/**
 * @author lgc
 * @date 2020年2月20日 上午11:11:33
 */
public class Result {

	private String state;
	private String error_code;
	private String error_msg;
	private Map<String,Object> extend_message;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public String getError_msg() {
		return error_msg;
	}

	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}

	public Map<String,Object> getExtend_message() {
		return extend_message;
	}

	public void setExtend_message(Map<String,Object> extend_message) {
		this.extend_message = extend_message;
	}

}
