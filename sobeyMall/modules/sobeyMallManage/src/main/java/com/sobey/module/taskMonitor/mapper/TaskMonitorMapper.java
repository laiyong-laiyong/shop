package com.sobey.module.taskMonitor.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.taskMonitor.model.TaskMonitor;

@Mapper
@Repository
public interface TaskMonitorMapper extends SupperMapper<TaskMonitor> {


	List<TaskMonitor> page(Page<TaskMonitor> page, TaskMonitor entity);
	List<TaskMonitor> list(TaskMonitor entity);
	List<HashMap<String, Object>> statistic();


}
