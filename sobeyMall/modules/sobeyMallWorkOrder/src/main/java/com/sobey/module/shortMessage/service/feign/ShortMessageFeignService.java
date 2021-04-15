package com.sobey.module.shortMessage.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiParam;



@FeignClient(name = "${address.msg.name}", url = "${address.msg.url}/${api.v1}/short-message")
//@FeignClient(name = "${address.msg.name}", url = "http://localhost:8080/sobeyMallMsg/${api.v1}/short-message")
public interface ShortMessageFeignService {

	
	@PostMapping
	public void send(
			@RequestHeader(value = "Authorization") String value,
			@RequestParam(required = true) @ApiParam(value = "电话号码") String phoneNumber,
			@ApiParam(value = "人员名称") @RequestParam(required = true) String name,
			@ApiParam(value = "工单编号") @RequestParam(required = true) String workOrderId,
			@ApiParam(value = "通知类型:reminder,distributed_operator,distributed_customer,customer") 
			@RequestParam(required = true) String notifyType);


}
