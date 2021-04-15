package com.sobey.module.productPrivilege.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.brightSpot.model.BrightSpot;
import com.sobey.module.productPrivilege.model.ProductPrivilege;

@Mapper
@Repository
public interface ProductPrivilegeMapper extends SupperMapper<ProductPrivilege> {

	List<ProductPrivilege> page(Page<ProductPrivilege> page, ProductPrivilege ct);
	
	List<ProductPrivilege> list(ProductPrivilege ct);
	

}
