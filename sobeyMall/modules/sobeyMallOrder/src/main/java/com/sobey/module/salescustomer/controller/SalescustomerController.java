package com.sobey.module.salescustomer.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.exception.AppException;
import com.sobey.module.salescustomer.model.Salescustomer;
import com.sobey.module.salescustomer.model.request.Customer;
import com.sobey.module.salescustomer.service.SalescustomerService;
import com.sobey.module.salesman.model.Salesman;
import com.sobey.util.common.ToolKit;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("${api.v1}/salescustomer")
@Api(value = "销售和客户关系接口", description = "销售和客户关系接口")
public class SalescustomerController {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SalescustomerService ss;

	@ApiOperation(value = "解绑")
	@DeleteMapping("/{uuid}")
	public void remove(@PathVariable("uuid") String uuid) {

		 Salescustomer db = this.ss.selectById(uuid);
		 if (db == null) {
		 throw new AppException("数据不存在,请核对");
		 }
		 this.ss.deleteById(uuid);

	}

	@ApiOperation(value = "分页")
	@PostMapping("/page")
	public Page<Salescustomer> page(@RequestParam int page, @RequestParam int size,
			@RequestBody(required = false) Salescustomer entity) {
		
			
		Page<Salescustomer> pg = new Page<Salescustomer>(page, size);
		ss.page(pg, entity);
		return pg;
	}

	@ApiOperation(value = "新增")
	@PostMapping
	@Transactional
	public void insert(@Validated @RequestBody Salescustomer entity, BindingResult bindingResult) {
		ToolKit.validData(bindingResult);
		
		List<Customer> customers = entity.getCustomers();
		for (Customer item : customers) {
			Salescustomer sc = new Salescustomer();
			sc.setSaleUserCode(entity.getSaleUserCode());
			sc.setCreateUserCode(entity.getCreateUserCode());
			String userCode = item.getUserCode();
			String loginName = item.getLoginName();
			sc.setCustomerUserCode(item.getUserCode());
			sc.setCustomerLoginName(item.getLoginName());
			sc.setSiteCode(item.getSiteCode());
			this.ss.check(userCode, loginName);
			this.ss.insert(sc);
		}
		
	}
	
	
	@ApiOperation(value = "修改")
	@PatchMapping
	public void update(@RequestBody Salescustomer entity) {
		String uuid = entity.getUuid();
		if (StringUtils.isBlank(uuid)) {
			throw new AppException("uuid不能为空");
		}
		
		Salescustomer db = this.ss.selectById(uuid);
		if (db == null) {
			throw new AppException("数据不存在，请检查");
		}
		
		EntityWrapper<Salescustomer> wp = new EntityWrapper<Salescustomer>();
		wp.eq("uuid", uuid);
		
		//防止修改usercode
		this.ss.updateForSet("description='"+entity.getDescription()+"'", wp);
	}

	


	

}
