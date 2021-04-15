package com.sobey.module.evaluate.controller;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;
import com.sobey.module.evaluate.model.Evaluate;
import com.sobey.module.evaluate.model.request.EvaluateRequest;
import com.sobey.module.evaluate.service.EvaluateService;
import com.sobey.module.workOrder.model.WorkOrder;
import com.sobey.module.workOrder.service.WorkOrderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("${api.v1}/work-order/evaluate")
@Api(value = "工单", description = "工单评价接口")
public class EvaluateController {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EvaluateService es;
	@Autowired
	private WorkOrderService wos;

	@ApiOperation(value = "新增", notes = "新增")
	@PostMapping
	public void insert(@RequestBody(required = true) EvaluateRequest entity) {

		if (entity == null) {
			throw new AppException(ExceptionType.SYS_PARAMETER);
		}
		
		WorkOrder workOrder = entity.getWorkOrder();
		wos.updateById(workOrder);
		
		List<Evaluate> list = entity.getList();
		if (list != null) {
			this.es.insertBatch(list);
		}
	}
	
	
	@ApiOperation(value = "评价查询")
	@GetMapping("/{workOrderId}")
	public List<Evaluate> get(@ApiParam(value="工单编号") @PathVariable(value = "workOrderId") String workOrderId) {

		Evaluate et = new Evaluate();
		et.setWorkOrderId(workOrderId);
		Wrapper<Evaluate> wp = new EntityWrapper<Evaluate>(et);
		List<Evaluate> list = this.es.selectList(wp);
		return list;
	}

}
