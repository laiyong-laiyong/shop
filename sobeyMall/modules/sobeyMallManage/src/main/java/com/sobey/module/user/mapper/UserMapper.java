package com.sobey.module.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.user.model.User;

@Mapper
@Repository
public interface UserMapper extends SupperMapper<User> {




}
