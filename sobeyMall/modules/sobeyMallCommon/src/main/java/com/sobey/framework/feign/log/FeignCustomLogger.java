package com.sobey.framework.feign.log;

import java.io.IOException;

import feign.Logger;
import feign.Request;
import feign.Response;

/**
 * 由于feign日志只能记录debug级别，
 * 自定义的feign日志，记录info级别
 * 
 * @author lgc
 * @date 2020年1月7日 下午3:14:16
 */
public class FeignCustomLogger extends Logger {

	private final org.slf4j.Logger logger;

	
	public FeignCustomLogger(org.slf4j.Logger logger) {
		this.logger = logger;
	}

	@Override
	protected void log(String configKey, String format, Object... args) {
		// info级别
		if (logger.isInfoEnabled()) {
			this.logger.info(String.format(methodTag(configKey) + format, args));
		}
	}

	@Override
	protected void logRequest(String configKey, Level logLevel, Request request) {
		// info级别
		if (this.logger.isInfoEnabled()) {
			super.logRequest(configKey, logLevel, request);
		}

	}

	@Override
	protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response,
			long elapsedTime) throws IOException {
		// info级别
		return this.logger.isInfoEnabled()
				? super.logAndRebufferResponse(configKey, logLevel, response, elapsedTime)
				: response;
	}
}