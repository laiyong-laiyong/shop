package com.sobey.module.discount.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.discount.model.Discount;

@Mapper
@Repository
public interface DiscountMapper extends SupperMapper<Discount> {

	List<Discount> page(Page<Discount> page, Discount ct);

}
