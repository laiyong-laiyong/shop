package com.sobey.module.metric.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.metric.mapper.MetricMapper;
import com.sobey.module.metric.model.Metric;
import com.sobey.module.product.enumeration.OperationType;
import com.sobey.util.common.uuid.UUIDUtils;

/**
 * 
 * 
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class MetricService extends ServiceImpl<MetricMapper, Metric> {

	@Autowired
	private MetricMapper mm;

	public void insertBatch(List<Metric> mts, String productId) {
		if (CollectionUtils.isEmpty(mts)) {
			return;
		}

		for (Metric vs : mts) {
			vs.setProductId(productId);
		}
		this.insertBatch(mts);

	}

	public void remove(Metric entity) {
		if (entity == null) {
			return;
		}
		mm.remove(entity);

	}

	public Page<Metric> page(Page<Metric> page, Metric entity) {

		List<Metric> cts = this.mm.page(page, entity);
		page.setRecords(cts);
		return page;
	}

	public List<Metric> list(Metric entity) {

		List<Metric> pts = mm.list(entity);
		return pts;
	}

	public void updateBatch(List<Metric> list) {
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		for (Metric item : list) {
			if (item != null) {
				String operationType = item.getOperationType();
				String uuid = item.getUuid();
				if (OperationType.delete.getName().equalsIgnoreCase(operationType)) {
					if (StringUtils.isNotEmpty(uuid)) {
						Metric item2 = new Metric();
						item2.setUuid(uuid);
						this.remove(item2);
					}
				} else {
					this.insertOrUpdate(item);
				}
			}
		}
	}

}
