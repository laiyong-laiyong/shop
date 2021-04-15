package com.sobey.module.job.job;

import com.sobey.util.common.date.DateKit;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class CornJob2 implements Job {
	
	private static final Logger log = LoggerFactory.getLogger(CornJob2.class);

	/**
	 * 核心方法,Quartz Job真正的执行逻辑.
	 * 
	 * @param cxt
	 * @throws JobExecutionException
	 *             execute()方法只允许抛出JobExecutionException异常
	 */
	@Override
	public void execute(JobExecutionContext cxt) throws JobExecutionException {
		Date fireTime = cxt.getFireTime();
		Date scheduledFireTime = cxt.getScheduledFireTime();
		System.out.println(cxt.getJobDetail().getKey().getName()+",当前：" + DateKit.getTime() + "，实际：" + DateKit.getTime(fireTime) + "，预计："
				+ DateKit.getTime(scheduledFireTime));
	}
}
