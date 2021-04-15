package com.sobey.module.productservice.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.sobey.module.mq.producer.ChargeProducer;
import com.sobey.module.order.entity.ResultInfo;
import com.sobey.module.productservice.entity.charge.Charging;
import com.sobey.module.productservice.entity.charge.Usage;
import com.sobey.module.productservice.model.ConsumeFailMsg;
import com.sobey.module.productservice.model.PublishFailMsg;
import com.sobey.module.productservice.service.ConsumeFailMsgService;
import com.sobey.module.productservice.service.PublishFailMsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author WCY
 * @createTime 2020/7/2 15:33
 * 查询计费失败的信息
 */
@RequestMapping("/${api.v1}/failMsg")
@RestController
@Api(description = "失败的计费信息相关")
public class FailMsgController {

    @Autowired
    private ConsumeFailMsgService cfs;
    @Autowired
    private PublishFailMsgService pfs;
    @Autowired
    private ChargeProducer producer;

    /**
     * 查询消费失败的计费信息
     * @param pageNum
     * @param pageSize
     * @param consumeFailMsg
     * @return
     */
    @PostMapping("/consume/pages")
    @ApiOperation(value = "查询接收mq消息后处理失败的计费信息",httpMethod = "POST")
    public Page<ConsumeFailMsg> consumeFailMsgPage(@RequestParam int pageNum,
                                                   @RequestParam int pageSize,
                                                   @RequestBody(required = false) ConsumeFailMsg consumeFailMsg){
        Page<ConsumeFailMsg> page = new Page<>(pageNum, pageSize,"createDate",false);

        return cfs.page(page, consumeFailMsg);
    }

    /**
     * 查询推送失败的计费信息
     * @param pageNum
     * @param pageSize
     * @param publishFailMsg
     * @return
     */
    @PostMapping("/publish/pages")
    @ApiOperation(value = "查询推送到mq失败的计费信息",httpMethod = "POST")
    public Page<PublishFailMsg> publishFailMsgPage(@RequestParam int pageNum,
                                                   @RequestParam int pageSize,
                                                   @RequestBody(required = false) PublishFailMsg publishFailMsg){
        Page<PublishFailMsg> page = new Page<>(pageNum, pageSize, "createDate", false);
        return pfs.page(page, publishFailMsg);
    }

    /**
     * 更新消费失败消息的人工处理状态为已处理
     * @param uuid
     * @return
     */
    @PutMapping("/consume")
    @ApiOperation(value = "更新消费失败的计费消息的人工处理状态为已处理",httpMethod = "PUT")
    public ResultInfo updateConsume(@RequestParam String uuid,
                                    @RequestParam @ApiParam(value = "处理人") String handler){
        ConsumeFailMsg consumeFailMsg = new ConsumeFailMsg();
        consumeFailMsg.setUuid(uuid);
        consumeFailMsg.setManualProcessStatus("1"); //1-已处理 0-未处理
        consumeFailMsg.setHandler(handler);
        consumeFailMsg.setHandleDate(new Date());
        if (cfs.updateById(consumeFailMsg)){
            return ResultInfo.withSuccess("更新成功");
        }

        return ResultInfo.withFail("更新失败");
    }

    /**
     * 更新消费失败消息的人工处理状态为已处理
     * @param uuid
     * @return
     */
    @PutMapping("/publish")
    @ApiOperation(value = "更新推送失败的计费消息的人工处理状态为已处理",httpMethod = "PUT")
    public ResultInfo updatePublish(@RequestParam String uuid,
                                    @RequestParam @ApiParam(value = "处理人") String handler){
        PublishFailMsg publishFailMsg = new PublishFailMsg();
        publishFailMsg.setUuid(uuid);
        publishFailMsg.setManualProcessStatus("1");
        publishFailMsg.setHandler(handler);
        publishFailMsg.setHandleDate(new Date());
        if (pfs.updateById(publishFailMsg)){
            return ResultInfo.withSuccess("更新成功");
        }

        return ResultInfo.withFail("更新失败");
    }

    /**
     * 重新计费
     * @return
     */
    @PostMapping("/renew")
    @ApiOperation(value = "重新计费",httpMethod = "POST")
    public ResultInfo renewCharge(@RequestParam @ApiParam(value = "主键") String uuid,
                                  @RequestParam @ApiParam(value = "失败消息类型,publish传'P',consume传'C'") String failMsgType,
                                  @RequestParam @ApiParam(value = "处理人") String handler){

        Charging charging = new Charging();
        charging.setRequestId(IdWorker.get32UUID());
        Date date = new Date();
        if ("P".equalsIgnoreCase(failMsgType)){
            PublishFailMsg publishFailMsg = pfs.selectById(uuid);
            if (null == publishFailMsg){
                return ResultInfo.withFail("uuid不存在");
            }
            Usage[] usages = JSON.parseObject(publishFailMsg.getUsage(), Usage[].class);
            charging.setUsage(usages);
            charging.setUserCode(publishFailMsg.getUserCode());
            charging.setAppId(publishFailMsg.getAppId());
            publishFailMsg.setManualProcessStatus("1");
            publishFailMsg.setHandler(handler);
            publishFailMsg.setHandleDate(date);
            pfs.updateById(publishFailMsg);
            producer.publishChargeMsg(charging);
            return ResultInfo.withSuccess("推送成功");
        }
        if ("C".equalsIgnoreCase(failMsgType)){
            ConsumeFailMsg consumeFailMsg = cfs.selectById(uuid);
            if (null == consumeFailMsg){
                return ResultInfo.withFail("uuid不存在");
            }
            Usage[] usages = JSON.parseObject(consumeFailMsg.getUsage(), Usage[].class);
            charging.setUsage(usages);
            charging.setUserCode(consumeFailMsg.getUserCode());
            charging.setAppId(consumeFailMsg.getAppId());
            consumeFailMsg.setManualProcessStatus("1");
            consumeFailMsg.setHandler(handler);
            consumeFailMsg.setHandleDate(date);
            cfs.updateById(consumeFailMsg);
            producer.publishChargeMsg(charging);
            return ResultInfo.withSuccess("推送成功");
        }
        return ResultInfo.withFail("failMsgType不正确");
    }

}
