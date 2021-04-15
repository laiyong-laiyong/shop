package com.sobey.module.metric.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.jwt.annotation.PassToken;
import com.sobey.module.metric.enumeration.MetricType;
import com.sobey.module.metric.model.Metric;
import com.sobey.module.metric.service.MetricService;
import com.sobey.util.common.json.JsonKit;
import com.sobey.util.common.uuid.UUIDUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("${api.v1}/metric")
@Api(value = "商品按量", description = "商品按量")
public class MetricController {

	@Autowired
	private MetricService ms;

	@ApiOperation(value = "删除")
	@DeleteMapping("/{uuid}")
	public void remove(@PathVariable("uuid") String uuid) {
		Metric et = new Metric();
		et.setUuid(uuid);
		this.ms.remove(et);
	}

	@ApiOperation(value = "分页查询")
	@PassToken
	@GetMapping
	public Page<Metric> page(@RequestParam int page, @RequestParam int size, Metric entity) {
		Page<Metric> page1 = new Page<Metric>(page, size);
		ms.page(page1, entity);
		return page1;
	}

	@ApiOperation(value = "查询")
	@PassToken
	@GetMapping("/list")
	public List<Metric> list(Metric entity) {

		List<Metric> list = ms.list(entity);
		return list;
	}
	
	@ApiOperation(value = "类型")
	@GetMapping("/type")
	public String getType() {
		
		String json = JsonKit.enumToJson(MetricType.class);
		return json;
	}
	
	
	@ApiOperation(value = "按量预新增")
	@GetMapping("/pre")
	public Metric preInsert() {
		
		Metric mt = new Metric();
		
		//C新增的时候才生成uuid
		String uuid = UUIDUtils.simpleUuid();
		mt.setUuid(uuid);
		return mt;

	}

}
