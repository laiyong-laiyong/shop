package com.sobey.module.evaluateStarCategory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sobey.module.evaluateStarCategory.model.EvaluateStarCategory;
import com.sobey.module.evaluateStarCategory.service.EvaluateStarCategoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("${api.v1}/work-order/evaluate-star-categorys")
@Api(value = "评价类别",description = "工单评价星级类别接口")
public class EvaluateStarCategoryController {

	@Autowired
	private EvaluateStarCategoryService ecs;

	@ApiOperation(value = "列表")
	@GetMapping("/list")
	public List<EvaluateStarCategory> list() {

		Wrapper<EvaluateStarCategory> wp = new EntityWrapper<EvaluateStarCategory>(null);
		List<EvaluateStarCategory> list = this.ecs.selectList(wp);
		return list;
	}

}
