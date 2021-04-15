/**
 * 
 */
package com.sobey.module.taskMonitor.model.response.message;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author lgc
 * @date 2020年10月12日 下午2:27:08
 */
public class MessageInfo {
	
	private String site;
	private String syetem;
	
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
//	private Date time;
	
	
	private String user;
	private String client_ip;
	/**
	 * @return the site
	 */
	public String getSite() {
		return site;
	}
	/**
	 * @param site the site to set
	 */
	public void setSite(String site) {
		this.site = site;
	}
	/**
	 * @return the syetem
	 */
	public String getSyetem() {
		return syetem;
	}
	/**
	 * @param syetem the syetem to set
	 */
	public void setSyetem(String syetem) {
		this.syetem = syetem;
	}
//	/**
//	 * @return the time
//	 */
//	public Date getTime() {
//		return time;
//	}
//	/**
//	 * @param time the time to set
//	 */
//	public void setTime(Date time) {
//		this.time = time;
//	}
	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}
	/**
	 * @return the client_ip
	 */
	public String getClient_ip() {
		return client_ip;
	}
	/**
	 * @param client_ip the client_ip to set
	 */
	public void setClient_ip(String client_ip) {
		this.client_ip = client_ip;
	}
	
	
	

}
