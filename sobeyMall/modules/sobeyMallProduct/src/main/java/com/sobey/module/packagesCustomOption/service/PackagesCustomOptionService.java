package com.sobey.module.packagesCustomOption.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.exception.AppException;
import com.sobey.module.packagesCustomOption.mapper.PackagesCustomOptionMapper;
import com.sobey.module.packagesCustomOption.model.PackagesCustomOption;
import com.sobey.module.product.enumeration.OperationType;

import cn.hutool.core.util.StrUtil;

/**
 * 
 * 
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class PackagesCustomOptionService
		extends ServiceImpl<PackagesCustomOptionMapper, PackagesCustomOption> {
	

	public void insertBatch(List<PackagesCustomOption> options, String customId) {
		if (CollectionUtils.isEmpty(options)) {
			return;
		}

		for (PackagesCustomOption cs : options) {
			this.insert(cs, customId);
		}
	}
	
	public void insert(PackagesCustomOption option, String customId) {
		if (option == null) {
			return;
		}
		
		option.setCustomId(customId);
		this.insert(option);
	}
	
	public void updateBatch(List<PackagesCustomOption> options, String customId) {
		if (CollectionUtils.isEmpty(options)) {
			return;
		}
		
		for (PackagesCustomOption item : options) {
		    
			if (item != null) {
				
				String uuid = item.getUuid();
				
				String operationType = item.getOperationType();
				if (OperationType.delete.getName().equalsIgnoreCase(operationType)) {
					if (StrUtil.isNotBlank(uuid)) {
						PackagesCustomOption item2 = new PackagesCustomOption();
						item2.setUuid(uuid);
						
						Wrapper<PackagesCustomOption> wrapper = new EntityWrapper<PackagesCustomOption>(item2);
						this.delete(wrapper);
					}
					
				}else if (OperationType.update.getName().equalsIgnoreCase(operationType)) {
					this.updateById(item);
				}else if (OperationType.insert.getName().equalsIgnoreCase(operationType)) {
					if (StringUtils.isBlank(customId)) {
						throw new AppException("修改时新增自定义选项必须传递customId");
					}else {
						item.setCustomId(customId);
						this.insert(item);
					}
				}
			}

		}
	}
	
	
	
	public List<PackagesCustomOption> list(PackagesCustomOption entity) {

		if (entity == null) {
			return null;
		}
		List<PackagesCustomOption> pts = this.baseMapper.list(entity);
		return pts;
	}
	
	
	public void remove(PackagesCustomOption entity) {
		if (entity == null) {
			return;
		}
		this.baseMapper.remove(entity);
		
	}
	
	
	

}
