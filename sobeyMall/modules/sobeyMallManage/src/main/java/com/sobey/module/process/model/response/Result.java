/**
 * 
 */
package com.sobey.module.process.model.response;

/**
 * @author lgc
 * @date 2020年10月12日 下午2:23:45
 */
public class Result {
	
	/**
	 * -1表示失败，1表示成功
	 */
	private String status;
	private String message;
	private String result;
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
	
	
	

}
