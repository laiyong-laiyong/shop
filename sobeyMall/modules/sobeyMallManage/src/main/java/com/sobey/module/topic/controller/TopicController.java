package com.sobey.module.topic.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sobey.framework.jwt.model.Token;
import com.sobey.framework.jwt.model.UserInfo;
import com.sobey.module.topic.model.request.Event;
import com.sobey.module.topic.model.request.Topic;
import com.sobey.module.topic.model.request.openDetail.OpenParam;
import com.sobey.module.topic.service.feign.TopicService;
import com.sobey.util.business.header.HeaderUtil;
import com.sobey.util.common.json.JsonKit;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@EnableAsync
@RestController
@RequestMapping("${api.v1}/manage/topic")
@Api(value = "主题", description = "主题")
public class TopicController {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TopicService ts;
	@Autowired
	private com.sobey.module.topic.service.TopicService topicService;

	/**
	 * 由于这里需要配合之前的开通商品的接口,所以参数是这样传递
	 * 
	 * 
	 * @param action
	 * @param detail
	 * @return
	 */
	@ApiOperation(value = "创建主题")
	@PostMapping("/action")
	public Object createTopic(@RequestParam(required=true,value="action-id") String  action,@ApiParam("开通明细") @RequestBody(required =false) OpenParam detail) {
		
		String token = HeaderUtil.getAuth();
		Jwt jwt = JwtHelper.decode(token);
		Token bean = null;
		String site = null;
		if (jwt !=null) {
			bean = JsonKit.jsonToBean(jwt.getClaims(), Token.class);
			UserInfo info = bean.getUser_info();
			if (info != null) {
				site = info.getSite();
			}
		}
		
		if (StringUtils.isBlank(site)) {
			Map<String, Object> map = this.warpErrorMsg("failed", "", "获取不到站点code", null);
			return map;
		}
		log.info("主题接受到参数：" + site);

		Topic topic = topicService.buildTopic(site);
		
		try {
			this.ts.createTopic(token,site, topic);
		} catch (Exception e) {
			log.info("创建主题报错",e);
			Map<String, Object> map = this.warpErrorMsg("failed", "",e.getMessage(), null);
			return map;
		}
		
		return warpSuccessMsg("success", "");

	}
	
	private Map<String, Object> warpErrorMsg(String state, String code, String message,
			String detailMsg) {

		Map<String, Object> map = new HashMap<>();
		map.put("state", state);
		map.put("error_code", code);
		map.put("error_msg", message);
		map.put("extend_message", detailMsg);

		return map;
	}
	
	private Map<String, Object> warpSuccessMsg(String state,String message) {
		
		Map<String, Object> map = new HashMap<>();
		map.put("state", state);
		map.put("message", message);
		return map;
	}

}
