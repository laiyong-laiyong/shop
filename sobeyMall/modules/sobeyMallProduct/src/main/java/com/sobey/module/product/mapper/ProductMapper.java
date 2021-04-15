package com.sobey.module.product.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.product.model.Product;

@Mapper
@Repository
public interface ProductMapper extends SupperMapper<Product> {


	List<Product> page(Page<Product> page, Product entity);
	List<Product> list(Product entity);
	List<HashMap<String, Object>> statistic();


}
