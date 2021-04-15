package com.sobey.module.user.service.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.sobey.module.user.model.request.UserRequest;
import com.sobey.module.user.model.request.UserRequestV2;
import com.sobey.module.user.model.response.UserResponseV2;

/**
 * 
 * @author lgc
 * @date 2021年2月2日 下午5:06:27
 */
@FeignClient(name = "${address.auth.name}", url = "${address.auth.url}")
public interface UserService {

	@PostMapping(value = "/v1.0/user")
	Long add(UserRequest user, @RequestHeader(value = "sobeyhive-http-token") String value,
			@RequestHeader(value = "sobeyhive-http-site") String site);
	
	
	
	@PostMapping(value = "/v2/kernel/configs/user")
	List<UserResponseV2> addV2(List<UserRequestV2> users, @RequestHeader(value = "sobeycloud-token") String value,
			@RequestHeader(value = "sobeycloud-site") String site);

	

}
