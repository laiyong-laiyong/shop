package com.sobey.module.userrole.service.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 
 * @author lgc
 * @date 2021年2月2日 下午5:06:27
 */
@FeignClient(name = "${address.auth.name}", url = "${address.auth.url}")
public interface UserRoleService {
	/**
	 * 对某个用户绑定的角色做全量更新
	 * 
	 * @param user
	 * @param value
	 * @param site
	 * @return
	 */
	@PutMapping(value = "/v1.0/user/{userCode}/roles/full-update")
	String bindingRole(@PathVariable(value = "userCode") String userCode,List<String> roleCode, @RequestHeader(value = "sobeyhive-http-token") String value,
			@RequestHeader(value = "sobeyhive-http-site") String site);
	

	

}
