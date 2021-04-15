package com.sobey.module.cloudProvider.util;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.sobey.module.cloudProvider.enumeration.CloudProviderType;
import com.sobey.module.cloudProvider.mapper.CloudProviderMapper;
import com.sobey.module.cloudProvider.model.CloudProvider;
import com.sobey.util.common.cache.Cache;

@Component
public class ClientUtil {
	
	@Autowired
	private CloudProviderMapper cpm;

	public  DefaultAcsClient getAliAcsClient() {
		CloudProvider cp = (CloudProvider) Cache.get(CloudProviderType.ali.getName());
		if (cp == null) {
			init();
			cp = (CloudProvider) Cache.get(CloudProviderType.ali.getName());
		}
		
		// RAM是Global Service, API入口位于华东 1 (杭州) , 这里Region填写"cn-hangzhou"
		IClientProfile profile = DefaultProfile.getProfile(cp.getRegionId(), cp.getAccessKey(),
				cp.getSecretKey());
		return new DefaultAcsClient(profile);
	}
	
	
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
