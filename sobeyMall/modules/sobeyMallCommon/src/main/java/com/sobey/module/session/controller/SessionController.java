package com.sobey.module.session.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sobey.framework.jwt.annotation.PassToken;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * 暂时注释，不用session共享
 * 
 * @author lgc
 * @date 2020年5月15日 上午10:23:13
 */
//@RestController
//@RequestMapping("${api.v1}/session")
//@Api(value = "session共享",description="session共享")
public class SessionController {

	@PassToken
	@ApiOperation(value = "设置session")
	@PostMapping
	public Map<String, Object> set(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		request.getSession().setAttribute("url", request.getRequestURL());
		map.put("url", request.getRequestURL());
		return map;
	}

	@PassToken
	@ApiOperation(value = "获取session")
	@GetMapping
	public Object get(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		map.put("sessionId", request.getSession().getId());
		map.put("message", request.getSession().getAttribute("url"));
		return map;
	}
}