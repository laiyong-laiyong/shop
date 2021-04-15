/**
 * 
 */
package com.sobey.module.aiVirtual.model.response.token;

/**
 * @author lgc
 * @date 2021年4月1日 下午2:25:23
 */
public class Data {
	
	private String access_token;
	private int expirein;
	/**
	 * @return the access_token
	 */
	public String getAccess_token() {
		return access_token;
	}
	/**
	 * @param access_token the access_token to set
	 */
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	/**
	 * @return the expirein
	 */
	public int getExpirein() {
		return expirein;
	}
	/**
	 * @param expirein the expirein to set
	 */
	public void setExpirein(int expirein) {
		this.expirein = expirein;
	}

}
