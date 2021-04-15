package com.sobey.module.message.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;



@FeignClient(name = "${address.msg.name}", url = "${address.msg.url}/${api.v1}")
public interface WorkOrderMsgFeignService {

	@PostMapping(value = "/workOrderMsg/handleNotice")
	void handleNotice(@RequestHeader(value = "Authorization") String value,
			@RequestParam String workOrderNum,@RequestParam String userCode);
	
	@PostMapping(value = "/workOrderMsg/workOrderAcceptance")
	void workOrderAcceptance(@RequestHeader(value = "Authorization") String value,
			@RequestParam String username,@RequestParam String workOrderNum,@RequestParam String userCode);
	
	@PostMapping(value = "/workOrderMsg/workOrderConfirmation")
	void workOrderConfirmation(@RequestHeader(value = "Authorization") String value,
			@RequestParam String username,@RequestParam String workOrderNum,@RequestParam String userCode);


}
