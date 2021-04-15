package com.sobey.module.versionCustomOption.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.versionCustomOption.model.VersionCustomOption;

@Mapper
@Repository
public interface VersionCustomOptionMapper extends SupperMapper<VersionCustomOption> {

	List<VersionCustomOption> page(Page<VersionCustomOption> page, VersionCustomOption ct);
	List<VersionCustomOption> list(VersionCustomOption ct);
	void remove(VersionCustomOption entity);
}
