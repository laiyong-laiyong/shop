package com.sobey.module.cloudProvider.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.cloudProvider.model.CloudProvider;

@Mapper
@Repository
public interface CloudProviderMapper extends SupperMapper<CloudProvider> {




}
