package com.sobey.module.workOrder.enumeration;

/**
 * 
 * @author lgc
 * @date 2020年1月17日 上午11:22:54
 */
public enum WorkOrderDeleteState {

	/**
	 */
	deleted(0, "已删除"), 
	not_delete(1, "未删除");

	WorkOrderDeleteState(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	private Integer code;
	private String name;

	

	/**
	 * @return the code
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(Integer code) {
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
