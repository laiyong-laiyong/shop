package com.sobey.module.aiVirtual.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sobey.exception.AppException;
import com.sobey.framework.jwt.annotation.PassToken;
import com.sobey.module.aiVirtual.model.request.user.UserRequest;
import com.sobey.module.aiVirtual.service.AiVirtualService;
import com.sobey.util.common.ToolKit;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("${api.v1}/manage/ai-virtual")
@Api(value = "AI虚拟主播", description = "AI虚拟主播")
public class AiVirtualController {

	@Autowired
	private AiVirtualService ai;

	@ApiOperation(value = "获取token", notes = "获取token")
	@PostMapping("/token")
	@PassToken
	public void getToken() {

		String token = ai.getToken();
		System.out.println(token);
	}
	
	

	@PassToken
	@ApiOperation(value = "导入用户")
	@PostMapping("/import")
	public String importUser(@Validated @RequestBody UserRequest entity) {
		String data = this.ai.importUser(entity);
		return data;

	}

	@PassToken
	@ApiOperation(value = "获取sso路径")
	@PostMapping("/sso")
	public String getUrl(String uid) {
		String data = this.ai.getUrl(uid);
		return data;
	}

}
