package com.sobey.module.evaluateLabelCategory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sobey.module.evaluateLabelCategory.model.EvaluateLabelCategory;
import com.sobey.module.evaluateLabelCategory.service.EvaluateLabelCategoryService;
import com.sobey.module.workOrder.model.WorkOrder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("${api.v1}/work-order/evaluate-label-categorys")
@Api(value = "评价标签",description = "工单评价标签类别接口")
public class EvaluateLabelCategoryController {

	@Autowired
	private EvaluateLabelCategoryService ecs;

	@ApiOperation(value = "列表")
	@PostMapping("/list")
	public List<EvaluateLabelCategory> list(@RequestBody(required=false) EvaluateLabelCategory entity) {

		Wrapper<EvaluateLabelCategory> wp = new EntityWrapper<EvaluateLabelCategory>(entity);
		List<EvaluateLabelCategory> list = this.ecs.selectList(wp);
		return list;
	}

}
