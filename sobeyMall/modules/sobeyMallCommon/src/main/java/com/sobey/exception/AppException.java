package com.sobey.exception;

public class AppException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String requestId;

	private String code;
	private String message;
	private String detailMsg;

	
	public AppException(String message) {
		super(message);
		this.code = null;
		this.message = message;
	}
	
	public AppException(String message,Throwable t) {
		super(message);
		this.code = message;
		this.message = message;
		this.detailMsg = ExceptionKit.toString(t);
	}
	
	public AppException(ExceptionType type) {
		super(type.getMessage());
		this.code = type.getCode();
		this.message = type.getMessage();
	}
	
	public AppException(ExceptionType type, Throwable t) {
		super(type.getMessage());
		this.code = type.getCode();
		this.message = type.getMessage();
		this.detailMsg = ExceptionKit.toString(t);
	}
	
	/**
	 * 
	 * 
	 * @param type
	 * @param message  这里作为自己写的描述,主要针对于调用其他平台接口返回报错，显示给前台
	 * @param t
	 */
	public AppException(ExceptionType type,String message, Throwable t) {
		super(message);
		this.code = type.getCode();
		this.message = message;
		this.detailMsg = ExceptionKit.toString(t);
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetailMsg() {
		return detailMsg;
	}

	public void setDetailMsg(String detailMsg) {
		this.detailMsg = detailMsg;
	}

	
}
