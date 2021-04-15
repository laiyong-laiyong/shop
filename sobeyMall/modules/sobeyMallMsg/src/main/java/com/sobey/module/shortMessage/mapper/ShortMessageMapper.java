package com.sobey.module.shortMessage.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.shortMessage.model.ShortMessage;

@Mapper
@Repository
public interface ShortMessageMapper extends SupperMapper<ShortMessage> {




}
