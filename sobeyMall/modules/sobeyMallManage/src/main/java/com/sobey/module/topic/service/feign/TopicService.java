package com.sobey.module.topic.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.sobey.module.topic.model.request.Topic;

/**
 * 
 * 
 * 
 * @author lgc
 * @date 2020年10月13日 上午9:55:14
 */
@FeignClient(name = "${address.hivemessage.name}", url = "${address.hivemessage.url}")
public interface TopicService {

	@PostMapping(value = "/v3.0/message/topic")
	Topic createTopic(@RequestHeader(value = "sobeycloud-token") String token,
			@RequestHeader(value = "sobeycloud-site") String site,
			@RequestBody Topic topic);

}
