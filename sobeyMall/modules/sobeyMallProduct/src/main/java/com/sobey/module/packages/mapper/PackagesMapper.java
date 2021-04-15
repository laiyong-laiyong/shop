package com.sobey.module.packages.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.packages.model.Packages;

@Mapper
@Repository
public interface PackagesMapper extends SupperMapper<Packages> {

	List<Packages> page(Page<Packages> page, Packages ct);

}
