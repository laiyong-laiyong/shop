package com.sobey.module.role.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.role.mapper.CopyRoleMapper;
import com.sobey.module.role.model.CopyRole;
import com.sobey.module.role.model.request.ResourcePermissions;
import com.sobey.module.role.model.response.resourceResultV3;
import com.sobey.module.token.fegin.AuthService;
import com.sobey.util.business.identity.Identity;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;

/**
 * 
 * 
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class CopyRoleService extends ServiceImpl<CopyRoleMapper, CopyRole> {

	@Autowired
	private com.sobey.module.role.service.feign.RoleService feignRole;
	@Autowired
	private AuthService auth;

	/**
	 * 创建角色并赋予权限
	 * 
	 * @param siteCode
	 */
	public void initRole(String siteCode) {

		JSONObject json = auth.getToken();
		String token = (String) json.get("access_token");

		List<CopyRole> list = this.selectList(null);
		if (CollUtil.isNotEmpty(list)) {
			for (CopyRole item : list) {
				String code = item.getCode();

				// 查询原角色对应的权限
				resourceResultV3 resources = feignRole.queryResource(Identity.PUBLIC_SITE_CODE.getCode(), code, 1, 100, token);


				// 创建新角色
				com.sobey.module.role.model.request.RoleRequest rq = new com.sobey.module.role.model.request.RoleRequest();
				String newCode = "lingyun_role_" + RandomUtil.randomString(10);
				rq.setName(item.getName());
				rq.setCode(newCode);
				rq.setDescription(item.getDescription());
				rq.setDisabled(false);
				feignRole.addroleV3(rq, siteCode, token);

				// 给新角色赋予权限
				if (resources != null) {
					List<ResourcePermissions> permissions = new ArrayList<ResourcePermissions>();

					List<ResourcePermissions> results = resources.getResults();
					if (CollUtil.isNotEmpty(results)) {
						for (ResourcePermissions item2 : results) {
							ResourcePermissions permission = new ResourcePermissions();
							permission.setRole_code(newCode);
							permission.setResource_code(item2.getResource_code());
							permission.setSite_code(siteCode);
							permission.setPermissions(item2.getPermissions());
							permissions.add(permission);
						}
						if (CollUtil.isNotEmpty(permissions)) {
							feignRole.appendResPermOnRoleV3(siteCode, newCode, permissions, token);
						}
					}
				}

			}
		}

	}

}
