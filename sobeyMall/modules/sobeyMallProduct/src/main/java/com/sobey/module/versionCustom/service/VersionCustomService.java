package com.sobey.module.versionCustom.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.product.enumeration.OperationType;
import com.sobey.module.versionCustom.mapper.VersionCustomMapper;
import com.sobey.module.versionCustom.model.VersionCustom;
import com.sobey.module.versionCustomOption.model.VersionCustomOption;
import com.sobey.module.versionCustomOption.service.VersionCustomOptionService;
import com.sobey.util.common.uuid.UUIDUtils;

import cn.hutool.core.util.StrUtil;

/**
 * 
 * 
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class VersionCustomService extends ServiceImpl<VersionCustomMapper, VersionCustom> {

	@Autowired
	private VersionCustomOptionService os;

	public void insertBatch(List<VersionCustom> cts, String versionId) {
		if (CollectionUtils.isEmpty(cts)) {
			return;
		}

		for (VersionCustom cs : cts) {
			this.insert(cs, versionId);
		}
	}
	
	
	public void insert(VersionCustom cs, String versionId) {
		if (cs == null) {
			return;
		}
		
		String uuid = UUIDUtils.simpleUuid();
		cs.setVersionId(versionId);
		cs.setUuid(uuid);
		this.insert(cs);
		
		List<VersionCustomOption> options = cs.getOptions();
		os.insertBatch(options, uuid);
	}

	public void updateBatch(List<VersionCustom> cts, String versionId) {
		if (CollectionUtils.isEmpty(cts)) {
			return;
		}

		for (VersionCustom item : cts) {
			if (item != null) {

				String uuid = item.getUuid();

				String operationType = item.getOperationType();
				if (OperationType.delete.getName().equalsIgnoreCase(operationType)) {
					if(StrUtil.isNotBlank(uuid)) {
						VersionCustom item2 = new VersionCustom();
						item2.setUuid(uuid);
						this.remove(item2);
					}
				} else if (OperationType.update.getName().equalsIgnoreCase(operationType)) {
					this.updateById(item);
				} else if (OperationType.insert.getName().equalsIgnoreCase(operationType)) {
					
					/**
					 * 
					 * 只插入自定义规格本身,这里不处理规格下的选项
					 * 这里要重新生成uuid，应为插入的时候没有uuid。VersionCustomOption关联却需要
					 */
					uuid = UUIDUtils.simpleUuid();
					item.setUuid(uuid);
					item.setVersionId(versionId);
					this.insert(item);
				}

				List<VersionCustomOption> options = item.getOptions();
				os.updateBatch(options, uuid);

			}
		}
	}

	/**
	 * C这里的查询都要先查一次,因为有可能通过版本号来删除VersionCustom
	 * 
	 * @param entity
	 */
	public void remove(VersionCustom entity) {

		if (entity == null) {
			return;
		}
		Wrapper<VersionCustom> wrapper3 = new EntityWrapper<VersionCustom>(entity);
		List<VersionCustom> list = this.selectList(wrapper3);
		if (CollectionUtils.isNotEmpty(list)) {
			for (VersionCustom item : list) {

				String uuid = item.getUuid();
				VersionCustomOption option = new VersionCustomOption();
				option.setCustomId(uuid);
				Wrapper<VersionCustomOption> wrapper = new EntityWrapper<VersionCustomOption>(
						option);
				this.os.delete(wrapper);

				this.deleteById(uuid);

			}
		}
	}

	

}
