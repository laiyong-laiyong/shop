package com.sobey.module.cooperationCase.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.brightSpot.model.BrightSpot;
import com.sobey.module.cooperationCase.mapper.CooperationCaseMapper;
import com.sobey.module.cooperationCase.model.CooperationCase;
import com.sobey.module.product.enumeration.OperationType;
import com.sobey.module.version.model.Version;

/**
 * 
 * 
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class CooperationCaseService extends ServiceImpl<CooperationCaseMapper, CooperationCase> {

	@Autowired
	private CooperationCaseMapper cm;

	public void insertBatch(List<CooperationCase> cases, String productId) {
		if (CollectionUtils.isEmpty(cases)) {
			return;
		}

		for (CooperationCase cs : cases) {
			cs.setProductId(productId);
		}
		this.insertBatch(cases);
	}

	public List<CooperationCase> list(CooperationCase entity) {

		if (entity == null) {
			return null;
		}
		List<CooperationCase> pts = cm.list(entity);
		return pts;
	}

	public void remove(CooperationCase entity) {
		if (entity == null) {
			return;
		}
		this.cm.remove(entity);
	}

	public void updateBatch(List<CooperationCase> list) {
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		for (CooperationCase item : list) {
			if (item != null) {
				String operationType = item.getOperationType();
				String uuid = item.getUuid();
				if (OperationType.delete.getName().equalsIgnoreCase(operationType)) {
					if (StringUtils.isNotEmpty(uuid)) {
						CooperationCase item2 = new CooperationCase();
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
