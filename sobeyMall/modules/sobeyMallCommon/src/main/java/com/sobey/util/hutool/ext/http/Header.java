/**
 * 
 */
package com.sobey.util.hutool.ext.http;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

/**
 * @author lgc
 * @date 2021年4月1日 下午1:52:26
 */
public class Header {

	public static Map<String, String> json() {

		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}

}
