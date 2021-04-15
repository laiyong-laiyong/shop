package com.sobey.module.product.enumeration;

/**
 * 
 * @author lgc
 * @date 2020年1月17日 上午11:22:54
 */
public enum VoucherType {

	/**
	 */
	hot("1", "支持代金券"), not_hot("2", "不支持代金券");

	VoucherType(String code, String name) {
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
