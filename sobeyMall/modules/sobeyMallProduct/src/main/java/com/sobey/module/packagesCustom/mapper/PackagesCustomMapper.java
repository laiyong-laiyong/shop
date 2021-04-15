package com.sobey.module.packagesCustom.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.packagesCustom.model.PackagesCustom;

@Mapper
@Repository
public interface PackagesCustomMapper extends SupperMapper<PackagesCustom> {

	List<PackagesCustom> page(Page<PackagesCustom> page, PackagesCustom ct);
	void remove(PackagesCustom entity);
}
