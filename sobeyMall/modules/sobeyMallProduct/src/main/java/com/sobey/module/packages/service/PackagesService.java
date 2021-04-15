package com.sobey.module.packages.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.packages.mapper.PackagesMapper;
import com.sobey.module.packages.model.Packages;
import com.sobey.module.packagesCustom.model.PackagesCustom;
import com.sobey.module.packagesCustom.service.PackagesCustomService;
import com.sobey.module.product.enumeration.OperationType;
import com.sobey.util.common.uuid.UUIDUtils;

/**
 * 
 * 
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class PackagesService extends ServiceImpl<PackagesMapper, Packages> {

	@Autowired
	private PackagesCustomService cs;

	public void insertBatch(List<Packages> list, String productId) {
		if (CollectionUtils.isEmpty(list)) {
			return;
		}

		for (Packages vs : list) {
			insert(vs, productId);
		}

	}

	public void insert(Packages entity, String productId) {
		if (entity == null) {
			return;
		}

		String packagesId = UUIDUtils.simpleUuid();
		entity.setProductId(productId);
		entity.setUuid(packagesId);
		this.insert(entity);

		List<PackagesCustom> customs = entity.getCustoms();
		cs.insertBatch(customs, packagesId);

	}

	public void updateBatch(List<Packages> list,String productId) {
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		for (Packages item : list) {
			if (item != null) {
				String uuid = item.getUuid();
				String operationType = item.getOperationType();
				if (OperationType.delete.getName().equalsIgnoreCase(operationType)) {
					if (StringUtils.isNotEmpty(uuid)) {
						Packages item2 = new Packages();
						item2.setUuid(uuid);
						this.remove(item2);
					}
				} else if (OperationType.update.getName().equalsIgnoreCase(operationType)) {
					this.updateById(item);
				} else if (OperationType.insert.getName().equalsIgnoreCase(operationType)) {
					/**
					 * 
					 * 只插入Packages本身,这里不处理Packages下的自定义规格
					 * 这里要重新生成uuid，应为插入的时候没有uuid。PackagesCustom关联缺需要
					 */
					uuid = UUIDUtils.simpleUuid();
					item.setUuid(uuid);
					item.setProductId(productId);
					this.insert(item);
				}

				List<PackagesCustom> customs = item.getCustoms();
				cs.updateBatch(customs, uuid);
			}
		}
	}

	public void remove(Packages entity) {
		
		if(entity == null) {
			return;
		}
		EntityWrapper<Packages> ew = new EntityWrapper<Packages>(entity);
		List<Packages> list = this.selectList(ew);
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		for (Packages vs : list) {
			String uuid = vs.getUuid();
			PackagesCustom ct = new PackagesCustom();
			ct.setPackagesId(uuid);
			this.cs.remove(ct);

			this.deleteById(uuid);
		}
	}

	public Page<Packages> page(Page<Packages> page, Packages entity) {

		List<Packages> cts = this.baseMapper.page(page, entity);
		page.setRecords(cts);
		return page;
	}


}
