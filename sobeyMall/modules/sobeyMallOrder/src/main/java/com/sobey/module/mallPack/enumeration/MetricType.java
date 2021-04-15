package com.sobey.module.mallPack.enumeration;

/**
 * 
 * @author lgc
 * @date 2020年1月17日 上午11:22:54
 */
public enum MetricType {

	/**
	 */
	PUBLIC_KB("1", "KB"), PUBLIC_MB("2", "MB"), PUBLIC_GB("3", "GB"), PUBLIC_SECONDS("4",
			"秒"), PUBLIC_MINUTES("5", "分钟"), PUBLIC_HOURS("6",
					"小时"), PUBLIC_NUMBER("7", "次"), PUBLIC_OTHER("8", "其他/默认单位");

	MetricType(String code, String name) {
		this.code = code;
		this.name = name;
	}

	/**
	 * 根据code获取单位名称
	 * @param code
	 * @return
	 */
	public static String getName(String code){
		for (MetricType metricType : MetricType.values()) {
			if (metricType.getCode().equals(code)){
				return metricType.getName();
			}
		}
		return "";
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
