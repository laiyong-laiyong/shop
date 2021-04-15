package com.sobey.module.role.service.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sobey.module.role.model.request.ResourcePermissions;
import com.sobey.module.role.model.request.Role;
import com.sobey.util.business.identity.Identity;
import com.sobey.util.common.uuid.UUIDUtils;

/**
 * 
 * 
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class RoleService {

	public Role warpRole(String name) {
		Role re = new Role();
		re.setName(name);
		String code = UUIDUtils.simpleUuid();
		re.setCode(code);
		List<String> client_codes = new ArrayList<String>();
		client_codes.add(Identity.PUBLIC_CLIENT_CODE.getCode());
		re.setClient_codes(client_codes);
		re.setClient_code(Identity.PUBLIC_CLIENT_CODE.getCode());
		return re;
	}
	public Role warpRole(String name,String code) {
		Role re = new Role();
		re.setName(name);
		re.setCode(code);
		return re;
	}

	public List<ResourcePermissions> warpPermission(String roleCode, String resourceCode,
			List<String> persmission, String siteCode) {
		ResourcePermissions it = new ResourcePermissions();
		it.setRole_code(roleCode);
		it.setResource_code(resourceCode);
		it.setPermissions(persmission);
		it.setSite_code(siteCode);

		List<ResourcePermissions> list = new ArrayList<ResourcePermissions>();
		list.add(it);
		return list;
	}

}
