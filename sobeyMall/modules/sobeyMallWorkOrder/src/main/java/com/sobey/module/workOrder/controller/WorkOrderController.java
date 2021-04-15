package com.sobey.module.workOrder.controller;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;
import com.sobey.module.dialogue.model.Dialogue;
import com.sobey.module.dialogue.service.DialogueService;
import com.sobey.module.evaluate.model.Evaluate;
import com.sobey.module.evaluate.service.EvaluateService;
import com.sobey.module.media.model.Media;
import com.sobey.module.media.service.MediaService;
import com.sobey.module.workOrder.enumeration.WorkOrderDeleteState;
import com.sobey.module.workOrder.enumeration.WorkOrderState;
import com.sobey.module.workOrder.enumeration.notifyType;
import com.sobey.module.workOrder.model.WorkOrder;
import com.sobey.module.workOrder.service.WorkOrderService;
import com.sobey.util.business.identity.Identity;
import com.sobey.util.common.ToolKit;
import com.sobey.util.common.file.FileUtil;
import com.sobey.util.common.json.JsonKit;
import com.sobey.util.common.uuid.UUIDUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("${api.v1}/work-order")
@Api(value = "工单", description = "工单相关接口")
public class WorkOrderController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private WorkOrderService wos;
	@Autowired
	private MediaService md;
	@Autowired
	private DialogueService ds;

	@Autowired
	private EvaluateService es;

	@ApiOperation(value = "工单分页查询", notes = "工单分页查询接口")
	@PostMapping("/page")
	public Page<WorkOrder> page(@RequestParam int page, @RequestParam int size,@RequestBody(required=false) WorkOrder entity) {
		Page<WorkOrder> page1 = new Page<WorkOrder>(page, size);
		wos.page(page1, entity);
		return page1;
	}

	@ApiOperation(value = "工单查询", notes = "工单查询接口")
	@GetMapping("/{uuid}")
	public WorkOrder get(@ApiParam(value="工单编号") @PathVariable(value = "uuid") String uuid) {

		WorkOrder pt = new WorkOrder();
		pt.setUuid(uuid);
		List<WorkOrder> list = wos.list(pt);
		if (CollectionUtils.isNotEmpty(list)) {
			return list.get(0);
		}else
			return null;
	}
	
	
	
	@ApiOperation(value = "新增", notes = "新增")
	@PostMapping
	public void insert(@Validated @RequestBody(required = true) WorkOrder entity, BindingResult bindingResult) {
		
		ToolKit.validData(bindingResult);
		
		if (entity == null) {
			throw new AppException(ExceptionType.SYS_PARAMETER);
		}

		String mediaDir = entity.getMediaDir();
		String handlerCode = entity.getHandlerCode();
		String uuid = entity.getUuid();
		if (StringUtils.isBlank(uuid)) {
			throw new AppException("uuid不能为空");
		}
		try {
			entity.setDeleteFlag(WorkOrderDeleteState.not_delete.getCode());
			this.wos.insert(entity);
		} catch (Exception e) {
			// C连父目录一起删除
			FileUtil.forceDelete(mediaDir);
			throw new AppException(ExceptionType.WORK_ORDER_SAVE, e);
		}
		
		this.wos.notify(Identity.PUBLIC_SITE_CODE.getCode(),handlerCode, uuid, notifyType.new_work_order.getName());
		
	}
	
	
	@ApiOperation(value = "修改")
	@PatchMapping
	public void update(@RequestBody(required=true) WorkOrder entity) {
		
		if (entity == null) {
			throw new AppException(ExceptionType.SYS_PARAMETER);
		}
		
		String uuid = entity.getUuid();
		WorkOrder db = this.wos.selectById(uuid);
		if (db == null) {
			throw new AppException(ExceptionType.WORK_ORDER_NOT_EXIST);
		}
		String state = entity.getState();
		String createUserCode = db.getCreateUserCode();
		String siteCode = db.getSiteCode();
		/**
		 * 这里需要是前台传递过来的处理者,不能是数据库查询的
		 */
		String handlerCode = entity.getHandlerCode();
		if(WorkOrderState.distributed.getCode().equals(state)) {
			//这里给用户和处理者都发消息
			this.wos.notify(siteCode,createUserCode, uuid, notifyType.distributed_customer.getName());
			this.wos.notify(Identity.PUBLIC_SITE_CODE.getCode(),handlerCode, uuid, notifyType.distributed_operator.getName());
		}else if(WorkOrderState.confirm.getCode().equals(state)) {
			//这里给用户发通知
			this.wos.notify(siteCode,createUserCode, uuid,notifyType.reminder_customer.getName());
		}
		
		this.wos.updateById(entity);
	}
	
	
	
	@ApiOperation(value = "工单预新增")
	@GetMapping("/pre")
	public WorkOrder preInsert() {
		
		WorkOrder et = new WorkOrder();
		
		String mediaPath = this.wos.getMediaDir();
		et.setMediaDir(mediaPath);
		//为了统一,uuid和media值一样
		String uuid = UUIDUtils.simpleUuid();
		et.setUuid(uuid);
		et.setMediaId(uuid);
		
		return et;

	}
	
	
	@ApiOperation(value = "催单")
	@GetMapping("/reminders/{uuid}")
	public void reminder(@ApiParam(value="工单编号")  @PathVariable(value = "uuid") String uuid) {
		
		WorkOrder workOrder = this.get(uuid);
		if (workOrder == null) {
			return;
		}
		
		String handler = workOrder.getHandlerCode();
		this.wos.notify(Identity.PUBLIC_SITE_CODE.getCode(),handler, uuid,notifyType.reminder_operator.getName());
	}
	
	
	@ApiOperation(value = "工单状态类型")
	@GetMapping("/type")
	public String getType() {
		
		String json = JsonKit.enumToJson(WorkOrderState.class);
		return json;
	}
	
	@ApiOperation(value = "工单逻辑删除类型")
	@GetMapping("/logic-delete/type")
	public String getLogicDeleteType() {
		
		String json = JsonKit.enumToJson(WorkOrderDeleteState.class);
		return json;
	}
	
	
	@ApiOperation(value = "统计", notes = "统计")
	@GetMapping("/statistic")
	public List<HashMap<String, Object>> statistic() {

		List<HashMap<String, Object>> list = this.wos.statistic();
		return list;
	}
	
	
	
	@ApiOperation(value = "删除", notes = "删除")
	@DeleteMapping("/{uuid}")
	public void delete(@ApiParam(value="工单编号") @PathVariable(name = "uuid") String uuid) {
		WorkOrder et = new WorkOrder();
		et.setUuid(uuid);
		List<WorkOrder> list = this.wos.list(et);
		
		if (list == null) {
			throw new AppException(ExceptionType.WORK_ORDER_NOT_EXIST);
		}
		
		
		try {
			for (WorkOrder itm : list) {
				
				//删除工单素材
				Media media = new Media();
				media.setWorkOrderId(uuid);
				Wrapper<Media> wp = new EntityWrapper<Media>(media);
				this.md.delete(wp);
				
				//删除素材父目录
				String destPath = itm.getMediaDir();
				FileUtil.forceDelete(destPath);
				
				//删除工单基本信息
				this.wos.deleteById(itm.getUuid());
				
				//删除工单对话
				Dialogue dl = new Dialogue();
				dl.setWorkOrderId(uuid);
				Wrapper<Dialogue> wp2 = new EntityWrapper<Dialogue>(dl);
				this.ds.delete(wp2);
				
				//删除工单评价
				Evaluate va = new Evaluate();
				va.setWorkOrderId(uuid);
				Wrapper<Evaluate> wp3 = new EntityWrapper<Evaluate>(va);
				this.es.delete(wp3);
				
			}
			
		} catch (Exception e) {
			log.error("删除工单失败", e);
		}
		
		
		
	}

	
	
	
	

	

}
