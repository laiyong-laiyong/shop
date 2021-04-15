package com.sobey.module.permissions.enumeration;

/**
 * 
 * @author lgc
 * @date 2020年2月11日 上午10:39:24
 */
public enum PermissionsType {

	/**
	 */
	PUBLIC_ALL("*", "全部"), 
	PUBLIC_CREATE("create", "新建"), 
	PUBLIC_UPDATE("update", "修改"), 
	PUBLIC_DELETE("delete", "删除"), 
	PUBLIC_VIEW("view", "查看");

	PermissionsType(String code, String name) {
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
