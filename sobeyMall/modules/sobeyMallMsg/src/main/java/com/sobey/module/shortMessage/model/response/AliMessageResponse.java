package com.sobey.module.shortMessage.model.response;

/**
 */
public class AliMessageResponse {
	
	
	private String Message;
	private String RequestId;
	private String Code;
	/**
	 * @return the message
	 */
	public String getMessage() {
		return Message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		Message = message;
	}
	/**
	 * @return the requestId
	 */
	public String getRequestId() {
		return RequestId;
	}
	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(String requestId) {
		RequestId = requestId;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return Code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		Code = code;
	}


}
