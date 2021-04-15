/**
 * 
 */
package com.sobey.module.productDebug.model.request;

import java.util.List;

import com.sobey.module.metric.model.Metric;

/**
 * @author lgc
 * @date 2020年3月23日 下午2:03:49
 */
public class Debug {
	
	private boolean debug;
	private String appId;
	private String userCode;
	private String openType;
	private String productCode;
	private String versionCode;
	private List<Metric>  charg;
	/**
	 * @return the debug
	 */
	public boolean isDebug() {
		return debug;
	}
	/**
	 * @param debug the debug to set
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}
	/**
	 * @param appId the appId to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}
	/**
	 * @return the userCode
	 */
	public String getUserCode() {
		return userCode;
	}
	/**
	 * @param userCode the userCode to set
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	/**
	 * @return the openType
	 */
	public String getOpenType() {
		return openType;
	}
	/**
	 * @param openType the openType to set
	 */
	public void setOpenType(String openType) {
		this.openType = openType;
	}
	/**
	 * @return the productCode
	 */
	public String getProductCode() {
		return productCode;
	}
	/**
	 * @param productCode the productCode to set
	 */
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	/**
	 * @return the versionCode
	 */
	public String getVersionCode() {
		return versionCode;
	}
	/**
	 * @param versionCode the versionCode to set
	 */
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	/**
	 * @return the charg
	 */
	public List<Metric> getCharg() {
		return charg;
	}
	/**
	 * @param charg the charg to set
	 */
	public void setCharg(List<Metric> charg) {
		this.charg = charg;
	}
	
	
	
	

}
