package com.sobey.module.workOrder.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.workOrder.model.WorkOrder;

@Mapper
@Repository
public interface WorkOrderMapper extends SupperMapper<WorkOrder> {


	List<WorkOrder> page(Page<WorkOrder> page, WorkOrder entity);
	List<WorkOrder> list(WorkOrder entity);
	List<HashMap<String, Object>> statistic();
	


}
