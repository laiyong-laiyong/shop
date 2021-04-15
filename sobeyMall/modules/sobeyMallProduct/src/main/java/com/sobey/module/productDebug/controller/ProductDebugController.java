package com.sobey.module.productDebug.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sobey.framework.httpclient.RestCaller;
import com.sobey.module.productDebug.model.request.Debug;
import com.sobey.module.productDebug.model.response.Result;
import com.sobey.module.versionCustomOption.model.VersionCustomOption;
import com.sobey.util.common.json.JsonKit;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("${api.v1}/products/debug")
@Api(value = "商品调试", description = "商品调试接口")
public class ProductDebugController {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@ApiOperation(value = "商品开通调试")
	@PostMapping("/open")
	public Result open(@RequestParam @ApiParam("开通URL") String openUrl,@RequestBody Debug debug) {


		Result rt = RestCaller.post(openUrl).setBody(JsonKit.beanToJson(debug)).executeReturn(Result.class);

		return rt;
		
	}
	@ApiOperation(value = "商品关闭调试")
	@PostMapping("/close")
	public Result close(@RequestParam @ApiParam("关闭URL") String closeUrl,@RequestBody Debug debug) {
		
		Result rt = RestCaller.post(closeUrl).setBody(JsonKit.beanToJson(debug)).executeReturn(Result.class);
		
		return rt;
		
	}
	@ApiOperation(value = "商品续费调试")
	@PostMapping("/renew")
	public Result renew(@RequestParam @ApiParam("续费URL") String renewUrl,@RequestBody Debug debug) {
		
		
		Result rt = RestCaller.post(renewUrl).setBody(JsonKit.beanToJson(debug)).executeReturn(Result.class);
		
		return rt;
		
	}

	/**
	 * 此接口模拟被调用的第三方接口
	 * 
	 * 
	 * @param debug
	 * @param openUrl
	 * @param userCode
	 * @param versionCode
	 * @param options
	 * @param endDate
	 * @return
	 */
	@ApiOperation(value = "模拟第三方的接口")
	@PostMapping("/action")
	@ResponseBody
	public Result thirdOpen(@RequestParam(value = "debug", required = false) Boolean debug,
			@RequestParam(value = "openUrl", required = false) String openUrl,
			@RequestParam(value = "userCode", required = false) String userCode,
			@RequestParam(value = "versionCode", required = false) String versionCode,
			@RequestBody(required = false) List<VersionCustomOption> options,
			@RequestParam(value = "endDate", required = false) String endDate) {

		Result rt = new Result();
		rt.setState("true");
		return rt;
	}

}
