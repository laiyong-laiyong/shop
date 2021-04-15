package com.sobey.module.metric.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.metric.model.Metric;

@Mapper
@Repository
public interface MetricMapper extends SupperMapper<Metric> {

	List<Metric> page(Page<Metric> page, Metric ct);
	List<Metric> list(Metric entity);
	void remove(Metric entity);

}
