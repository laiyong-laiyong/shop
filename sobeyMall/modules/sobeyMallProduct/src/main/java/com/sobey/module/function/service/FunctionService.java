package com.sobey.module.function.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.function.mapper.FunctionMapper;
import com.sobey.module.function.model.Function;
import com.sobey.module.product.enumeration.OperationType;

/**
 * 
 * 
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class FunctionService extends ServiceImpl<FunctionMapper, Function> {

	@Autowired
	private FunctionMapper fm;

	public void insertBatch(List<Function> fts, String productId) {
		if (CollectionUtils.isEmpty(fts)) {
			return;
		}

		for (Function cs : fts) {
			cs.setProductId(productId);
		}
		this.insertBatch(fts);
	}

	public List<Function> list(Function entity) {

		if (entity == null) {
			return null;
		}
		List<Function> pts = fm.list(entity);
		return pts;
	}

	public void remove(Function entity) {
		if (entity == null) {
			return;
		}
		this.fm.remove(entity);
	}

	public void updateBatch(List<Function> list) {
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		for (Function item : list) {
			if (item != null) {
				String operationType = item.getOperationType();
				String uuid = item.getUuid();
				if (OperationType.delete.getName().equalsIgnoreCase(operationType)) {

					if (StringUtils.isNotEmpty(uuid)) {
						Function item2 = new Function();
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
