package com.sobey.module.resource.enumeration;

/**
 * 
 * @author lgc
 * @date 2020年2月11日 上午10:39:24
 */
public enum SuccessType {

	/**
	 */
	YES("yes", "成功"), 
	NO("no", "失败");

	SuccessType(String code, String name) {
		this.code = code;
		this.name = name;
	}

	private String code;
	private String name;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
