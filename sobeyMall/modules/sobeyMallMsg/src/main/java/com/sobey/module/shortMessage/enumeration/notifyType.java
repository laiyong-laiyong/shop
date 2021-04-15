package com.sobey.module.shortMessage.enumeration;

/**
 * 
 * @author lgc
 * @date 2020年1月17日 上午11:22:54
 */
public enum notifyType {

	/**
	 * 催单通知运维
	 */
	reminder_operator("1", "reminder_operator"),
	
	/**
	 * 派单通知运维
	 * 
	 */
	distributed_operator("2", "distributed_operator"),
	/**
	 * 派单通知客户
	 * 
	 */
	distributed_customer("3", "distributed_customer"),
	
	/**
	 * 通知客户工单有消息反馈
	 * 
	 */
	reminder_customer("4", "reminder_customer"),
	/**
	 * 新增工单
	 * 
	 */
	new_work_order("5", "new_work_order"),
	/**
	 * 代金券短信通知
	 * 
	 */
	voucher_short_message("6", "voucher_short_message"),
	/**
	 * 欠费通知商城管理人员
	 * 
	 */
	arrears_short_message("7", "arrears_short_message");

	notifyType(String code, String name) {
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
