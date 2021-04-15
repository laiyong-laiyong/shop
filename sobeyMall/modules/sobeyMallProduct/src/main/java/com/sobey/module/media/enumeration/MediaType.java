package com.sobey.module.media.enumeration;

/**
 * 
 * @author lgc
 * @date 2020年1月17日 上午11:22:54
 */
public enum MediaType {

	/**
	 */
	MEDIA_LOGO("1", "商品LOGO"), 
	MEDIA_MATER_GRAPH("2", "商品主图"), 
	MEDIA_BANNER("3", "商品横幅"), 
	MEDIA_SCENE("4", "商品应用场景"), 
	MEDIA_USER_GUIDE("5", "用户手册"), 
	MEDIA_DEVELOP_GUIDE("6", "开发手册"), 
	MEDIA_PRICE_TABLE("7", "定价表"),
	MEDIA_SPOT("7", "亮点");

	MediaType(String code, String name) {
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
