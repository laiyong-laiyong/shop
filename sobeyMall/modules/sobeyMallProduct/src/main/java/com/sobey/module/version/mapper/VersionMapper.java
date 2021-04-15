package com.sobey.module.version.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.version.model.Version;

@Mapper
@Repository
public interface VersionMapper extends SupperMapper<Version> {

	List<Version> page(Page<Version> page, Version ct);
	List<Version> list(Version entity);

}
