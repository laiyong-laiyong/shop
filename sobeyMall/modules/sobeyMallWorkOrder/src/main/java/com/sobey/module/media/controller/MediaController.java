package com.sobey.module.media.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;
import com.sobey.framework.jwt.annotation.PassToken;
import com.sobey.module.media.model.Media;
import com.sobey.module.media.service.MediaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("${api.v1}/work-order/medias")
@Api(value = "素材", description = "素材相关接口")
public class MediaController {

	@Autowired
	private MediaService ms;

	@ApiOperation(value = "素材查询", notes = "素材查询")
	@PostMapping("/page")
	public Page<Media> page(@RequestParam int offset, @RequestParam int limit,
			@RequestBody(required = false) Media entity) {
		Page<Media> page = new Page<Media>(offset, limit);
		ms.page(page, entity);
		return page;
	}

	@ApiOperation(value = "素材删除", notes = "素材删除")
	@DeleteMapping("/{uuid}")
	public void remove(@ApiParam(value="素材编号") @PathVariable("uuid") String uuid) {
		Media md = new Media();
		md.setUuid(uuid);
		this.ms.remove(md);
	}

	@ApiOperation(value = "素材查询", notes = "素材查询接口")
	@GetMapping
	public List<Media> list(Media media) {
		List<Media> list = this.ms.list(media);
		return list;
	}

	@ApiOperation(value = "素材新增", notes = "素材新增")
	@PostMapping(consumes = "multipart/*", headers = "content-type=multipart/form-data")
	@Transactional
	@PassToken
	public String insert(Media md, @RequestParam(required = true) @ApiParam("主目录") String mediaDir) {
		if (md == null) {
			throw new AppException(ExceptionType.PRODUCT_MEDIA_NOT_NULL);
		}

		String workOrderId = md.getWorkOrderId();
		String mediaId = md.getMediaId();
		if ((!StringUtils.isEmpty(workOrderId)) && (!StringUtils.isEmpty(mediaId))) {
			String uuid =this.ms.save(md.getFiles(), mediaDir, workOrderId, mediaId, md.getType());
			return uuid;
		} else {
			throw new AppException(ExceptionType.SYS_RUNTIME, "workOrderId和mediaId必须传递", null);
		}
	}

}
