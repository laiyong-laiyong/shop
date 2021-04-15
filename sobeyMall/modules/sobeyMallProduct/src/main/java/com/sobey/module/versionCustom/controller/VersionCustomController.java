package com.sobey.module.versionCustom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.sobey.module.versionCustom.model.VersionCustom;
import com.sobey.module.versionCustom.service.VersionCustomService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("${api.v1}/version-custom")
@Api(value = "商品版本自定义规格", description = "商品版本自定义规格相关接口")
public class VersionCustomController {

	@Autowired
	private VersionCustomService vs;


	@ApiOperation(value = "商品版本自定义规格删除", notes = "商品版本自定义规格删除")
	@DeleteMapping
	public void delete(@ApiParam(value = "版本自定义规格编号") @RequestParam String uuid) {
		this.vs.deleteById(uuid);
	}
	
	@ApiOperation(value = "查询")
	@PostMapping("/list")
	public List<VersionCustom> list(@RequestBody(required =false) VersionCustom entity) {

		EntityWrapper<VersionCustom> ew = new EntityWrapper<VersionCustom>(entity);
		List<VersionCustom> list = vs.selectList(ew);
		return list;
	}

}
