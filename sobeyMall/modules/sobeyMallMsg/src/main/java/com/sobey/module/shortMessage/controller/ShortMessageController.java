package com.sobey.module.shortMessage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sobey.framework.jwt.annotation.PassToken;
import com.sobey.module.shortMessage.enumeration.notifyType;
import com.sobey.module.shortMessage.service.AliShortMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("${api.v1}/short-message")
@Api(value = "短信", description = "短信接口")
public class ShortMessageController {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AliShortMessage ali;

	@ApiOperation(value = "发送工单相关短信")
	@PostMapping
	public void send(
			@RequestParam(required = true) @ApiParam(value = "电话号码") String phoneNumber,
			@ApiParam(value = "人员名称") @RequestParam(required = true) String name,
			@ApiParam(value = "工单编号") @RequestParam(required = true) String workOrderId,
			@ApiParam(value = "通知类型,具体参见notifyType类:reminder_operator,distributed_operator,distributed_customer,reminder_customer,new_work_order") 
			@RequestParam(required = true) String notifyType) {

		JSONObject obj = new JSONObject();
		obj.put("name", name);
		obj.put("workOrderId", workOrderId);
		ali.send(phoneNumber, JSON.toJSONString(obj), notifyType);
	}
	
	@ApiOperation(value = "发送自定义短信")
	@PostMapping("/custom")
	public void normalSend(
			@RequestParam(required = true) @ApiParam(value = "电话号码") String phoneNumber,
			@RequestParam(required = true) @ApiParam(value = "占位符,例如:{\"name\":\"lgc\"}")  String placeholder,
			@RequestParam(required = true) @ApiParam(value = "阿里签名")  String signName,
			@RequestParam(required = true) @ApiParam(value = "阿里模板code")String templateCode) {
		
		ali.send(phoneNumber, placeholder, signName,templateCode);
	}
	
	@ApiOperation(value = "发送代金券短信")
	@PostMapping("/voucher")
	public void sendNormalShortMessage(
			@RequestParam(required = true) @ApiParam(value = "电话号码") String phoneNumber,
			@ApiParam(value = "人员名称") @RequestParam(required = true) String name,
			@ApiParam(value = "代金券码") @RequestParam(required = true) String voucherCode,
			@ApiParam(value = "通知类型,代金券：voucher_short_message") 
			@RequestParam(required = true) String notifyType) {
		
		JSONObject obj = new JSONObject();
		obj.put("name", name);
		obj.put("voucherCode", voucherCode);
		ali.send(phoneNumber, JSON.toJSONString(obj), notifyType);
	}
	
	
	@ApiOperation(value = "欠费通知商城管理人员")
	@PostMapping("/arrears")
	public void arrearsShortMessage(
			@RequestParam(required = true) @ApiParam(value = "管理人员电话") String phoneNumber,
			@ApiParam(value = "客户名称") @RequestParam(required = true) String customer) {
		
		JSONObject obj = new JSONObject();
		obj.put("customer", customer);
		ali.send(phoneNumber, JSON.toJSONString(obj), "arrears_short_message");
	}

}
