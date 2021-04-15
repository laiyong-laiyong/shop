package com.sobey.module.taskMonitor.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.jwt.annotation.PassToken;
import com.sobey.module.process.model.response.Result;
import com.sobey.module.process.service.feign.ProcessService;
import com.sobey.module.taskMonitor.enumeration.EventType;
import com.sobey.module.taskMonitor.enumeration.StatusType;
import com.sobey.module.taskMonitor.model.TaskMonitor;
import com.sobey.module.taskMonitor.model.response.message.Context;
import com.sobey.module.taskMonitor.model.response.message.Message;
import com.sobey.module.taskMonitor.service.TaskMonitorService;
import com.sobey.module.token.fegin.AuthService;
import com.sobey.util.business.header.HeaderUtil;
import com.sobey.util.common.json.JsonKit;

import cn.hutool.http.HttpRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@EnableAsync
@RestController
@RequestMapping("${api.v1}/manage/taskMonitor")
@Api(value = "任务监控", description = "任务监控相关接口")
public class TaskMonitorController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private TaskMonitorService tms;
	@Autowired
	private ProcessService ps;
	@Autowired
	private AuthService as;
	
	
	@ApiOperation(value = "分页", notes = "分页")
	@PostMapping("/page")
	public Page<TaskMonitor> page(@RequestParam int page, @RequestParam int size,@RequestBody(required=false) TaskMonitor entity) {
		Page<TaskMonitor> page1 = new Page<TaskMonitor>(page, size);
		tms.page(page1, entity);
		return page1;
	}
	
	@ApiOperation(value = "列表")
	@PostMapping("/list")
	public List<TaskMonitor> list(@RequestBody(required=false) TaskMonitor entity) {
		List<TaskMonitor> list = this.tms.list(entity);
		return list;
	}


	@ApiOperation(value = "统计", notes = "统计")
	@GetMapping("/statistic")
	public List<HashMap<String, Object>> statistic() {

		List<HashMap<String, Object>> list = tms.statistic();
		return list;
	}
	
	/**
	 * C还没有跨租户接口
	 * 
	 * @param list
	 */
	@ApiOperation(value = "删除", notes = "删除")
	@DeleteMapping
	public void delete(@ApiParam("只填processId和site") @RequestBody(required=true) List<TaskMonitor> list) {
		
		if (CollectionUtils.isEmpty(list)) {
			return;
		}else {
			//由于时跨租户删除流程，所以这里用客户端模式获取token
			JSONObject object = this.as.getToken();
			
			for (TaskMonitor item : list) {
				if (item != null) {
					String processId = item.getProcessId();
					String site = item.getSite();
					if (StringUtils.isNotBlank(processId) && StringUtils.isNotBlank(site)) {
						String token = (String) object.get("access_token");
						
						try {
							List<String> ids = new ArrayList<String>();
							ids.add(processId);
							Result rt = this.ps.delete(token,site,ids);
							
							Wrapper<TaskMonitor> wp = new EntityWrapper<TaskMonitor>();
							wp.eq("processId", processId);
							this.tms.delete(wp);
							
							
//							if (rt != null) {
//								String status = rt.getStatus();
//								if ("1".equalsIgnoreCase(status)) {
//									Wrapper<TaskMonitor> wp = new EntityWrapper<TaskMonitor>();
//									wp.eq("processId", processId);
//									this.tms.delete(wp);
//								}
//							}
							
							
						} catch (Exception e) {
							log.info("删除流程失败",e);
						}
						
					}
				}
				
			}
		}
		
	}
	
	
	/**
	 * 
	 * @param list
	 */
	@ApiOperation(value = "重做", notes = "重做")
	@PutMapping
	public void redo(@ApiParam("只填processId和site") @RequestBody(required=true) List<TaskMonitor> list) {
		
		if (CollectionUtils.isEmpty(list)) {
			return;
		}else {
			//由于时跨租户删除流程，所以这里用客户端模式获取token
			JSONObject object = this.as.getToken();
			
			for (TaskMonitor item : list) {
				if (item != null) {
					String processId = item.getProcessId();
					String site = item.getSite();
					if (StringUtils.isNotBlank(processId) && StringUtils.isNotBlank(site)) {
						String token = (String) object.get("access_token");
						
						try {
							List<String> ids = new ArrayList<String>();
							ids.add(processId);
							Result rt = this.ps.redo(token,site,ids);
							if (rt != null) {
								String status = rt.getStatus();
								//1表示成功，-1表示失败
								if ("1".equalsIgnoreCase(status)) {
									Wrapper<TaskMonitor> wp = new EntityWrapper<TaskMonitor>();
									wp.eq("processId", processId);
									this.tms.updateForSet("status='0'", wp);
								}
							}
						} catch (Exception e) {
							log.info("重做失败",e);
						}
						
					}
				}
				
			}
		}
		
	}
	
	/**
	 * 
	 * @param list
	 */
	@ApiOperation(value = "取消", notes = "取消")
	@PatchMapping
	public void cancel(@ApiParam("只填processId和site") @RequestBody(required=true) List<TaskMonitor> list) {
		
		if (CollectionUtils.isEmpty(list)) {
			return;
		}else {
			//由于时跨租户删除流程，所以这里用客户端模式获取token
			JSONObject object = this.as.getToken();
			
			for (TaskMonitor item : list) {
				if (item != null) {
					String processId = item.getProcessId();
					String site = item.getSite();
					if (StringUtils.isNotBlank(processId) && StringUtils.isNotBlank(site)) {
						String token = (String) object.get("access_token");
						
						try {
							List<String> ids = new ArrayList<String>();
							ids.add(processId);
							Result rt = this.ps.cancle(token,site,ids);
							if (rt != null) {
								String status = rt.getStatus();
								if ("1".equalsIgnoreCase(status)) {
									Wrapper<TaskMonitor> wp = new EntityWrapper<TaskMonitor>();
									wp.eq("processId", processId);
									this.tms.updateForSet("status='-1'", wp);
								}
							}
							
							
						} catch (Exception e) {
							log.info("取消失败",e);
						}
						
					}
				}
				
			}
		}
		
	}
	
	@ApiOperation(value = "状态枚举")
	@GetMapping("/status-type")
	@PassToken
	public String getStatusType() {
		
		String json = JsonKit.enumToJson(StatusType.class);
		return json;
	}
	
	
	/**
	 * 不需要使用@Async
	 * 
	 */
	@ApiOperation(value = "接收回调")
	@PostMapping("/callback")
	@PassToken
	public void callback(@RequestBody(required=true) Message msg) {
		log.info("接受到回调参数："+JsonKit.beanToJson(msg));
		String event = msg.getEvent();
		if (StringUtils.isNotBlank(event)) {
			if (StringUtils.contains(event, EventType.started.getCode())) {
				Context ctt = msg.getMessage();
				this.tms.save(ctt);
			}else if (StringUtils.contains(event, EventType.success.getCode())) {
				Context ctt = msg.getMessage();
				this.tms.update(ctt,StatusType.success.getCode());
			}else if (StringUtils.contains(event, EventType.redirected.getCode())) {
				Context ctt = msg.getMessage();
				this.tms.update(ctt,StatusType.executing.getCode());
			}else if (StringUtils.contains(event, EventType.failed.getCode())
					|| StringUtils.contains(event, EventType.cancel.getCode())) {
				Context ctt = msg.getMessage();
				this.tms.update(ctt,StatusType.failed.getCode());
			}
		}
	}
	
	
	
	
	
	
	
	

	

}
