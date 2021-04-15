package com.sobey.module.evaluate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.evaluate.model.Evaluate;

@Mapper
@Repository
public interface EvaluateMapper extends SupperMapper<Evaluate> {

	


}
