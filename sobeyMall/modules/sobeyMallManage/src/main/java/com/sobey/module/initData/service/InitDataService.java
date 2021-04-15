package com.sobey.module.initData.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.sobey.exception.AppException;
import com.sobey.module.initData.config.InitDataRunner;
import com.sobey.module.permissions.model.Permissions;
import com.sobey.module.permissions.service.PermissionsService;
import com.sobey.module.resource.enumeration.SuccessType;
import com.sobey.module.resource.model.Resource;
import com.sobey.module.resource.model.request.Extend;
import com.sobey.module.resource.model.request.ResourceRequest;
import com.sobey.module.resource.service.ResourceService;
import com.sobey.module.role.model.Role;
import com.sobey.module.role.model.request.ResourcePermissions;
import com.sobey.module.role.service.RoleService;
import com.sobey.module.token.fegin.AuthService;
import com.sobey.module.user.model.User;
import com.sobey.module.user.model.request.UserRequestV2;
import com.sobey.module.user.model.response.UserResponseV2;
import com.sobey.module.user.service.UserService;
import com.sobey.module.userrole.model.UserRole;
import com.sobey.module.userrole.service.UserRoleService;
import com.sobey.util.business.identity.Identity;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;

/**
 * 
 * 
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class InitDataService {

	private static final Logger log = LoggerFactory.getLogger(InitDataRunner.class);

	@Autowired
	private UserRoleService urs;
	@Autowired
	private com.sobey.module.userrole.service.feign.UserRoleService feignUrs;
	@Autowired
	private com.sobey.module.user.service.feign.UserService feignUser;
	@Autowired
	private AuthService auth;
	@Autowired
	private UserService userSv;
	@Autowired
	private PermissionsService psSv;
	@Autowired
	private RoleService roleSv;
	@Autowired
	private com.sobey.module.role.service.feign.RoleService feignRole;
	@Autowired
	private ResourceService rs;
	@Autowired
	private com.sobey.module.resource.service.feign.ResourceService feignRs;
	@Value("${initdata}")
	private boolean initdata;

	public void run() {
		log.info("是否初始化商城数据：{}", initdata);


		if (initdata == true) {
			/**
			 * C获取token
			 * 
			 */
			JSONObject json = auth.getToken();
			String token = (String) json.get("access_token");
			/**
			 * C这里要catch住异常，不然swagger启动不了
			 * 
			 */
			try {
				initResource("0", null, token);
				initRole(token);
				initPermission(token);
				initUser(token);
				initUserRole(token);
				
			} catch (Exception e) {
				log.error("初始化数据没有成功", e);
			}
		}
	}

	/**
	 * C初始化资源，递归处理
	 * 
	 * @param parentId
	 *            自己表中的parentId
	 * @param parent_id
	 *            认证中心的id
	 * @param token
	 */
	public void initResource(String parentId, Long parent_id, String token) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parentId", parentId);
		List<Resource> list = rs.selectByMap(map);
		if (CollUtil.isNotEmpty(list)) {
			Long resourceId = null;
			for (Resource item : list) {
				String success = item.getSuccess();
				/**
				 * 
				 * 有可能是上级资源已经新增成功了，但是下级资源没有新增成功， 所以这里当成功状态为yes的时候，还需要嵌套处理下级资源
				 */
				if (SuccessType.YES.getCode().equalsIgnoreCase(success)) {
					// 上级资源新增成功后，这里一定会有resourceId
					resourceId = item.getResourceId();
					// 递归处理下级资源
					initResource(item.getUuid(), resourceId, token);
				} else {
					ResourceRequest rq = new ResourceRequest();

					String code = item.getCode();
					rq.setName(item.getName());
					rq.setCode(code);
					// 如果不是根节点,需要加上parent_id
					if (StrUtil.equals(parentId, "0")) {
						rq.setParent_id(1L);
					} else {
						rq.setParent_id(parent_id);
					}
					rq.setUrl("/");
					rq.setShown(true);
					rq.setClient_code(item.getClientCode());
					rq.setSite_code(item.getSiteCode());

					Extend extend = new Extend();
					extend.setDesc(item.getDesc());
					rq.setExtend(extend);
					try {
						resourceId = feignRs.add(rq, token, Identity.PUBLIC_SITE_CODE.getCode());
						item.setSuccess(SuccessType.YES.getCode());
						item.setResourceId(resourceId);
						this.rs.updateById(item);
					} catch (Exception e) {
						item.setSuccess(SuccessType.NO.getCode());
						this.rs.updateById(item);
						throw new AppException(StrBuilder.create().append("资源编号:").append(code).toString(), e);
					}
					// 递归处理下级资源
					initResource(item.getUuid(), resourceId, token);
				}
			}
		}
	}

	/**
	 * C初始化角色
	 * 
	 * @param token
	 */
	public void initRole(String token) {

		List<Role> list = roleSv.selectList(null);
		if (CollUtil.isNotEmpty(list)) {
			for (Role item : list) {
				String success = item.getSuccess();
				/**
				 * 处理没执行成功和还未执行的数据
				 */
				if (SuccessType.NO.getCode().equalsIgnoreCase(success) || success == null ) {
					com.sobey.module.role.model.request.RoleRequest rq = new com.sobey.module.role.model.request.RoleRequest();
					String code = item.getCode();
					rq.setName(item.getName());
					rq.setCode(code);
					rq.setClient_code(item.getClientCode());
					rq.setDescription(item.getDescription());
					rq.setDisabled(false);

					try {

						feignRole.addRole(rq, token, Identity.PUBLIC_SITE_CODE.getCode());
						item.setSuccess(SuccessType.YES.getCode());
						this.roleSv.updateById(item);
					} catch (Exception e) {
						item.setSuccess(SuccessType.NO.getCode());
						this.roleSv.updateById(item);
						throw new AppException(StrBuilder.create().append("角色编号:").append(code).toString(), e);
					}
				}
			}
		}

	}

	/**
	 * C初始化权限
	 * 
	 * @param token
	 */
	public void initPermission(String token) {

		List<Permissions> list = psSv.selectList(null);
		if (CollUtil.isNotEmpty(list)) {
			for (Permissions item : list) {

				String success = item.getSuccess();
				/**
				 * 处理没执行成功和还未执行的数据
				 */
				if (SuccessType.NO.getCode().equalsIgnoreCase(success) || success == null ) {
					ResourcePermissions rq = new ResourcePermissions();
					String roleCode = item.getRoleCode();
					rq.setRole_code(roleCode);
					rq.setResource_code(item.getResourceCode());
					rq.setPermissions(ListUtil.toList(item.getPermissions()));

					ArrayList<ResourcePermissions> permissions = ListUtil.toList(rq);

					try {
						feignRole.appendResPermOnRole(roleCode, permissions, token, Identity.PUBLIC_SITE_CODE.getCode());
						item.setSuccess(SuccessType.YES.getCode());
						this.psSv.updateById(item);
					} catch (Exception e) {

						item.setSuccess(SuccessType.NO.getCode());
						this.psSv.updateById(item);
						throw new AppException(StrBuilder.create().append("权限id:").append(item.getUuid()).toString(), e);
					}
				}
			}
		}

	}

	/**
	 * C初始化用户,包括用户权限
	 * 
	 * @param token
	 */
	public void initUser(String token) {

		List<User> list = this.userSv.selectList(null);
		if (CollUtil.isNotEmpty(list)) {
			for (User item : list) {
				String success = item.getSuccess();
				String userId = item.getUuid();
				/**
				 * 处理没执行成功和还未执行的数据
				 */
				if (SuccessType.NO.getCode().equalsIgnoreCase(success) || success == null ) {
					UserRequestV2 user = new UserRequestV2();
					String loginName = item.getLoginName();
					user.setLoginName(loginName);
					user.setNickName(item.getNickName());
					user.setPassword(item.getPassword());
					user.setPhone(item.getPhone());
					user.setSiteCode(item.getSiteCode());
					user.setRootUserFlag(true);
					ArrayList<UserRequestV2> users = ListUtil.toList(user);

					List<UserResponseV2> userRps = null;
					try {
						userRps = feignUser.addV2(users, token, Identity.PUBLIC_SITE_CODE.getCode());
						item.setSuccess(SuccessType.YES.getCode());
						this.userSv.updateById(item);

						/**
						 * 这里更新用户角色的关联表，方便以后更新关系
						 * 
						 */
						if (CollUtil.isNotEmpty(userRps)) {
							for (UserResponseV2 it : userRps) {
								if (it != null) {
									String userCode = it.getUserCode();
									EntityWrapper<UserRole> ew = new EntityWrapper<UserRole>();
									ew.eq("userId", userId);
									this.urs.updateForSet("userCode='" + userCode + "'", ew);
								}
							}
						}

					} catch (Exception e) {
						item.setSuccess(SuccessType.NO.getCode());
						this.userSv.updateById(item);
						throw new AppException(StrBuilder.create().append("用户loginName:").append(loginName).toString(), e);
					}
				}
			}
		}

	}

	/**
	 * C初始化用户和角色的关系
	 * 
	 * @param token
	 */
	public void initUserRole(String token) {

		List<UserRole> list = this.urs.selectList(null);
		if (CollUtil.isNotEmpty(list)) {
			for (UserRole item : list) {
				String success = item.getSuccess();
				String userCode = item.getUserCode();
				String roleCode = item.getRoleCode();
				String uuid = item.getUuid();
				/**
				 * 处理没执行成功和还未执行的数据
				 */
				if (SuccessType.NO.getCode().equalsIgnoreCase(success) || success == null ) {

					try {
						// 更新用户的角色
						feignUrs.bindingRole(userCode, ListUtil.toList(roleCode), token, Identity.PUBLIC_SITE_CODE.getCode());
						item.setSuccess(SuccessType.YES.getCode());
						this.urs.updateById(item);
					} catch (Exception e) {
						item.setSuccess(SuccessType.NO.getCode());
						this.urs.updateById(item);
						throw new AppException(StrBuilder.create().append("用户角色表编号:").append(uuid).toString(), e);
					}
				}
			}
		}

	}

}
