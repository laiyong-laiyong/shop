package com.sobey.module.resource.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sobey.module.resource.model.request.ResourceRequest;
import com.sobey.module.resource.service.feign.ResourceService;
import com.sobey.util.business.header.HeaderUtil;
import com.sobey.util.business.identity.Identity;
import com.sobey.util.common.uuid.RandomUtils;
import com.sobey.util.common.uuid.UUIDUtils;

/**
 * 
 * 
 * @author lgc
 * @date 2020年2月6日 下午4:21:16
 */
@RestController
@RequestMapping("${api.v1}/resource")
public class ResourceController {

	@Autowired
	private ResourceService rs;

	@PostMapping()
	public Long add(@RequestBody ResourceRequest re) {

		String authorization = HeaderUtil.getAuth();
		
		String code = re.getCode();
		if (StringUtils.isEmpty(code)) {
			String code1 = UUIDUtils.simpleUuid();
			re.setCode(code1);
		}
		Long add = rs.add(re, authorization,Identity.PUBLIC_SITE_CODE.getCode());
		return add;
	}

	@PutMapping()
	public void update(@RequestBody ResourceRequest re) {

		String authorization = HeaderUtil.getAuth();
		rs.update(re, authorization,Identity.PUBLIC_SITE_CODE.getCode());
	}
	
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable(name = "id") String id) {
		
		String authorization = HeaderUtil.getAuth();
		rs.delete(id, authorization,Identity.PUBLIC_SITE_CODE.getCode());
	}

}
