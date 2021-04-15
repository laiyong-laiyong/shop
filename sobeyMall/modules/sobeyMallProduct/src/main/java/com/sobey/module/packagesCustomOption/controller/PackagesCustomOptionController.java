package com.sobey.module.packagesCustomOption.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.sobey.framework.jwt.annotation.PassToken;
import com.sobey.module.packagesCustom.model.PackagesCustom;
import com.sobey.module.packagesCustomOption.model.PackagesCustomOption;
import com.sobey.module.packagesCustomOption.service.PackagesCustomOptionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("${api.v1}/packages-custom-option")
@Api(value = "商品套餐自定义选项", description = "商品套餐自定义选项相关接口")
public class PackagesCustomOptionController {

	@Autowired
	private PackagesCustomOptionService vs;


	@ApiOperation(value = "商品套餐自定义选项删除", notes = "商品套餐自定义选项删除")
	@DeleteMapping
	public void delete(@ApiParam(value = "套餐自定义选项编号") @RequestParam String uuid) {
		this.vs.deleteById(uuid);
	}
	
	@ApiOperation(value = "套餐查询")
	@PassToken
	@PostMapping("/list")
	public List<PackagesCustomOption> list(@RequestBody(required =false) PackagesCustomOption entity) {

		EntityWrapper<PackagesCustomOption> ew = new EntityWrapper<PackagesCustomOption>(entity);
		ew.orderBy("`order`",true);
		List<PackagesCustomOption> list = vs.selectList(ew);
		return list;
	}

}
