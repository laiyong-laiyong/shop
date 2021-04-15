package com.sobey.module.version.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.brightSpot.model.BrightSpot;
import com.sobey.module.product.enumeration.OperationType;
import com.sobey.module.version.mapper.VersionMapper;
import com.sobey.module.version.model.Version;
import com.sobey.module.versionCustom.model.VersionCustom;
import com.sobey.module.versionCustom.service.VersionCustomService;
import com.sobey.util.common.uuid.UUIDUtils;

/**
 * 
 * 
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class VersionService extends ServiceImpl<VersionMapper, Version> {

	@Autowired
	private VersionCustomService cs;
	@Autowired
	private VersionMapper vm;

	public void insertBatch(List<Version> versions, String productId) {
		if (CollectionUtils.isEmpty(versions)) {
			return;
		}

		for (Version vs : versions) {
			insert(vs, productId);
		}

	}

	public void insert(Version vs, String productId) {
		if (vs == null) {
			return;
		}

		String versionId = UUIDUtils.simpleUuid();
		vs.setProductId(productId);
		vs.setUuid(versionId);
		this.insert(vs);

		List<VersionCustom> customs = vs.getCustoms();
		cs.insertBatch(customs, versionId);

	}

	public void updateBatch(List<Version> list,String productId) {
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		for (Version item : list) {
			if (item != null) {
				String uuid = item.getUuid();
				String operationType = item.getOperationType();
				if (OperationType.delete.getName().equalsIgnoreCase(operationType)) {
					if (StringUtils.isNotEmpty(uuid)) {
						Version item2 = new Version();
						item2.setUuid(uuid);
						this.remove(item2);
					}
				} else if (OperationType.update.getName().equalsIgnoreCase(operationType)) {
					this.updateById(item);
				} else if (OperationType.insert.getName().equalsIgnoreCase(operationType)) {
					/**
					 * 
					 * 只插入version本身,这里不处理version下的自定义规格
					 * 这里要重新生成uuid，应为插入的时候没有uuid。VersionCustom关联缺需要
					 */
					uuid = UUIDUtils.simpleUuid();
					item.setUuid(uuid);
					item.setProductId(productId);
					this.insert(item);
				}

				List<VersionCustom> customs = item.getCustoms();
				cs.updateBatch(customs, uuid);
			}
		}
	}

	public void remove(Version entity) {

		List<Version> list = vm.list(entity);

		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		for (Version vs : list) {
			String uuid = vs.getUuid();
			VersionCustom ct = new VersionCustom();
			ct.setVersionId(uuid);
			this.cs.remove(ct);

			this.deleteById(uuid);
		}
	}

	public Page<Version> page(Page<Version> page, Version entity) {

		List<Version> cts = this.vm.page(page, entity);
		page.setRecords(cts);
		return page;
	}

	public List<Version> list(Version entity) {

		List<Version> pts = vm.list(entity);
		return pts;
	}

}
