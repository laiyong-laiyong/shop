package com.sobey.module.packages.enumeration;

/**
 * 
 * @author lgc
 * @date 2020年1月17日 上午11:22:54
 */
public enum EffectiveDurationType {

	/**
	 */
	year("1", "年"), 
	month("2", "月"),
	day("3", "天"),
	hour("4", "小时");

	EffectiveDurationType(String code, String name) {
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

	@Override
	public String toString() {
		return code + ":" + name;
	}

}
