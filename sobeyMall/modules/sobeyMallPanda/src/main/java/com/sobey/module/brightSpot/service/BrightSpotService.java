package com.sobey.module.brightSpot.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.brightSpot.mapper.BrightSpotMapper;
import com.sobey.module.brightSpot.model.BrightSpot;

/**
 * 
 * 
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class BrightSpotService extends ServiceImpl<BrightSpotMapper, BrightSpot> {

	@Autowired
	private BrightSpotMapper  bsm;
	
	public void insertBatch(List<BrightSpot> spots, String productId) throws IllegalStateException, IOException {
		if (CollectionUtils.isEmpty(spots)) {
			return;
		}

		/**
		 * C这里亮点的uuid通过前台传递过来
		 * 
		 */
		for (BrightSpot spot : spots) {
			spot.setProductId(productId);
		}
		this.insertBatch(spots);
	}
	
	
	public void updateBatch(List<BrightSpot> list) {
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		
	}
	
	public List<BrightSpot> list(BrightSpot entity) {

		if (entity == null) {
			return null;
		}
		List<BrightSpot> pts = bsm.list(entity);
		return pts;
	}
	
	public void remove(BrightSpot entity) {
		if (entity == null) {
			return;
		}
		
		
		this.bsm.remove(entity);
	}

}
