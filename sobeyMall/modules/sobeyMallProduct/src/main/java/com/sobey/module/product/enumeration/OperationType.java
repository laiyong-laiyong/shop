package com.sobey.module.product.enumeration;

/**
 * C修改商品的时候字段的操作类型:新增，修改，删除
 * 
 * @author lgc
 * @date 2020年1月17日 上午11:22:54
 */
public enum OperationType {

	/**
	 */
	insert("1", "insert"), 
	update("2", "update"),
	delete("3", "delete");

	OperationType(String code, String name) {
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
