package com.sobey.module.packagesCustomOption.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.packagesCustomOption.model.PackagesCustomOption;

@Mapper
@Repository
public interface PackagesCustomOptionMapper extends SupperMapper<PackagesCustomOption> {

	List<PackagesCustomOption> list(PackagesCustomOption ct);
	void remove(PackagesCustomOption entity);
}
