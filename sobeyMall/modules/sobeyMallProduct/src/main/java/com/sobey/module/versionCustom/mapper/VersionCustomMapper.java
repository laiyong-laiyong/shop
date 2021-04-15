package com.sobey.module.versionCustom.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.versionCustom.model.VersionCustom;

@Mapper
@Repository
public interface VersionCustomMapper extends SupperMapper<VersionCustom> {

	List<VersionCustom> page(Page<VersionCustom> page, VersionCustom ct);
	void remove(VersionCustom entity);
}
