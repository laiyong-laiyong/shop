package com.sobey.module.version.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.jwt.annotation.PassToken;
import com.sobey.module.productPrivilege.model.ProductPrivilege;
import com.sobey.module.version.model.Version;
import com.sobey.module.version.service.VersionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("${api.v1}/version")
@Api(value = "商品版本", description = "商品版本相关接口")
public class VersionController {

	@Autowired
	private VersionService vs;

	@ApiOperation(value = "商品版本删除")
	@DeleteMapping("/{uuid}")
	public void remove(@PathVariable("uuid") String uuid) {
		
		Version et = new Version();
		et.setUuid(uuid);
		this.vs.remove(et);
	}

	@ApiOperation(value = "版本分页查询")
	@PassToken
	@GetMapping
	public Page<Version> page(@RequestParam int page, @RequestParam int size, Version entity) {
		Page<Version> page1 = new Page<Version>(page, size);
		vs.page(page1, entity);
		return page1;
	}

	@ApiOperation(value = "版本查询")
	@PassToken
	@GetMapping("/list")
	public List<Version> list(Version entity) {

		List<Version> list = vs.list(entity);
		return list;
	}

}
