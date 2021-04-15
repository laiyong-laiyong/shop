package com.sobey.module.file.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionKit;
import com.sobey.exception.ExceptionType;
import com.sobey.util.common.file.FileUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("${api.v1}/file")
@Api(value = "文件", description = "文件")
public class FileController {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@ApiOperation(value = "下载,请使用postman测试")
	@GetMapping("/download")
	public void download(@ApiParam(value="下载地址") @RequestParam String address, HttpServletResponse response) {

		String name = StringUtils.substringAfterLast(address, "/");
		if (StringUtils.isEmpty(name)) {
			throw new AppException(ExceptionType.SYS_FILE_NAME_NOT_EXIST);
		} else {
			try {
				name = URLEncoder.encode(name, "utf-8");
			} catch (UnsupportedEncodingException e) {
				log.error("解析文件名报错", ExceptionKit.toString(e));
				return;
			}
		}
		response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		response.addHeader("Content-Disposition",
				"attachment;filename=" + name + ";filename*=utf-8''" + name);

		FileUtil.download(address, response);
	}
	
	
	

}
