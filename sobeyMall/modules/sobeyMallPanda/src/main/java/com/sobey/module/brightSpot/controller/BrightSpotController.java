package com.sobey.module.brightSpot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sobey.framework.jwt.annotation.PassToken;
import com.sobey.module.brightSpot.model.BrightSpot;
import com.sobey.module.brightSpot.service.BrightSpotService;
import com.sobey.util.common.uuid.UUIDUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("${api.v1}/bright-spot")
@Api(value = "商品亮点", description = "商品亮点相关接口")
public class BrightSpotController {

	@Autowired
	private BrightSpotService bs;


	
	@ApiOperation(value = "查询")
	@PassToken
	@GetMapping
	public List<BrightSpot> list(BrightSpot entity) {

		List<BrightSpot> list = bs.list(entity);
		return list;
	}
	
	@ApiOperation(value = "删除")
	@DeleteMapping("/{uuid}")
	public void remove(@PathVariable("uuid") String uuid) {
		BrightSpot et = new BrightSpot();
		et.setUuid(uuid);
		this.bs.remove(et);
	}
	
	@ApiOperation(value = "亮点预新增")
	@GetMapping("/pre")
	public BrightSpot preInsert() {
		
		BrightSpot entity = new BrightSpot();
		String uuid = UUIDUtils.simpleUuid();
		entity.setUuid(uuid);
		return entity;

	}

}
