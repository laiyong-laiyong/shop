package com.sobey.module.role.model.request;

import java.util.List;

/**
 * @Description 资源权限
 * @Author WuChenYang
 * @Since 2020/1/22 11:20
 */
public class ResourcePermissions {

	private String role_code;
	private String resource_code;
	private List<String> permissions;// 取值范围 *、create、update、delete、view
	private String site_code;

	public String getSite_code() {
		return site_code;
	}

	public void setSite_code(String site_code) {
		this.site_code = site_code;
	}

	public String getRole_code() {
		return role_code;
	}

	public void setRole_code(String role_code) {
		this.role_code = role_code;
	}

	public String getResource_code() {
		return resource_code;
	}

	public void setResource_code(String resource_code) {
		this.resource_code = resource_code;
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

}
