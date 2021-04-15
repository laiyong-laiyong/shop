package com.sobey.module.productDebug.service;

import java.util.Date;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sobey.module.versionCustomOption.model.VersionCustomOption;

import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.swagger.annotations.ApiParam;

/**
 * C此类暂时不用
 * 
 * @author lgc
 * @date 2020年2月20日 上午10:33:04
 */
public interface ProductFeignService {

	/**
	 * 调试商品开通接口
	 * 
	 * @param userCode
	 * @param version
	 * @param option
	 * @param endDate
	 * @return
	 */
//	@RequestLine("POST /action?action-id=open")
//	String open(
//			@Headers(value = "sobeyhive-http-token") String value,
//			@Param(value = "debug") Boolean debug,
//			@Param(value = "openUrl")  String openUrl,
//			@Param(value = "userCode")  String userCode,
//			@Param(value = "versionCode")  String versionCode,
//			@RequestBody  List<VersionCustomOption> options,
//			@Param(value = "endDate")  String endDate);
	
	
	@PostMapping("/action?action-id=open")
	String open1(
			@RequestHeader(value = "sobeyhive-http-token") String value,
			@RequestParam(value = "debug") Boolean debug,
			@RequestParam(value = "openUrl")  String openUrl,
			@RequestParam(value = "userCode")  String userCode,
			@RequestParam(value = "versionCode")  String versionCode,
			@RequestBody  List<VersionCustomOption> options,
			@RequestParam(value = "endDate")  String endDate);


}
