package com.sobey.module.discount.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.discount.model.DiscountRequestInfo;

@Mapper
@Repository
public interface DiscountRequestInfoMapper extends SupperMapper<DiscountRequestInfo> {


}
