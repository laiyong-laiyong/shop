package com.sobey.module.productPrivilege.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionKit;
import com.sobey.exception.ExceptionType;
import com.sobey.module.metric.model.Metric;
import com.sobey.module.product.enumeration.OperationType;
import com.sobey.module.productPrivilege.mapper.ProductPrivilegeMapper;
import com.sobey.module.productPrivilege.model.ProductPrivilege;
import com.sobey.module.resource.model.Resource;
import com.sobey.module.resource.service.feign.ResourceFeignService;
import com.sobey.module.role.model.request.ResourcePermissions;
import com.sobey.module.role.model.request.Role;
import com.sobey.module.role.service.business.RoleService;
import com.sobey.util.business.header.HeaderUtil;
import com.sobey.util.business.identity.Identity;

/**
 * 
 * 
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class ProductPrivilegeService extends ServiceImpl<ProductPrivilegeMapper, ProductPrivilege> {

	@Autowired
	private ProductPrivilegeMapper ppm;
	@Autowired
	private ResourceFeignService resourceFeign;
	@Autowired
	private com.sobey.module.resource.service.business.ResourceService resourceBusi;
	@Autowired
	private RoleService roleBusi;
	@Autowired
	private com.sobey.module.role.service.feign.RoleService rolefeign;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	public Page<ProductPrivilege> page(Page<ProductPrivilege> page, ProductPrivilege ct) {
		List<ProductPrivilege> cts = ppm.page(page, ct);
		page.setRecords(cts);
		return page;
	}

	public void remove(ProductPrivilege entity) {

		List<ProductPrivilege> list = this.list(entity);

		if (CollectionUtils.isEmpty(list)) {
			return;
		}

		String auth = HeaderUtil.getAuth();
		String site = Identity.PUBLIC_SITE_CODE.getCode();

		for (ProductPrivilege item : list) {
			String uuid = item.getUuid();
			ProductPrivilege db = this.selectById(uuid);
			if (db != null) {
				this.deleteById(uuid);

				//暂时不操作角色，和赋权
				//删除角色,资源及他们之间的关联关系信息
//				String roleCode = db.getRoleCode();
//				if (StringUtils.isNotEmpty(roleCode)) {
//					this.rolefeign.deleteRole(roleCode, auth, site);
//				}
//				String resourceId = db.getResourceId();
//				if (StringUtils.isNotEmpty(resourceId)) {
//					try {
//						this.resourceFeign.delete(resourceId, false, auth, site);
//					} catch (Exception e) {
//						//不处理
//					}
//				}
//
//				String resourceCode = db.getResourceCode();
//				List<String> resourceCodes = new ArrayList<String>();
//				resourceCodes.add(resourceCode);
//				if (StringUtils.isNotEmpty(roleCode)) {
//					this.rolefeign.deleteResPermOnRole(roleCode, resourceCodes, auth, site);
//				}
			}
		}
	}

	public void insertBatch(List<ProductPrivilege> privileges, String productId,
			Long resourceParentId) {
		if (CollectionUtils.isEmpty(privileges)) {
			return;
		}

		try {
			for (int i = 0; i < privileges.size(); i++) {

				ProductPrivilege vs = privileges.get(i);
				this.insert(vs, productId, resourceParentId);

			}

		} catch (Exception e) {
			log.error(ExceptionKit.toString(e));
			/**
			 * C因为不知道是哪个报错,所以这里要批量回滚
			 * 这里回滚是担心同步资源到认证中心报错，才回滚，现在不
			 * 需要同步到认证中心，所以注释掉
			 */
