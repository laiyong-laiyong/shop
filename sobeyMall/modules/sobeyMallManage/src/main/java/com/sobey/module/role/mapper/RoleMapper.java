package com.sobey.module.role.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.role.model.Role;

@Mapper
@Repository
public interface RoleMapper extends SupperMapper<Role> {




}
