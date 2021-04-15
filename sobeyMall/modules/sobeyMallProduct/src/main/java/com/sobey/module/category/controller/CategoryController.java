package com.sobey.module.category.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.jwt.annotation.PassToken;
import com.sobey.module.category.model.Category;
import com.sobey.module.category.service.CategoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("${api.v1}/categorys")
@Api(value = "商品类别", description = "商品类别相关接口")
public class CategoryController {

	@Autowired
	private CategoryService cs;

	@ApiOperation(value = "分页")
	@PassToken
	@PostMapping("/page")
	public Page<Category> page(@RequestParam int offset, @RequestParam int limit,
			@RequestBody(required = false) Category entity) {
		Page<Category> page = new Page<Category>(offset, limit);
		cs.page(page, entity);
		return page;
	}
	
	@ApiOperation(value = "列表")
	@PassToken
	@PostMapping("/list")
	public List<Category> list(@RequestBody(required = false) Category entity) {
		
		Wrapper<Category> wrapper = new EntityWrapper<Category>(entity);
		wrapper.orderBy("`order`", true);
		List<Category> list = this.cs.selectList(wrapper);
		return list;
	}
	
	

	@ApiOperation(value = "新增")
	@PostMapping
	public void insert(@RequestBody Category entity) {
		this.cs.insert(entity);
	}
	
	@ApiOperation(value = "商品类别查询", notes = "商品类别查询接口")
	@PatchMapping
	public void patch(@RequestBody(required =false) Category entity) {
		this.cs.updateById(entity);
	}

}
