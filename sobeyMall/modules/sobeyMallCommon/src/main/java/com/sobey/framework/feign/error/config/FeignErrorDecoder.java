package com.sobey.framework.feign.error.config;

import java.io.IOException;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;

/**
 * 获取feign的报错信息
 * 
 * 
 * @author lgc
 * @date 2020年1月7日 下午3:18:05
 */
@Configuration
public class FeignErrorDecoder implements ErrorDecoder {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Exception decode(String methodKey, Response response) {

		if (HttpStatus.SC_UNAUTHORIZED == response.status()) {
			return new AppException(ExceptionType.SYS_TOKEN_AUTH_CENTER);
		}

		String json = null;
		try {
			if (response.body() != null) {
				json = Util.toString(response.body().asReader());
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		AppException ex = new AppException(ExceptionType.SYS_RUNTIME, json, null);
		return ex;
	}

}