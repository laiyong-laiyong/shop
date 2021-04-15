package com.sobey.module.product.enumeration;

/**
 * 
 * @author lgc
 * @date 2020年1月17日 上午11:22:54
 */
public enum ProductState {

	/**
	 */
	on_shelves("1", "已上架"), not_shelves("2", "未上架"),
	debuging("3", "待调试"),draft("4", "草稿"),off_shelves("5", "已下架")
	,off_shelvesing("6", "即将下架");

	ProductState(String code, String name) {
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
