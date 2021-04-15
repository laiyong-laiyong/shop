package com.sobey.module.brightSpot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.brightSpot.model.BrightSpot;

@Mapper
@Repository
public interface BrightSpotMapper extends SupperMapper<BrightSpot> {

	List<BrightSpot> page(Page<BrightSpot> page, BrightSpot ct);
	List<BrightSpot> list(BrightSpot entity);
	void remove(BrightSpot entity);

}