//			rollback(privileges);
			throw e;
		}
	}

	public void insert(ProductPrivilege item, String productId, Long resourceParentId) {

		if (item == null) {
			return;
		}

		String auth = HeaderUtil.getAuth();
		String site = Identity.PUBLIC_SITE_CODE.getCode();

		String name = item.getName();

		if (StringUtils.isEmpty(name)) {
			throw new AppException(ExceptionType.SYS_RUNTIME, "自定义权限的名称不能为空", null);
		}
		// 新增资源
		/**
		 * 这里暂时不再需要同步。新增角色和赋权
		 * 
		 */
//		Resource resource = resourceBusi.warpResource(name, resourceParentId, false);
//		String resourceCode = resource.getCode();
//		Long resourceId = this.resourceFeign.add(resource, auth, site);

//		// 新增角色
//		Role role = roleBusi.warpRole(name);
//		String roleCode = role.getCode();
//		Long roleId = this.rolefeign.addRole(role, auth, site);
//
//		// 给角色赋予资源的信息
//		List<String> permission = new ArrayList<String>();
//		permission.add("*");
//		List<ResourcePermissions> warpPermission = roleBusi.warpPermission(roleCode, resourceCode,
//				permission, resource.getSite_code());
//		this.rolefeign.appendResPermOnRole(roleCode, warpPermission, auth, site);

//		item.setResourceId(resourceId.toString());
//		item.setResourceCode(resourceCode);
//		item.setRoleId(roleId.toString());
//		item.setRoleCode(roleCode);
		item.setProductId(productId);

		this.insert(item);

	}

	public void updateBatch(List<ProductPrivilege> privileges, String productId,
			Long resourceParentId) {
		if (CollectionUtils.isEmpty(privileges)) {
			return;
		}

		for (int i = 0; i < privileges.size(); i++) {

			ProductPrivilege item = privileges.get(i);

			if (item != null) {
				String operationType = item.getOperationType();
				String uuid = item.getUuid();
				if (OperationType.delete.getName().equalsIgnoreCase(operationType)) {
					if (StringUtils.isNotEmpty(uuid)) {
						ProductPrivilege item2 = new ProductPrivilege();
						item2.setUuid(uuid);
						this.remove(item2);
					}
				} else if (OperationType.insert.getName().equalsIgnoreCase(operationType)) {
					this.insert(item, productId, resourceParentId);
				} else if (OperationType.update.getName().equalsIgnoreCase(operationType)) {
					this.update(item);
				}
			}
		}
	}

	public void update(ProductPrivilege item) {
		if (item == null) {
			return;
		}
		String name = item.getName();
		//暂时不需要这块逻辑
		// C名字不为空，需要修改对应的资源和角色名称
//		if (StringUtils.isNotEmpty(name)) {
//			String uuid = item.getUuid();
//			ProductPrivilege db = this.selectById(uuid);
//			if (db != null) {
//
//				String auth = HeaderUtil.getAuth();
//				String site = Identity.PUBLIC_SITE_CODE.getCode();
//
//				String resourceId = db.getResourceId();
//				String resourceCode = db.getResourceCode();
//				// 修改资源
//				Resource resource = resourceBusi.warpResource(name, resourceId, resourceCode);
//				this.resourceFeign.update(resource, auth, site);
//
//				// 这里暂时不再修改角色
//				String roleCode = db.getRoleCode();
//				Role role = roleBusi.warpRole(name, roleCode);
//				this.rolefeign.updateRole(role, auth, site);
//			}
//		}

		this.updateById(item);

	}

	/**
	 * C当添加权限报错时，回滚数据
	 * 
	 * @param privileges
	 */
	public void rollback(List<ProductPrivilege> privileges) {
		if (CollectionUtils.isEmpty(privileges)) {
			return;
		}

		String auth = HeaderUtil.getAuth();
		String site = Identity.PUBLIC_SITE_CODE.getCode();
		for (int i = 0; i < privileges.size(); i++) {
			try {

				ProductPrivilege vs = privileges.get(i);
				String resourceId = vs.getResourceId();
				String roleCode = vs.getRoleCode();
				/**
				 * C因为批量增加的时候，如果某一个报错了，会导致没有resourceId和roleCode 所以这里判断下
				 * 
				 */
				if (StringUtils.isNotEmpty(resourceId)) {
					this.resourceFeign.delete(resourceId, false, auth, site);
				}
				//暂时不操作角色
//				if (StringUtils.isNotEmpty(roleCode)) {
//					this.rolefeign.deleteRole(roleCode, auth, site);
//				}
			} catch (Exception e) {
				// 什么也不用做
			}
		}

	}

	public List<ProductPrivilege> list(ProductPrivilege entity) {

		if (entity == null) {
			return null;
		}
		List<ProductPrivilege> pts = ppm.list(entity);
		return pts;
	}

}
