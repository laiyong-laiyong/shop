package com.sobey.module.function.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.function.model.Function;

@Mapper
@Repository
public interface FunctionMapper extends SupperMapper<Function> {

	List<Function> page(Page<Function> page, Function ct);
	List<Function> list(Function entity);
	void remove(Function entity);

}
