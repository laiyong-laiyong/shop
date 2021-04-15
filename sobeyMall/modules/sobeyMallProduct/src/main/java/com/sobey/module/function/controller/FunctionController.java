package com.sobey.module.function.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sobey.framework.jwt.annotation.PassToken;
import com.sobey.module.function.model.Function;
import com.sobey.module.function.service.FunctionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("${api.v1}/functions")
@Api(value = "商品功能", description = "商品功能相关接口")
public class FunctionController {

	@Autowired
	private FunctionService fs;


	
	@ApiOperation(value = "查询")
	@PassToken
	@GetMapping
	public List<Function> list(Function entity) {

		List<Function> list = fs.list(entity);
		return list;
	}
	
	@ApiOperation(value = "删除", notes = "删除")
	@DeleteMapping("/{uuid}")
	public void remove(@PathVariable("uuid") String uuid) {
		Function et = new Function();
		et.setUuid(uuid);
		this.fs.remove(et);
	}

}
