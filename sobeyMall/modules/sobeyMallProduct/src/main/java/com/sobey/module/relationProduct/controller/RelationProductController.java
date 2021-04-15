package com.sobey.module.relationProduct.controller;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.jwt.annotation.PassToken;
import com.sobey.module.relationProduct.model.RelationProduct;
import com.sobey.module.relationProduct.service.RelationProductService;
import com.sobey.util.common.ToolKit;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("${api.v1}/relationProduct")
@Api(value = "关联商品", description = "关联商品")
public class RelationProductController {

	@Autowired
	private RelationProductService ms;


//	@ApiOperation(value = "分页")
//	@PostMapping("/page")
//	public Page<RelationProduct> page(@RequestParam int page, @RequestParam int size,@RequestBody(required=false)  RelationProduct entity) {
//		Page<RelationProduct> page1 = new Page<RelationProduct>(page, size);
//		ms.page(page1, entity);
//		return page1;
//	}

	@ApiOperation(value = "列表")
	@PostMapping("/list")
	public List<RelationProduct> list(@RequestBody(required=false) RelationProduct entity) {

		List<RelationProduct> list = ms.list(entity);
		return list;
	}
	
	
	@ApiOperation(value = "根据商品编号查询关联工具")
	@GetMapping("/{code}")
	public List<RelationProduct> list(@PathVariable(value = "code") String code) {
		List<RelationProduct> list = ms.selectByCode(code);
		return list;
	}
	
	

}
