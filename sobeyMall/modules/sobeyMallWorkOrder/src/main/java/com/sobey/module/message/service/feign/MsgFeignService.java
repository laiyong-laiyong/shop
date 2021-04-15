package com.sobey.module.message.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.sobey.module.dialogue.model.request.DialogueContext;



@FeignClient(name = "${address.msg.name}", url = "${address.msg.url}/${api.v1}")
public interface MsgFeignService {

	@PostMapping(value = "/message/onlineChat")
	void onlineChat(@RequestHeader(value = "Authorization") String value,@RequestBody DialogueContext context);
	


}
