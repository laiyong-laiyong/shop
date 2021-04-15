package com.sobey.module.product.service;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.framework.config.FileUpload;
import com.sobey.module.product.mapper.ProductMapper;
import com.sobey.module.product.model.Product;
import com.sobey.module.account.model.response.UserDetail;
import com.sobey.module.account.service.feign.UserFeignService;
import com.sobey.util.business.header.HeaderUtil;
import com.sobey.util.common.uuid.UUIDUtils;

/**
 * 
 * 
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class ProductService extends ServiceImpl<ProductMapper, Product> {

	@Autowired
	private ProductMapper ptMp;
	@Autowired
	private UserFeignService user;
	@Autowired
	private FileUpload upload;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	public Page<Product> page(Page<Product> page, Product entity) {

		List<Product> cts = ptMp.page(page, entity);
		//由于要优化接口性能，这里暂时注释
//		handleAdditional(cts);
		page.setRecords(cts);
		return page;
	}

	public UserDetail queryUser(String siteCode,String userCode) {
		
		if (StringUtils.isNotEmpty(userCode)) {
			String auth = HeaderUtil.getAuth();
			UserDetail user = this.user.queryUserCrossTenant(auth, siteCode,userCode);
			return user;
		}else {
			return null;
		}


	}
	
	public void handleAdditional(List<Product> cts) {

		if (CollectionUtils.isEmpty(cts)) {
			return;
		}

		for (Product item : cts) {
			if (item != null) {
				//设置创建者名称
				String creatCode = item.getCreateUserCode();
				String siteCode = item.getSiteCode();
					UserDetail user = this.queryUser(siteCode,creatCode);
					if (user != null) {
						String userName = user.getLogin_name();
						item.setCreateUserName(userName);
					}
			}
		}

	}

	public List<Product> list(Product entity) {

		List<Product> pts = ptMp.list(entity);
		return pts;
	}

	/**
	 * 获取产品素材的主目录
	 * 
	 * @return
	 */
	public String getMediaDir() {
		String parentDir = UUIDUtils.simpleUuid();
		/**
		 * C这里使用在存储路径destPath统一价格时间戳作为父目录
		 * 
		 */
		String path = upload.getPath();

		String destPath = path + "/" + parentDir;

		return destPath;
	}

	public List<HashMap<String, Object>> statistic() {
		List<HashMap<String, Object>> map = this.ptMp.statistic();
		return map;
	}

}
