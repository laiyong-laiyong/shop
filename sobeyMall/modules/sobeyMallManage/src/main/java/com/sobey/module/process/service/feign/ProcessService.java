package com.sobey.module.process.service.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.sobey.module.process.model.response.Result;

/**
 * C流程相关
 * 
 * 
 * @author lgc
 * @date 2020年10月13日 上午9:55:14
 */
@FeignClient(name = "${address.hiveprocess.name}", url = "${address.hiveprocess.url}")
public interface ProcessService {

	@DeleteMapping(value = "/v1/flow/instance")
	Result delete(@RequestHeader(value = "sobeycloud-token") String value,
			@RequestHeader(value = "sobeycloud-site") String site,
			@RequestParam(value = "instanceIds") List<String> instanceIds);
	
	@PutMapping(value = "/v1/flow/instance")
	Result redo(@RequestHeader(value = "sobeycloud-token") String value,
			@RequestHeader(value = "sobeycloud-site") String site,
			@RequestParam(value = "procInstIds") List<String> instanceIds);
	
	
	@PatchMapping(value = "/v1/flow/instance")
	Result cancle(@RequestHeader(value = "sobeycloud-token") String value,
			@RequestHeader(value = "sobeycloud-site") String site,
			@RequestParam(value = "instanceIds") List<String> instanceIds);

	

}
