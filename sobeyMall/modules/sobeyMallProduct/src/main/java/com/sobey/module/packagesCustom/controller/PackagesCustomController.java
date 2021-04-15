package com.sobey.module.packagesCustom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.sobey.framework.jwt.annotation.PassToken;
import com.sobey.module.packagesCustom.model.PackagesCustom;
import com.sobey.module.packagesCustom.service.PackagesCustomService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("${api.v1}/packages-custom")
@Api(value = "商品套餐自定义资源", description = "商品套餐自定义资源相关接口")
public class PackagesCustomController {

	@Autowired
	private PackagesCustomService vs;


	@ApiOperation(value = "商品套餐自定义资源删除", notes = "商品套餐自定义资源删除")
	@DeleteMapping
	public void delete(@ApiParam(value = "套餐自定义资源编号") @RequestParam String uuid) {
		this.vs.deleteById(uuid);
	}
	
	
	@ApiOperation(value = "套餐查询")
	@PassToken
	@PostMapping("/list")
	public List<PackagesCustom> list(@RequestBody(required =false) PackagesCustom entity) {

		EntityWrapper<PackagesCustom> ew = new EntityWrapper<PackagesCustom>(entity);
		ew.orderBy("`order`",true);
		List<PackagesCustom> list = vs.selectList(ew);
		return list;
	}

}
