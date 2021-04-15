package com.sobey.module.token.test;

import com.sobey.framework.config.Authorization;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/feign")
@Api(value = "feign", description = "feign相关接口")
@ApiIgnore
public class FeignController {

	@Autowired
	Authorization au;
//	@Autowired
	private OuthTest outhTest;

	@ApiOperation(value = "测试feign接口", notes = "测试feign接口")
	@PostMapping("/oauth")
	public String oauth() {
		String token = outhTest.getToken();
		return token;
	}

	@ApiOperation(value = "测试feign接口", notes = "测试feign接口")
	@PostMapping("/oauth/header")
	public String oauthHeader() {
		String token = outhTest.getToken2(au.getToken());
		return token;
	}

}
