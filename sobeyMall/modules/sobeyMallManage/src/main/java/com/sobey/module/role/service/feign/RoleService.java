package com.sobey.module.role.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.sobey.module.role.model.request.ResourcePermissions;
import com.sobey.module.role.model.request.RoleRequest;
import com.sobey.module.role.model.response.resourceResultV3;

import java.util.List;
import java.util.Map;

/**
 * @Description 角色相关
 * @Author WuChenYang
 * @Since 2020/1/22 10:44
 */
@FeignClient(name = "${address.auth.name}", url = "${address.auth.url}")
public interface RoleService {

	@PostMapping(value = "/v1.0/role")
	Long addRole(Object role, @RequestHeader(value = "sobeyhive-http-token") String value,
			@RequestHeader(value = "sobeyhive-http-site") String site);

	@DeleteMapping(value = "/v1.0/role/{roleCode}")
	void deleteRole(@PathVariable(name = "roleCode") String roleCode,
			@RequestHeader(value = "sobeyhive-http-token") String value,
			@RequestHeader(value = "sobeyhive-http-site") String site);

	/**
	 * 删除角色 角色编号用逗号隔开
	 *
	 * @param roleCodes
	 */
	@DeleteMapping(value = "/v1.0/role/{roleCodes}")
	void batchDeleteRoles(@PathVariable(name = "roleCodes") String roleCodes,
			@RequestHeader(value = "sobeyhive-http-token") String value,
			@RequestHeader(value = "sobeyhive-http-site") String site);

	/**
	 * 通过code更新角色
	 *
	 * @param role
	 */
	@PutMapping(value = "/v1.0/role")
	void updateRole(Object role, @RequestHeader(value = "sobeyhive-http-token") String value,
			@RequestHeader(value = "sobeyhive-http-site") String site);

	/**
	 * 批量更新
	 *
	 * @param roles
	 */
	@PutMapping(value = "/v1.0/role/batch")
	void batchUpdateRoles(List<Object> roles, @RequestHeader(value = "sobeyhive-http-token") String value,
			@RequestHeader(value = "sobeyhive-http-site") String site);

	/**
	 * 批量给某一个角色赋予用户,原用户全删除
	 *
	 * @param roleCode
	 * @param userCodes
	 */
	@PostMapping(value = "/v1.0/role/users/{roleCode}")
	void batchAddUsersOnRole(@PathVariable(value = "roleCode") String roleCode,
			List<String> userCodes, @RequestHeader(value = "sobeyhive-http-token") String value,
			@RequestHeader(value = "sobeyhive-http-site") String site);

	/**
	 * 给某一个角色赋予资源权限
	 *
	 * @param roleCode
	 * @param resourcePermissions
	 */
	@PostMapping(value = "/v1.0/role/resource/{roleCode}")
	void addResPermOnRole(@PathVariable(value = "roleCode") String roleCode,
			List<ResourcePermissions> resourcePermissions,
			@RequestHeader(value = "sobeyhive-http-token") String value,
			@RequestHeader(value = "sobeyhive-http-site") String site);
	
	
	@PutMapping(value = "/v1.0/role/resource/{roleCode}/list")
	void appendResPermOnRole(@PathVariable(value = "roleCode") String roleCode,
			List<ResourcePermissions> resourcePermissions,
			@RequestHeader(value = "sobeyhive-http-token") String value,
			@RequestHeader(value = "sobeyhive-http-site") String site);

	/**
	 * 查询角色,参数详情查看http://172.16.148.113/cloud-api/common/010/002/004.html。
	 * <p>
	 * keyword disabled 是否被禁用 shown 是否要隐藏 orderBy 排序字段,默认createTime sort 排序方向,默认
	 * desc countUsers 是否统计所属用户数量,默认false page size
	 *
	 * @param queryParams
	 *            要拼接的查询参数 以 ? 开头
	 * @param extend
	 *            扩展条件,精确匹配
	 * @return
	 */
	@PostMapping(value = "/v1.0/role/query/{queryParams}")
	Object queryRoles(@PathVariable(value = "queryParams") String queryParams,
			Map<String, Object> extend, @RequestHeader(value = "sobeyhive-http-token") String value,
			@RequestHeader(value = "sobeyhive-http-site") String site);

	/**
	 * 查询一个角色信息
	 *
	 * @param roleCode
	 * @param countUsers
	 * @param value
	 * @return
	 */
	@GetMapping(value = "/v1.0/role/{roleCode}")
	Object queryRole(@PathVariable(value = "roleCode") String roleCode,
			@RequestParam(value = "countUsers", required = false) Boolean countUsers,
			@RequestHeader(value = "sobeyhive-http-token") String value,
			@RequestHeader(value = "sobeyhive-http-site") String site);

	/**
	 * 获取一个角色被赋予资源权限
	 * 
	 * @param roleCode
	 * @param value
	 * @return
	 */
	@GetMapping(value = "/v1.0/role/resource/{roleCode}")
	Object queryResPerm(@PathVariable(value = "roleCode") String roleCode,
			@RequestHeader(value = "sobeyhive-http-token") String value,
			@RequestHeader(value = "sobeyhive-http-site") String site);

	/**
	 * 查询某个角色直接绑定的用户列表 roleCode如果为空,表示查询没有被角色绑定的用户 revert反选, 也就是选择没有直接绑定这个角色的用户
	 * 
	 * @param roleCode
	 * @param revert
	 * @param value
	 * @return
	 */
	@GetMapping(value = "/v1.0/role/users")
	Object queryUsers(@RequestParam(value = "roleCode", required = false) String roleCode,
			@RequestParam(value = "revert", required = false) String revert,
			@RequestHeader(value = "sobeyhive-http-token") String value,
			@RequestHeader(value = "sobeyhive-http-site") String site);
	
	
	/**
	 * 跨租户-创建角色
	 * 
	 * @param role
	 * @param site_code
	 * @param value
	 */
	@PostMapping(value = "/v3.0/cross-tenant/{site_code}/role")
	void addroleV3(RoleRequest role,@PathVariable(value = "site_code") String site_code,
			@RequestHeader(value = "sobeycloud-token") String value);
	
	/**
	 * 
	 * 跨租户-获取一个角色被赋予资源权限
	 * @param site_code
	 * @param role_code
	 * @param page
	 * @param size
	 * @param value
	 * @return
	 */
	@GetMapping(value = "/v3.0/cross-tenant/{site_code}/role/resource/{role_code}")
	resourceResultV3 queryResource(@PathVariable(value = "site_code") String site_code,
						@PathVariable(value = "role_code") String role_code,
						@RequestParam(value = "page", required = false) Integer page,
						@RequestParam(value = "size", required = false) Integer size,
						@RequestHeader(value = "sobeycloud-token") String value);
	
	/**
	 * 
	 * 跨租户-给某一个角色赋予资源权限
	 * @param site_code
	 * @param role_code
	 * @param resourcePermissions
	 * @param value
	 */
	@PostMapping(value = "/v3.0/cross-tenant/{site_code}/role/resource/{role_code}")
	void appendResPermOnRoleV3(@PathVariable(value = "site_code") String site_code,
			@PathVariable(value = "role_code") String role_code,
			List<ResourcePermissions> resourcePermissions,
			@RequestHeader(value = "sobeycloud-token") String value);

}
