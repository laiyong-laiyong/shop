package com.sobey.module.relationProduct.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.metric.model.Metric;
import com.sobey.module.packages.model.Packages;
import com.sobey.module.product.enumeration.OperationType;
import com.sobey.module.relationProduct.mapper.RelationProductMapper;
import com.sobey.module.relationProduct.model.RelationProduct;

import cn.hutool.core.util.StrUtil;

/**
 * 
 * 
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class RelationProductService extends ServiceImpl<RelationProductMapper, RelationProduct> {

	@Autowired
	private RelationProductMapper mm;

	public void insertBatch(List<RelationProduct> mts, String productId) {
		if (CollectionUtils.isEmpty(mts)) {
			return;
		}

		for (RelationProduct item : mts) {
			if (item != null) {
				String relationProductId = item.getRelationProductId();
				if (!checkExists(productId, relationProductId)) {
					item.setProductId(productId);
					this.insert(item);
				}
			}
		}

	}

	public void updateBatch(List<RelationProduct> list, String productId) {
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		for (RelationProduct item : list) {
			if (item != null) {
				String operationType = item.getOperationType();
				String uuid = item.getUuid();
				if (OperationType.delete.getName().equalsIgnoreCase(operationType)) {
					if (StringUtils.isNotEmpty(uuid)) {
						this.deleteById(uuid);
					}
				} else {
					String relationProductId = item.getRelationProductId();
					// C没有重复关联商品
					if (!checkExists(productId, relationProductId)) {
						item.setProductId(productId);
						this.insertOrUpdate(item);
					}
				}
			}
		}
	}

	/**
	 * C校验是否重复关联商品
	 * 
	 * @param productId
	 * @param relationProductId
	 * @return
	 */
	public boolean checkExists(String productId, String relationProductId) {

		if (StrUtil.isNotBlank(productId) && StrUtil.isNotBlank(relationProductId)) {
			RelationProduct item = new RelationProduct();
			item.setProductId(productId);
			item.setRelationProductId(relationProductId);
			EntityWrapper<RelationProduct> ew = new EntityWrapper<RelationProduct>(item);
			int count = this.selectCount(ew);
			if (count >= 1) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	public Page<RelationProduct> page(Page<RelationProduct> page, RelationProduct entity) {

		List<RelationProduct> cts = this.mm.page(page, entity);
		page.setRecords(cts);
		return page;
	}

	public List<RelationProduct> list(RelationProduct entity) {

		List<RelationProduct> pts = mm.list(entity);
		return pts;
	}
	
	public List<RelationProduct> selectByCode(String code) {
		
		List<RelationProduct> pts = mm.selectByCode(code);
		return pts;
	}

}
