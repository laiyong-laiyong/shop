/**
 * 
 */
package com.sobey.module.taskMonitor.model.response.message;

/**
 * @author lgc
 * @date 2020年10月12日 下午2:23:45
 */
public class Message {
	
	private String event;
	
	
	private Context message;
	
	private MessageInfo op;

	/**
	 * @return the event
	 */
	public String getEvent() {
		return event;
	}

	/**
	 * @param event the event to set
	 */
	public void setEvent(String event) {
		this.event = event;
	}

	/**
	 * @return the message
	 */
	public Context getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(Context message) {
		this.message = message;
	}

	/**
	 * @return the op
	 */
	public MessageInfo getOp() {
		return op;
	}

	/**
	 * @param op the op to set
	 */
	public void setOp(MessageInfo op) {
		this.op = op;
	}
	
	
	

}
