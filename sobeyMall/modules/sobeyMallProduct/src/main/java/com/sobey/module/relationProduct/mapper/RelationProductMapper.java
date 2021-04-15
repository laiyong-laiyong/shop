package com.sobey.module.relationProduct.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.relationProduct.model.RelationProduct;

@Mapper
@Repository
public interface RelationProductMapper extends SupperMapper<RelationProduct> {

	List<RelationProduct> page(Page<RelationProduct> page, RelationProduct ct);
	List<RelationProduct> list(RelationProduct entity);
	List<RelationProduct> selectByCode(@Param("code") String code);

}
