/**
 * 
 */
package com.sobey.module.topic.model.request;

/**
 * @author lgc
 * @date 2020年10月13日 下午6:35:16
 */
public class Subscription {

	private String content_type;
	private String protocol;
	private String terminal;

	/**
	 * @return the content_type
	 */
	public String getContent_type() {
		return content_type;
	}

	/**
	 * @param content_type
	 *            the content_type to set
	 */
	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}

	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol
	 *            the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return the terminal
	 */
	public String getTerminal() {
		return terminal;
	}

	/**
	 * @param terminal
	 *            the terminal to set
	 */
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

}
