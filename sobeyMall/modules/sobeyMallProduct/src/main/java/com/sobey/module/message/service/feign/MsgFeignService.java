package com.sobey.module.message.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;



@FeignClient(name = "${address.msg.name}", url = "${address.msg.url}/${api.v1}")
public interface MsgFeignService {

	@PostMapping(value = "/message/sendTextMsg")
	void sendTextMsg(@RequestHeader(value = "Authorization") String value,
			@RequestParam String msgTypeCode,@RequestParam String accountId,
			@RequestParam String msgContent);


}
