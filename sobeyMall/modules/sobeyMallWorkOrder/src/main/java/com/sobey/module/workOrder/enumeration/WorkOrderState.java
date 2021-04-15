package com.sobey.module.workOrder.enumeration;

/**
 * 
 * @author lgc
 * @date 2020年1月17日 上午11:22:54
 */
public enum WorkOrderState {

	/**
	 */
	distributing("1", "派发中"), 
	distributed("2", "已派发"),
	processing("3", "处理中"),
	feedback("4", "待我反馈"),
	confirm("5", "待我确认"),
	closed("6", "已关闭"),
	canceled("7", "已撤销");

	WorkOrderState(String code, String name) {
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
