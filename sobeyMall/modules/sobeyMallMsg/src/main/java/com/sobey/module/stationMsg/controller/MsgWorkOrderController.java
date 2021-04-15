package com.sobey.module.stationMsg.controller;

import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.sobey.module.stationMsg.entity.Result;
import com.sobey.module.stationMsg.enumeration.MsgStatus;
import com.sobey.module.stationMsg.enumeration.MsgSubType;
import com.sobey.module.stationMsg.enumeration.TemplatePlaceholder;
import com.sobey.module.stationMsg.model.MallMsg;
import com.sobey.module.stationMsg.model.MallMsgTemplate;
import com.sobey.module.stationMsg.service.MallMsgTemplateService;
import com.sobey.module.stationMsg.util.MsgUtil;
import com.sobey.module.stationMsg.util.TemplateReplaceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @Author WCY
 * @CreateTime 2020/4/13 10:41
 * 工单消息相关
 */
@RestController
@Api(description = "工单消息相关接口")
@RequestMapping("/${api.v1}/workOrderMsg")
public class MsgWorkOrderController {

    @Autowired
    private MallMsgTemplateService mmts;
    @Autowired
    private MsgUtil msgUtil;

    /**
     * 工单处理提醒(给运维)
     * @param workOrderNum
     * @param userCode
     * @return
     */
    @PostMapping("/handleNotice")
    @ApiOperation(value = "工单处理提醒(给运维)",httpMethod = "POST")
    public Result handleWorkOrderNotice(@RequestParam @ApiParam(value = "工单号") String workOrderNum,
                                        @RequestParam @ApiParam(value = "接收人userCode") String userCode){
        Result fail = Result.withFail("");
        if (StringUtils.isBlank(workOrderNum) || StringUtils.isBlank(userCode)){
            fail.setMsg("参数为空");
            return fail;
        }
        Map<String,Object> map = new HashMap<>();
        map.put("msgTypeCode", MsgSubType.WorkOrderPending.getCode());
        List<MallMsgTemplate> templates = mmts.selectByMap(map);
        if (null == templates || templates.size() != 1){
            fail.setMsg("未找到该类型的消息模板");
            return fail;
        }
        MallMsgTemplate template = templates.get(0);
        Map<String,String> replacements = new HashMap<>();
        replacements.put(TemplatePlaceholder.workOrderNumber,workOrderNum);
        String msg = TemplateReplaceUtil.replace(template.getMsgTemplate(), replacements);
        MallMsg mallMsg = new MallMsg();
        mallMsg.setAccountId(userCode);
        mallMsg.setMsgContent(msg);
        mallMsg.setBasicMsgType(template.getParentTypeCode());
        mallMsg.setSubMsgType(template.getMsgTypeCode());
        mallMsg.setMsgStatus(MsgStatus.Unread.getCode());
        mallMsg.setCreateTime(new Date());
        mallMsg.setUuid(IdWorker.get32UUID());
        msgUtil.sendMsg(Collections.singletonList(mallMsg));
        return Result.withSuccess("发送成功");
    }

    /**
     * 工单确认通知(用户)
     * @param username
     * @param workOrderNum
     * @param userCode
     * @return
     */
    @PostMapping("/workOrderConfirmation")
    @ApiOperation(value = "工单确认通知(给用户)",httpMethod = "POST")
    public Result workOrderConfirmation(@RequestParam @ApiParam(value = "接收人用户名") String username,
                                        @RequestParam @ApiParam(value = "工单号") String workOrderNum,
                                        @RequestParam @ApiParam(value = "接收人userCode") String userCode){
        Result fail = Result.withFail("");
        if (StringUtils.isAnyBlank(username,workOrderNum,userCode)){
            fail.setMsg("参数为空");
            return fail;
        }
        Map<String,Object> map = new HashMap<>();
        map.put("msgTypeCode", MsgSubType.WorkOrderConfirmation.getCode());
        List<MallMsgTemplate> templates = mmts.selectByMap(map);
        if (null == templates || templates.size() != 1){
            fail.setMsg("未找到该类型的消息模板");
            return fail;
        }
        MallMsgTemplate template = templates.get(0);
        Map<String,String> replacements = new HashMap<>();
        replacements.put(TemplatePlaceholder.workOrderNumber,workOrderNum);
        replacements.put(TemplatePlaceholder.username,username);
        String msg = TemplateReplaceUtil.replace(template.getMsgTemplate(), replacements);
        MallMsg mallMsg = new MallMsg();
        mallMsg.setAccountId(userCode);
        mallMsg.setMsgContent(msg);
        mallMsg.setBasicMsgType(template.getParentTypeCode());
        mallMsg.setSubMsgType(template.getMsgTypeCode());
        mallMsg.setMsgStatus(MsgStatus.Unread.getCode());
        mallMsg.setCreateTime(new Date());
        mallMsg.setUuid(IdWorker.get32UUID());
        msgUtil.sendMsg(Collections.singletonList(mallMsg));
        return Result.withSuccess("发送成功");
    }

    /**
     * 工单受理通知
     * @param username
     * @param workOrderNum
     * @param userCode
     * @return
     */
    @PostMapping("/workOrderAcceptance")
    @ApiOperation(value = "工单受理通知(给用户)",httpMethod = "POST")
    public Result workOrderAcceptance(@RequestParam @ApiParam(value = "接收人用户名") String username,
                                      @RequestParam @ApiParam(value = "工单号") String workOrderNum,
                                      @RequestParam @ApiParam(value = "接收人userCode") String userCode){

        Result fail = Result.withFail("");
        if (StringUtils.isAnyBlank(username,workOrderNum,userCode)){
            fail.setMsg("参数为空");
            return fail;
        }
        Map<String,Object> map = new HashMap<>();
        map.put("msgTypeCode", MsgSubType.WorkOrderAcceptance.getCode());
        List<MallMsgTemplate> templates = mmts.selectByMap(map);
        if (null == templates || templates.size() != 1){
            fail.setMsg("未找到该类型的消息模板");
            return fail;
        }
        MallMsgTemplate template = templates.get(0);
        Map<String,String> replacements = new HashMap<>();
        replacements.put(TemplatePlaceholder.workOrderNumber,workOrderNum);
        replacements.put(TemplatePlaceholder.username,username);
        String msg = TemplateReplaceUtil.replace(template.getMsgTemplate(), replacements);
        MallMsg mallMsg = new MallMsg();
        mallMsg.setAccountId(userCode);
        mallMsg.setMsgContent(msg);
        mallMsg.setBasicMsgType(template.getParentTypeCode());
        mallMsg.setSubMsgType(template.getMsgTypeCode());
        mallMsg.setMsgStatus(MsgStatus.Unread.getCode());
        mallMsg.setCreateTime(new Date());
        mallMsg.setUuid(IdWorker.get32UUID());
        msgUtil.sendMsg(Collections.singletonList(mallMsg));
        return Result.withSuccess("发送成功");

    }

}
