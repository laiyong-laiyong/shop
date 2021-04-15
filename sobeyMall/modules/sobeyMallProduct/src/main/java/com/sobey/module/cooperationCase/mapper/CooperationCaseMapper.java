package com.sobey.module.cooperationCase.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.cooperationCase.model.CooperationCase;

@Mapper
@Repository
public interface CooperationCaseMapper extends SupperMapper<CooperationCase> {

	List<CooperationCase> page(Page<CooperationCase> page, CooperationCase ct);
	List<CooperationCase> list(CooperationCase entity);
	void remove(CooperationCase entity);
}
