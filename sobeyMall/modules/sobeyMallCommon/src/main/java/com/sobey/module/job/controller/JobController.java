package com.sobey.module.job.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sobey.framework.jwt.annotation.PassToken;
import com.sobey.module.job.service.JobService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/task")
@Api(description = "定时任务")
public class JobController {
	@Autowired
	private JobService jobService;

	/**
	 * 创建cron任务
	 * 
	 * @param jobName
	 * @param jobGroup
	 * @return
	 */
	@RequestMapping(value = "/cron", method = RequestMethod.POST)
	@PassToken
	@ApiOperation(value = "不需要token,corn表达式任务")
	public String startCronJob(@RequestParam("jobName") String jobName,
			@RequestParam("jobGroup") String jobGroup,
			@ApiParam("corn表达式,例如:0/5 \\* \\* \\* \\* ?") @RequestParam("corn") String corn,
			@ApiParam("定时类的全路径,例如:com.sobey.module.job.job.CornJob,这里没有.class后缀") @RequestParam("path") String path) {
		jobService.addCronJob(jobName, jobGroup, corn, path);
		return "success";
	}

	/**
	 * 创建异步任务
	 * 
	 * @param jobName
	 * @param jobGroup
	 * @return
	 */
	@PassToken
	@ApiOperation(value = "不需要token。异步调用,只执行一次,默认账单job的name和group为：bill,")
	@RequestMapping(value = "/async", method = RequestMethod.POST)
	public String startAsyncJob(@RequestParam("jobName") String jobName,
			@RequestParam("jobGroup") String jobGroup,
			@ApiParam("定时类的全路径,例如:com.sobey.CornJob") @RequestParam("path") String path) {
		jobService.addAsyncJob(jobName, jobGroup, path);
		return "success";
	}

	/**
	 * 暂停任务
	 * 
	 * @param jobName
	 * @param jobGroup
	 * @return
	 */
	@PassToken
	@RequestMapping(value = "/pause", method = RequestMethod.POST)
	@ApiOperation(value = "不需要token")
	public String pauseJob(@RequestParam("jobName") String jobName,
			@RequestParam("jobGroup") String jobGroup) {
		jobService.pauseJob(jobName, jobGroup);
		return "pause job success";
	}

	/**
	 * 恢复任务
	 * 
	 * @param jobName
	 * @param jobGroup
	 * @return
	 */
	@ApiOperation(value = "不需要token,恢复任务")
	@PassToken
	@RequestMapping(value = "/resume", method = RequestMethod.POST)
	public String resumeJob(@RequestParam("jobName") String jobName,
			@RequestParam("jobGroup") String jobGroup) {
		jobService.resumeJob(jobName, jobGroup);
		return "resume job success";
	}

	/**
	 * 删除务
	 * 
	 * @param jobName
	 * @param jobGroup
	 * @return
	 */
	@ApiOperation(value = "不需要token")
	@PassToken
	@RequestMapping(value = "/delete", method = RequestMethod.PUT)
	public String deleteJob(@RequestParam("jobName") String jobName,
			@RequestParam("jobGroup") String jobGroup) {
		jobService.deleteJob(jobName, jobGroup);
		return "delete job success";
	}
}