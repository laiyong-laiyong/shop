package com.sobey.module.packages.controller;

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

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.jwt.annotation.PassToken;
import com.sobey.module.packages.enumeration.EffectiveDurationType;
import com.sobey.module.packages.model.Packages;
import com.sobey.module.packages.service.PackagesService;
import com.sobey.module.product.enumeration.SaleType;
import com.sobey.util.common.json.JsonKit;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("${api.v1}/packages")
@Api(value = "商品套餐", description = "商品套餐相关接口")
public class PackagesController {

	@Autowired
	private PackagesService vs;

	@ApiOperation(value = "商品套餐删除")
	@DeleteMapping("/{uuid}")
	public void remove(@PathVariable("uuid") String uuid) {
		
		Packages et = new Packages();
		et.setUuid(uuid);
		this.vs.remove(et);
	}

	@ApiOperation(value = "套餐分页查询")
	@PassToken
	@PostMapping
	public Page<Packages> page(@RequestParam int page, @RequestParam int size, @RequestBody(required =false) Packages entity) {
		Page<Packages> page1 = new Page<Packages>(page, size);
		vs.page(page1, entity);
		return page1;
	}

	@ApiOperation(value = "套餐查询")
	@PassToken
	@PostMapping("/list")
	public List<Packages> list(@RequestBody(required =false) Packages entity) {

		EntityWrapper<Packages> ew = new EntityWrapper<Packages>(entity);
		ew.orderBy("`order`",true);
		List<Packages> list = vs.selectList(ew);
		return list;
	}
	
	
	@ApiOperation(value = "时长单位枚举")
	@GetMapping("/effective-duration")
	@PassToken
	public String getEffectiveDurationType() {
		String json = JsonKit.enumToJson(EffectiveDurationType.class);
		return json;
	}

}
