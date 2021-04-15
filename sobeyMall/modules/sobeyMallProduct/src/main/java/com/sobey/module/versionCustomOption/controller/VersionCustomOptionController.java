package com.sobey.module.versionCustomOption.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.sobey.module.versionCustomOption.model.VersionCustomOption;
import com.sobey.module.versionCustomOption.service.VersionCustomOptionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("${api.v1}/version-custom-option")
@Api(value = "商品版本自定义规格选项", description = "商品版本自定义规格选项相关接口")
public class VersionCustomOptionController {

	@Autowired
	private VersionCustomOptionService vs;


	@ApiOperation(value = "商品版本自定义规格选项删除", notes = "商品版本自定义规格选项删除")
	@DeleteMapping
	public void delete(@ApiParam(value = "版本自定义规格选项编号") @RequestParam String uuid) {
		this.vs.deleteById(uuid);
	}
	
	
	@ApiOperation(value = "查询")
	@PostMapping("/list")
	public List<VersionCustomOption> list(@RequestBody(required =false) VersionCustomOption entity) {

		EntityWrapper<VersionCustomOption> ew = new EntityWrapper<VersionCustomOption>(entity);
		List<VersionCustomOption> list = vs.selectList(ew);
		return list;
	}

}
