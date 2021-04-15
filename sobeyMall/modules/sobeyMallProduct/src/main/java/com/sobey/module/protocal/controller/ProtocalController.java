package com.sobey.module.protocal.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.sobey.exception.AppException;
import com.sobey.framework.jwt.annotation.PassToken;
import com.sobey.module.discount.enumeration.DiscountType;
import com.sobey.module.discount.model.Discount;
import com.sobey.module.product.enumeration.OperationType;
import com.sobey.module.protocal.enumeration.ProtocalType;
import com.sobey.module.protocal.model.Protocal;
import com.sobey.module.protocal.service.ProtocalService;
import com.sobey.util.common.json.JsonKit;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("${api.v1}/protocal")
@Api(value = "协议", description = "协议")
public class ProtocalController {

	@Autowired
	private ProtocalService ops;
	
	@ApiOperation(value = "分页")
	@PostMapping("/page")
	public Page<Protocal> page(@RequestParam int page, @RequestParam int size,
			@RequestBody(required = false) Protocal entity) {
		Page<Protocal> pg = new Page<Protocal>(page, size);
		this.ops.selectPage(pg);
		return pg;
	}
	

	@PassToken
	@ApiOperation(value = "查看")
	@GetMapping("/{type}")
	public String query(@PathVariable("type") String type) {

		Wrapper<Protocal> wp = new EntityWrapper<Protocal>();
		wp.eq("type", type);

		List<Protocal> list = this.ops.selectList(wp);
		if (CollectionUtils.isNotEmpty(list)) {
			Protocal bean = list.get(0);
			return bean.getText();
		} else {
			return null;
		}
	}

	@ApiOperation(value = "新增")
	@PostMapping
	public void insert(@Validated @RequestBody Protocal protocal) {

		this.ops.insert(protocal);
	}
	
	
	@ApiOperation(value = "修改")
	@PatchMapping
	public void update(@RequestBody Protocal entity) {

		String uuid = entity.getUuid();
		if (StringUtils.isBlank(uuid)) {
			throw new AppException("参数uuid不能为空");
		}
		
		this.ops.updateById(entity);
		
	}

	@ApiOperation(value = "协议类型,不需要token")
	@GetMapping("/protocal-type")
	@PassToken
	public String getDiscountType() {

		String json = JsonKit.enumToJson(ProtocalType.class);
		return json;
	}

}
