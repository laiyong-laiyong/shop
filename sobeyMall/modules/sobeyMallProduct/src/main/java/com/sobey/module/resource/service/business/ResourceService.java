package com.sobey.module.resource.service.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import com.sobey.module.resource.model.Resource;
import com.sobey.module.resource.service.feign.ResourceFeignService;
import com.sobey.module.role.service.business.RoleService;
import com.sobey.util.business.identity.Identity;
import com.sobey.util.common.uuid.UUIDUtils;

/**
 * 
 * 
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class ResourceService {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ResourceFeignService rfs;
	
	

	@Autowired
	private com.sobey.framework.config.resource.Resource rsConfig;

	/**
	 * 
	 * 
	 * @param name
	 * @param parentId
	 * @param productRoot
	 * @return
	 */
	public Resource warpResource(String name, Long parentId, boolean productRoot) {
		Resource re = new Resource();
		String code = UUIDUtils.simpleUuid();
		re.setCode(code);
		re.setName(name);
		/**
		 * 
		 * 如果是商品根目录,则parentId需要从配置文件中取, 否则使用传递的parentId 商品资源结构如下： 商品 商品1 模块1 商品2
		 * 模块1
		 */
		if (productRoot) {
			re.setParent_id(Long.valueOf(rsConfig.getProduct()));
		} else {
			re.setParent_id(parentId);
		}
		re.setClient_code(Identity.PUBLIC_CLIENT_CODE.getCode());
		re.setUrl("1");
		re.setSite_code(Identity.PUBLIC_SITE_CODE.getCode());
		re.setShown(true);
		return re;
	}

	public Resource warpResource(String name, String id, String code) {
		Resource re = new Resource();
		re.setId(Long.valueOf(id));
		re.setCode(code);
		re.setName(name);
		return re;
	}

	/**
	 * C删除认证中心的资源
	 * 
	 * @param id
	 * @param delete_child
	 * @param auth
	 * @param site
	 */
	public void deleteFromAuthCenter(Long id, boolean delete_child, String auth, String site) {

		if (id == null) {
			return;
		}
		
		try {
			//删除以商品名为名称的资源
			this.rfs.delete(id.toString(),delete_child, auth,site);
		} catch (Exception e) {
			log.error("删除认证中心的资源失败", e);
		}
		
	}
	
	

}
