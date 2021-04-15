package com.sobey.module.taskMonitor.enumeration;

/**
 * 
 * @author lgc
 * @date 2020年1月17日 上午11:22:54
 */
public enum EventType {

	/**
	 */
	started("STARTED", "开始"), failed("FAILED", "失败"),success("SUCCEED", "成功"),
	redirected("REDIRECTED", "重定向"),cancel("CANCELED", "取消");

	EventType(String code, String name) {
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
