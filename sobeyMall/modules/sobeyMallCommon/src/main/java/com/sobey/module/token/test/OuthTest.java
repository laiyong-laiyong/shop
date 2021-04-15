/**
 * 
 */
package com.sobey.module.token.test;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author lgc
 * @date 2020年1月6日 下午3:23:10
 */
//@FeignClient(name="Oauth", url="${feignclient.url}")
public interface OuthTest {
	
	/**
	 * 测试成功
	 * 
	 * @return
	 */
	@PostMapping(value="/oauth/token?grant_type=password&username=admin&password=123456",
			headers = {"Authorization=Basic YWRtaW46JDBiRXlIaXZlJjJvMVNpeA=="})
	public String getToken();
	
	/**
	 * 测试单独的header参数传递
	 * 
	 * @return
	 */
	@PostMapping(value="/oauth/token?grant_type=password&username=admin&password=123456")
	public String getToken2(@RequestHeader(name = "Authorization") String value);

}
