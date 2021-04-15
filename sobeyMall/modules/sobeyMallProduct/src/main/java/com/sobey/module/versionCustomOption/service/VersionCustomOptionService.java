package com.sobey.module.versionCustomOption.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.function.model.Function;
import com.sobey.module.metric.model.Metric;
import com.sobey.module.product.enumeration.OperationType;
import com.sobey.module.productPrivilege.model.ProductPrivilege;
import com.sobey.module.versionCustom.model.VersionCustom;
import com.sobey.module.versionCustomOption.mapper.VersionCustomOptionMapper;
import com.sobey.module.versionCustomOption.model.VersionCustomOption;
import com.sobey.util.common.uuid.UUIDUtils;

import cn.hutool.core.util.StrUtil;

/**
 * 
 * 
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class VersionCustomOptionService
		extends ServiceImpl<VersionCustomOptionMapper, VersionCustomOption> {
	

	public void insertBatch(List<VersionCustomOption> options, String customId) {
		if (CollectionUtils.isEmpty(options)) {
			return;
		}

		for (VersionCustomOption cs : options) {
			this.insert(cs, customId);
		}
	}
	
	public void insert(VersionCustomOption option, String customId) {
		if (option == null) {
			return;
		}
		
		option.setCustomId(customId);
		this.insert(option);
	}
	
	public void updateBatch(List<VersionCustomOption> options, String customId) {
		if (CollectionUtils.isEmpty(options)) {
			return;
		}
		
		for (VersionCustomOption item : options) {
		    
			if (item != null) {
				
				String uuid = item.getUuid();
				
				String operationType = item.getOperationType();
				if (OperationType.delete.getName().equalsIgnoreCase(operationType)) {
					if(StrUtil.isNotBlank(uuid)) {
						VersionCustomOption item2 = new VersionCustomOption();
						item2.setUuid(uuid);
						Wrapper<VersionCustomOption> wrapper = new EntityWrapper<VersionCustomOption>(item2);
						this.delete(wrapper);
					}
					
				}else if (OperationType.update.getName().equalsIgnoreCase(operationType)) {
					this.updateById(item);
				}else if (OperationType.insert.getName().equalsIgnoreCase(operationType)) {
					item.setCustomId(customId);
					this.insert(item);
				}
			}

		}
	}
	
	
	
	public List<VersionCustomOption> list(VersionCustomOption entity) {

		if (entity == null) {
			return null;
		}
		List<VersionCustomOption> pts = this.baseMapper.list(entity);
		return pts;
	}
	
	
	public void remove(VersionCustomOption entity) {
		if (entity == null) {
			return;
		}
		this.baseMapper.remove(entity);
		
	}
	
	
	public void deleteBatch_bak(List<VersionCustomOption> list) {
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		for (VersionCustomOption cs : list) {
			String uuid = cs.getUuid();
			this.deleteById(uuid);
		}
	}

}
