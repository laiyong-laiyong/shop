package com.sobey.module.health.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sobey.framework.jwt.annotation.PassToken;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/health")
@Api(value = "健康检查",description="健康检查")
public class HealthController {

	
	@GetMapping(value = "/live")
	@PassToken
	@ApiOperation(value = "live接口")
	public void live() {
		
	}
	@GetMapping(value = "/ready")
	@PassToken
	@ApiOperation(value = "ready接口")
	public void ready () {
		
	}

}