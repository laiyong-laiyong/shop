package com.sobey.module.cloudProvider.config;

import com.sobey.module.cloudProvider.enumeration.CloudProviderType;
import com.sobey.module.cloudProvider.mapper.CloudProviderMapper;
import com.sobey.module.cloudProvider.model.CloudProvider;
import com.sobey.util.common.cache.Cache;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.annotation.PostConstruct;


//@Component
//@Lazy
public class CloudProviderConfig {

	@Autowired
	private CloudProviderMapper cpm;

	@PostConstruct
	private void init() {
		List<CloudProvider> list = cpm.selectList(null);
		if (CollectionUtils.isNotEmpty(list)) {
			for (CloudProvider item : list) {
				if (CloudProviderType.ali.getName().equalsIgnoreCase(item.getName())) {
					Cache.put(CloudProviderType.ali.getName(), item);
				}
			}
		}
	}

	

}
