package com.sobey.module.resource.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.resource.model.Resource;

@Mapper
@Repository
public interface ResourceMapper extends SupperMapper<Resource> {




}
