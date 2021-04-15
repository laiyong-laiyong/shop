package com.sobey.module.productPrivilege.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.jwt.annotation.PassToken;
import com.sobey.module.productPrivilege.model.ProductPrivilege;
import com.sobey.module.productPrivilege.service.ProductPrivilegeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("${api.v1}/product-privilege")
@Api(value = "商品自定义权限", description = "商品自定义权限相关接口")
public class ProductPrivilegeController {

	@Autowired
	private ProductPrivilegeService pps;

	@ApiOperation(value = "分页查询", notes = "分页查询")
	@PassToken
	@PostMapping()
	public Page<ProductPrivilege> page(@RequestParam int offset, @RequestParam int limit,
			@RequestBody(required = false) ProductPrivilege entity) {
		Page<ProductPrivilege> page = new Page<ProductPrivilege>(offset, limit);
		pps.page(page, entity);
		return page;
	}

	@ApiOperation(value = "删除", notes = "删除")
	@DeleteMapping("/{uuid}")
	public void delete(@PathVariable("uuid") String uuid) {
		ProductPrivilege entity = new ProductPrivilege();
		entity.setUuid(uuid);
		this.pps.remove(entity);
	}

	@ApiOperation(value = "列表查询")
	@PassToken
	@GetMapping
	public List<ProductPrivilege> list(ProductPrivilege entity) {

		List<ProductPrivilege> list = pps.list(entity);
		return list;
	}

}
