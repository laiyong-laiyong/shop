package com.sobey.framework.httpclient;

import com.alibaba.fastjson.JSONObject;
import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;
import com.sobey.framework.spring.SpringContextHolder;
import com.sobey.util.common.ToolKit;
import com.sobey.util.common.json.JsonKit;
import com.sobey.util.common.uuid.UUIDUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * 
 * 
 * @author lgc
 * @date 2019年12月11日 下午1:46:07
 */
public class RestCaller {

	private String url;
	private String body;
	private HttpMethod method;
	private Map<String, String> headParams;

	private static final ThreadLocal<String> urlLocal = new ThreadLocal<>();

	private RestTemplate rest;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	public RestCaller(String url, HttpMethod method) {
		this.url = url;
		this.method = method;
		rest = SpringContextHolder.getBean("restTemplate");
	}

	public static RestCaller get(String url) {
		return new RestCaller(url, HttpMethod.GET);
	}

	public static RestCaller post(String url) {
		return new RestCaller(url, HttpMethod.POST);
	}

	public static RestCaller put(String url) {
		return new RestCaller(url, HttpMethod.PUT);
	}

	public static RestCaller delete(String url) {
		return new RestCaller(url, HttpMethod.DELETE);
	}

	public static RestCaller patch(String url) {
		return new RestCaller(url, HttpMethod.PATCH);
	}

	public String execute() {
		return executeReturn(String.class);
	}

	public <T> T executeReturn(Class<T> clazz) {

		HttpHeaders headers = this.getHeader(this.headParams);
		HttpEntity<String> entity = new HttpEntity<String>(this.body, headers);
		String uuid = UUIDUtils.uuid();
		log.info("编号"+uuid+"调用外部接口:" + ToolKit.builder(this.url).append(" method:").append(this.method).append(" params:")
				.append(this.body).append(" headers:").append(JsonKit.beanToJson(headers, null)));
		ResponseEntity<String> rs = loopCall(entity);
		urlLocal.set(uuid);
		T rt = this.handleResponse(rs, clazz);
		return rt;
	}

	private ResponseEntity<String> loopCall(HttpEntity<String> entity) {

		ResponseEntity<String> rs = null;
		try {

			rs = this.rest.exchange(this.url, this.method, entity, String.class);
		} catch (RestClientException e) {
			if (e instanceof RestClientResponseException) {
				// C当报错的时候,这里的编码是ISO-8859-1,中文出现乱码,所以这里用UTF-8转换一下
				String rt = new String(((RestClientResponseException) e).getResponseBodyAsByteArray(), Charset.forName("UTF-8"));
				JSONObject obj = JsonKit.jsonToBean(rt, JSONObject.class);
				log.error("rest报错", e);
				throw new AppException(ExceptionType.SYS_REST,obj.getString("message"),e);
			}
		}

		
		return rs;
	}

	@SuppressWarnings("unchecked")
	private <T> T handleResponse(ResponseEntity<String> rs, Class<T> clazz) {

		if (rs != null) {
			String body = rs.getBody();
			if (StringUtils.isBlank(body)){
				throw new AppException(ExceptionType.SYS_REST);
			}
			log.info("外部系统返回(调用编号{"+urlLocal.get()+"}):" + body);
			urlLocal.remove();
			if (clazz == String.class) {
				return (T) body;
			} else {
				T bean = JsonKit.jsonToBean(body, clazz);
				return bean;
			}
		} else {
			throw new AppException(ExceptionType.SYS_REST);
		}
	}

	private HttpHeaders getHeader(Map<String, String> headParams) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		if (MapUtils.isNotEmpty(headParams)) {
			for (Map.Entry<String, String> requestHeader : headParams.entrySet()) {
				headers.add(requestHeader.getKey(), requestHeader.getValue());
			}
		}

		return headers;
	}

	public RestCaller addHeader(Map<String, String> headParams) {
		this.headParams = headParams;
		return this;
	}

	public RestCaller setBody(String body) {
		this.body = body;
		return this;
	}

}