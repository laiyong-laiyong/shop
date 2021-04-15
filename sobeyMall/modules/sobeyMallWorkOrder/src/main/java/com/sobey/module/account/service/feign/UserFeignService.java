package com.sobey.module.account.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.sobey.module.account.model.response.UserDetail;



@FeignClient(name = "${address.auth.name}", url = "${address.auth.url}")
public interface UserFeignService {

	@GetMapping(value = "/v1.0/user/{user_code}")
	UserDetail queryUser(@RequestHeader(value = "sobeyhive-http-token") String value,
			@RequestHeader(value = "sobeyhive-http-site") String site,
			@PathVariable(value = "user_code") String userCode);
	
	/**
	 * C跨租户查询用户
	 * 
	 * @param value
	 * @param site_code
	 * @param userCode
	 * @return
	 */
	@GetMapping(value = "/v3.0/cross-tenant/{site_code}/user/{userCode}")
	UserDetail queryUserCrossTenant(@RequestHeader(value = "x-auth-token") String value,
			@PathVariable(value = "site_code") String site_code,
			@PathVariable(value = "userCode") String userCode);


}
