package com.sobey.module.role.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.role.model.CopyRole;
import com.sobey.module.role.model.Role;

@Mapper
@Repository
public interface CopyRoleMapper extends SupperMapper<CopyRole> {




}
