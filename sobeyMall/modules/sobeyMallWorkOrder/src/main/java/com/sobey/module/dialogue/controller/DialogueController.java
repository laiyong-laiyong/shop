package com.sobey.module.dialogue.controller;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;
import com.sobey.module.dialogue.model.Dialogue;
import com.sobey.module.dialogue.model.request.DialogueContext;
import com.sobey.module.dialogue.service.DialogueService;
import com.sobey.module.media.model.Media;
import com.sobey.module.media.service.MediaService;
import com.sobey.module.message.service.feign.MsgFeignService;
import com.sobey.util.business.header.HeaderUtil;
import com.sobey.util.common.uuid.UUIDUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("${api.v1}/work-order/dialogue")
@Api(value = "工单对话", description = "工单对话接口")
public class DialogueController {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DialogueService ds;
	@Autowired
	private MediaService ms;
	@Autowired
	private MsgFeignService mfs;

	@ApiOperation(value = "工单对话查询", notes = "工单对话查询接口")
	@GetMapping
	public List<Dialogue> list(Dialogue entity) {

		List<Dialogue> list = ds.list(entity);
		return list;
	}

	@ApiOperation(value = "新增", notes = "新增")
	@PostMapping
	public void insert(@RequestBody(required = true) Dialogue entity) {

		if (entity == null) {
			throw new AppException(ExceptionType.PRODUCT_NOT_NULL);
		}
		String from = entity.getFrom();
		String to = entity.getTo();
		String workOrderId = entity.getWorkOrderId();
		if (StringUtils.isBlank(from) 
				||StringUtils.isBlank(to)
				||StringUtils.isBlank(workOrderId)) {
			throw new AppException("from,to,workOrderId不能为空");
		}
		
		this.ds.insert(entity);

		//这里调用推送消息接口
		pushMsg(entity);
	}
	
	
	public void pushMsg(Dialogue entity) {
		
		
		String mediaId = entity.getMediaId();
		String context = entity.getContext();
		String to = entity.getTo();
		String from = entity.getFrom();
		String uuid = entity.getWorkOrderId();
		
		String token = HeaderUtil.getAuth();
		
		DialogueContext it = new DialogueContext();
		it.setSourceUserCode(from);
		it.setTargetUserCode(to);
		it.setWorkOrderNum(uuid);
		
		
		//此对话是图片
		if (StringUtils.isNotBlank(mediaId)) {
			Media md = new Media();
			md.setMediaId(mediaId);
			List<Media> mds = this.ms.list(md);
			String path = "";
			if (CollectionUtils.isNotEmpty(mds)) {
				for (Media item : mds) {
					String address = item.getAddress();
					path = path + "," + address;
				}
				//去除开始的逗号
				path = StringUtils.removeStart(path, ",");
			}
			
			it.setContent(path);
			it.setTypeCode("1");
			mfs.onlineChat(token,it);
			
		}
		//此对话是文字内容
		if (StringUtils.isNotBlank(context)) {
			it.setContent(context);
			it.setTypeCode("0");
			mfs.onlineChat(token,it);
		}
		
	}

	@ApiOperation(value = "对话预新增")
	@GetMapping("/pre")
	public Dialogue preInsert() {

		Dialogue et = new Dialogue();

		String uuid = UUIDUtils.simpleUuid();
		et.setMediaId(uuid);

		return et;
	}

}
