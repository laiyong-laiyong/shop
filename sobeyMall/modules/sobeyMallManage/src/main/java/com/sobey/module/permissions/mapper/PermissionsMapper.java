package com.sobey.module.permissions.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.permissions.model.Permissions;

@Mapper
@Repository
public interface PermissionsMapper extends SupperMapper<Permissions> {




}
