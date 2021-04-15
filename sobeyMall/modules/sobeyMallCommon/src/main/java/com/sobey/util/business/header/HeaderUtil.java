/**
 * 
 */
package com.sobey.util.business.header;

import org.apache.commons.lang3.StringUtils;

import com.sobey.framework.spring.filter.HttpContext;

/**
 * @author lgc
 * @date 2020年2月10日 上午11:18:52
 */
public class HeaderUtil {

	public static String getAuth() {
		HttpContext httpContext = HttpContext.get();
		String authorization = httpContext.getRequest().getHeader("Authorization");
//		if (StringUtils.isNotEmpty(authorization)) {
//			if (!StringUtils.startsWithIgnoreCase(authorization, "bearer")) {
//				authorization = "bearer " + authorization;
//			}
//		}
		return authorization;
	}
	
	

}
