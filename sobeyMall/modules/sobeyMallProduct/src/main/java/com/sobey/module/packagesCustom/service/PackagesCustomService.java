package com.sobey.module.packagesCustom.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.product.enumeration.OperationType;
import com.sobey.exception.AppException;
import com.sobey.module.packagesCustom.mapper.PackagesCustomMapper;
import com.sobey.module.packagesCustom.model.PackagesCustom;
import com.sobey.module.packagesCustomOption.model.PackagesCustomOption;
import com.sobey.module.packagesCustomOption.service.PackagesCustomOptionService;
import com.sobey.util.common.uuid.UUIDUtils;

import cn.hutool.core.util.StrUtil;

/**
 * 
 * 
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class PackagesCustomService extends ServiceImpl<PackagesCustomMapper, PackagesCustom> {

	@Autowired
	private PackagesCustomOptionService os;

	public void insertBatch(List<PackagesCustom> list, String packagesId) {
		if (CollectionUtils.isEmpty(list)) {
			return;
		}

		for (PackagesCustom cs : list) {
			this.insert(cs, packagesId);
		}
	}
	
	
	public void insert(PackagesCustom entity, String packagesId) {
		if (entity == null) {
			return;
		}
		
		String metricId = entity.getMetricId();
		if (StringUtils.isBlank(metricId)) {
			throw new AppException("metricId必须传递");
		}
		
		String uuid = UUIDUtils.simpleUuid();
		entity.setPackagesId(packagesId);
		entity.setUuid(uuid);
		this.insert(entity);
		
		List<PackagesCustomOption> options = entity.getOptions();
		os.insertBatch(options, uuid);
	}

	public void updateBatch(List<PackagesCustom> cts, String packagesId) {
		if (CollectionUtils.isEmpty(cts)) {
			return;
		}

		for (PackagesCustom item : cts) {
			if (item != null) {

				String uuid = item.getUuid();

				String operationType = item.getOperationType();
				if (OperationType.delete.getName().equalsIgnoreCase(operationType)) {
					if (StrUtil.isNotBlank(uuid)) {
						PackagesCustom item2 = new PackagesCustom();
						item2.setUuid(uuid);
						this.remove(item2);
					}
				} else if (OperationType.update.getName().equalsIgnoreCase(operationType)) {
					this.updateById(item);
				} else if (OperationType.insert.getName().equalsIgnoreCase(operationType)) {
					
					/**
					 * 
					 * 只插入自定义规格本身,这里不处理规格下的选项
					 * 这里要重新生成uuid，应为插入的时候没有uuid。PackagesCustomOption关联却需要
					 */
					if (StringUtils.isBlank(packagesId)) {
						throw new AppException("修改时新增资源必须传递packagesId");
					}else {
						uuid = UUIDUtils.simpleUuid();
						item.setUuid(uuid);
						item.setPackagesId(packagesId);
						this.insert(item);
					}
				}

				List<PackagesCustomOption> options = item.getOptions();
				os.updateBatch(options, uuid);

			}
		}
	}

	/**
	 * C这里的查询都要先查一次,因为有可能通过版本号来删除PackagesCustom
	 * 
	 * @param entity
	 */
	public void remove(PackagesCustom entity) {

		if (entity == null) {
			return;
		}
		Wrapper<PackagesCustom> wrapper3 = new EntityWrapper<PackagesCustom>(entity);
		List<PackagesCustom> list = this.selectList(wrapper3);
		if (CollectionUtils.isNotEmpty(list)) {
			for (PackagesCustom item : list) {

				String uuid = item.getUuid();
				PackagesCustomOption option = new PackagesCustomOption();
				option.setCustomId(uuid);
				Wrapper<PackagesCustomOption> wrapper = new EntityWrapper<PackagesCustomOption>(
						option);
				this.os.delete(wrapper);

				this.deleteById(uuid);

			}
		}
	}

	

}
