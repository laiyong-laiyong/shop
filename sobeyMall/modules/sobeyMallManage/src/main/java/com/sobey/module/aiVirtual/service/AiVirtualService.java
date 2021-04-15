package com.sobey.module.aiVirtual.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sobey.module.aiVirtual.model.request.TokenRequest;
import com.sobey.module.aiVirtual.model.request.user.UserRequest;
import com.sobey.module.aiVirtual.model.response.token.TokenResponse;
import com.sobey.module.aiVirtual.model.response.user.UserResponse;
import com.sobey.util.common.cache.Cache;
import com.sobey.util.hutool.ext.http.Header;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * 
 * 
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class AiVirtualService {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${aivirtual.url}")
	private String url;

	@Value("${aivirtual.key}")
	private String key;

	@Value("${aivirtual.secret}")
	private String secret;


	private String getSignature(long timestamp) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("key", key);
		map.put("timestamp", timestamp);
		String sign = SecureUtil.signParamsSha1(map, secret);
		return sign;
	}

	public String getToken() {

		long timestamp = DateUtil.currentSeconds() * 1000;
		String signature = this.getSignature(timestamp);
		TokenRequest rq = new TokenRequest();
		rq.setKey(key);
		rq.setSignature(signature);
		rq.setTimestamp(timestamp);
		String data = JSONUtil.toJsonStr(rq);
		String body = HttpRequest.post(url + "/api/thirdpart/token").addHeaders(Header.json()).body(data).execute().body();
		log.info("获取token收到返回的数据：" + body);
		TokenResponse bean = JSONUtil.toBean(body, TokenResponse.class);
		if (bean != null) {
			int code = bean.getCode();
			// 表示成功
			String token = null;
			if (code == 2) {
				token = bean.getData().getAccess_token();
				return token;
			} else {
				return null;
			}
		} else {
			return null;
		}

	}

	/**
	 * C如果是多个实例，从缓存中取会有问题
	 * 
	 */
	public void getTokenFromCache() {

		String token = (String) Cache.get("aiToken");
		if (StrUtil.isEmpty(token)) {
			String newToken = this.getToken();
			// 设置半个小时过期
			Cache.put("aiToken", newToken, 1800000);
		}
	}

	public String importUser(UserRequest entity) {
		if (entity == null) {
			return null;
		}
		String token =  this.getToken();
		entity.setToken(token);
		String data = JSONUtil.toJsonStr(entity);
		String body = HttpRequest.post(url + "/api/thirdpart/user/import").addHeaders(Header.json()).body(data).execute().body();
		log.info("导入用户收到返回的数据：" + body);
		return body;

	}
	
	public String test() {
		
		String body = HttpRequest.post("172.16.131.35:80/sobeyMallManage/V1/manage/ai-virtual/test").addHeaders(Header.json()).execute().body();
		log.info("导入用户收到返回的数据：" + body);
		return body;
		
	}

	public String getUrl(String uid) {
		if (StrUtil.isEmpty(uid)) {
			return null;
		}
		String token = this.getToken();
		JSONObject object = new JSONObject();
		object.put("token", token);
		object.put("uid", uid);

		String data = JSONUtil.toJsonStr(object);
		String body = HttpRequest.post(url + "/api/thirdpart/sso").addHeaders(Header.json()).body(data).execute().body();
		log.info("导入用户收到返回的数据：" + body);
		return body;

	}

}
