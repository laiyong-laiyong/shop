package com.sobey.module.role.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sobey.framework.jwt.annotation.PassToken;
import com.sobey.module.role.service.CopyRoleService;

import io.swagger.annotations.ApiParam;

/**
 * 这个是专门给注册用户赋予角色的
 * 
 * 
 * @author lgc
 * @date 2021年3月23日 下午7:27:14
 */
@RestController
@RequestMapping(value = "${api.v1}/copy-role")
public class CopyRoleController {

	@Autowired
	private CopyRoleService roleService;


	@PostMapping
	public void initRole(@ApiParam("站点code") @RequestParam String siteCode) {
		this.roleService.initRole(siteCode);
	}

}
