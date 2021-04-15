package com.sobey.module.initData.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sobey.module.initData.service.InitDataService;

import io.swagger.annotations.ApiOperation;

/**
 * 
 * 
 * @author lgc
 * @date 2020年2月6日 下午4:21:16
 */
@RestController
@RequestMapping("${api.v1}/initData")
public class InitDataController {

	@Autowired
	private InitDataService ids;

	@ApiOperation(value = "初始化商城基础数据", notes = "初始化商城基础数据")
	@PostMapping
	public void config() {
		this.ids.run();
	}


}
