package com.sobey.module.role.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;
import com.sobey.module.role.model.request.ResourcePermissions;
import com.sobey.module.role.model.request.RoleRequest;
import com.sobey.module.role.model.response.Result;
import com.sobey.module.role.model.response.RoleList;
import com.sobey.module.role.model.response.resourceResult;
import com.sobey.module.role.service.feign.RoleService;
import com.sobey.module.user.model.response.UserList;
import com.sobey.util.business.header.HeaderUtil;
import com.sobey.util.business.identity.Identity;
import com.sobey.util.common.uuid.UUIDUtils;

import io.swagger.annotations.ApiOperation;

/**
 * @Description 角色相关操作
 * @Author WuChenYang
 * @Since 2020/1/22 14:46
 */
@RestController
@RequestMapping(value = "${api.v1}/role")
public class RoleController {

	@Autowired
	private RoleService roleService;

	/**
	 * 新增角色
	 *
	 * @param role
	 * @param request
	 * @return
	 */
	@PostMapping
	public String addRole(@RequestBody RoleRequest role) {

		if (null == role) {
			throw new AppException(ExceptionType.SYS_RUNTIME, "参数错误", new RuntimeException());
		}
		// 获取token
		String authorization = HeaderUtil.getAuth();
		String code = UUIDUtils.simpleUuid();
		try {
			role.setCode(code);// 全局code,每个角色的唯一标识
			Date date = new Date();
			role.setCreate_time(date);
			Long id = roleService.addRole(role, authorization,Identity.PUBLIC_SITE_CODE.getCode());
			role.setId(id);
		} catch (Exception e) {
			throw new AppException(ExceptionType.SYS_REST, "新增角色异常", e);
		}
		return code;
	}

	/**
	 * 删除角色 多个roleCode用逗号隔开
	 *
	 * @param roleCodes
	 * @param request
	 * @return
	 */
	@DeleteMapping(value = "/{roleCodes}")
	public String deleteRoles(@PathVariable(name = "roleCodes") String roleCodes) {

		if (StringUtils.isEmpty(roleCodes)) {
			throw new AppException(ExceptionType.SYS_RUNTIME, "参数错误", new RuntimeException());
		}
		try {
			// 获取token
			String authorization = HeaderUtil.getAuth();
			roleService.deleteRole(roleCodes, authorization,Identity.PUBLIC_SITE_CODE.getCode());
		} catch (Exception e) {
			throw new AppException(ExceptionType.SYS_REST, "删除角色异常", e);
		}
		return "success";
	}

	/**
	 * 通过code更新角色
	 *
	 * @param role
	 */
	@PutMapping(value = "")
	public RoleRequest updateRole(@RequestBody RoleRequest role) {
		if (null == role) {
			throw new AppException(ExceptionType.SYS_RUNTIME, "参数错误", new RuntimeException());
		}
		String authorization = HeaderUtil.getAuth();
		try {
			roleService.updateRole(role, authorization,Identity.PUBLIC_SITE_CODE.getCode());
		} catch (Exception e) {
			throw new AppException(ExceptionType.SYS_REST, "更新角色异常", e);
		}
		return role;
	}

	/**
	 * 批量更新
	 *
	 * @param roles
	 */
	@PutMapping(value = "/batch")
	public String batchUpdateRoles(List<Object> roles) {

		if (null == roles || roles.size() == 0) {
			throw new AppException(ExceptionType.SYS_RUNTIME, "参数错误", new RuntimeException());
		}

		String authorization = HeaderUtil.getAuth();
		try {
			roleService.batchUpdateRoles(roles, authorization,Identity.PUBLIC_SITE_CODE.getCode());
		} catch (Exception e) {
			throw new AppException(ExceptionType.SYS_REST, "批量更新角色异常", e);
		}
		return "success";
	}

	/**
	 * 批量给某一个角色赋予用户,原用户全删除
	 *
	 * @param roleCode
	 * @param userCodes
	 */
	@PostMapping(value = "/users/{roleCode}")
	public String batchAddUsersOnRole(@PathVariable(value = "roleCode") String roleCode,
			List<String> userCodes) {

		if (StringUtils.isEmpty(roleCode)) {
			throw new AppException(ExceptionType.SYS_RUNTIME, "参数错误", new RuntimeException());
		}
		String authorization = HeaderUtil.getAuth();
		try {
			roleService.batchAddUsersOnRole(roleCode, userCodes, authorization,Identity.PUBLIC_SITE_CODE.getCode());
		} catch (Exception e) {
			throw new AppException(ExceptionType.SYS_REST, "批量更新角色拥有用户异常", e);
		}

		return "success";
	}

