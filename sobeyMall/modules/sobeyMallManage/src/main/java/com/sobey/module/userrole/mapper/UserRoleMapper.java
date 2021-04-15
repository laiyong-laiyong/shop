package com.sobey.module.userrole.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.userrole.model.UserRole;

@Mapper
@Repository
public interface UserRoleMapper extends SupperMapper<UserRole> {




}
