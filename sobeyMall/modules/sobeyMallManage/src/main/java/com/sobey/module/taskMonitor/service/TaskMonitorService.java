package com.sobey.module.taskMonitor.service;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.taskMonitor.enumeration.StatusType;
import com.sobey.module.taskMonitor.mapper.TaskMonitorMapper;
import com.sobey.module.taskMonitor.model.TaskMonitor;
import com.sobey.module.taskMonitor.model.response.message.Context;

/**
 * 
 * 
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class TaskMonitorService extends ServiceImpl<TaskMonitorMapper, TaskMonitor> {

	@Autowired
	private TaskMonitorMapper tmMp;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	public Page<TaskMonitor> page(Page<TaskMonitor> page, TaskMonitor entity) {

		List<TaskMonitor> cts = tmMp.page(page, entity);
		page.setRecords(cts);
		return page;
	}

	public List<TaskMonitor> list(TaskMonitor entity) {

		List<TaskMonitor> pts = tmMp.list(entity);
		return pts;
	}

	public List<HashMap<String, Object>> statistic() {
		List<HashMap<String, Object>> map = this.tmMp.statistic();
		return map;
	}

	public void save(Context ctt) {
		if (ctt != null) {
			TaskMonitor tm = new TaskMonitor();
			tm.setProcessId(ctt.getInstanceId());
			tm.setProcessStartDate(ctt.getCreateTime());
			tm.setProcessEndDate(ctt.getEndTime());
			tm.setSite(ctt.getSite());
			tm.setStatus(StatusType.executing.getCode());
			tm.setProcessName(ctt.getFlowName());
			tm.setTaskName(ctt.getKeyword());
			this.insert(tm);
		}
	}
	
	public void update(Context ctt,String status) {
		if (ctt != null) {
			TaskMonitor tm = new TaskMonitor();
			tm.setProcessStartDate(ctt.getCreateTime());
			tm.setProcessEndDate(ctt.getEndTime());
			tm.setStatus(status);
			tm.setProcessName(ctt.getFlowName());
			tm.setTaskName(ctt.getKeyword());
			
			Wrapper<TaskMonitor> wp = new EntityWrapper<TaskMonitor>();
			wp.eq("processId", ctt.getInstanceId());
			this.update(tm, wp);
		}
	}

}
