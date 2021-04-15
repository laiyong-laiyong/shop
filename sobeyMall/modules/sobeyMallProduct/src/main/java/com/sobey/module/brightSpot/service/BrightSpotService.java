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
import com.sobey.module.media.model.Media;
import com.sobey.module.media.service.MediaService;
import com.sobey.module.product.enumeration.OperationType;

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
	@Autowired
	private MediaService  ms;
	
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
		for (BrightSpot item : list) {
			if (item != null) {
				String uuid = item.getUuid();
				String operationType = item.getOperationType();
				if (OperationType.delete.getName().equalsIgnoreCase(operationType)) {
					
					if (StringUtils.isNotEmpty(uuid)) {
						/**
						 * 修改时删除，只需要uuid
						 * 
						 */
						BrightSpot item2 =new BrightSpot();
						item2.setUuid(uuid);
						this.remove(item2);
					}
				}else {
					this.insertOrUpdate(item);
				}
			}
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
		List<BrightSpot> list = this.list(entity);
		if (CollectionUtils.isNotEmpty(list)) {
			for (BrightSpot item : list) {
				String uuid = item.getUuid();
				Media md = new Media();
				md.setMediaId(uuid);
				this.ms.remove(md);
			}
		}
		
		this.bsm.remove(entity);
	}

}
