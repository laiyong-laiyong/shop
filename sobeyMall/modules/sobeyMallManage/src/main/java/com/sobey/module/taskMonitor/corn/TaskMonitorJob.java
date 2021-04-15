package com.sobey.module.taskMonitor.corn;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sobey.module.taskMonitor.model.TaskMonitor;
import com.sobey.module.taskMonitor.service.TaskMonitorService;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

/**
 * @author lgc
 * @createTime 2020/4/26 14:05
 * 服务即将到期时提醒
 */
public class TaskMonitorJob implements Job {

	@Autowired
	private  TaskMonitorService tms;
    private static final Logger log = LoggerFactory.getLogger(TaskMonitorJob.class);

    /**
     * 每个月凌晨过一分执行一次，表达式为：0 1 0 0 1-12 ? 
     * 
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("扫描服务到期时间任务执行");
        
        Wrapper<TaskMonitor> wp = new EntityWrapper<TaskMonitor>();
        //获取上上个月的时间
        DateTime time = DateUtil.offsetMonth(DateUtil.date(), -2);
        String format = DateUtil.format(time, DatePattern.NORM_DATE_PATTERN);
		wp.le("processStartDate", format);
        this.tms.delete(wp);
    }
}
