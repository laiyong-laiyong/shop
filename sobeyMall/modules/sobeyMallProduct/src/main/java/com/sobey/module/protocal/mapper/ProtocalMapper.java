package com.sobey.module.protocal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.protocal.model.Protocal;

@Mapper
@Repository
public interface ProtocalMapper extends SupperMapper<Protocal> {


}
