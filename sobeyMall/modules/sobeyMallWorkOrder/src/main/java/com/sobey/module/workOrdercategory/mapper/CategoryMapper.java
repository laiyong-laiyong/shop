package com.sobey.module.workOrdercategory.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.workOrdercategory.model.Category;

@Mapper
@Repository
public interface CategoryMapper extends SupperMapper<Category> {

	List<Category> page(Page<Category> page, Category ct);

}
