package com.sobey.module.salesman.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.exception.AppException;
import com.sobey.module.bill.model.PersonalBill;
import com.sobey.module.salesman.model.Salesman;
import com.sobey.module.salesman.service.SalesmanService;
import com.sobey.module.utils.DateUtil;
import com.sobey.util.common.ToolKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("${api.v1}/salesman")
@Api(value = "销售人员接口", description = "销售人员接口")
public class SalesmanController {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SalesmanService ss;

	@ApiOperation(value = "删除")
	@DeleteMapping("/{uuid}")
	public void remove(@PathVariable("uuid") String uuid) {

		Salesman db = this.ss.selectById(uuid);
		if (db == null) {
			throw new AppException("数据不存在,请核对");
		}
		this.ss.deleteById(uuid);

	}

	@ApiOperation(value = "分页")
	@PostMapping("/page")
	public Page<Salesman> page(@RequestParam int page, @RequestParam int size,
			@RequestBody(required = false) Salesman entity) {
		Page<Salesman> pg = new Page<Salesman>(page, size);
		ss.page(pg, entity);
		return pg;
	}

	@ApiOperation(value = "新增")
	@PostMapping
	public void insert(@Validated @RequestBody Salesman entity, BindingResult bindingResult) {
		ToolKit.validData(bindingResult);

		this.ss.check(entity.getUserCode());
		this.ss.insert(entity);
	}

	@ApiOperation(value = "修改")
	@PatchMapping
	public void update(@RequestBody Salesman entity) {
		String uuid = entity.getUuid();
		if (StringUtils.isBlank(uuid)) {
			throw new AppException("uuid不能为空");
		}
		
		Salesman db = this.ss.selectById(uuid);
		if (db == null) {
			throw new AppException("数据不存在，请检查");
		}
		
		//防止修改usercode
		entity.setUserCode(db.getUserCode());
		this.ss.updateById(entity);
	}

	/**
	 * 月度销售情况查询(后台管理页面) 查询所有销售
	 * @param date
	 */
	@GetMapping("/monthSales")
	@ApiOperation(value = "月度销售情况查询",httpMethod = "GET")
	public Page<Salesman> monthSales(@RequestParam int pageNum,
									 @RequestParam int pageSize,
									 @RequestParam @ApiParam("日期 yyyy/MM") String date,
									 @RequestParam(required = false) @ApiParam("用户名") String loginName,
									 @RequestParam(required = false) @ApiParam("销售名") String name){
		//校验日期格式
		try {
			DateUtil.parse(date,"yyyy/MM");
		} catch (ParseException e) {
			throw new AppException("日期格式错误",e);
		}
		Page<Salesman> page = new Page<>(pageNum, pageSize);
		return ss.monthSales(page,date,loginName,name);
	}

	/**
	 * 每月销售情况查询
	 * @param start
	 * @param end
	 * @return
	 */
	@GetMapping("perMonthSales")
	@ApiOperation(value = "我的每月销售情况查询",httpMethod = "GET")
	public List<Salesman> perMonthSales(@RequestParam @ApiParam(value = "销售人员的userCode") String userCode,
										@RequestParam @ApiParam("开始时间 yyyy/MM") String start,
										@RequestParam @ApiParam("结束时间 yyyy/MM") String end){
		//校验日期格式
		try {
			DateUtil.parse(start,"yyyy/MM");
			DateUtil.parse(end,"yyyy/MM");
		} catch (ParseException e) {
			throw new AppException("日期格式错误",e);
		}
		return ss.perMonthSales(userCode,start,end);

	}

	/**
	 * 客户消费top
	 * @param
	 * @return
	 */
	@GetMapping("/customerConsumeRank")
	@ApiOperation(value = "客户消费top",httpMethod = "GET")
	public List<PersonalBill> customerConsumeRank(@RequestParam @ApiParam(value = "销售人员的userCode") String userCode){
		return ss.customerConsumeRank(userCode);
	}

	/**
	 * 我的本月销售额与销售利润
	 * @return
	 */
	@GetMapping("/currentMonthSale")
	@ApiOperation(value = "我的本月销售额与销售利润",httpMethod = "GET")
	public Salesman currentMonthSale(@RequestParam @ApiParam(value = "销售人员的userCode") String userCode){
		return ss.currentMonthSale(userCode);
	}

}
