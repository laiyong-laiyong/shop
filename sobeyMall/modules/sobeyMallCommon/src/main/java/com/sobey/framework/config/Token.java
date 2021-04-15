package com.sobey.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "token.time")
public class Token {

	private int method;
	private int playPath;
	private boolean playPathEnable;
	public int getMethod() {
		return method;
	}
	public void setMethod(int method) {
		this.method = method;
	}
	public int getPlayPath() {
		return playPath;
	}
	public void setPlayPath(int playPath) {
		this.playPath = playPath;
	}
	public boolean isPlayPathEnable() {
		return playPathEnable;
	}
	public void setPlayPathEnable(boolean playPathEnable) {
		this.playPathEnable = playPathEnable;
	}

	

}
