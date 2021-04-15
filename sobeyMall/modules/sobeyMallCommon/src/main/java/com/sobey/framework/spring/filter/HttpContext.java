package com.sobey.framework.spring.filter;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class HttpContext {
	private HttpServletRequest request;
	private HttpServletResponse response;
	private final static ThreadLocal<HttpContext> contextContainer = new InheritableThreadLocal<HttpContext>();

	/**
	 * 初始化
	 */
	public static HttpContext begin(HttpServletRequest req, HttpServletResponse res) {
		HttpContext context = new HttpContext();
		context.request = req;
		context.response = res;
		contextContainer.set(context);
		return context;
	}

	public static HttpContext get() {
		return contextContainer.get();
	}

	/**
	 * 销毁
	 */
	public void end() {
		this.request = null;
		this.response = null;
		contextContainer.remove();
	}

	public ServletContext getContext() {
		return this.request.getServletContext();
	}

	public HttpSession getSession() {
		return this.request.getSession(false);
	}

	public HttpServletRequest getRequest() {
		return this.request;
	}

	public HttpServletResponse getResponse() {
		return this.response;
	}

	public Map<String, Cookie> getCookies() {
		Map<String, Cookie> map = new HashMap<String, Cookie>();
		Cookie[] cookies = this.request.getCookies();
		if (cookies != null)
			for (Cookie ck : cookies) {
				map.put(ck.getName(), ck);
			}
		return map;
	}
}