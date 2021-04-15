package com.sobey.module.discount.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.discount.model.Statement;

@Mapper
@Repository
public interface StatementMapper extends SupperMapper<Statement> {


}
