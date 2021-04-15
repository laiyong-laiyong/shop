package com.sobey.module.evaluateLabelCategory.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.evaluateLabelCategory.model.EvaluateLabelCategory;

@Mapper
@Repository
public interface EvaluateLabelCategoryMapper extends SupperMapper<EvaluateLabelCategory> {


}