	/**
	 * 给某一个角色赋予资源权限
	 *
	 * @param roleCode
	 * @param resourcePermissions
	 */
	@PostMapping(value = "/resource/{roleCode}")
	@ApiOperation(value = "给某一个角色赋予资源权限", notes = "给某一个角色赋予资源权限")
	public String addResPermOnRole(@PathVariable(value = "roleCode") String roleCode,
			@RequestBody List<ResourcePermissions> resourcePermissions) {

		if (StringUtils.isEmpty(roleCode)) {
			throw new AppException(ExceptionType.SYS_RUNTIME, "参数错误", new RuntimeException());
		}
		try {
			// 获取token
			String authorization = HeaderUtil.getAuth();
			roleService.addResPermOnRole(roleCode, resourcePermissions, authorization,Identity.PUBLIC_SITE_CODE.getCode());
		} catch (Exception e) {
			throw new AppException(ExceptionType.SYS_REST, "赋予资源权限异常异常", e);
		}

		return "success";
	}

	/**
	 * 查询角色
	 * 
	 * @param queryParams
	 * @param extend
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/query/{queryParams}")
	public RoleList queryRoles(@PathVariable(value = "queryParams") String queryParams,
			Map<String, Object> extend) {

		RoleList roleList = null;
		String authorization = HeaderUtil.getAuth();
		try {
			Object results = roleService.queryRoles(queryParams, extend, authorization,Identity.PUBLIC_SITE_CODE.getCode());
			if (null != results) {
				roleList = JSON.parseObject(JSON.toJSONString(results), RoleList.class);

			}
		} catch (Exception e) {
			throw new AppException(ExceptionType.SYS_REST, "查询角色异常", e);
		}
		return roleList;
	}

	/**
	 * 查询一个角色信息
	 *
	 * @param roleCode
	 * @param countUsers
	 * @return
	 */
	@GetMapping(value = "/{roleCode}")
	public Result queryRole(@PathVariable(value = "roleCode") String roleCode,
			@RequestParam(value = "countUsers", required = false) Boolean countUsers) {
		if (StringUtils.isEmpty(roleCode)) {
			throw new AppException(ExceptionType.SYS_RUNTIME, "参数错误", new RuntimeException());
		}
		Result result = null;
		try {
			// 获取token
			String authorization = HeaderUtil.getAuth();
			Object obj = roleService.queryRole(roleCode, countUsers, authorization,Identity.PUBLIC_SITE_CODE.getCode());
			if (null != obj) {
				result = JSON.parseObject(JSON.toJSONString(obj), Result.class);
			}
		} catch (Exception e) {
			throw new AppException(ExceptionType.SYS_REST, "查询角色信息异常", e);
		}

		return result;
	}

	/**
	 * 获取一个角色被赋予资源权限
	 * 
	 * @param roleCode
	 * @return
	 */
	@GetMapping(value = "/resource/{roleCode}")
	@ApiOperation(value = "获取一个角色被赋予资源权限", notes = "获取一个角色被赋予资源权限")
	public resourceResult queryResPerm(@PathVariable(value = "roleCode") String roleCode) {

		if (StringUtils.isEmpty(roleCode)) {
			throw new AppException(ExceptionType.SYS_RUNTIME, "参数错误", new RuntimeException());
		}
		resourceResult resPerms = null;
		// 获取token
		String authorization = HeaderUtil.getAuth();
		try {
			Object obj = roleService.queryResPerm(roleCode, authorization,Identity.PUBLIC_SITE_CODE.getCode());
			if (null != obj) {
				resPerms = JSON.parseObject(JSON.toJSONString(obj), resourceResult.class);
			}
		} catch (Exception e) {
			throw new AppException(ExceptionType.SYS_REST, "查询角色拥有的资源权限异常", e);
		}
		return resPerms;
	}

	/**
	 * 查询某个角色直接绑定的用户列表 roleCode如果为空,表示查询没有被角色绑定的用户 revert反选, 也就是选择没有直接绑定这个角色的用户
	 * 
	 * @param roleCode
	 * @param revert
	 * @return
	 */
	@GetMapping(value = "/users")
	public Object queryUsers(@RequestParam(value = "roleCode", required = false) String roleCode,
			@RequestParam(value = "revert", required = false) String revert) {

		UserList userList = null;
		String authorization = HeaderUtil.getAuth();
		try {
			Object obj = roleService.queryUsers(roleCode, revert, authorization,Identity.PUBLIC_SITE_CODE.getCode());
			if (null != obj) {
				userList = JSON.parseObject(JSON.toJSONString(obj), UserList.class);
			}
		} catch (Exception e) {
			throw new AppException(ExceptionType.SYS_REST, "通过角色查询用户列表异常", e);
		}
		return userList;
	}

}
