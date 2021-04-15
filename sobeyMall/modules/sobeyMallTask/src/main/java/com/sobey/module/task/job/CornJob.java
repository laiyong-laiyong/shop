package com.sobey.module.task.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

//import com.sobey.util.common.date.DateKit;

public class CornJob implements Job {
	
	private static final Logger logger = LoggerFactory.getLogger(CornJob.class);

	/**
	 * 核心方法,Quartz Job真正的执行逻辑.
	 * 
	 * @param JobExecutionContext中封装有Quartz运行所需要的所有信息
	 * @throws JobExecutionException
	 *             execute()方法只允许抛出JobExecutionException异常
	 */
	@Override
	public void execute(JobExecutionContext cxt) throws JobExecutionException {
		Date fireTime = cxt.getFireTime();
		Date scheduledFireTime = cxt.getScheduledFireTime();
//		System.out.println("当前：" + DateKit.getTime() + "，实际：" + DateKit.getTime(fireTime) + "，预计："
//				+ DateKit.getTime(scheduledFireTime));
	}
}
