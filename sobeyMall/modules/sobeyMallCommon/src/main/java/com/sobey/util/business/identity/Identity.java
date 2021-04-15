package com.sobey.util.business.identity;

/**
 * 凌云商城公共的字段标识
 * 
 * @author lgc
 * @date 2020年1月17日 上午11:22:54
 */
public enum Identity {

	/**
	 */
	
	PUBLIC_SITE_CODE("sobeyLingYunMall", "站点code,site_code"), 
	
	//备注client_code之前用的是Lingyun_mall
	PUBLIC_CLIENT_CODE("Lingyun_mall", "客户端,client_code"), 
	PUBLIC_ORG("sobeyMallOrg", "组织结构根节点");

	Identity(String code, String name) {
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
