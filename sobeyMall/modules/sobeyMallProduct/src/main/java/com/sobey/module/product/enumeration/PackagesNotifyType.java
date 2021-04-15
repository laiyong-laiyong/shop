package com.sobey.module.product.enumeration;

/**
 * 是否启用套餐包通知枚举
 * @author lgc
 * @date 2020年1月17日 上午11:22:54
 */
public enum PackagesNotifyType {

	/**
	 */
	yes("1", "启用"), no("2", "不启用");

	PackagesNotifyType(String code, String name) {
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
