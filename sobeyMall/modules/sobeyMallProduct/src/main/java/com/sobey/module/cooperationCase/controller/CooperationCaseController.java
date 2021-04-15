package com.sobey.module.cooperationCase.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sobey.framework.jwt.annotation.PassToken;
import com.sobey.module.brightSpot.model.BrightSpot;
import com.sobey.module.cooperationCase.model.CooperationCase;
import com.sobey.module.cooperationCase.service.CooperationCaseService;
import com.sobey.module.version.model.Version;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("${api.v1}/cooperation-case")
@Api(value = "商品合作案例", description = "商品合作案例相关接口")
public class CooperationCaseController {

	@Autowired
	private CooperationCaseService fs;

	@ApiOperation(value = "删除", notes = "删除")
	@DeleteMapping("/{uuid}")
	public void remove(@PathVariable("uuid") String uuid) {
		CooperationCase et = new CooperationCase();
		et.setUuid(uuid);
		this.fs.remove(et);
	}

	@ApiOperation(value = "查询")
	@PassToken
	@GetMapping
	public List<CooperationCase> list(CooperationCase entity) {

		List<CooperationCase> list = fs.list(entity);
		return list;
	}

}
